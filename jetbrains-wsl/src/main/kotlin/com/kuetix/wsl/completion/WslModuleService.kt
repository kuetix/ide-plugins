package com.kuetix.wsl.completion

import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent
import com.google.gson.JsonParser
import java.io.File
import java.io.InputStreamReader

/**
 * Data classes for module information
 */
data class ModuleMethod(
    val value: String,
    val label: String,
    val description: String
)

data class ModuleInfo(
    val namespace: String,
    val `class`: String,
    val label: String,
    val description: String
)

data class ModuleData(
    val info: ModuleInfo,
    val methods: List<ModuleMethod>
)

/**
 * Service to load and manage WSL modules from modules.json
 */
@Service(Service.Level.PROJECT)
class WslModuleService(private val project: Project) {

    private val logger = Logger.getInstance(WslModuleService::class.java)
    private var modulesCache: Map<String, ModuleData>? = null

    init {
        // Set up file watcher for modules.json changes
        setupFileWatcher()
    }

    companion object {
        fun getInstance(project: Project): WslModuleService {
            return project.getService(WslModuleService::class.java)
        }
    }

    /**
     * Setup file watcher to reload modules when modules.json changes
     */
    private fun setupFileWatcher() {
        project.messageBus.connect().subscribe(
            VirtualFileManager.VFS_CHANGES,
            object : BulkFileListener {
                override fun after(events: List<VFileEvent>) {
                    for (event in events) {
                        val file = event.file
                        if (file != null && file.name == "modules.json") {
                            logger.info("modules.json changed, clearing cache: ${file.path}")
                            clearCache()
                        }
                    }
                }
            }
        )
    }

    /**
     * Load modules from modules.json file(s)
     * Loads from multiple sources and merges them:
     * 1. Project root modules.json
     * 2. workflows/modules.json
     * 3. runtime/workflows/modules.json
     * 4. Plugin bundled resources (fallback)
     */
    fun loadModules(): Map<String, ModuleData> {
        if (modulesCache != null) {
            return modulesCache!!
        }

        val modules = mutableMapOf<String, ModuleData>()
        var loadedFromProject = false

        val projectBasePath = project.basePath
        if (projectBasePath != null) {
            // Define all possible module file locations
            val modulePaths = listOf(
                File(projectBasePath, "modules.json") to "project root",
                File(projectBasePath, "workflows/modules.json") to "workflows folder",
                File(projectBasePath, "runtime/workflows/modules.json") to "runtime/workflows folder"
            )

            // Try to load from each location, skip on errors
            for ((moduleFile, location) in modulePaths) {
                if (moduleFile.exists()) {
                    try {
                        val loadedModules = parseModulesJson(moduleFile.reader())
                        modules.putAll(loadedModules)
                        logger.info("Loaded ${loadedModules.size} modules from $location: ${moduleFile.absolutePath}")
                        loadedFromProject = true
                    } catch (e: Exception) {
                        logger.warn("Failed to parse modules.json from $location: ${moduleFile.absolutePath}, skipping", e)
                        // Continue to next file instead of breaking
                    }
                }
            }
        }

        // Fallback to plugin resources if no project modules found
        if (!loadedFromProject) {
            try {
                val resourceStream = javaClass.classLoader.getResourceAsStream("modules.json")
                if (resourceStream != null) {
                    modules.putAll(parseModulesJson(InputStreamReader(resourceStream)))
                    logger.info("Loaded ${modules.size} modules from plugin resources")
                } else {
                    logger.warn("modules.json not found in any project locations or plugin resources")
                }
            } catch (e: Exception) {
                logger.warn("Failed to parse bundled modules.json, skipping", e)
            }
        } else {
            logger.info("Total ${modules.size} modules loaded from all sources")
        }

        modulesCache = modules
        return modules
    }

    /**
     * Parse modules.json content
     */
    private fun parseModulesJson(reader: java.io.Reader): Map<String, ModuleData> {
        val modules = mutableMapOf<String, ModuleData>()

        try {
            val jsonElement = JsonParser.parseReader(reader)
            if (!jsonElement.isJsonObject) {
                logger.warn("modules.json is not a valid JSON object")
                return modules
            }

            val rootObject = jsonElement.asJsonObject

            for ((modulePath, moduleElement) in rootObject.entrySet()) {
                try {
                    if (!moduleElement.isJsonObject) continue

                    val moduleObj = moduleElement.asJsonObject
                    val infoObj = moduleObj.getAsJsonObject("info")
                    val methodsArray = moduleObj.getAsJsonArray("methods")

                    if (infoObj == null || methodsArray == null) continue

                    val info = ModuleInfo(
                        namespace = infoObj.get("namespace")?.asString ?: "",
                        `class` = infoObj.get("class")?.asString ?: "",
                        label = infoObj.get("label")?.asString ?: modulePath,
                        description = infoObj.get("description")?.asString ?: ""
                    )

                    val methods = methodsArray.mapNotNull { methodElement ->
                        if (!methodElement.isJsonObject) return@mapNotNull null
                        val methodObj = methodElement.asJsonObject
                        ModuleMethod(
                            value = methodObj.get("value")?.asString ?: "",
                            label = methodObj.get("label")?.asString ?: "",
                            description = methodObj.get("description")?.asString ?: ""
                        )
                    }

                    modules[modulePath] = ModuleData(info, methods)
                } catch (e: Exception) {
                    logger.warn("Failed to parse module: $modulePath", e)
                }
            }
        } catch (e: Exception) {
            logger.error("Failed to parse modules.json", e)
        } finally {
            reader.close()
        }

        return modules
    }

    /**
     * Get all module paths
     */
    fun getModulePaths(): List<String> {
        return loadModules().keys.toList()
    }

    /**
     * Get module data by path
     */
    fun getModule(path: String): ModuleData? {
        return loadModules()[path]
    }

    /**
     * Get all methods for a module path
     */
    fun getModuleMethods(path: String): List<ModuleMethod> {
        return getModule(path)?.methods ?: emptyList()
    }

    /**
     * Clear the cache to force reload
     */
    fun clearCache() {
        modulesCache = null
    }
}


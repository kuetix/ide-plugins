package com.kuetix.wsl.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.util.ProcessingContext
import com.kuetix.wsl.language.WslIcons
import com.kuetix.wsl.language.WslLanguage

/**
 * Code completion contributor for WSL language.
 * Provides keyword and snippet completions.
 */
class WslCompletionContributor : CompletionContributor() {
    
    init {
        // Add completion for all positions
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement().withLanguage(WslLanguage),
            WslCompletionProvider()
        )
    }
}

/**
 * Completion provider for WSL elements.
 */
class WslCompletionProvider : CompletionProvider<CompletionParameters>() {
    
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        val position = parameters.position

        // Add keyword completions
        addKeywordCompletions(result, position)
        
        // Add snippet completions based on context
        addSnippetCompletions(result, position)
    }
    
    private fun addKeywordCompletions(result: CompletionResultSet, position: PsiElement) {
        // Header keywords
        HEADER_KEYWORDS.forEach { (keyword, description) ->
            result.addElement(
                LookupElementBuilder.create(keyword)
                    .withIcon(WslIcons.FILE)
                    .withTypeText(description)
                    .withBoldness(true)
            )
        }
        
        // Workflow keywords
        WORKFLOW_KEYWORDS.forEach { (keyword, description) ->
            result.addElement(
                LookupElementBuilder.create(keyword)
                    .withIcon(WslIcons.FILE)
                    .withTypeText(description)
                    .withBoldness(true)
            )
        }
        
        // State keywords
        STATE_KEYWORDS.forEach { (keyword, description) ->
            result.addElement(
                LookupElementBuilder.create(keyword)
                    .withIcon(WslIcons.FILE)
                    .withTypeText(description)
                    .withBoldness(true)
            )
        }
        
        // Transition keywords
        TRANSITION_KEYWORDS.forEach { (keyword, description) ->
            result.addElement(
                LookupElementBuilder.create(keyword)
                    .withIcon(WslIcons.FILE)
                    .withTypeText(description)
                    .withBoldness(true)
            )
        }
        
        // Boolean keywords
        result.addElement(
            LookupElementBuilder.create("true")
                .withIcon(WslIcons.FILE)
                .withTypeText("Boolean literal")
        )
        result.addElement(
            LookupElementBuilder.create("false")
                .withIcon(WslIcons.FILE)
                .withTypeText("Boolean literal")
        )

        // Add module completions from modules.json
        addModuleCompletions(result, position)
    }

    private fun addModuleCompletions(result: CompletionResultSet, position: PsiElement) {
        try {
            val project = position.project
            val moduleService = WslModuleService.getInstance(project)
            val modules = moduleService.loadModules()

            // Add module path completions
            modules.forEach { (modulePath, moduleData) ->
                result.addElement(
                    LookupElementBuilder.create(modulePath)
                        .withIcon(WslIcons.FILE)
                        .withTypeText(moduleData.info.description)
                        .withTailText(" (module)", true)
                )

                // Add method completions for each module
                moduleData.methods.forEach { method ->
                    val fullMethodPath = "$modulePath.${method.value}()"
                    result.addElement(
                        LookupElementBuilder.create(fullMethodPath)
                            .withIcon(WslIcons.FILE)
                            .withTypeText(method.description)
                            .withTailText(" {${moduleData.info.label}}", true)
                            .withPresentableText("${method.label}()")
                            .withLookupString(method.value)
                            .withLookupString(method.label)
                    )
                }
            }
        } catch (e: Exception) {
            // Fail silently - module completions are optional
        }
    }
    
    @Suppress("UNUSED_PARAMETER")
    private fun addSnippetCompletions(result: CompletionResultSet, position: PsiElement) {
        // Workflow template - positions cursor at workflow_name for immediate editing
        result.addElement(
            LookupElementBuilder.create("workflow")
                .withIcon(WslIcons.FILE)
                .withTailText(" { ... }")
                .withTypeText("Workflow declaration")
                .withInsertHandler { context, _ ->
                    val document = context.document
                    val offset = context.tailOffset
                    val template = " workflow_name {\n  start: InitialState\n\n  state InitialState {\n    action your/action()\n    end ok\n  }\n}"
                    document.insertString(offset, template)
                    // Select "workflow_name" for easy editing (offset + 1 to skip space, length 13 for "workflow_name")
                    context.editor.selectionModel.setSelection(offset + 1, offset + 14)
                    context.editor.caretModel.moveToOffset(offset + 14)
                }
        )
        
        // State template - positions cursor at StateName for immediate editing
        result.addElement(
            LookupElementBuilder.create("state")
                .withIcon(WslIcons.FILE)
                .withTailText(" { ... }")
                .withTypeText("State declaration")
                .withInsertHandler { context, _ ->
                    val document = context.document
                    val offset = context.tailOffset
                    val template = " StateName {\n    action your/action()\n    on success -> NextState\n    on error -> ErrorState\n  }"
                    document.insertString(offset, template)
                    // Select "StateName" for easy editing
                    context.editor.selectionModel.setSelection(offset + 1, offset + 10)
                    context.editor.caretModel.moveToOffset(offset + 10)
                }
        )
        
        // Action template - positions cursor at path for editing
        result.addElement(
            LookupElementBuilder.create("action")
                .withIcon(WslIcons.FILE)
                .withTailText(" path/to.Action()")
                .withTypeText("Action statement")
                .withInsertHandler { context, _ ->
                    val document = context.document
                    val offset = context.tailOffset
                    val template = " path/to.Action()"
                    document.insertString(offset, template)
                    // Select "path/to.Action" for easy editing
                    context.editor.selectionModel.setSelection(offset + 1, offset + 15)
                    context.editor.caretModel.moveToOffset(offset + 15)
                }
        )
        
        // On success template
        result.addElement(
            LookupElementBuilder.create("on success")
                .withIcon(WslIcons.FILE)
                .withTailText(" -> NextState")
                .withTypeText("Success transition")
                .withInsertHandler { context, _ ->
                    val document = context.document
                    val offset = context.tailOffset
                    document.insertString(offset, " -> NextState")
                    context.editor.caretModel.moveToOffset(offset + 4)
                }
        )
        
        // On error template
        result.addElement(
            LookupElementBuilder.create("on error")
                .withIcon(WslIcons.FILE)
                .withTailText(" -> ErrorState")
                .withTypeText("Error transition")
                .withInsertHandler { context, _ ->
                    val document = context.document
                    val offset = context.tailOffset
                    document.insertString(offset, " -> ErrorState")
                    context.editor.caretModel.moveToOffset(offset + 4)
                }
        )
        
        // End ok template
        result.addElement(
            LookupElementBuilder.create("end ok")
                .withIcon(WslIcons.FILE)
                .withTypeText("Terminal success state")
        )
        
        // End error template
        result.addElement(
            LookupElementBuilder.create("end error")
                .withIcon(WslIcons.FILE)
                .withTypeText("Terminal error state")
        )
        
        // Module template
        result.addElement(
            LookupElementBuilder.create("module")
                .withIcon(WslIcons.FILE)
                .withTailText(" namespace.name")
                .withTypeText("Module declaration")
                .withInsertHandler { context, _ ->
                    val document = context.document
                    val offset = context.tailOffset
                    document.insertString(offset, " your.module.name")
                    context.editor.caretModel.moveToOffset(offset + 1)
                }
        )
        
        // Import template
        result.addElement(
            LookupElementBuilder.create("import")
                .withIcon(WslIcons.FILE)
                .withTailText(" path/to/module")
                .withTypeText("Import declaration")
                .withInsertHandler { context, _ ->
                    val document = context.document
                    val offset = context.tailOffset
                    document.insertString(offset, " services/common")
                    context.editor.caretModel.moveToOffset(offset + 1)
                }
        )
        
        // Const template
        result.addElement(
            LookupElementBuilder.create("const")
                .withIcon(WslIcons.FILE)
                .withTailText(" { ... }")
                .withTypeText("Constants block")
                .withInsertHandler { context, _ ->
                    val document = context.document
                    val offset = context.tailOffset
                    document.insertString(offset, " {\n    key: \"value\"\n}")
                    context.editor.caretModel.moveToOffset(offset + 7)
                }
        )
        
        // Extends template
        result.addElement(
            LookupElementBuilder.create("extends")
                .withIcon(WslIcons.FILE)
                .withTailText(" \"resolver\"")
                .withTypeText("Extends declaration")
                .withInsertHandler { context, _ ->
                    val document = context.document
                    val offset = context.tailOffset
                    document.insertString(offset, " \"resolvers\"")
                    context.editor.caretModel.moveToOffset(offset + 2)
                }
        )


    }
    
    companion object {
        private val HEADER_KEYWORDS = mapOf(
            "module" to "Module declaration",
            "import" to "Import module",
            "extends" to "Extend configuration",
            "const" to "Constants block"
        )
        
        private val WORKFLOW_KEYWORDS = mapOf(
            "workflow" to "Workflow declaration",
            "start" to "Start state"
        )
        
        private val STATE_KEYWORDS = mapOf(
            "state" to "State declaration",
            "action" to "Action statement",
            "as" to "Alias"
        )
        
        private val TRANSITION_KEYWORDS = mapOf(
            "on" to "Transition condition",
            "success" to "Success outcome",
            "error" to "Error outcome",
            "end" to "Terminal state",
            "ok" to "OK status",
            "fail" to "Fail status"
        )
    }
}

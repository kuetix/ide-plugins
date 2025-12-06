package com.kuetix.wsl.language

import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

/**
 * WSL file type definition (.wsl extension).
 */
class WslFileType private constructor() : LanguageFileType(WslLanguage) {
    override fun getName(): String = "WSL"

    override fun getDescription(): String = "WSL workflow file"

    override fun getDefaultExtension(): String = "wsl"

    override fun getIcon(): Icon = WslIcons.FILE
    
    companion object {
        @JvmField
        val INSTANCE = WslFileType()
    }
}

/**
 * Icons used by the WSL plugin.
 */
object WslIcons {
    @JvmField
    val FILE: Icon = IconLoader.getIcon("/icons/wsl.svg", WslIcons::class.java)
}

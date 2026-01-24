package com.kuetix.swsl.language

import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.util.IconLoader
import javax.swing.Icon
import com.kuetix.wsl.language.WslIcons

/**
 * SimplifiedWSL file type definition (.swsl extension).
 */
class SimplifiedWslFileType private constructor() : LanguageFileType(SimplifiedWslLanguage) {
    override fun getName(): String = "SimplifiedWSL"

    override fun getDescription(): String = "SimplifiedWSL workflow file"

    override fun getDefaultExtension(): String = "swsl"

    override fun getIcon(): Icon = WslIcons.FILE
    
    companion object {
        @JvmField
        val INSTANCE = SimplifiedWslFileType()
    }
}

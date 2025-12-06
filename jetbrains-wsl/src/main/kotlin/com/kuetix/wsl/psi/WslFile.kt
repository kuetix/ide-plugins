package com.kuetix.wsl.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.kuetix.wsl.language.WslFileType
import com.kuetix.wsl.language.WslLanguage

/**
 * PSI file for WSL documents.
 */
class WslFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, WslLanguage) {
    override fun getFileType(): FileType = WslFileType.INSTANCE

    override fun toString(): String = "WSL File"
}

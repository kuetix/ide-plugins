package com.kuetix.swsl.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.kuetix.swsl.language.SimplifiedWslFileType
import com.kuetix.swsl.language.SimplifiedWslLanguage

/**
 * PSI file for SimplifiedWSL documents.
 */
class SimplifiedWslFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, SimplifiedWslLanguage) {
    override fun getFileType(): FileType = SimplifiedWslFileType.INSTANCE

    override fun toString(): String = "SimplifiedWSL File"
}

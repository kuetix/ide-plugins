package com.kuetix.wsl.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import com.kuetix.wsl.language.WslLanguage
import com.kuetix.wsl.lexer.WslLexer
import com.kuetix.wsl.lexer.WslTokenTypes
import com.kuetix.wsl.psi.WslFile

/**
 * Parser definition for WSL language.
 */
class WslParserDefinition : ParserDefinition {
    
    override fun createLexer(project: Project?): Lexer = WslLexer()

    override fun createParser(project: Project?): PsiParser = WslParser()

    override fun getFileNodeType(): IFileElementType = FILE

    override fun getCommentTokens(): TokenSet = WslTokenTypes.COMMENTS

    override fun getStringLiteralElements(): TokenSet = WslTokenTypes.STRINGS

    override fun createElement(node: ASTNode): PsiElement = 
        WslPsiElementFactory.createElement(node)

    override fun createFile(viewProvider: FileViewProvider): PsiFile = 
        WslFile(viewProvider)

    companion object {
        val FILE = IFileElementType(WslLanguage)
    }
}

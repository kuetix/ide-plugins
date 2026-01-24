package com.kuetix.swsl.parser

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
import com.kuetix.swsl.language.SimplifiedWslLanguage
import com.kuetix.swsl.lexer.SimplifiedWslLexer
import com.kuetix.swsl.lexer.SimplifiedWslTokenTypes
import com.kuetix.swsl.psi.SimplifiedWslFile
import com.kuetix.swsl.psi.SimplifiedWslPsiElement

/**
 * Parser definition for SimplifiedWSL language.
 */
class SimplifiedWslParserDefinition : ParserDefinition {
    
    override fun createLexer(project: Project?): Lexer = SimplifiedWslLexer()

    override fun createParser(project: Project?): PsiParser = SimplifiedWslParser()

    override fun getFileNodeType(): IFileElementType = FILE

    override fun getCommentTokens(): TokenSet = SimplifiedWslTokenTypes.COMMENTS

    override fun getStringLiteralElements(): TokenSet = SimplifiedWslTokenTypes.STRINGS

    override fun createElement(node: ASTNode): PsiElement = 
        SimplifiedWslPsiElement(node)

    override fun createFile(viewProvider: FileViewProvider): PsiFile = 
        SimplifiedWslFile(viewProvider)

    companion object {
        val FILE = IFileElementType(SimplifiedWslLanguage)
    }
}

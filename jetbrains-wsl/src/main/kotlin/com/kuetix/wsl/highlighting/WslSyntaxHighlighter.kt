package com.kuetix.wsl.highlighting

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.tree.IElementType
import com.kuetix.wsl.lexer.WslLexer
import com.kuetix.wsl.lexer.WslTokenTypes

/**
 * Syntax highlighter factory for WSL files.
 */
class WslSyntaxHighlighterFactory : SyntaxHighlighterFactory() {
    override fun getSyntaxHighlighter(project: Project?, virtualFile: VirtualFile?): SyntaxHighlighter {
        return WslSyntaxHighlighter()
    }
}

/**
 * Syntax highlighter for WSL language.
 * Provides color information for different token types.
 */
class WslSyntaxHighlighter : SyntaxHighlighterBase() {
    
    override fun getHighlightingLexer(): Lexer = WslLexer()
    
    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        return when {
            WslTokenTypes.KEYWORDS.contains(tokenType) -> KEYWORD_KEYS
            WslTokenTypes.LITERALS.contains(tokenType) -> KEYWORD_KEYS
            WslTokenTypes.OPERATORS.contains(tokenType) -> OPERATOR_KEYS
            WslTokenTypes.COMMENTS.contains(tokenType) -> COMMENT_KEYS
            tokenType == WslTokenTypes.STRING -> STRING_KEYS
            tokenType == WslTokenTypes.NUMBER -> NUMBER_KEYS
            tokenType == WslTokenTypes.IDENTIFIER -> IDENTIFIER_KEYS
            tokenType == WslTokenTypes.LBRACE || tokenType == WslTokenTypes.RBRACE -> BRACES_KEYS
            tokenType == WslTokenTypes.LPAREN || tokenType == WslTokenTypes.RPAREN -> PARENTHESES_KEYS
            tokenType == WslTokenTypes.LBRACKET || tokenType == WslTokenTypes.RBRACKET -> BRACES_KEYS
            tokenType == WslTokenTypes.DOLLAR -> REFERENCE_KEYS
            tokenType == WslTokenTypes.TEMPLATE_START || tokenType == WslTokenTypes.TEMPLATE_END -> TEMPLATE_KEYS
            tokenType == WslTokenTypes.COLON ||
                tokenType == WslTokenTypes.COMMA ||
                tokenType == WslTokenTypes.DOT ||
                tokenType == WslTokenTypes.SLASH -> PUNCTUATION_KEYS
            tokenType == WslTokenTypes.BAD_CHARACTER -> BAD_CHAR_KEYS
            else -> EMPTY_KEYS
        }
    }
    
    companion object {
        // Text attribute keys
        val KEYWORD = createTextAttributesKey("WSL_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
        val COMMENT = createTextAttributesKey("WSL_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
        val STRING = createTextAttributesKey("WSL_STRING", DefaultLanguageHighlighterColors.STRING)
        val NUMBER = createTextAttributesKey("WSL_NUMBER", DefaultLanguageHighlighterColors.NUMBER)
        val IDENTIFIER = createTextAttributesKey("WSL_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER)
        val OPERATOR = createTextAttributesKey("WSL_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN)
        val BRACES = createTextAttributesKey("WSL_BRACES", DefaultLanguageHighlighterColors.BRACES)
        val PARENTHESES = createTextAttributesKey("WSL_PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES)
        val REFERENCE = createTextAttributesKey("WSL_REFERENCE", DefaultLanguageHighlighterColors.INSTANCE_FIELD)
        val TEMPLATE = createTextAttributesKey("WSL_TEMPLATE", DefaultLanguageHighlighterColors.MARKUP_TAG)
        val PUNCTUATION = createTextAttributesKey("WSL_PUNCTUATION", DefaultLanguageHighlighterColors.DOT)
        val BAD_CHARACTER = createTextAttributesKey("WSL_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER)
        
        // Token key arrays
        private val KEYWORD_KEYS = arrayOf(KEYWORD)
        private val COMMENT_KEYS = arrayOf(COMMENT)
        private val STRING_KEYS = arrayOf(STRING)
        private val NUMBER_KEYS = arrayOf(NUMBER)
        private val IDENTIFIER_KEYS = arrayOf(IDENTIFIER)
        private val OPERATOR_KEYS = arrayOf(OPERATOR)
        private val BRACES_KEYS = arrayOf(BRACES)
        private val PARENTHESES_KEYS = arrayOf(PARENTHESES)
        private val REFERENCE_KEYS = arrayOf(REFERENCE)
        private val TEMPLATE_KEYS = arrayOf(TEMPLATE)
        private val PUNCTUATION_KEYS = arrayOf(PUNCTUATION)
        private val BAD_CHAR_KEYS = arrayOf(BAD_CHARACTER)
        private val EMPTY_KEYS = emptyArray<TextAttributesKey>()
    }
}

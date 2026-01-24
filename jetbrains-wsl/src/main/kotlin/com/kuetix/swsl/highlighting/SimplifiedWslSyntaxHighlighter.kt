package com.kuetix.swsl.highlighting

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
import com.kuetix.swsl.lexer.SimplifiedWslLexer
import com.kuetix.swsl.lexer.SimplifiedWslTokenTypes

/**
 * Syntax highlighter factory for SimplifiedWSL files.
 */
class SimplifiedWslSyntaxHighlighterFactory : SyntaxHighlighterFactory() {
    override fun getSyntaxHighlighter(project: Project?, virtualFile: VirtualFile?): SyntaxHighlighter {
        return SimplifiedWslSyntaxHighlighter()
    }
}

/**
 * Syntax highlighter for SimplifiedWSL language.
 * Provides color information for different token types.
 */
class SimplifiedWslSyntaxHighlighter : SyntaxHighlighterBase() {
    
    override fun getHighlightingLexer(): Lexer = SimplifiedWslLexer()
    
    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        return when (tokenType) {
            // Keywords
            SimplifiedWslTokenTypes.MODULE,
            SimplifiedWslTokenTypes.IMPORT,
            SimplifiedWslTokenTypes.AS,
            SimplifiedWslTokenTypes.CONTEXT,
            SimplifiedWslTokenTypes.CONST,
            SimplifiedWslTokenTypes.EXTENDS,
            SimplifiedWslTokenTypes.DEF -> KEYWORD_KEYS
            
            // Workflow type keywords
            SimplifiedWslTokenTypes.WORKFLOW,
            SimplifiedWslTokenTypes.FEATURE,
            SimplifiedWslTokenTypes.SOLUTION -> WORKFLOW_TYPE_KEYS
            
            // Boolean and null
            SimplifiedWslTokenTypes.TRUE,
            SimplifiedWslTokenTypes.FALSE,
            SimplifiedWslTokenTypes.NULL -> CONSTANT_KEYS
            
            // Comments
            SimplifiedWslTokenTypes.LINE_COMMENT,
            SimplifiedWslTokenTypes.BLOCK_COMMENT -> COMMENT_KEYS
            
            // Strings
            SimplifiedWslTokenTypes.STRING -> STRING_KEYS
            
            // Numbers
            SimplifiedWslTokenTypes.NUMBER -> NUMBER_KEYS
            
            // Identifiers
            SimplifiedWslTokenTypes.IDENTIFIER -> IDENTIFIER_KEYS
            
            // Operators
            SimplifiedWslTokenTypes.ARROW,
            SimplifiedWslTokenTypes.ERROR_ARROW -> OPERATOR_KEYS
            SimplifiedWslTokenTypes.PIPE,
            SimplifiedWslTokenTypes.EQUAL -> OPERATOR_KEYS
            
            // Braces
            SimplifiedWslTokenTypes.LBRACE,
            SimplifiedWslTokenTypes.RBRACE -> BRACES_KEYS
            
            // Parentheses
            SimplifiedWslTokenTypes.LPAREN,
            SimplifiedWslTokenTypes.RPAREN -> PARENTHESES_KEYS
            
            // Brackets
            SimplifiedWslTokenTypes.LBRACKET,
            SimplifiedWslTokenTypes.RBRACKET -> BRACKETS_KEYS
            
            // Dollar sign for references
            SimplifiedWslTokenTypes.DOLLAR -> REFERENCE_KEYS
            
            // Template markers
            SimplifiedWslTokenTypes.TEMPLATE_START,
            SimplifiedWslTokenTypes.TEMPLATE_END -> TEMPLATE_KEYS
            
            // Punctuation
            SimplifiedWslTokenTypes.COLON,
            SimplifiedWslTokenTypes.COMMA,
            SimplifiedWslTokenTypes.DOT,
            SimplifiedWslTokenTypes.SLASH -> PUNCTUATION_KEYS
            
            // Bad character
            SimplifiedWslTokenTypes.BAD_CHARACTER -> BAD_CHAR_KEYS
            
            else -> EMPTY_KEYS
        }
    }
    
    companion object {
        // Text attribute keys
        val KEYWORD = createTextAttributesKey("SWSL_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
        val WORKFLOW_TYPE = createTextAttributesKey("SWSL_WORKFLOW_TYPE", DefaultLanguageHighlighterColors.CLASS_NAME)
        val CONSTANT = createTextAttributesKey("SWSL_CONSTANT", DefaultLanguageHighlighterColors.CONSTANT)
        val COMMENT = createTextAttributesKey("SWSL_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
        val STRING = createTextAttributesKey("SWSL_STRING", DefaultLanguageHighlighterColors.STRING)
        val NUMBER = createTextAttributesKey("SWSL_NUMBER", DefaultLanguageHighlighterColors.NUMBER)
        val IDENTIFIER = createTextAttributesKey("SWSL_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER)
        val OPERATOR = createTextAttributesKey("SWSL_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN)
        val BRACES = createTextAttributesKey("SWSL_BRACES", DefaultLanguageHighlighterColors.BRACES)
        val PARENTHESES = createTextAttributesKey("SWSL_PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES)
        val BRACKETS = createTextAttributesKey("SWSL_BRACKETS", DefaultLanguageHighlighterColors.BRACKETS)
        val REFERENCE = createTextAttributesKey("SWSL_REFERENCE", DefaultLanguageHighlighterColors.INSTANCE_FIELD)
        val TEMPLATE = createTextAttributesKey("SWSL_TEMPLATE", DefaultLanguageHighlighterColors.MARKUP_TAG)
        val PUNCTUATION = createTextAttributesKey("SWSL_PUNCTUATION", DefaultLanguageHighlighterColors.DOT)
        val BAD_CHARACTER = createTextAttributesKey("SWSL_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER)
        
        // Token key arrays
        private val KEYWORD_KEYS = arrayOf(KEYWORD)
        private val WORKFLOW_TYPE_KEYS = arrayOf(WORKFLOW_TYPE)
        private val CONSTANT_KEYS = arrayOf(CONSTANT)
        private val COMMENT_KEYS = arrayOf(COMMENT)
        private val STRING_KEYS = arrayOf(STRING)
        private val NUMBER_KEYS = arrayOf(NUMBER)
        private val IDENTIFIER_KEYS = arrayOf(IDENTIFIER)
        private val OPERATOR_KEYS = arrayOf(OPERATOR)
        private val BRACES_KEYS = arrayOf(BRACES)
        private val PARENTHESES_KEYS = arrayOf(PARENTHESES)
        private val BRACKETS_KEYS = arrayOf(BRACKETS)
        private val REFERENCE_KEYS = arrayOf(REFERENCE)
        private val TEMPLATE_KEYS = arrayOf(TEMPLATE)
        private val PUNCTUATION_KEYS = arrayOf(PUNCTUATION)
        private val BAD_CHAR_KEYS = arrayOf(BAD_CHARACTER)
        private val EMPTY_KEYS = emptyArray<TextAttributesKey>()
    }
}

package com.kuetix.swsl.lexer

import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet

/**
 * Token types for SimplifiedWSL lexer.
 * These extend WSL tokens with SimplifiedWSL-specific tokens.
 */
object SimplifiedWslTokenTypes {
    // Special tokens
    @JvmField val WHITE_SPACE: IElementType = TokenType.WHITE_SPACE
    @JvmField val BAD_CHARACTER: IElementType = TokenType.BAD_CHARACTER

    // Comments
    @JvmField val LINE_COMMENT = SimplifiedWslTokenType("LINE_COMMENT")
    @JvmField val BLOCK_COMMENT = SimplifiedWslTokenType("BLOCK_COMMENT")

    // Identifiers and literals
    @JvmField val IDENTIFIER = SimplifiedWslTokenType("IDENTIFIER")
    @JvmField val NUMBER = SimplifiedWslTokenType("NUMBER")
    @JvmField val STRING = SimplifiedWslTokenType("STRING")

    // Basic keywords
    @JvmField val MODULE = SimplifiedWslTokenType("module")
    @JvmField val IMPORT = SimplifiedWslTokenType("import")
    @JvmField val AS = SimplifiedWslTokenType("as")
    @JvmField val CONTEXT = SimplifiedWslTokenType("context")
    @JvmField val CONST = SimplifiedWslTokenType("const")
    @JvmField val EXTENDS = SimplifiedWslTokenType("extends")

    // SimplifiedWSL-specific keywords
    @JvmField val DEF = SimplifiedWslTokenType("def")
    @JvmField val WORKFLOW = SimplifiedWslTokenType("workflow")
    @JvmField val FEATURE = SimplifiedWslTokenType("feature")
    @JvmField val SOLUTION = SimplifiedWslTokenType("solution")
    
    // Boolean literals
    @JvmField val TRUE = SimplifiedWslTokenType("true")
    @JvmField val FALSE = SimplifiedWslTokenType("false")
    @JvmField val NULL = SimplifiedWslTokenType("null")

    // Punctuators/operators
    @JvmField val LBRACE = SimplifiedWslTokenType("{")
    @JvmField val RBRACE = SimplifiedWslTokenType("}")
    @JvmField val LPAREN = SimplifiedWslTokenType("(")
    @JvmField val RPAREN = SimplifiedWslTokenType(")")
    @JvmField val LBRACKET = SimplifiedWslTokenType("[")
    @JvmField val RBRACKET = SimplifiedWslTokenType("]")
    @JvmField val COLON = SimplifiedWslTokenType(":")
    @JvmField val COMMA = SimplifiedWslTokenType(",")
    @JvmField val ARROW = SimplifiedWslTokenType("->")
    @JvmField val ERROR_ARROW = SimplifiedWslTokenType("<-")
    @JvmField val DOT = SimplifiedWslTokenType(".")
    @JvmField val EQUAL = SimplifiedWslTokenType("=")
    @JvmField val PIPE = SimplifiedWslTokenType("|")
    @JvmField val DOLLAR = SimplifiedWslTokenType("$")
    @JvmField val SLASH = SimplifiedWslTokenType("/")

    // Template markers
    @JvmField val TEMPLATE_START = SimplifiedWslTokenType("<<")
    @JvmField val TEMPLATE_END = SimplifiedWslTokenType(">>")

    // Token sets for syntax highlighting
    @JvmField val KEYWORDS = TokenSet.create(
        MODULE, IMPORT, AS, CONTEXT, CONST, EXTENDS, DEF,
        WORKFLOW, FEATURE, SOLUTION, TRUE, FALSE, NULL
    )

    @JvmField val COMMENTS = TokenSet.create(LINE_COMMENT, BLOCK_COMMENT)

    @JvmField val STRINGS = TokenSet.create(STRING)

    @JvmField val NUMBERS = TokenSet.create(NUMBER)

    @JvmField val OPERATORS = TokenSet.create(ARROW, ERROR_ARROW, PIPE, EQUAL)

    @JvmField val BRACES = TokenSet.create(LBRACE, RBRACE)

    @JvmField val PARENTHESES = TokenSet.create(LPAREN, RPAREN)

    @JvmField val BRACKETS = TokenSet.create(LBRACKET, RBRACKET)

    @JvmField val WHITE_SPACES = TokenSet.create(WHITE_SPACE)

    // Map of keyword strings to tokens
    private val KEYWORD_MAP = mapOf(
        "module" to MODULE,
        "import" to IMPORT,
        "as" to AS,
        "context" to CONTEXT,
        "const" to CONST,
        "extends" to EXTENDS,
        "def" to DEF,
        "workflow" to WORKFLOW,
        "feature" to FEATURE,
        "solution" to SOLUTION,
        "true" to TRUE,
        "false" to FALSE,
        "null" to NULL
    )

    /**
     * Get the token type for a keyword, or IDENTIFIER if not a keyword.
     */
    fun getKeywordOrIdentifier(text: String): IElementType {
        return KEYWORD_MAP[text] ?: IDENTIFIER
    }
}

package com.kuetix.wsl.lexer

import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet

/**
 * Token types for WSL lexer.
 * These correspond to the tokens defined in the Go implementation.
 */
object WslTokenTypes {
    // Special tokens
    @JvmField val WHITE_SPACE: IElementType = TokenType.WHITE_SPACE
    @JvmField val BAD_CHARACTER: IElementType = TokenType.BAD_CHARACTER

    // Comments
    @JvmField val LINE_COMMENT = WslTokenType("LINE_COMMENT")
    @JvmField val BLOCK_COMMENT = WslTokenType("BLOCK_COMMENT")

    // Identifiers and literals
    @JvmField val IDENTIFIER = WslTokenType("IDENTIFIER")
    @JvmField val NUMBER = WslTokenType("NUMBER")
    @JvmField val STRING = WslTokenType("STRING")

    // Keywords
    @JvmField val MODULE = WslTokenType("module")
    @JvmField val IMPORT = WslTokenType("import")
    @JvmField val AS = WslTokenType("as")
    @JvmField val CONTEXT = WslTokenType("context")
    @JvmField val WORKFLOW = WslTokenType("workflow")
    @JvmField val START = WslTokenType("start")
    @JvmField val STATE = WslTokenType("state")
    @JvmField val ACTION = WslTokenType("action")
    @JvmField val ON = WslTokenType("on")
    @JvmField val END = WslTokenType("end")
    @JvmField val FAIL = WslTokenType("fail")
    @JvmField val OK = WslTokenType("ok")
    @JvmField val CONST = WslTokenType("const")
    @JvmField val EXTENDS = WslTokenType("extends")

    // Condition keywords
    @JvmField val SUCCESS = WslTokenType("success")
    @JvmField val ERROR = WslTokenType("error")
    @JvmField val ELSE = WslTokenType("else")
    
    // Boolean literals
    @JvmField val TRUE = WslTokenType("true")
    @JvmField val FALSE = WslTokenType("false")

    // Punctuators/operators
    @JvmField val LBRACE = WslTokenType("{")
    @JvmField val RBRACE = WslTokenType("}")
    @JvmField val LPAREN = WslTokenType("(")
    @JvmField val RPAREN = WslTokenType(")")
    @JvmField val COLON = WslTokenType(":")
    @JvmField val COMMA = WslTokenType(",")
    @JvmField val ARROW = WslTokenType("->")
    @JvmField val DOT = WslTokenType(".")
    @JvmField val EQUAL = WslTokenType("=")
    @JvmField val PIPE = WslTokenType("|")
    @JvmField val DOLLAR = WslTokenType("$")
    @JvmField val SLASH = WslTokenType("/")

    // Template markers
    @JvmField val TEMPLATE_START = WslTokenType("<<")
    @JvmField val TEMPLATE_END = WslTokenType(">>")

    // Token sets for syntax highlighting
    @JvmField val KEYWORDS = TokenSet.create(
        MODULE, IMPORT, AS, CONTEXT, WORKFLOW, START, STATE, ACTION,
        ON, END, FAIL, OK, CONST, EXTENDS, SUCCESS, ERROR, ELSE, TRUE, FALSE
    )

    @JvmField val COMMENTS = TokenSet.create(LINE_COMMENT, BLOCK_COMMENT)

    @JvmField val STRINGS = TokenSet.create(STRING)

    @JvmField val NUMBERS = TokenSet.create(NUMBER)

    @JvmField val OPERATORS = TokenSet.create(ARROW, PIPE, EQUAL)

    @JvmField val BRACES = TokenSet.create(LBRACE, RBRACE)

    @JvmField val PARENTHESES = TokenSet.create(LPAREN, RPAREN)

    @JvmField val WHITE_SPACES = TokenSet.create(WHITE_SPACE)

    // Map of keyword strings to tokens
    private val KEYWORD_MAP = mapOf(
        "module" to MODULE,
        "import" to IMPORT,
        "as" to AS,
        "context" to CONTEXT,
        "workflow" to WORKFLOW,
        "start" to START,
        "state" to STATE,
        "action" to ACTION,
        "on" to ON,
        "end" to END,
        "fail" to FAIL,
        "ok" to OK,
        "const" to CONST,
        "extends" to EXTENDS,
        "success" to SUCCESS,
        "error" to ERROR,
        "else" to ELSE,
        "true" to TRUE,
        "false" to FALSE
    )

    /**
     * Get the token type for a keyword, or IDENTIFIER if not a keyword.
     */
    fun getKeywordOrIdentifier(text: String): IElementType {
        return KEYWORD_MAP[text] ?: IDENTIFIER
    }
}

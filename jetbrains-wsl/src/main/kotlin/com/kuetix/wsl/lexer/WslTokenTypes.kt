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
    @JvmField val IF = WslTokenType("if")
    @JvmField val WHEN = WslTokenType("when")
    @JvmField val LET = WslTokenType("let")
    @JvmField val FOREACH = WslTokenType("foreach")
    @JvmField val IN = WslTokenType("in")
    @JvmField val WHILE = WslTokenType("while")
    @JvmField val RETRY = WslTokenType("retry")
    @JvmField val PARALLEL = WslTokenType("parallel")
    @JvmField val WAIT = WslTokenType("wait")
    @JvmField val JOIN = WslTokenType("join")
    @JvmField val BRANCH = WslTokenType("branch")
    @JvmField val CONTINUE = WslTokenType("continue")
    @JvmField val SKIP = WslTokenType("skip")
    @JvmField val DEF = WslTokenType("def")

    // Condition keywords
    @JvmField val SUCCESS = WslTokenType("success")
    @JvmField val ERROR = WslTokenType("error")
    @JvmField val ELSE = WslTokenType("else")

    // Boolean literals
    @JvmField val TRUE = WslTokenType("true")
    @JvmField val FALSE = WslTokenType("false")
    @JvmField val NULL = WslTokenType("null")

    // Punctuators/operators
    @JvmField val LBRACE = WslTokenType("{")
    @JvmField val RBRACE = WslTokenType("}")
    @JvmField val LPAREN = WslTokenType("(")
    @JvmField val RPAREN = WslTokenType(")")
    @JvmField val COLON = WslTokenType(":")
    @JvmField val COMMA = WslTokenType(",")
    @JvmField val ARROW = WslTokenType("->")
    @JvmField val LEFT_ARROW = WslTokenType("<-")
    @JvmField val DOT = WslTokenType(".")
    @JvmField val EQUAL = WslTokenType("=")
    @JvmField val PIPE = WslTokenType("|")
    @JvmField val DOLLAR = WslTokenType("$")
    @JvmField val SLASH = WslTokenType("/")
    @JvmField val LBRACKET = WslTokenType("[")
    @JvmField val RBRACKET = WslTokenType("]")

    // Expression operators
    @JvmField val EQ_EQ = WslTokenType("==")
    @JvmField val NEQ = WslTokenType("!=")
    @JvmField val GTE = WslTokenType(">=")
    @JvmField val LTE = WslTokenType("<=")
    @JvmField val AND_AND = WslTokenType("&&")
    @JvmField val OR_OR = WslTokenType("||")
    @JvmField val COALESCE = WslTokenType("??")
    @JvmField val LT = WslTokenType("<")
    @JvmField val GT = WslTokenType(">")
    @JvmField val BANG = WslTokenType("!")
    @JvmField val PLUS = WslTokenType("+")
    @JvmField val STAR = WslTokenType("*")
    @JvmField val PERCENT = WslTokenType("%")

    // Template markers
    @JvmField val TEMPLATE_START = WslTokenType("<<")
    @JvmField val TEMPLATE_END = WslTokenType(">>")

    // Token sets for syntax highlighting
    @JvmField val KEYWORDS = TokenSet.create(
        MODULE, IMPORT, AS, CONTEXT, WORKFLOW, START, STATE, ACTION,
        ON, END, FAIL, OK, CONST, EXTENDS, SUCCESS, ERROR, ELSE,
        IF, WHEN, LET, FOREACH, IN, WHILE, RETRY, PARALLEL, WAIT, JOIN,
        BRANCH, CONTINUE, SKIP, DEF
    )

    @JvmField val LITERALS = TokenSet.create(TRUE, FALSE, NULL)

    @JvmField val COMMENTS = TokenSet.create(LINE_COMMENT, BLOCK_COMMENT)

    @JvmField val STRINGS = TokenSet.create(STRING)

    @JvmField val NUMBERS = TokenSet.create(NUMBER)

    @JvmField val OPERATORS = TokenSet.create(
        ARROW, LEFT_ARROW, PIPE, EQUAL,
        EQ_EQ, NEQ, GTE, LTE, AND_AND, OR_OR, COALESCE,
        LT, GT, BANG, PLUS, STAR, PERCENT
    )

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
        "if" to IF,
        "when" to WHEN,
        "let" to LET,
        "foreach" to FOREACH,
        "in" to IN,
        "while" to WHILE,
        "retry" to RETRY,
        "parallel" to PARALLEL,
        "wait" to WAIT,
        "join" to JOIN,
        "branch" to BRANCH,
        "continue" to CONTINUE,
        "skip" to SKIP,
        "def" to DEF,
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

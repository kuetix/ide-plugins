package com.kuetix.wsl.lexer

import com.intellij.lexer.LexerBase
import com.intellij.psi.tree.IElementType

/**
 * Hand-written lexer for WSL language.
 * This mirrors the Go implementation in internal/wsl/lexer.go.
 */
class WslLexer : LexerBase() {
    private var buffer: CharSequence = ""
    private var bufferEnd: Int = 0
    private var tokenStart: Int = 0
    private var tokenEnd: Int = 0
    private var currentToken: IElementType? = null

    override fun start(buffer: CharSequence, startOffset: Int, endOffset: Int, initialState: Int) {
        this.buffer = buffer
        this.bufferEnd = endOffset
        this.tokenStart = startOffset
        this.tokenEnd = startOffset
        advance()
    }

    override fun getState(): Int = 0

    override fun getTokenType(): IElementType? = currentToken

    override fun getTokenStart(): Int = tokenStart

    override fun getTokenEnd(): Int = tokenEnd

    override fun advance() {
        tokenStart = tokenEnd
        if (tokenStart >= bufferEnd) {
            currentToken = null
            return
        }
        currentToken = scanToken()
    }

    override fun getBufferSequence(): CharSequence = buffer

    override fun getBufferEnd(): Int = bufferEnd

    private fun peek(): Char = if (tokenEnd < bufferEnd) buffer[tokenEnd] else '\u0000'

    private fun peekAt(offset: Int): Char = 
        if (tokenEnd + offset < bufferEnd) buffer[tokenEnd + offset] else '\u0000'

    private fun peekString(length: Int): String {
        val end = minOf(tokenEnd + length, bufferEnd)
        return buffer.subSequence(tokenEnd, end).toString()
    }

    private fun consumeChars(count: Int = 1) {
        tokenEnd = minOf(tokenEnd + count, bufferEnd)
    }

    private fun scanToken(): IElementType {
        val c = peek()

        // Whitespace
        if (c.isWhitespace()) {
            while (tokenEnd < bufferEnd && peek().isWhitespace()) {
                consumeChars()
            }
            return WslTokenTypes.WHITE_SPACE
        }

        // Line comment: // or #
        if (peekString(2) == "//") {
            consumeChars(2)
            while (tokenEnd < bufferEnd && peek() != '\n') {
                consumeChars()
            }
            return WslTokenTypes.LINE_COMMENT
        }
        if (c == '#') {
            consumeChars()
            while (tokenEnd < bufferEnd && peek() != '\n') {
                consumeChars()
            }
            return WslTokenTypes.LINE_COMMENT
        }

        // Block comment: /* ... */
        if (peekString(2) == "/*") {
            consumeChars(2)
            while (tokenEnd < bufferEnd) {
                if (peekString(2) == "*/") {
                    consumeChars(2)
                    break
                }
                consumeChars()
            }
            return WslTokenTypes.BLOCK_COMMENT
        }

        // Arrow: ->
        if (peekString(2) == "->") {
            consumeChars(2)
            return WslTokenTypes.ARROW
        }

        // Template markers: << and >>
        if (peekString(2) == "<<") {
            consumeChars(2)
            return WslTokenTypes.TEMPLATE_START
        }
        if (peekString(2) == ">>") {
            consumeChars(2)
            return WslTokenTypes.TEMPLATE_END
        }

        // Single character tokens
        when (c) {
            '{' -> { consumeChars(); return WslTokenTypes.LBRACE }
            '}' -> { consumeChars(); return WslTokenTypes.RBRACE }
            '(' -> { consumeChars(); return WslTokenTypes.LPAREN }
            ')' -> { consumeChars(); return WslTokenTypes.RPAREN }
            ':' -> { consumeChars(); return WslTokenTypes.COLON }
            ',' -> { consumeChars(); return WslTokenTypes.COMMA }
            '.' -> { consumeChars(); return WslTokenTypes.DOT }
            '=' -> { consumeChars(); return WslTokenTypes.EQUAL }
            '|' -> { consumeChars(); return WslTokenTypes.PIPE }
            '$' -> { consumeChars(); return WslTokenTypes.DOLLAR }
            '/' -> { consumeChars(); return WslTokenTypes.SLASH }
        }

        // String literal
        if (c == '"') {
            consumeChars()
            while (tokenEnd < bufferEnd) {
                val ch = peek()
                if (ch == '\\') {
                    consumeChars(2) // Skip escape sequence
                    continue
                }
                if (ch == '"') {
                    consumeChars()
                    break
                }
                consumeChars()
            }
            return WslTokenTypes.STRING
        }

        // Number literal
        if (c.isDigit()) {
            while (tokenEnd < bufferEnd && (peek().isDigit() || peek() == '.' || peek() == '_')) {
                consumeChars()
            }
            return WslTokenTypes.NUMBER
        }

        // Identifier or keyword
        if (isIdentStart(c)) {
            while (tokenEnd < bufferEnd && isIdentPart(peek())) {
                consumeChars()
            }
            val text = buffer.subSequence(tokenStart, tokenEnd).toString()
            return WslTokenTypes.getKeywordOrIdentifier(text)
        }

        // Unknown character
        consumeChars()
        return WslTokenTypes.BAD_CHARACTER
    }

    private fun isIdentStart(c: Char): Boolean = 
        c == '_' || c == '-' || c.isLetter()

    private fun isIdentPart(c: Char): Boolean = 
        isIdentStart(c) || c.isDigit() || c == '/' || c == '.'
}

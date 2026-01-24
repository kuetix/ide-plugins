package com.kuetix.swsl.lexer

import com.intellij.lexer.LexerBase
import com.intellij.psi.tree.IElementType

/**
 * Hand-written lexer for SimplifiedWSL language.
 * This is similar to WSL lexer but handles SimplifiedWSL-specific operators and keywords.
 */
class SimplifiedWslLexer : LexerBase() {
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
            return SimplifiedWslTokenTypes.WHITE_SPACE
        }

        // Line comment: // or #
        if (peekString(2) == "//") {
            consumeChars(2)
            while (tokenEnd < bufferEnd && peek() != '\n') {
                consumeChars()
            }
            return SimplifiedWslTokenTypes.LINE_COMMENT
        }
        if (c == '#') {
            consumeChars()
            while (tokenEnd < bufferEnd && peek() != '\n') {
                consumeChars()
            }
            return SimplifiedWslTokenTypes.LINE_COMMENT
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
            return SimplifiedWslTokenTypes.BLOCK_COMMENT
        }

        // Template markers: << and >>
        if (peekString(2) == "<<") {
            consumeChars(2)
            return SimplifiedWslTokenTypes.TEMPLATE_START
        }
        if (peekString(2) == ">>") {
            consumeChars(2)
            return SimplifiedWslTokenTypes.TEMPLATE_END
        }

        // Arrow operators: -> and <-
        if (peekString(2) == "->") {
            consumeChars(2)
            return SimplifiedWslTokenTypes.ARROW
        }
        if (peekString(2) == "<-") {
            consumeChars(2)
            return SimplifiedWslTokenTypes.ERROR_ARROW
        }

        // Single character tokens
        when (c) {
            '{' -> { consumeChars(); return SimplifiedWslTokenTypes.LBRACE }
            '}' -> { consumeChars(); return SimplifiedWslTokenTypes.RBRACE }
            '(' -> { consumeChars(); return SimplifiedWslTokenTypes.LPAREN }
            ')' -> { consumeChars(); return SimplifiedWslTokenTypes.RPAREN }
            '[' -> { consumeChars(); return SimplifiedWslTokenTypes.LBRACKET }
            ']' -> { consumeChars(); return SimplifiedWslTokenTypes.RBRACKET }
            ':' -> { consumeChars(); return SimplifiedWslTokenTypes.COLON }
            ',' -> { consumeChars(); return SimplifiedWslTokenTypes.COMMA }
            '.' -> { consumeChars(); return SimplifiedWslTokenTypes.DOT }
            '=' -> { consumeChars(); return SimplifiedWslTokenTypes.EQUAL }
            '|' -> { consumeChars(); return SimplifiedWslTokenTypes.PIPE }
            '$' -> { consumeChars(); return SimplifiedWslTokenTypes.DOLLAR }
            '/' -> { consumeChars(); return SimplifiedWslTokenTypes.SLASH }
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
            return SimplifiedWslTokenTypes.STRING
        }

        // Number literal
        if (c.isDigit()) {
            while (tokenEnd < bufferEnd && (peek().isDigit() || peek() == '.')) {
                consumeChars()
            }
            return SimplifiedWslTokenTypes.NUMBER
        }

        // Identifier or keyword
        if (c.isLetter() || c == '_') {
            while (tokenEnd < bufferEnd) {
                val ch = peek()
                if (ch.isLetterOrDigit() || ch == '_') {
                    consumeChars()
                } else {
                    break
                }
            }
            val text = buffer.subSequence(tokenStart, tokenEnd).toString()
            return SimplifiedWslTokenTypes.getKeywordOrIdentifier(text)
        }

        // Unknown character
        consumeChars()
        return SimplifiedWslTokenTypes.BAD_CHARACTER
    }
}

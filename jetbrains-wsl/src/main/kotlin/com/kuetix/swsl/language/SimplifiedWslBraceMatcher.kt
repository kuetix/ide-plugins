package com.kuetix.swsl.language

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import com.kuetix.swsl.lexer.SimplifiedWslTokenTypes

/**
 * Brace matcher for SimplifiedWSL language.
 * Matches curly braces {}, parentheses (), and brackets [].
 */
class SimplifiedWslBraceMatcher : PairedBraceMatcher {
    override fun getPairs(): Array<BracePair> = PAIRS

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean = true

    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int = openingBraceOffset

    companion object {
        private val PAIRS = arrayOf(
            BracePair(SimplifiedWslTokenTypes.LBRACE, SimplifiedWslTokenTypes.RBRACE, true),
            BracePair(SimplifiedWslTokenTypes.LPAREN, SimplifiedWslTokenTypes.RPAREN, false),
            BracePair(SimplifiedWslTokenTypes.LBRACKET, SimplifiedWslTokenTypes.RBRACKET, false)
        )
    }
}

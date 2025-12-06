package com.kuetix.wsl.language

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import com.kuetix.wsl.lexer.WslTokenTypes

/**
 * Brace matcher for WSL language.
 * Matches curly braces {} and parentheses ().
 */
class WslBraceMatcher : PairedBraceMatcher {
    override fun getPairs(): Array<BracePair> = PAIRS

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean = true

    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int): Int = openingBraceOffset

    companion object {
        private val PAIRS = arrayOf(
            BracePair(WslTokenTypes.LBRACE, WslTokenTypes.RBRACE, true),
            BracePair(WslTokenTypes.LPAREN, WslTokenTypes.RPAREN, false)
        )
    }
}

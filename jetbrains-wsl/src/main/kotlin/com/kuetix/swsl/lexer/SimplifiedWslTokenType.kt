package com.kuetix.swsl.lexer

import com.intellij.psi.tree.IElementType
import com.kuetix.swsl.language.SimplifiedWslLanguage

/**
 * Custom IElementType for SimplifiedWSL tokens.
 */
class SimplifiedWslTokenType(debugName: String) : IElementType(debugName, SimplifiedWslLanguage) {
    override fun toString(): String = "SimplifiedWslTokenType." + super.toString()
}

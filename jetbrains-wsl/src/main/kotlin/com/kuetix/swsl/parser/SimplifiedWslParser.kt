package com.kuetix.swsl.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType
import com.kuetix.swsl.lexer.SimplifiedWslTokenTypes

/**
 * Parser for SimplifiedWSL language.
 * Implements a simple parser that accepts all tokens without strict validation.
 * This provides syntax highlighting while allowing flexible SimplifiedWSL syntax.
 */
class SimplifiedWslParser : PsiParser {
    
    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
        val rootMarker = builder.mark()
        
        // Accept all tokens without strict structure validation
        // SimplifiedWSL has flexible syntax that's validated by the runtime engine
        while (!builder.eof()) {
            builder.advanceLexer()
        }
        
        rootMarker.done(root)
        return builder.treeBuilt
    }
}

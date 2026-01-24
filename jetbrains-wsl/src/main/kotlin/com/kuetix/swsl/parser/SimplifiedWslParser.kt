package com.kuetix.swsl.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType
import com.kuetix.swsl.lexer.SimplifiedWslTokenTypes

/**
 * Parser for SimplifiedWSL language.
 * Implements a simple parser that recognizes basic SimplifiedWSL structures.
 * This provides better IDE support while allowing flexible SimplifiedWSL syntax.
 */
class SimplifiedWslParser : PsiParser {
    
    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
        val rootMarker = builder.mark()
        
        // Parse SimplifiedWSL constructs with basic structure recognition
        while (!builder.eof()) {
            when (builder.tokenType) {
                SimplifiedWslTokenTypes.MODULE -> parseModule(builder)
                SimplifiedWslTokenTypes.CONST -> parseConst(builder)
                SimplifiedWslTokenTypes.DEF -> parseDef(builder)
                SimplifiedWslTokenTypes.FEATURE,
                SimplifiedWslTokenTypes.SOLUTION,
                SimplifiedWslTokenTypes.WORKFLOW -> parseWorkflowType(builder)
                else -> builder.advanceLexer()
            }
        }
        
        rootMarker.done(root)
        return builder.treeBuilt
    }
    
    private fun parseModule(builder: PsiBuilder) {
        // module name
        builder.advanceLexer() // consume 'module'
        while (!builder.eof() && 
               (builder.tokenType == SimplifiedWslTokenTypes.IDENTIFIER ||
                builder.tokenType == SimplifiedWslTokenTypes.DOT)) {
            builder.advanceLexer()
        }
    }
    
    private fun parseConst(builder: PsiBuilder) {
        // const { ... }
        builder.advanceLexer() // consume 'const'
        if (builder.tokenType == SimplifiedWslTokenTypes.LBRACE) {
            var depth = 0
            while (!builder.eof()) {
                if (builder.tokenType == SimplifiedWslTokenTypes.LBRACE) depth++
                else if (builder.tokenType == SimplifiedWslTokenTypes.RBRACE) {
                    depth--
                    builder.advanceLexer()
                    if (depth == 0) break
                    continue
                }
                builder.advanceLexer()
            }
        }
    }
    
    private fun parseDef(builder: PsiBuilder) {
        // def action.Call() as alias -> .
        builder.advanceLexer() // consume 'def'
        // Skip until we find terminal or end of line
        while (!builder.eof() && 
               builder.tokenType != SimplifiedWslTokenTypes.DOT) {
            val tokenText = builder.tokenText
            if (tokenText != null && tokenText.contains("\n")) {
                break
            }
            builder.advanceLexer()
        }
        if (builder.tokenType == SimplifiedWslTokenTypes.DOT) {
            builder.advanceLexer()
        }
    }
    
    private fun parseWorkflowType(builder: PsiBuilder) {
        // feature, solution, or workflow
        builder.advanceLexer()
        // Optional name
        if (builder.tokenType == SimplifiedWslTokenTypes.IDENTIFIER) {
            builder.advanceLexer()
        }
    }
}

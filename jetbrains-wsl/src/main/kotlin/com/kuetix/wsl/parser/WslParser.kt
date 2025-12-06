package com.kuetix.wsl.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.PsiBuilder
import com.intellij.lang.PsiParser
import com.intellij.psi.tree.IElementType
import com.kuetix.wsl.lexer.WslTokenTypes
import com.kuetix.wsl.psi.WslTypes

/**
 * Parser for WSL language.
 * Implements a simple recursive descent parser for WSL syntax.
 */
class WslParser : PsiParser {
    
    override fun parse(root: IElementType, builder: PsiBuilder): ASTNode {
        val rootMarker = builder.mark()
        
        while (!builder.eof()) {
            parseTopLevel(builder)
        }
        
        rootMarker.done(root)
        return builder.treeBuilt
    }
    
    private fun parseTopLevel(builder: PsiBuilder) {
        when (builder.tokenType) {
            WslTokenTypes.MODULE -> parseModuleDecl(builder)
            WslTokenTypes.IMPORT -> parseImportDecl(builder)
            WslTokenTypes.EXTENDS -> parseExtendsDecl(builder)
            WslTokenTypes.CONST -> parseConstDecl(builder)
            WslTokenTypes.WORKFLOW -> parseWorkflowDecl(builder)
            else -> {
                // Skip unknown tokens
                builder.advanceLexer()
            }
        }
    }
    
    private fun parseModuleDecl(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer() // consume 'module'
        
        // Parse module name (identifier with dots)
        while (!builder.eof() && 
               (builder.tokenType == WslTokenTypes.IDENTIFIER || 
                builder.tokenType == WslTokenTypes.DOT)) {
            builder.advanceLexer()
        }
        
        marker.done(WslTypes.MODULE_DECL)
    }
    
    private fun parseImportDecl(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer() // consume 'import'
        
        // Parse import path
        while (!builder.eof() && 
               (builder.tokenType == WslTokenTypes.IDENTIFIER ||
                builder.tokenType == WslTokenTypes.SLASH ||
                builder.tokenType == WslTokenTypes.DOT)) {
            builder.advanceLexer()
        }
        
        marker.done(WslTypes.IMPORT_DECL)
    }
    
    private fun parseExtendsDecl(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer() // consume 'extends'
        
        if (builder.tokenType == WslTokenTypes.STRING) {
            builder.advanceLexer()
        }
        
        marker.done(WslTypes.EXTENDS_DECL)
    }
    
    private fun parseConstDecl(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer() // consume 'const'
        
        if (builder.tokenType == WslTokenTypes.LBRACE) {
            parseObjectLiteral(builder)
        }
        
        marker.done(WslTypes.CONST_DECL)
    }
    
    private fun parseWorkflowDecl(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer() // consume 'workflow'
        
        // Parse workflow name
        if (builder.tokenType == WslTokenTypes.IDENTIFIER) {
            builder.advanceLexer()
        }
        
        // Parse workflow body
        if (builder.tokenType == WslTokenTypes.LBRACE) {
            builder.advanceLexer()
            
            while (!builder.eof() && builder.tokenType != WslTokenTypes.RBRACE) {
                when (builder.tokenType) {
                    WslTokenTypes.START -> parseStartDecl(builder)
                    WslTokenTypes.STATE -> parseStateDecl(builder)
                    else -> builder.advanceLexer()
                }
            }
            
            if (builder.tokenType == WslTokenTypes.RBRACE) {
                builder.advanceLexer()
            }
        }
        
        marker.done(WslTypes.WORKFLOW_DECL)
    }
    
    private fun parseStartDecl(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer() // consume 'start'
        
        if (builder.tokenType == WslTokenTypes.COLON) {
            builder.advanceLexer()
        }
        
        if (builder.tokenType == WslTokenTypes.IDENTIFIER) {
            builder.advanceLexer()
        }
        
        // Optional parameters
        if (builder.tokenType == WslTokenTypes.LPAREN) {
            parseArgList(builder)
        }
        
        marker.done(WslTypes.START_DECL)
    }
    
    private fun parseStateDecl(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer() // consume 'state'
        
        // Parse state name
        if (builder.tokenType == WslTokenTypes.IDENTIFIER) {
            builder.advanceLexer()
        }
        
        // Optional parameters
        if (builder.tokenType == WslTokenTypes.LPAREN) {
            parseArgList(builder)
        }
        
        // Parse state body
        if (builder.tokenType == WslTokenTypes.LBRACE) {
            parseStateBody(builder)
        }
        
        marker.done(WslTypes.STATE_DECL)
    }
    
    private fun parseStateBody(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer() // consume '{'
        
        while (!builder.eof() && builder.tokenType != WslTokenTypes.RBRACE) {
            when (builder.tokenType) {
                WslTokenTypes.ACTION -> parseActionStmt(builder)
                WslTokenTypes.ON -> parseOnClause(builder)
                WslTokenTypes.END -> parseEndStmt(builder)
                else -> builder.advanceLexer()
            }
        }
        
        if (builder.tokenType == WslTokenTypes.RBRACE) {
            builder.advanceLexer()
        }
        
        marker.done(WslTypes.STATE_BODY)
    }
    
    private fun parseActionStmt(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer() // consume 'action'
        
        // Parse action path
        while (!builder.eof() && 
               (builder.tokenType == WslTokenTypes.IDENTIFIER ||
                builder.tokenType == WslTokenTypes.SLASH ||
                builder.tokenType == WslTokenTypes.DOT)) {
            builder.advanceLexer()
        }
        
        // Parse arguments
        if (builder.tokenType == WslTokenTypes.LPAREN) {
            parseArgList(builder)
        }
        
        // Parse optional alias
        if (builder.tokenType == WslTokenTypes.AS) {
            builder.advanceLexer()
            if (builder.tokenType == WslTokenTypes.IDENTIFIER) {
                builder.advanceLexer()
            }
        }
        
        marker.done(WslTypes.ACTION_STMT)
    }
    
    private fun parseOnClause(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer() // consume 'on'
        
        // Parse outcome (success/error)
        if (builder.tokenType == WslTokenTypes.SUCCESS || 
            builder.tokenType == WslTokenTypes.ERROR) {
            builder.advanceLexer()
        }
        
        // Parse arrow
        if (builder.tokenType == WslTokenTypes.ARROW) {
            builder.advanceLexer()
        }
        
        // Parse next target
        parseNextTarget(builder)
        
        marker.done(WslTypes.ON_CLAUSE)
    }
    
    private fun parseNextTarget(builder: PsiBuilder) {
        val marker = builder.mark()
        
        if (builder.tokenType == WslTokenTypes.IDENTIFIER) {
            builder.advanceLexer()
        }
        
        // Optional arguments
        if (builder.tokenType == WslTokenTypes.LPAREN) {
            parseArgList(builder)
        }
        
        marker.done(WslTypes.NEXT_TARGET)
    }
    
    private fun parseEndStmt(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer() // consume 'end'
        
        // Parse outcome (ok/error)
        if (builder.tokenType == WslTokenTypes.OK || 
            builder.tokenType == WslTokenTypes.ERROR) {
            builder.advanceLexer()
        }
        
        marker.done(WslTypes.END_STMT)
    }
    
    private fun parseArgList(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer() // consume '('
        
        while (!builder.eof() && builder.tokenType != WslTokenTypes.RPAREN) {
            if (builder.tokenType == WslTokenTypes.IDENTIFIER) {
                parseNamedArg(builder)
            } else if (builder.tokenType == WslTokenTypes.COMMA) {
                builder.advanceLexer()
            } else {
                builder.advanceLexer()
            }
        }
        
        if (builder.tokenType == WslTokenTypes.RPAREN) {
            builder.advanceLexer()
        }
        
        marker.done(WslTypes.ARG_LIST)
    }
    
    private fun parseNamedArg(builder: PsiBuilder) {
        val marker = builder.mark()
        
        // Parse name
        if (builder.tokenType == WslTokenTypes.IDENTIFIER) {
            builder.advanceLexer()
        }
        
        // Parse colon
        if (builder.tokenType == WslTokenTypes.COLON) {
            builder.advanceLexer()
        }
        
        // Parse value expression
        parseExpression(builder)
        
        marker.done(WslTypes.NAMED_ARG)
    }
    
    private fun parseExpression(builder: PsiBuilder) {
        when (builder.tokenType) {
            WslTokenTypes.STRING -> builder.advanceLexer()
            WslTokenTypes.NUMBER -> builder.advanceLexer()
            WslTokenTypes.TRUE, WslTokenTypes.FALSE -> builder.advanceLexer()
            WslTokenTypes.DOLLAR -> parseDollarRef(builder)
            WslTokenTypes.LBRACE -> parseObjectLiteral(builder)
            WslTokenTypes.IDENTIFIER -> {
                // Could be a path or identifier
                while (!builder.eof() && 
                       (builder.tokenType == WslTokenTypes.IDENTIFIER ||
                        builder.tokenType == WslTokenTypes.SLASH ||
                        builder.tokenType == WslTokenTypes.DOT)) {
                    builder.advanceLexer()
                }
            }
            else -> {}
        }
        
        // Handle cast |Type
        if (builder.tokenType == WslTokenTypes.PIPE) {
            builder.advanceLexer()
            if (builder.tokenType == WslTokenTypes.IDENTIFIER) {
                builder.advanceLexer()
            }
        }
    }
    
    private fun parseDollarRef(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer() // consume '$'
        
        while (!builder.eof() && 
               (builder.tokenType == WslTokenTypes.IDENTIFIER ||
                builder.tokenType == WslTokenTypes.DOT)) {
            builder.advanceLexer()
        }
        
        marker.done(WslTypes.DOLLAR_REF)
    }
    
    private fun parseObjectLiteral(builder: PsiBuilder) {
        val marker = builder.mark()
        builder.advanceLexer() // consume '{'
        
        while (!builder.eof() && builder.tokenType != WslTokenTypes.RBRACE) {
            if (builder.tokenType == WslTokenTypes.IDENTIFIER) {
                parseObjectPair(builder)
            } else if (builder.tokenType == WslTokenTypes.COMMA) {
                builder.advanceLexer()
            } else {
                builder.advanceLexer()
            }
        }
        
        if (builder.tokenType == WslTokenTypes.RBRACE) {
            builder.advanceLexer()
        }
        
        marker.done(WslTypes.OBJECT_LITERAL)
    }
    
    private fun parseObjectPair(builder: PsiBuilder) {
        val marker = builder.mark()
        
        // Parse key
        if (builder.tokenType == WslTokenTypes.IDENTIFIER) {
            builder.advanceLexer()
        }
        
        // Parse colon
        if (builder.tokenType == WslTokenTypes.COLON) {
            builder.advanceLexer()
        }
        
        // Parse value
        parseExpression(builder)
        
        marker.done(WslTypes.OBJECT_PAIR)
    }
}

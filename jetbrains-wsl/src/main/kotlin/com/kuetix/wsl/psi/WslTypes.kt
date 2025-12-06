package com.kuetix.wsl.psi

import com.intellij.psi.tree.IElementType
import com.kuetix.wsl.lexer.WslElementType

/**
 * Element types for WSL PSI nodes.
 */
object WslTypes {
    // Document structure
    @JvmField val DOCUMENT = WslElementType("DOCUMENT")
    
    // Header declarations
    @JvmField val MODULE_DECL = WslElementType("MODULE_DECL")
    @JvmField val IMPORT_DECL = WslElementType("IMPORT_DECL")
    @JvmField val EXTENDS_DECL = WslElementType("EXTENDS_DECL")
    @JvmField val CONST_DECL = WslElementType("CONST_DECL")
    
    // Workflow structure
    @JvmField val WORKFLOW_DECL = WslElementType("WORKFLOW_DECL")
    @JvmField val START_DECL = WslElementType("START_DECL")
    @JvmField val STATE_DECL = WslElementType("STATE_DECL")
    @JvmField val STATE_BODY = WslElementType("STATE_BODY")
    
    // Statements
    @JvmField val ACTION_STMT = WslElementType("ACTION_STMT")
    @JvmField val ON_CLAUSE = WslElementType("ON_CLAUSE")
    @JvmField val END_STMT = WslElementType("END_STMT")
    
    // Expressions
    @JvmField val ACTION_PATH = WslElementType("ACTION_PATH")
    @JvmField val ARG_LIST = WslElementType("ARG_LIST")
    @JvmField val NAMED_ARG = WslElementType("NAMED_ARG")
    @JvmField val NEXT_TARGET = WslElementType("NEXT_TARGET")
    
    // Literals and references
    @JvmField val OBJECT_LITERAL = WslElementType("OBJECT_LITERAL")
    @JvmField val OBJECT_PAIR = WslElementType("OBJECT_PAIR")
    @JvmField val DOLLAR_REF = WslElementType("DOLLAR_REF")
    @JvmField val TEMPLATE_EXPR = WslElementType("TEMPLATE_EXPR")
    @JvmField val CAST_EXPR = WslElementType("CAST_EXPR")
    @JvmField val PATH_EXPR = WslElementType("PATH_EXPR")
}

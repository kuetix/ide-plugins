package com.kuetix.wsl.parser

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.impl.source.tree.LeafPsiElement
import com.kuetix.wsl.psi.WslTypes
import com.kuetix.wsl.psi.impl.*

/**
 * Factory for creating PSI elements from AST nodes.
 */
object WslPsiElementFactory {
    
    fun createElement(node: ASTNode): PsiElement {
        return when (node.elementType) {
            WslTypes.MODULE_DECL -> WslModuleDeclImpl(node)
            WslTypes.IMPORT_DECL -> WslImportDeclImpl(node)
            WslTypes.EXTENDS_DECL -> WslExtendsDeclImpl(node)
            WslTypes.CONST_DECL -> WslConstDeclImpl(node)
            WslTypes.WORKFLOW_DECL -> WslWorkflowDeclImpl(node)
            WslTypes.START_DECL -> WslStartDeclImpl(node)
            WslTypes.STATE_DECL -> WslStateDeclImpl(node)
            WslTypes.STATE_BODY -> WslStateBodyImpl(node)
            WslTypes.ACTION_STMT -> WslActionStmtImpl(node)
            WslTypes.ON_CLAUSE -> WslOnClauseImpl(node)
            WslTypes.END_STMT -> WslEndStmtImpl(node)
            WslTypes.ACTION_PATH -> WslActionPathImpl(node)
            WslTypes.ARG_LIST -> WslArgListImpl(node)
            WslTypes.NAMED_ARG -> WslNamedArgImpl(node)
            WslTypes.NEXT_TARGET -> WslNextTargetImpl(node)
            WslTypes.OBJECT_LITERAL -> WslObjectLiteralImpl(node)
            WslTypes.OBJECT_PAIR -> WslObjectPairImpl(node)
            WslTypes.DOLLAR_REF -> WslDollarRefImpl(node)
            WslTypes.TEMPLATE_EXPR -> WslTemplateExprImpl(node)
            WslTypes.CAST_EXPR -> WslCastExprImpl(node)
            WslTypes.PATH_EXPR -> WslPathExprImpl(node)
            else -> WslPsiElementImpl(node)
        }
    }
}

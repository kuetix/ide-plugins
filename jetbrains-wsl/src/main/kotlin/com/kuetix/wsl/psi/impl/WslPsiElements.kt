package com.kuetix.wsl.psi.impl

import com.intellij.lang.ASTNode
import com.kuetix.wsl.lexer.WslTokenTypes

/**
 * Module declaration implementation.
 */
class WslModuleDeclImpl(node: ASTNode) : WslNamedPsiElement(node) {
    override fun getElementName(): String? {
        val names = mutableListOf<String>()
        var child = node.firstChildNode
        while (child != null) {
            if (child.elementType == WslTokenTypes.IDENTIFIER) {
                names.add(child.text)
            }
            child = child.treeNext
        }
        return names.joinToString(".")
    }
}

/**
 * Import declaration implementation.
 */
class WslImportDeclImpl(node: ASTNode) : WslNamedPsiElement(node) {
    override fun getElementName(): String? {
        val parts = mutableListOf<String>()
        var child = node.firstChildNode
        while (child != null) {
            if (child.elementType == WslTokenTypes.IDENTIFIER ||
                child.elementType == WslTokenTypes.SLASH ||
                child.elementType == WslTokenTypes.DOT) {
                parts.add(child.text)
            }
            child = child.treeNext
        }
        return parts.joinToString("")
    }
}

/**
 * Extends declaration implementation.
 */
class WslExtendsDeclImpl(node: ASTNode) : WslNamedPsiElement(node) {
    override fun getElementName(): String? {
        var child = node.firstChildNode
        while (child != null) {
            if (child.elementType == WslTokenTypes.STRING) {
                return child.text.trim('"')
            }
            child = child.treeNext
        }
        return null
    }
}

/**
 * Const declaration implementation.
 */
class WslConstDeclImpl(node: ASTNode) : WslPsiElementImpl(node)

/**
 * Workflow declaration implementation.
 */
class WslWorkflowDeclImpl(node: ASTNode) : WslNamedPsiElement(node) {
    override fun getElementName(): String? {
        var child = node.firstChildNode
        while (child != null) {
            if (child.elementType == WslTokenTypes.IDENTIFIER) {
                return child.text
            }
            child = child.treeNext
        }
        return null
    }
}

/**
 * Start declaration implementation.
 */
class WslStartDeclImpl(node: ASTNode) : WslNamedPsiElement(node) {
    override fun getElementName(): String? {
        var child = node.firstChildNode
        while (child != null) {
            if (child.elementType == WslTokenTypes.IDENTIFIER) {
                return child.text
            }
            child = child.treeNext
        }
        return null
    }
}

/**
 * State declaration implementation.
 */
class WslStateDeclImpl(node: ASTNode) : WslNamedPsiElement(node) {
    override fun getElementName(): String? {
        var child = node.firstChildNode
        while (child != null) {
            if (child.elementType == WslTokenTypes.IDENTIFIER) {
                return child.text
            }
            child = child.treeNext
        }
        return null
    }
}

/**
 * State body implementation.
 */
class WslStateBodyImpl(node: ASTNode) : WslPsiElementImpl(node)

/**
 * Action statement implementation.
 */
class WslActionStmtImpl(node: ASTNode) : WslNamedPsiElement(node) {
    override fun getElementName(): String? {
        val parts = mutableListOf<String>()
        var child = node.firstChildNode
        while (child != null) {
            if (child.elementType == WslTokenTypes.IDENTIFIER ||
                child.elementType == WslTokenTypes.SLASH ||
                child.elementType == WslTokenTypes.DOT) {
                parts.add(child.text)
            } else if (child.elementType == WslTokenTypes.LPAREN) {
                break
            }
            child = child.treeNext
        }
        return parts.joinToString("")
    }
}

/**
 * On clause implementation.
 */
class WslOnClauseImpl(node: ASTNode) : WslPsiElementImpl(node)

/**
 * End statement implementation.
 */
class WslEndStmtImpl(node: ASTNode) : WslPsiElementImpl(node)

/**
 * Action path implementation.
 */
class WslActionPathImpl(node: ASTNode) : WslPsiElementImpl(node)

/**
 * Argument list implementation.
 */
class WslArgListImpl(node: ASTNode) : WslPsiElementImpl(node)

/**
 * Named argument implementation.
 */
class WslNamedArgImpl(node: ASTNode) : WslNamedPsiElement(node) {
    override fun getElementName(): String? {
        var child = node.firstChildNode
        while (child != null) {
            if (child.elementType == WslTokenTypes.IDENTIFIER) {
                return child.text
            }
            child = child.treeNext
        }
        return null
    }
}

/**
 * Next target implementation.
 */
class WslNextTargetImpl(node: ASTNode) : WslNamedPsiElement(node) {
    override fun getElementName(): String? {
        var child = node.firstChildNode
        while (child != null) {
            if (child.elementType == WslTokenTypes.IDENTIFIER) {
                return child.text
            }
            child = child.treeNext
        }
        return null
    }
}

/**
 * Object literal implementation.
 */
class WslObjectLiteralImpl(node: ASTNode) : WslPsiElementImpl(node)

/**
 * Object pair implementation.
 */
class WslObjectPairImpl(node: ASTNode) : WslNamedPsiElement(node) {
    override fun getElementName(): String? {
        var child = node.firstChildNode
        while (child != null) {
            if (child.elementType == WslTokenTypes.IDENTIFIER) {
                return child.text
            }
            child = child.treeNext
        }
        return null
    }
}

/**
 * Dollar reference implementation.
 */
class WslDollarRefImpl(node: ASTNode) : WslPsiElementImpl(node)

/**
 * Template expression implementation.
 */
class WslTemplateExprImpl(node: ASTNode) : WslPsiElementImpl(node)

/**
 * Cast expression implementation.
 */
class WslCastExprImpl(node: ASTNode) : WslPsiElementImpl(node)

/**
 * Path expression implementation.
 */
class WslPathExprImpl(node: ASTNode) : WslPsiElementImpl(node)

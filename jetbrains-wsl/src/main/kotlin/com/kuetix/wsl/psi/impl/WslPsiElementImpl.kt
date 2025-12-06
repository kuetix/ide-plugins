package com.kuetix.wsl.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.NavigatablePsiElement
import com.kuetix.wsl.language.WslIcons
import javax.swing.Icon

/**
 * Base PSI element implementation for WSL.
 */
open class WslPsiElementImpl(node: ASTNode) : ASTWrapperPsiElement(node), NavigatablePsiElement {
    override fun getPresentation(): ItemPresentation? = null
}

/**
 * Abstract named element for elements that have a name.
 */
abstract class WslNamedPsiElement(node: ASTNode) : WslPsiElementImpl(node) {
    abstract fun getElementName(): String?
    
    override fun getName(): String? = getElementName()
    
    override fun getPresentation(): ItemPresentation {
        val elementName = getElementName() ?: ""
        return object : ItemPresentation {
            override fun getPresentableText(): String = elementName
            override fun getLocationString(): String? = containingFile?.name
            override fun getIcon(unused: Boolean): Icon = WslIcons.FILE
        }
    }
}

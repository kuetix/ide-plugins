package com.kuetix.wsl.psi

import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.structureView.*
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.lang.PsiStructureViewFactory
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.editor.Editor
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.kuetix.wsl.language.WslIcons
import com.kuetix.wsl.lexer.WslTokenTypes
import javax.swing.Icon

/**
 * Structure view factory for WSL files.
 * Provides navigation through workflow structure in the IDE.
 */
class WslStructureViewFactory : PsiStructureViewFactory {
    override fun getStructureViewBuilder(psiFile: PsiFile): StructureViewBuilder? {
        if (psiFile !is WslFile) return null
        return object : TreeBasedStructureViewBuilder() {
            override fun createStructureViewModel(editor: Editor?): StructureViewModel {
                return WslStructureViewModel(psiFile)
            }
        }
    }
}

/**
 * Structure view model for WSL files.
 */
class WslStructureViewModel(psiFile: WslFile) : 
    StructureViewModelBase(psiFile, WslStructureViewElement(psiFile)),
    StructureViewModel.ElementInfoProvider {
    
    override fun isAlwaysShowsPlus(element: StructureViewTreeElement): Boolean = false
    
    override fun isAlwaysLeaf(element: StructureViewTreeElement): Boolean = false
}

/**
 * Structure view element for WSL PSI elements.
 */
class WslStructureViewElement(private val element: NavigatablePsiElement) : StructureViewTreeElement {
    
    override fun getValue(): Any = element
    
    override fun getPresentation(): ItemPresentation {
        return element.presentation ?: PresentationData(element.text, null, WslIcons.FILE, null)
    }
    
    override fun getChildren(): Array<TreeElement> {
        if (element !is WslFile) return emptyArray()
        
        val children = mutableListOf<TreeElement>()
        
        // Find workflow and state declarations
        var child = element.firstChild
        while (child != null) {
            if (child is NavigatablePsiElement) {
                val elementType = child.node?.elementType
                if (elementType == WslTypes.WORKFLOW_DECL || elementType == WslTypes.STATE_DECL) {
                    children.add(WslStructureViewElement(child))
                }
            }
            child = child.nextSibling
        }
        
        return children.toTypedArray()
    }
    
    override fun navigate(requestFocus: Boolean) {
        element.navigate(requestFocus)
    }
    
    override fun canNavigate(): Boolean = element.canNavigate()
    
    override fun canNavigateToSource(): Boolean = element.canNavigateToSource()
}

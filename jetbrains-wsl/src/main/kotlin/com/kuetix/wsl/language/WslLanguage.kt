package com.kuetix.wsl.language

import com.intellij.lang.Language

/**
 * WSL (Workflow Specific Language) language definition.
 * This is the entry point for the language support in JetBrains IDEs.
 */
object WslLanguage : Language("WSL") {
    override fun getDisplayName(): String = "WSL"
    
    override fun isCaseSensitive(): Boolean = true
}

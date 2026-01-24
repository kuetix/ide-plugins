package com.kuetix.swsl.language

import com.intellij.lang.Language

/**
 * SimplifiedWSL (Simplified Workflow Specific Language) language definition.
 * This is the entry point for the language support in JetBrains IDEs.
 */
object SimplifiedWslLanguage : Language("SimplifiedWSL") {
    override fun getDisplayName(): String = "SimplifiedWSL"
    
    override fun isCaseSensitive(): Boolean = true
}

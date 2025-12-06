package com.kuetix.wsl.lexer

import com.intellij.psi.tree.IElementType
import com.kuetix.wsl.language.WslLanguage

/**
 * Custom element type for WSL tokens.
 */
class WslTokenType(debugName: String) : IElementType(debugName, WslLanguage)

/**
 * Custom element type for WSL composite elements (AST nodes).
 */
class WslElementType(debugName: String) : IElementType(debugName, WslLanguage)

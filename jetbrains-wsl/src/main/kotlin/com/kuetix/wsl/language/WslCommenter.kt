package com.kuetix.wsl.language

import com.intellij.lang.Commenter

/**
 * Commenter for WSL language.
 * Supports line comments with // prefix.
 */
class WslCommenter : Commenter {
    override fun getLineCommentPrefix(): String = "//"

    override fun getBlockCommentPrefix(): String? = "/*"

    override fun getBlockCommentSuffix(): String? = "*/"

    override fun getCommentedBlockCommentPrefix(): String? = null

    override fun getCommentedBlockCommentSuffix(): String? = null
}

package com.kuetix.swsl.language

import com.intellij.lang.Commenter

/**
 * Commenter for SimplifiedWSL language.
 * Supports line comments with // and # prefix, and block comments.
 */
class SimplifiedWslCommenter : Commenter {
    override fun getLineCommentPrefix(): String = "//"

    override fun getBlockCommentPrefix(): String? = "/*"

    override fun getBlockCommentSuffix(): String? = "*/"

    override fun getCommentedBlockCommentPrefix(): String? = null

    override fun getCommentedBlockCommentSuffix(): String? = null
}

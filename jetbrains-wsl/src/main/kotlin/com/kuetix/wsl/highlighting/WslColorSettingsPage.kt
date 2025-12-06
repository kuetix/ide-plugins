package com.kuetix.wsl.highlighting

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import com.kuetix.wsl.language.WslIcons
import javax.swing.Icon

/**
 * Color settings page for WSL syntax highlighting.
 * Allows users to customize syntax highlighting colors in IDE settings.
 */
class WslColorSettingsPage : ColorSettingsPage {
    
    override fun getIcon(): Icon = WslIcons.FILE
    
    override fun getHighlighter(): SyntaxHighlighter = WslSyntaxHighlighter()
    
    override fun getDemoText(): String = """
        // WSL (Workflow Specific Language) Example
        module example.auth
        
        import services/common
        import workflow/repositorium
        
        extends "resolvers"
        
        const {
            event: "login",
            description: "User authentication workflow",
            version: "1.0.0"
        }
        
        workflow user_login {
          start: Request
        
          state Request {
            action parameters/Request(type: "login") as Req
            on success -> Lookup(Req)
            on error -> Fail
          }
        
          state Lookup(Req) {
            action workflow/repositorium/user/get(id: "<<helpers.Id(Req.Email)>>") as User
            on success -> CheckPassword(Req, User)
            on error -> Fail
          }
        
          state CheckPassword(Req, User) {
            action login/CheckPassword(request: ${'$'}Req, user: ${'$'}User)
            on success -> GenerateToken(User)
            on error -> Invalid
          }
        
          state GenerateToken(User) {
            action login/GenerateToken(user: ${'$'}User, expires: 3600|int) as Token
            on success -> Respond(User, Token)
            on error -> TokenFailed
          }
        
          state Respond(User, Token) {
            action response/ResponseEntity(entityName: "user", token: ${'$'}Token.value, success: true)
            end ok
          }
        
          state Invalid {
            action response/ResponseError(message: "Invalid credentials", code: 401)
            end error
          }
        
          state TokenFailed {
            action response/ResponseError(message: "Token generation failed", code: 500)
            end error
          }
        
          state Fail {
            action response/ResponseError(message: "Request failed", code: 400)
            end error
          }
        }
    """.trimIndent()
    
    override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String, TextAttributesKey>? = null
    
    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = DESCRIPTORS
    
    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY
    
    override fun getDisplayName(): String = "WSL"
    
    companion object {
        private val DESCRIPTORS = arrayOf(
            AttributesDescriptor("Keyword", WslSyntaxHighlighter.KEYWORD),
            AttributesDescriptor("Comment", WslSyntaxHighlighter.COMMENT),
            AttributesDescriptor("String", WslSyntaxHighlighter.STRING),
            AttributesDescriptor("Number", WslSyntaxHighlighter.NUMBER),
            AttributesDescriptor("Identifier", WslSyntaxHighlighter.IDENTIFIER),
            AttributesDescriptor("Operator", WslSyntaxHighlighter.OPERATOR),
            AttributesDescriptor("Braces", WslSyntaxHighlighter.BRACES),
            AttributesDescriptor("Parentheses", WslSyntaxHighlighter.PARENTHESES),
            AttributesDescriptor("Reference ($)", WslSyntaxHighlighter.REFERENCE),
            AttributesDescriptor("Template Markers (<< >>)", WslSyntaxHighlighter.TEMPLATE),
            AttributesDescriptor("Punctuation", WslSyntaxHighlighter.PUNCTUATION),
            AttributesDescriptor("Bad Character", WslSyntaxHighlighter.BAD_CHARACTER)
        )
    }
}

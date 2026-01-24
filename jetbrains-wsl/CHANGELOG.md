# Change Log

All notable changes to the WSL Language Support extension will be documented in this file.

## [1.0.0] - 2024

### Added
- Initial release of WSL Language Support for Visual Studio Code
- Full syntax highlighting for WSL keywords, strings, numbers, comments, and operators
- Keyword completion for all WSL keywords with descriptions
- Smart code snippets for common patterns:
  - Workflow structures
  - State definitions
  - Action declarations
  - Const blocks
- Dynamic module and method completion from `modules.json` files
- Multi-source module loading (project root, workflows/, runtime/workflows/)
- Automatic reload of modules on file changes
- Bracket matching for `{}`, `[]`, and `()`
- Comment toggling support (line and block comments)
- Auto-closing pairs for brackets and quotes
- Custom file icon for `.wsl` files
- File type recognition for `.wsl` extension
- Example workflow and modules.json files

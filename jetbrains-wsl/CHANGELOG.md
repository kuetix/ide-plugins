# Change Log

All notable changes to the WSL Language Support plugin will be documented in this file.

## [1.5.0] - 2026

### Added
- Lexing and syntax highlighting for the engine's expression / control-flow
  constructs:
  - keywords `if`, `when`, `let`, `foreach`, `in`, `while`, `retry`, `parallel`,
    `wait`, `join`, `branch`, `continue`, `skip`, `def`, and the `null` literal
  - expression operators `== != >= <= && || ?? < > ! + * %`, and `<-`
  - bracket tokens `[` `]`
- Completion entries for `if`, `let`, `retry`, `foreach`, `while`, `when`,
  `continue on fail`, `skip to`.

Note: the parser is intentionally forgiving (unknown tokens in a state body are
skipped), so `let` / `foreach` / `while` / `retry` blocks highlight correctly
without dedicated PSI nodes. Structured PSI for these is a follow-up.

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

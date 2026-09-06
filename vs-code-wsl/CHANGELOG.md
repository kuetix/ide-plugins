# Change Log

All notable changes to the WSL Language Support extension will be documented in this file.

## [1.3.0] - 2026

### Added
- Syntax highlighting and keyword completion for the expression / control-flow
  constructs added to the engine:
  - `let <name> = <expr>` bindings
  - `if <expr>`, and `on success when <expr>` guards (ordered, first match wins)
  - `foreach <x> in <expr> [parallel[limit: K]] { action ... }`
  - `while[max: N] <expr> { action ... }`
  - `retry[max: N, delay: "...", on: "<expr>"]`
- Highlighting for expression operators (`== != <= >= && || ! ?? + - * / %`) and
  `${...}` string interpolation.
- Highlighting for previously-missing keywords: `if`, `when`, `continue`, `skip`,
  `parallel`, `wait`, `join`, `branch`, `null`, builtin functions (`len`,
  `contains`, `int`, …).
- Snippets: `let`, `if`, `on-success-when`, `foreach`, `foreach-parallel`,
  `while`, `retry`.

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

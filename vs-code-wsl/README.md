# WSL Language Support for VS Code

This extension provides comprehensive language support for the **Kuetix Workflow Specific Language (WSL)** and **SimplifiedWSL** in Visual Studio Code.

## Features

### Syntax Highlighting
- Full syntax highlighting for WSL and SimplifiedWSL keywords, strings, numbers, comments, and operators
- Support for both `.wsl` and `.swsl` file extensions
- Customizable color themes

### Code Completion
- Keyword completion for all WSL and SimplifiedWSL keywords
- Smart snippets for common patterns (workflow, state, action declarations)
- SimplifiedWSL-specific snippets (feature, solution, def, action flows)
- Template insertions with cursor positioning
- Dynamic module and method completion from `modules.json`
  - Place a `modules.json` file in your project root to define custom modules
  - Get intelligent suggestions for module paths and methods

### Code Navigation
- Bracket matching for `{}` and `()`
- Comment toggling with `Ctrl+/` (or `Cmd+/` on macOS)
- Auto-closing brackets and quotes

### File Type Recognition
- Automatic recognition of `.wsl` and `.swsl` files
- Custom file icon for WSL files

## Installation

### From VS Code Marketplace
1. Open Visual Studio Code
2. Go to **Extensions** (Ctrl+Shift+X or Cmd+Shift+X on macOS)
3. Search for "WSL Language Support"
4. Click **Install**

### From VSIX File
1. Download the `.vsix` file from the releases
2. Open Visual Studio Code
3. Go to **Extensions** → **...** → **Install from VSIX**
4. Select the downloaded file

## Building from Source

### Prerequisites
- Node.js 18 or higher
- npm or yarn

### Build Commands

```bash
# Navigate to the extension directory
cd ide-plugins/vs-code-wsl

# Install dependencies
npm install

# Compile TypeScript
npm run compile

# Package the extension
npm install -g @vscode/vsce
vsce package
```

The built extension will be in the current directory as a `.vsix` file.

## SimplifiedWSL

SimplifiedWSL (`.swsl`) is a streamlined syntax for WSL that removes verbosity while maintaining full engine compatibility.

### Key Features

- **No workflow wrappers**: Direct action flows without `workflow name { }` blocks
- **Simplified operators**: `->` for flow, `<-` for error binding, `.` for terminal
- **Workflow types**: Support for `feature`, `solution`, `workflow`, and custom types
- **Inline definitions**: Use `def` for reusable action definitions
- **Hierarchical execution**: Call workflows, features, and solutions

### Basic SimplifiedWSL Example

```swsl
module payment_processing

feature payment_feature

const {
    timeout: 5000,
    retryCount: 3
}

def errors.LogError(level: "error") as errorHandler -> .

// Validate payment
workflow:validate_payment(timeout: $constants.timeout) <- errorHandler ->

// Process payment
workflow:process_payment(retries: $constants.retryCount) <- errorHandler ->

// Send receipt
payment.SendReceipt() <- errorHandler -> .
```

### SimplifiedWSL vs Traditional WSL

**SimplifiedWSL** is ideal for:
- Simple linear or branching workflows
- Rapid prototyping
- Reducing visual complexity
- Quick scaffolding

**Traditional WSL** is better for:
- Multiple named workflows in one file
- Advanced state management
- Complex conditional logic
- Explicit control over state names

### Documentation

- [SimplifiedWSL Guide](./SIMPLIFIED_WSL.md) - Complete syntax reference
- [Hierarchical Execution](./HIERARCHICAL_EXECUTION.md) - Workflow orchestration patterns
- [Examples](./examples/hierarchical/) - Working examples

## WSL Language Overview

WSL (Workflow Specific Language) is the native authoring format for Kuetix Engine workflows.

### Basic Syntax

```wsl
module example.auth

import services/common

const {
    event: "login",
    version: "1.0.0"
}

workflow user_login {
  start: Request

  state Request {
    action parameters/Request(type: "login") as Req
    on success -> Validate(Req)
    on error -> Fail
  }

  state Validate(Req) {
    action auth/ValidateCredentials(request: $Req)
    on success -> Success
    on error -> Invalid
  }

  state Success {
    action response/ResponseOK(message: "Login successful")
    end ok
  }

  state Invalid {
    action response/ResponseError(message: "Invalid credentials", code: 401)
    end error
  }

  state Fail {
    action response/ResponseError(message: "Request failed", code: 400)
    end error
  }
}
```

### Key Elements

| Element | Description |
|---------|-------------|
| `module` | Declares a namespaced module name |
| `import` | Brings other modules/namespaces into scope |
| `extends` | Indicates inheritance of resolvers/configs |
| `const` | Declares key/value constants accessible as `$constants.*` |
| `workflow` | Declares a workflow with states and transitions |
| `state` | Defines a state block with action and transitions |
| `action` | Specifies an action to execute |
| `on success/error` | Defines transitions based on action outcome |
| `end ok/error` | Marks a terminal state |

### Expression Types

- **String literals**: `"hello world"`
- **Numbers**: `42`, `3.14`
- **Booleans**: `true`, `false`
- **References**: `$variable.path.to.value`, `$variable.array[0].nested[1].value`
- **Templates**: `"<<expression|type>>"`
- **Array literals**: `[item1, item2, ...]`
- **Object literals**: `{key: value, ...}`
- **Nested structures**: Arrays and objects can be nested, e.g., `[{key: [value1, value2]}]`
- **Type casts**: `value|int`, `value|string`

### Nested Arrays and Objects

WSL supports deeply nested data structures with arrays and objects:

```wsl
workflow example {
  start: CreateNestedData

  state CreateNestedData {
    action module/method(
      data: [
        {
          response: [
            {
              event: $constants.event,
              code: "Code202"
            }
          ],
          statusCode: 202
        }
      ]
    ) as Result
    on success -> AccessNestedData(Result)
  }

  state AccessNestedData(Result) {
    action module/method(
      event: $Result.data[0].response[0].event,
      code: $Result.data[0].response[0].code
    )
    end ok
  }
}
```

Array indexing uses square brackets (e.g., `[0]`, `[1]`) and can be combined with dot notation for nested property access.


## Configuration

### Module Definitions

Define custom modules for code completion by creating a `modules.json` file in your project root directory:

```json
{
  "auth.login": {
    "info": {
      "namespace": "auth",
      "class": "LoginModule",
      "label": "Login Module",
      "description": "Handles user authentication"
    },
    "methods": [
      {
        "value": "authenticate",
        "label": "authenticate(username, password)",
        "description": "Authenticate user with credentials"
      }
    ]
  }
}
```

The extension automatically watches for changes to `modules.json` files in:
- Project root: `modules.json`
- Workflows directory: `workflows/modules.json`
- Runtime workflows: `runtime/workflows/modules.json`

## Snippets

The extension includes helpful code snippets:

**WSL Snippets:**
- `module` - Create a module declaration
- `import` - Import statement
- `workflow` - Create a complete workflow structure
- `state` - Create a state with transitions
- `state-end` - Create a terminal state
- `action` - Create an action
- `const` - Create a const block
- `on-success` - Success transition
- `on-error` - Error transition
- `template` - Template expression

**SimplifiedWSL Snippets:**
- `swsl-feature` - Create a feature template
- `swsl-solution` - Create a solution template
- `swsl-workflow` - Create a workflow template
- `swsl-module` - Module declaration
- `swsl-const` - Constants block
- `swsl-def-error` - Define error handler
- `swsl-def` - Define reusable action
- `swsl-flow` - Action flow with error handling
- `swsl-chain` - Chained action flow
- `swsl-call-workflow` - Call workflow hierarchically
- `swsl-call-feature` - Call feature hierarchically

Type the prefix and press `Tab` to expand the snippet.

## Examples

Check the `examples/` directory for:
- `login.wsl` - A complete user login workflow
- `hierarchical/` - SimplifiedWSL hierarchical execution examples
  - `solution_example.swsl` - Solution orchestrating features
  - `feature_payment.swsl` - Feature orchestrating workflows
  - `workflow_validate.swsl` - Workflow with actions
- `modules.json` - Sample module definitions

## Requirements

- Visual Studio Code version 1.75.0 or higher

## Known Issues

None at this time. Please report issues on the GitHub repository.

## Contributing

Contributions are welcome! Please read the contributing guidelines in the main repository.

## License

This extension is part of the Kuetix Engine project. See the LICENSE file in the repository root for details.

## Support

- **Documentation**: See the WSL specification documentation
- **Issues**: Report bugs and feature requests on the GitHub repository
- **Repository**: https://github.com/kuetix/ide-plugins

## Release Notes

### 1.1.0

Added SimplifiedWSL support:
- Full syntax highlighting for `.swsl` files
- SimplifiedWSL-specific snippets (feature, solution, workflow types)
- Hierarchical execution examples
- Comprehensive documentation for SimplifiedWSL and hierarchical workflows
- Support for workflow types: feature, solution, workflow, custom

### 1.0.0

Initial release of WSL Language Support for VS Code

- Full syntax highlighting for WSL files
- Keyword completion with smart snippets
- Dynamic module and method completion from modules.json files
- Multi-source module loading (project root, workflows/, runtime/workflows/)
- Auto-reload modules on file changes
- Bracket matching and comment support
- Custom file icon for .wsl files

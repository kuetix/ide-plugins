# WSL Language Support for JetBrains IDEs

This plugin provides comprehensive language support for the **Kuetix Workflow Specific Language (WSL)** and **SimplifiedWSL** in JetBrains IDEs (IntelliJ IDEA, GoLand, WebStorm, etc.).

## Features

### Syntax Highlighting
- Full syntax highlighting for WSL and SimplifiedWSL keywords, strings, numbers, comments, and operators
- Support for both `.wsl` and `.swsl` file extensions
- Customizable color schemes via IDE settings

### Code Completion
- Keyword completion for all WSL and SimplifiedWSL keywords
- Dynamic module and method completion from `modules.json` files
- Smart completion for workflow elements

### Code Navigation
- Bracket matching for `{}`, `()`, and `[]`
- Code structure view for workflows
- Comment toggling with `Ctrl+/` (or `Cmd+/` on macOS)
- Auto-closing brackets and quotes

### File Type Recognition
- Automatic recognition of `.wsl` and `.swsl` files
- Custom file icon for WSL files

## Installation

### From JetBrains Marketplace
1. Open your JetBrains IDE (IntelliJ IDEA, GoLand, WebStorm, etc.)
2. Go to **Settings/Preferences** → **Plugins**
3. Search for "WSL Language Support"
4. Click **Install**
5. Restart the IDE

### From Plugin File
1. Download the plugin `.jar` or `.zip` file from the releases
2. Go to **Settings/Preferences** → **Plugins**
3. Click the gear icon → **Install Plugin from Disk**
4. Select the downloaded file
5. Restart the IDE

## Building from Source

### Prerequisites
- JDK 17 or higher
- Gradle (included via wrapper)

### Build Commands

```bash
# Navigate to the plugin directory
cd ide-plugins/jetbrains-wsl

# Build the plugin
./gradlew buildPlugin

# The built plugin will be in build/distributions/
```

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
- [Examples](./runtime/workflows/hierarchical/) - Working examples

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
- **References**: `$variable.path.to.value`
- **Templates**: `"<<expression|type>>"`
- **Object literals**: `{key: value, ...}`
- **Type casts**: `value|int`, `value|string`

## Configuration

### Module Definitions

Define custom modules for code completion by creating a `modules.json` file in your project:

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

The plugin automatically watches for changes to `modules.json` files in:
- Project root: `modules.json`
- Workflows directory: `workflows/modules.json`
- Runtime workflows: `runtime/workflows/modules.json`

## Examples

Check the `runtime/workflows/` directory for:
- `workflow/login.wsl` - A complete user login workflow
- `hierarchical/` - SimplifiedWSL hierarchical execution examples
  - `solution_example.swsl` - Solution orchestrating features
  - `feature_payment.swsl` - Feature orchestrating workflows
  - `workflow_validate.swsl` - Workflow with actions

## Compatibility

- Compatible with IntelliJ Platform 2023.3 - 2025.2
- Tested with IntelliJ IDEA, GoLand, WebStorm, PyCharm, and other JetBrains IDEs

## Known Issues

None at this time. Please report issues on the GitHub repository.

## Contributing

Contributions are welcome! Please read the contributing guidelines in the main repository.

## License

This plugin is part of the Kuetix Engine project. See the LICENSE file in the repository root for details.

## Support

- **Documentation**: See the WSL specification documentation
- **Issues**: Report bugs and feature requests on the GitHub repository
- **Repository**: https://github.com/kuetix/ide-plugins

## Release Notes

### Version 2024.1.0

Added SimplifiedWSL support:
- Full syntax highlighting for `.swsl` files
- Support for SimplifiedWSL workflow types (feature, solution, workflow)
- SimplifiedWSL operators: `->`, `<-`, `.`
- SimplifiedWSL `def` keyword for reusable definitions
- Hierarchical execution examples
- Comprehensive documentation for SimplifiedWSL and hierarchical workflows

### Version 2023.3.0

Initial release of WSL Language Support plugin:
- Full syntax highlighting for WSL files
- Keyword completion for workflow elements
- Dynamic module and method completion from modules.json files
- Multi-source module loading (project root, workflows/, runtime/workflows/)
- Auto-reload modules on file changes
- Robust error handling - skip invalid modules gracefully
- Code structure view for workflows and states
- Brace matching and code commenting support
- Customizable color scheme
- Compatible with IntelliJ Platform 2023.3 - 2025.2

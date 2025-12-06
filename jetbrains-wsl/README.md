# WSL Language Support for JetBrains IDEs

This plugin provides comprehensive language support for the **Kuetix Workflow Specific Language (WSL)** in JetBrains IDEs including IntelliJ IDEA, GoLand, WebStorm, and other JetBrains products.

## Features

### Syntax Highlighting
- Full syntax highlighting for WSL keywords, strings, numbers, comments, and operators
- Customizable color scheme through IDE settings

### Code Completion
- Keyword completion for all WSL keywords
- Smart snippets for common patterns (workflow, state, action declarations)
- Template insertions with cursor positioning
- Dynamic module and method completion from `modules.json` (see [Module Completion Guide](MODULE_COMPLETION.md))
  - Place a `modules.json` file in your project root to define custom modules
  - Get intelligent suggestions for module paths and methods

### Code Navigation
- Structure view for workflows and states
- Brace matching for `{}` and `()`
- Comment toggling with `Ctrl+/` (or `Cmd+/` on macOS)

### File Type Recognition
- Automatic recognition of `.wsl` files
- Custom file icon for WSL files

## Installation

### From JetBrains Marketplace
1. Open your JetBrains IDE
2. Go to **Settings/Preferences** → **Plugins** → **Marketplace**
3. Search for "WSL Language Support"
4. Click **Install**

### From Disk
1. Download the plugin ZIP file from the releases
2. Open your JetBrains IDE
3. Go to **Settings/Preferences** → **Plugins** → **⚙️** → **Install Plugin from Disk**
4. Select the downloaded ZIP file

## Building from Source

### Prerequisites
- JDK 17 or higher
- Gradle 8.x

### Build Commands

```bash
# Navigate to the plugin directory
cd ide-plugins/jetbrains-wsl

# Build the plugin
./gradlew build

# Build and run in a sandbox IDE
./gradlew runIde

# Create distributable ZIP
./gradlew buildPlugin
```

The built plugin will be in `build/distributions/`.

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

For more details, see [Modules Loading Guide](MODULES_LOADING.md).

### Color Scheme

Customize WSL syntax highlighting colors:

1. Go to **Settings/Preferences** → **Editor** → **Color Scheme** → **WSL**
2. Modify colors for keywords, strings, comments, etc.

## Compatibility

- **IDE Version**: IntelliJ Platform 2023.3 and later
- **Supported IDEs**: IntelliJ IDEA, GoLand, WebStorm, PyCharm, PhpStorm, CLion, Rider, and other JetBrains IDEs

## License

This plugin is part of the Kuetix Engine project. See the LICENSE file in the repository root for details.

## Contributing

Contributions are welcome! Please read the contributing guidelines in the main repository.

## Support

- **Documentation**: See the `docs/WSL_SPEC.md` file for the complete WSL specification
- **Issues**: Report bugs and feature requests on the GitHub repository
- **Discussions**: Join the community discussions on GitHub

# WSL Language Support v1.0.0 - Initial Release 🎉

We're excited to announce the first official release of **WSL Language Support** for JetBrains IDEs!

This plugin provides comprehensive language support for the **Kuetix Workflow Specific Language (WSL)**, enabling developers to write workflow definitions with full IDE assistance.

---

## 🚀 Features

### Syntax Highlighting
- Full syntax highlighting for WSL keywords, strings, numbers, comments, and operators
- Customizable color scheme through IDE settings
- Support for all WSL language elements: `module`, `workflow`, `state`, `action`, `const`, etc.

### Intelligent Code Completion
- **Keyword completion** for all WSL language constructs
- **Smart snippets** for common patterns (workflow, state, action declarations)
- **Dynamic module completion** from `modules.json` files
  - Supports module paths and method suggestions
  - IntelliSense for module namespaces and methods
- Template insertions with automatic cursor positioning

### Advanced Module Loading
- **Multi-source module loading** from:
  - `<project-root>/modules.json` (base modules)
  - `<project-root>/workflows/modules.json` (workflow-specific modules)
  - `<project-root>/runtime/workflows/modules.json` (runtime modules)
- **Automatic merging** of modules from all sources
- **Auto-reload on file changes** - no IDE restart needed!
- **Robust error handling** - invalid modules are skipped gracefully

### Code Navigation & Structure
- **Structure view** for workflows and states
- **Brace matching** for `{}` and `()`
- **Code commenting** with `Ctrl+/` (or `Cmd+/` on macOS)
- Navigate through workflow definitions easily

### File Type Support
- Automatic recognition of `.wsl` files
- Custom file icon for WSL files
- Proper file type association

---

## 📦 Installation

### From JetBrains Marketplace
1. Open your JetBrains IDE (IntelliJ IDEA, GoLand, WebStorm, etc.)
2. Go to **Settings/Preferences** → **Plugins** → **Marketplace**
3. Search for "WSL Language Support"
4. Click **Install**

### From Release
1. Download `jetbrains-wsl-1.0.0.zip` from this release
2. Open your JetBrains IDE
3. Go to **Settings/Preferences** → **Plugins** → **⚙️** → **Install Plugin from Disk**
4. Select the downloaded ZIP file
5. Restart the IDE

---

## 🎯 Compatibility

- **IDE Version**: IntelliJ Platform 2023.3 and later (build 233 - 252.*)
- **Supported IDEs**:
  - IntelliJ IDEA (Community & Ultimate)
  - GoLand
  - WebStorm
  - PyCharm
  - PhpStorm
  - CLion
  - Rider
  - All other JetBrains IDEs

---

## 💡 Usage Example

### Basic WSL Workflow

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
}
```

### Module Completion Setup

Create a `modules.json` file in your project root:

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

Now get intelligent completion when typing `auth.login.` in your `.wsl` files!

---

## 🔧 Configuration

### Custom Module Definitions
Place `modules.json` files in:
- `<project-root>/modules.json` - Base modules
- `<project-root>/workflows/modules.json` - Workflow-specific modules
- `<project-root>/runtime/workflows/modules.json` - Runtime modules

All files are automatically loaded and merged. Changes apply instantly without IDE restart!

### Color Scheme Customization
1. Go to **Settings/Preferences** → **Editor** → **Color Scheme** → **WSL**
2. Customize colors for keywords, strings, comments, and more

---

## 🎨 Key Highlights

- ✅ **Zero configuration** - works out of the box with `.wsl` files
- ✅ **Flexible module loading** - supports multiple module sources
- ✅ **Hot reload** - file changes detected automatically
- ✅ **Error resilient** - gracefully handles invalid configurations
- ✅ **Production ready** - follows JetBrains plugin best practices
- ✅ **Well documented** - comprehensive guides included

---

## 📚 Documentation

Included in this release:
- **README.md** - Complete user guide
- **MODULE_COMPLETION.md** - Module completion guide
- **MODULES_LOADING.md** - Module loading and configuration
- **QUICK_REFERENCE.md** - Quick reference guide

---

## 🐛 Known Issues

None at this time. Please report any issues you encounter!

---

## 🙏 Acknowledgments

This plugin is part of the **Kuetix Engine** project, providing powerful workflow automation capabilities.

---

## 📄 License

See LICENSE file in the repository for details.

---

## 🔗 Links

- **Plugin Homepage**: [JetBrains Marketplace](https://plugins.jetbrains.com/) (coming soon)
- **Source Code**: [GitHub Repository](https://github.com/kuetix/engine)
- **Documentation**: See included guides in the distribution
- **Support**: Report issues on GitHub

---

## 📦 What's Included

This release includes:
- Full WSL language support for JetBrains IDEs
- Syntax highlighting and code completion
- Multi-source module loading with auto-reload
- Comprehensive documentation and examples
- Example `modules.json` files

---

## 🚀 Getting Started

1. Install the plugin from this release
2. Open or create a `.wsl` file
3. Start typing - enjoy syntax highlighting and code completion!
4. (Optional) Create `modules.json` for custom module completion
5. Check the included documentation for advanced features

---

**Thank you for using WSL Language Support!** 

We hope this plugin enhances your workflow development experience. Feel free to open issues for bugs or feature requests.

Happy coding! 🎉


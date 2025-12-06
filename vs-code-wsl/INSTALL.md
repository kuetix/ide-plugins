# Installation Guide

## Installing the WSL Language Support Extension in VS Code

### Method 1: Install from VSIX File

1. Locate the `wsl-language-support-1.0.0.vsix` file in this directory
2. Open Visual Studio Code
3. Click on the Extensions icon in the sidebar (or press `Ctrl+Shift+X` / `Cmd+Shift+X`)
4. Click on the "..." menu at the top of the Extensions panel
5. Select "Install from VSIX..."
6. Navigate to and select the `wsl-language-support-1.0.0.vsix` file
7. Restart VS Code if prompted

### Method 2: Install from Source

If you want to make modifications or rebuild:

```bash
# Navigate to the extension directory
cd vs-code-wsl

# Install dependencies
npm install

# Compile the TypeScript code
npm run compile

# Package the extension (optional)
npm install -g @vscode/vsce
vsce package

# Install the newly created VSIX file as described in Method 1
```

## Verifying Installation

1. Create a new file with the `.wsl` extension (e.g., `test.wsl`)
2. You should see the WSL icon next to the file name
3. Type `workflow` and you should see auto-completion suggestions
4. Try typing `//` and the rest of the line should be highlighted as a comment

## Using Module Completion

To enable module and method completion:

1. Create a `modules.json` file in your project root (or in `workflows/` or `runtime/workflows/`)
2. Define your modules following the format in `examples/modules.json`
3. The extension will automatically detect and load module definitions
4. When you type `action ` in a state block, you'll get completion suggestions for module paths and methods

## Troubleshooting

### Extension not loading
- Check the VS Code Extensions panel to ensure the extension is installed and enabled
- Open the Developer Console (Help → Toggle Developer Tools) to check for errors

### Completion not working
- Ensure you're in a `.wsl` file
- Check that your `modules.json` file is valid JSON
- Try reloading the VS Code window (Developer: Reload Window)

### Syntax highlighting issues
- Ensure the file has the `.wsl` extension
- Try closing and reopening the file

## Uninstalling

1. Open the Extensions panel in VS Code
2. Find "WSL Language Support"
3. Click the gear icon and select "Uninstall"
4. Restart VS Code

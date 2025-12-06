import * as vscode from 'vscode';
import * as fs from 'fs';
import * as path from 'path';

interface ModuleMethod {
    value: string;
    label: string;
    description: string;
}

interface ModuleInfo {
    namespace: string;
    class: string;
    label: string;
    description: string;
}

interface ModuleDefinition {
    info: ModuleInfo;
    methods: ModuleMethod[];
}

interface ModulesData {
    [key: string]: ModuleDefinition;
}

export class ModuleCompletionProvider implements vscode.CompletionItemProvider {
    private modules: ModulesData = {};
    private fileWatchers: vscode.FileSystemWatcher[] = [];

    constructor() {
        this.loadModules();
        this.watchModuleFiles();
    }

    private watchModuleFiles() {
        if (!vscode.workspace.workspaceFolders) {
            return;
        }

        // Watch modules.json files in common locations
        const patterns = [
            '**/modules.json',
            '**/workflows/modules.json',
            '**/runtime/workflows/modules.json'
        ];

        for (const pattern of patterns) {
            const watcher = vscode.workspace.createFileSystemWatcher(pattern);

            watcher.onDidCreate(() => this.loadModules());
            watcher.onDidChange(() => this.loadModules());
            watcher.onDidDelete(() => this.loadModules());

            this.fileWatchers.push(watcher);
        }
    }

    private loadModules() {
        this.modules = {};

        if (!vscode.workspace.workspaceFolders) {
            return;
        }

        // Try to load from multiple locations
        const searchPaths = [
            'modules.json',
            'modules/modules.json',
            'workflows/modules.json',
            'runtime/workflows/modules.json'
        ];

        for (const workspaceFolder of vscode.workspace.workspaceFolders) {
            for (const searchPath of searchPaths) {
                const modulesPath = path.join(workspaceFolder.uri.fsPath, searchPath);

                if (fs.existsSync(modulesPath)) {
                    try {
                        const content = fs.readFileSync(modulesPath, 'utf-8');
                        const moduleData: ModulesData = JSON.parse(content);

                        // Merge modules from this file
                        Object.assign(this.modules, moduleData);

                        console.log(`Loaded modules from ${modulesPath}`);
                    } catch (error) {
                        console.error(`Error loading modules from ${modulesPath}:`, error);
                    }
                }
            }
        }
    }

    provideCompletionItems(
        document: vscode.TextDocument,
        position: vscode.Position,
        _token: vscode.CancellationToken,
        _context: vscode.CompletionContext
    ): vscode.ProviderResult<vscode.CompletionItem[] | vscode.CompletionList> {
        const completionItems: vscode.CompletionItem[] = [];
        const lineText = document.lineAt(position.line).text;
        const textBeforeCursor = lineText.substring(0, position.character);

        // Check if we're in an action context
        let actionMatch = textBeforeCursor.match(/action\s+([a-zA-Z0-9_/.]*)([./])/);

        if (!actionMatch) {
            actionMatch = textBeforeCursor.match(/action\s+/);
            if (!actionMatch) {
                return completionItems;
            }
        }

        const currentPath = actionMatch?.[1] ?? "";
        const endsWithDot = actionMatch?.[2] ?? null;

        // If we have a complete module path (namespace/module.), suggest methods
        if (endsWithDot === ".") {
            const moduleKey = currentPath;

            // Look for matching module
            for (const [key, moduleData] of Object.entries(this.modules)) {
                if (key === moduleKey || key.endsWith('.' + moduleKey)) {
                    for (const method of moduleData.methods) {
                        const item = new vscode.CompletionItem(
                            method.value,
                            vscode.CompletionItemKind.Method
                        );
                        item.detail = method.label;
                        item.documentation = new vscode.MarkdownString(method.description);
                        item.insertText = method.value;
                        completionItems.push(item);
                    }
                }
            }
        } else {
            // Suggest module paths
            const moduleKeys = Object.keys(this.modules);
            const seenPaths = new Set<string>();

            for (const namespace of moduleKeys) {
                if (!seenPaths.has(namespace)) {
                    seenPaths.add(namespace);

                    const moduleData = this.modules[namespace];
                    const item = new vscode.CompletionItem(
                        namespace,
                        vscode.CompletionItemKind.Module
                    );
                    item.detail = moduleData.info.label;
                    item.documentation = new vscode.MarkdownString(moduleData.info.description);
                    item.insertText = namespace;
                    completionItems.push(item);
                }
            }
        }

        return completionItems;
    }

    dispose() {
        for (const watcher of this.fileWatchers) {
            watcher.dispose();
        }
    }
}

import * as vscode from 'vscode';
import { ModuleCompletionProvider } from './moduleCompletion';
import { KeywordCompletionProvider } from './keywordCompletion';

export function activate(context: vscode.ExtensionContext) {
    console.log('WSL Language Support extension is now active');

    // Register keyword completion provider
    const keywordProvider = vscode.languages.registerCompletionItemProvider(
        'wsl',
        new KeywordCompletionProvider(),
        ''
    );

    // Register module completion provider
    const moduleProvider = vscode.languages.registerCompletionItemProvider(
        'wsl',
        new ModuleCompletionProvider(),
        '/', // Trigger on slash for module paths
        '.' // Trigger on dot for nested paths
    );

    context.subscriptions.push(keywordProvider, moduleProvider);
}

export function deactivate() {
    console.log('WSL Language Support extension is now deactivated');
}

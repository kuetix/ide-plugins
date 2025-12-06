import * as vscode from 'vscode';

const WSL_KEYWORDS = [
    'module', 'import', 'extends', 'const', 'workflow', 'state', 
    'action', 'on', 'end', 'start', 'context', 'as', 'else',
    'success', 'error', 'fail', 'ok', 'true', 'false'
];

const KEYWORD_DETAILS: { [key: string]: string } = {
    'module': 'Declares a namespaced module name',
    'import': 'Brings other modules/namespaces into scope',
    'extends': 'Indicates inheritance of resolvers/configs',
    'const': 'Declares key/value constants accessible as $constants.*',
    'workflow': 'Declares a workflow with states and transitions',
    'state': 'Defines a state block with action and transitions',
    'action': 'Specifies an action to execute',
    'on': 'Defines transition conditions',
    'end': 'Marks a terminal state',
    'start': 'Specifies the initial state of a workflow',
    'context': 'Context keyword',
    'as': 'Alias keyword for naming results',
    'else': 'Else condition',
    'success': 'Success transition condition',
    'error': 'Error transition condition',
    'fail': 'Fail keyword',
    'ok': 'OK keyword',
    'true': 'Boolean true literal',
    'false': 'Boolean false literal'
};

export class KeywordCompletionProvider implements vscode.CompletionItemProvider {
    provideCompletionItems(
        document: vscode.TextDocument,
        position: vscode.Position,
        token: vscode.CancellationToken,
        context: vscode.CompletionContext
    ): vscode.ProviderResult<vscode.CompletionItem[] | vscode.CompletionList> {
        const completionItems: vscode.CompletionItem[] = [];

        for (const keyword of WSL_KEYWORDS) {
            const item = new vscode.CompletionItem(keyword, vscode.CompletionItemKind.Keyword);
            item.detail = KEYWORD_DETAILS[keyword] || 'WSL keyword';
            item.documentation = new vscode.MarkdownString(KEYWORD_DETAILS[keyword] || '');
            
            // Add snippet-like insertions for common patterns
            if (keyword === 'workflow') {
                item.insertText = new vscode.SnippetString(
                    'workflow ${1:workflow_name} {\n  start: ${2:InitialState}\n\n  state ${2:InitialState} {\n    action ${3:module/method}(${4:params})\n    on success -> ${5:NextState}\n    on error -> ${6:ErrorState}\n  }\n\n  state ${5:NextState} {\n    action ${7:module/method}(${8:params})\n    end ok\n  }\n\n  state ${6:ErrorState} {\n    action response/ResponseError(message: "${9:Error message}", code: ${10:500})\n    end error\n  }\n}'
                );
            } else if (keyword === 'state') {
                item.insertText = new vscode.SnippetString(
                    'state ${1:StateName} {\n  action ${2:module/method}(${3:params})\n  on success -> ${4:NextState}\n  on error -> ${5:ErrorState}\n}'
                );
            } else if (keyword === 'action') {
                item.insertText = new vscode.SnippetString(
                    'action ${1:module/method}(${2:params})${3: as ${4:Result}}'
                );
            } else if (keyword === 'const') {
                item.insertText = new vscode.SnippetString(
                    'const {\n  ${1:key}: "${2:value}"$0\n}'
                );
            }

            completionItems.push(item);
        }

        return completionItems;
    }
}

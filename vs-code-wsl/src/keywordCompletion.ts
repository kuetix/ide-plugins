import * as vscode from 'vscode';

const WSL_KEYWORDS = [
    'module', 'import', 'extends', 'const', 'workflow', 'state',
    'action', 'on', 'end', 'start', 'context', 'as', 'else',
    'if', 'when', 'let', 'foreach', 'in', 'while', 'retry', 'parallel',
    'continue on fail', 'skip to',
    'success', 'error', 'fail', 'ok', 'true', 'false', 'null'
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
    'else': 'Path taken when an `if` condition is false',
    'if': 'Pre-condition: skip the state (take `else`) when the expression is falsy',
    'when': 'Guard on a transition: `on success when <expr> -> Next` (first truthy guard wins)',
    'let': 'Bind an intermediate value before the action: `let name = <expr>` (single-assignment)',
    'foreach': 'Loop the body action over a collection: `foreach x in <expr> [parallel[limit: K]] { action ... }`',
    'in': 'Separates the loop variable from the collection in `foreach`',
    'while': 'Loop the body while an expression is truthy: `while[max: N] <expr> { action ... }` (max required)',
    'retry': 'Re-run the action after a failure: `retry[max: N, delay: "200ms", on: "<expr>"]`',
    'parallel': 'Concurrency: `parallel[count: N]` state, or `foreach ... parallel[limit: K]`',
    'continue on fail': 'Proceed even if the action errors',
    'skip to': 'Skip this state under certain conditions',
    'success': 'Success transition condition',
    'error': 'Error transition condition (alias: fail)',
    'fail': 'Failure transition condition',
    'ok': 'Terminal success: `end ok`',
    'true': 'Boolean true literal',
    'false': 'Boolean false literal',
    'null': 'Null literal'
};

export class KeywordCompletionProvider implements vscode.CompletionItemProvider {
    provideCompletionItems(
        _document: vscode.TextDocument,
        _position: vscode.Position,
        _token: vscode.CancellationToken,
        _context: vscode.CompletionContext
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
            } else if (keyword === 'let') {
                item.insertText = new vscode.SnippetString('let ${1:name} = ${2:expr}');
            } else if (keyword === 'foreach') {
                item.insertText = new vscode.SnippetString(
                    'foreach ${1:item} in ${2:<<collection>>} {\n  action ${3:module/method}(${4:params}) as ${5:Result}\n}'
                );
            } else if (keyword === 'while') {
                item.insertText = new vscode.SnippetString(
                    'while[max: ${1:500}] ${2:<<condition>>} {\n  action ${3:module/method}(${4:params}) as ${5:Result}\n}'
                );
            } else if (keyword === 'retry') {
                item.insertText = new vscode.SnippetString(
                    'retry[max: ${1:3}, delay: "${2:200ms}", on: "${3:err.retryable == true}"]'
                );
            }

            completionItems.push(item);
        }

        return completionItems;
    }
}

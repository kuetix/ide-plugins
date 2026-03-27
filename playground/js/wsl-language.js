// Monaco Editor Language Definitions for WSL and SimplifiedWSL

// SimplifiedWSL Language Definition
const swslLanguageDefinition = {
    defaultToken: '',
    tokenPostfix: '.swsl',
    
    keywords: [
        'module', 'import', 'const', 'def', 'as',
        'workflow', 'feature', 'solution', 'microservice',
        'state', 'on', 'end', 'start', 'action',
        'if', 'else', 'for', 'while', 'return'
    ],

    operators: [
        '->', '<-', '.', ':', '(', ')', '{', '}', '[', ']',
        '=', ',', '<<', '>>'
    ],

    // Common constants
    constants: [
        'true', 'false', 'null', 'undefined'
    ],

    // Built-in variables
    builtins: [
        '$context', '$constants', '$input', '$state', '$output', '$error'
    ],

    // Comments
    tokenizer: {
        root: [
            // Comments
            [/\/\/.*$/, 'comment'],
            [/#.*$/, 'comment'],
            [/\/\*/, 'comment', '@comment'],

            // Workflow type declarations
            [/\b(workflow|feature|solution|microservice)\s+(\w+)?\b/, ['keyword', 'type']],
            [/\b(workflow|feature|solution|microservice)\b/, 'keyword'],

            // Module and imports
            [/\bmodule\s+(\w+)/, ['keyword', 'namespace']],
            [/\bimport\s+/, 'keyword'],

            // Keywords
            [/\b(const|def|as|state|on|end|start|action|if|else|for|while|return)\b/, 'keyword'],

            // Constants
            [/\b(true|false|null|undefined)\b/, 'constant'],

            // Built-in variables
            [/\$\w+(\.\w+)*/, 'variable.predefined'],

            // Numbers
            [/\d+\.?\d*/, 'number'],

            // Strings
            [/"([^"\\]|\\.)*$/, 'string.invalid'],
            [/'([^'\\]|\\.)*$/, 'string.invalid'],
            [/"/, 'string', '@string_double'],
            [/'/, 'string', '@string_single'],

            // Operators
            [/->/, 'operator'],
            [/<-/, 'operator'],
            [/\.(?!\w)/, 'operator'],
            [/:(?!:)/, 'operator'],
            [/<<|>>/, 'delimiter'],

            // Action calls (module.Method or module/submodule.Method)
            [/\w+([\/\.]\w+)*\.\w+/, 'type.identifier'],

            // Identifiers
            [/[a-z_]\w*/, 'identifier'],
            [/[A-Z][\w]*/, 'type'],

            // Delimiters
            [/[{}()\[\]]/, '@brackets'],
            [/[,;]/, 'delimiter'],
        ],

        comment: [
            [/[^\/*]+/, 'comment'],
            [/\*\//, 'comment', '@pop'],
            [/[\/*]/, 'comment']
        ],

        string_double: [
            [/[^\\"]+/, 'string'],
            [/\\./, 'string.escape'],
            [/"/, 'string', '@pop']
        ],

        string_single: [
            [/[^\\']+/, 'string'],
            [/\\./, 'string.escape'],
            [/'/, 'string', '@pop']
        ]
    }
};

// WSL Language Definition (Traditional)
const wslLanguageDefinition = {
    defaultToken: '',
    tokenPostfix: '.wsl',
    
    keywords: [
        'workflow', 'state', 'action', 'on', 'success', 'error',
        'start', 'end', 'ok', 'module', 'import'
    ],

    operators: [
        ':', '{', '}', '(', ')', ',', '->'
    ],

    constants: [
        'true', 'false', 'null'
    ],

    builtins: [
        '$input', '$state', '$output', '$error', '$context'
    ],

    tokenizer: {
        root: [
            // Comments
            [/\/\/.*$/, 'comment'],
            [/\/\*/, 'comment', '@comment'],

            // Keywords
            [/\b(workflow|state|action|on|success|error|start|end|ok|module|import)\b/, 'keyword'],

            // Constants
            [/\b(true|false|null)\b/, 'constant'],

            // Built-in variables
            [/\$\w+/, 'variable.predefined'],

            // Numbers
            [/\d+\.?\d*/, 'number'],

            // Strings
            [/"([^"\\]|\\.)*$/, 'string.invalid'],
            [/"/, 'string', '@string'],

            // Identifiers
            [/[a-z_][\w]*/, 'identifier'],
            [/[A-Z][\w]*/, 'type'],

            // Operators
            [/->/, 'operator'],

            // Delimiters
            [/[{}()\[\]]/, '@brackets'],
            [/[,;:]/, 'delimiter'],
        ],

        comment: [
            [/[^\/*]+/, 'comment'],
            [/\*\//, 'comment', '@pop'],
            [/[\/*]/, 'comment']
        ],

        string: [
            [/[^\\"]+/, 'string'],
            [/\\./, 'string.escape'],
            [/"/, 'string', '@pop']
        ]
    }
};

// Language Configuration (applies to both WSL and SWSL)
const languageConfiguration = {
    comments: {
        lineComment: '//',
        blockComment: ['/*', '*/']
    },
    brackets: [
        ['{', '}'],
        ['[', ']'],
        ['(', ')']
    ],
    autoClosingPairs: [
        { open: '{', close: '}' },
        { open: '[', close: ']' },
        { open: '(', close: ')' },
        { open: '"', close: '"' },
        { open: "'", close: "'" }
    ],
    surroundingPairs: [
        { open: '{', close: '}' },
        { open: '[', close: ']' },
        { open: '(', close: ')' },
        { open: '"', close: '"' },
        { open: "'", close: "'" }
    ],
    folding: {
        markers: {
            start: /^\s*\/\/#region/,
            end: /^\s*\/\/#endregion/
        }
    }
};

// Theme definition for SWSL/WSL
const wslTheme = {
    base: 'vs',
    inherit: true,
    rules: [
        { token: 'comment', foreground: '6a737d', fontStyle: 'italic' },
        { token: 'keyword', foreground: 'd73a49', fontStyle: 'bold' },
        { token: 'operator', foreground: 'd73a49' },
        { token: 'string', foreground: '032f62' },
        { token: 'number', foreground: '005cc5' },
        { token: 'type', foreground: '6f42c1' },
        { token: 'type.identifier', foreground: '005cc5' },
        { token: 'variable.predefined', foreground: 'e36209' },
        { token: 'constant', foreground: '005cc5' },
        { token: 'namespace', foreground: '6f42c1' }
    ],
    colors: {
        'editor.foreground': '#24292e',
        'editor.background': '#ffffff'
    }
};

// Dark theme
const wslThemeDark = {
    base: 'vs-dark',
    inherit: true,
    rules: [
        { token: 'comment', foreground: '8b949e', fontStyle: 'italic' },
        { token: 'keyword', foreground: 'ff7b72', fontStyle: 'bold' },
        { token: 'operator', foreground: 'ff7b72' },
        { token: 'string', foreground: 'a5d6ff' },
        { token: 'number', foreground: '79c0ff' },
        { token: 'type', foreground: 'd2a8ff' },
        { token: 'type.identifier', foreground: '79c0ff' },
        { token: 'variable.predefined', foreground: 'ffa657' },
        { token: 'constant', foreground: '79c0ff' },
        { token: 'namespace', foreground: 'd2a8ff' }
    ],
    colors: {
        'editor.foreground': '#e6edf3',
        'editor.background': '#0d1117'
    }
};

// WSL Playground Main Script

let editor;
let currentTheme = 'light';
let currentLanguage = 'swsl';

// Initialize the playground
function init() {
    // Set up Monaco Editor loader
    require.config({ 
        paths: { 
            'vs': 'https://cdnjs.cloudflare.com/ajax/libs/monaco-editor/0.45.0/min/vs' 
        } 
    });

    require(['vs/editor/editor.main'], function() {
        // Register languages
        monaco.languages.register({ id: 'swsl' });
        monaco.languages.register({ id: 'wsl' });

        // Set language configurations
        monaco.languages.setLanguageConfiguration('swsl', languageConfiguration);
        monaco.languages.setLanguageConfiguration('wsl', languageConfiguration);

        // Set token providers (syntax highlighting)
        monaco.languages.setMonarchTokensProvider('swsl', swslLanguageDefinition);
        monaco.languages.setMonarchTokensProvider('wsl', wslLanguageDefinition);

        // Define themes
        monaco.editor.defineTheme('wsl-light', wslTheme);
        monaco.editor.defineTheme('wsl-dark', wslThemeDark);

        // Create editor instance
        editor = monaco.editor.create(document.getElementById('editor'), {
            value: getDefaultCode(),
            language: 'swsl',
            theme: 'wsl-light',
            fontSize: 14,
            lineNumbers: 'on',
            roundedSelection: false,
            scrollBeyondLastLine: false,
            readOnly: false,
            minimap: {
                enabled: true
            },
            automaticLayout: true,
            tabSize: 2,
            wordWrap: 'on',
            wrappingIndent: 'indent',
            folding: true,
            lineDecorationsWidth: 10,
            lineNumbersMinChars: 3
        });

        // Set up event listeners
        setupEventListeners();

        // Load code from URL if present
        loadFromURL();
    });
}

// Get default code
function getDefaultCode() {
    const example = getExample('hello_world');
    return example ? example.code : '// Start writing your WSL code here\n';
}

// Setup event listeners
function setupEventListeners() {
    // Language selector
    document.getElementById('language-selector').addEventListener('change', (e) => {
        currentLanguage = e.target.value;
        changeLanguage(currentLanguage);
    });

    // Example selector
    document.getElementById('example-selector').addEventListener('change', (e) => {
        const exampleKey = e.target.value;
        if (exampleKey) {
            loadExample(exampleKey);
        }
    });

    // Theme toggle
    document.getElementById('theme-toggle').addEventListener('click', toggleTheme);

    // Share button
    document.getElementById('share-btn').addEventListener('click', shareCode);

    // Format button
    document.getElementById('format-btn').addEventListener('click', formatCode);

    // Clear button
    document.getElementById('clear-btn').addEventListener('click', clearEditor);

    // Clear output button
    document.getElementById('clear-output-btn').addEventListener('click', clearOutput);

    // Editor change listener for validation
    editor.onDidChangeModelContent(() => {
        validateCode();
    });

    // Handle messages from parent window (for iframe embedding)
    window.addEventListener('message', handleMessage);
}

// Change editor language
function changeLanguage(lang) {
    if (editor) {
        monaco.editor.setModelLanguage(editor.getModel(), lang);
        addOutput(`Language changed to: ${lang.toUpperCase()}`, 'info');
    }
}

// Load example
function loadExample(key) {
    const example = getExample(key);
    if (example) {
        editor.setValue(example.code);
        if (example.language !== currentLanguage) {
            currentLanguage = example.language;
            document.getElementById('language-selector').value = example.language;
            changeLanguage(example.language);
        }
        addOutput(`Loaded example: ${example.name}`, 'success');
    }
}

// Toggle theme
function toggleTheme() {
    currentTheme = currentTheme === 'light' ? 'dark' : 'light';
    const theme = currentTheme === 'dark' ? 'wsl-dark' : 'wsl-light';
    const icon = document.querySelector('.theme-icon');
    
    monaco.editor.setTheme(theme);
    document.body.setAttribute('data-theme', currentTheme);
    icon.textContent = currentTheme === 'dark' ? '☀️' : '🌙';
    
    addOutput(`Theme changed to: ${currentTheme}`, 'info');
}

// Format code (basic formatting)
function formatCode() {
    editor.getAction('editor.action.formatDocument').run();
    addOutput('Code formatted', 'success');
}

// Clear editor
function clearEditor() {
    if (confirm('Are you sure you want to clear the editor?')) {
        editor.setValue('');
        addOutput('Editor cleared', 'info');
    }
}

// Clear output
function clearOutput() {
    document.getElementById('output').innerHTML = '';
}

// Add output message
function addOutput(message, type = 'info') {
    const output = document.getElementById('output');
    const messageDiv = document.createElement('div');
    messageDiv.className = `output-message ${type}`;
    messageDiv.textContent = message;
    output.appendChild(messageDiv);
    output.scrollTop = output.scrollHeight;
}

// Validate code (basic syntax checking)
function validateCode() {
    const code = editor.getValue();
    const language = monaco.editor.getModel(editor.getModel()).getLanguageId();
    
    // Clear previous markers
    monaco.editor.setModelMarkers(editor.getModel(), 'syntax', []);
    
    // Basic validation rules
    const errors = [];
    const lines = code.split('\n');
    
    if (language === 'swsl') {
        // Check for proper workflow type declaration
        const hasWorkflowType = /^\s*(workflow|feature|solution|microservice)\s+\w*/m.test(code);
        
        // Check for terminal operator
        const hasTerminal = /\.\s*$/m.test(code);
        
        // Check for unbalanced operators
        const arrows = (code.match(/->/g) || []).length;
        const terminals = (code.match(/\.\s*($|\n)/g) || []).length;
        
        // Validate each line
        lines.forEach((line, index) => {
            const trimmed = line.trim();
            
            // Check for invalid operator sequences
            if (trimmed.includes('->->') || trimmed.includes('<-<-')) {
                errors.push({
                    startLineNumber: index + 1,
                    startColumn: 1,
                    endLineNumber: index + 1,
                    endColumn: line.length + 1,
                    message: 'Invalid operator sequence',
                    severity: monaco.MarkerSeverity.Error
                });
            }
            
            // Check for terminal operator not at end
            if (trimmed.includes('.') && !trimmed.endsWith('.') && !/\.\w/.test(trimmed)) {
                const dotIndex = trimmed.indexOf('.');
                if (dotIndex < trimmed.length - 1 && trimmed[dotIndex + 1] !== ' ') {
                    errors.push({
                        startLineNumber: index + 1,
                        startColumn: dotIndex + 1,
                        endLineNumber: index + 1,
                        endColumn: dotIndex + 2,
                        message: 'Terminal operator should be at end of workflow or part of method call',
                        severity: monaco.MarkerSeverity.Warning
                    });
                }
            }
        });
    }
    
    // Set markers
    if (errors.length > 0) {
        monaco.editor.setModelMarkers(editor.getModel(), 'syntax', errors);
    }
}

// Share code
function shareCode() {
    const code = editor.getValue();
    const lang = currentLanguage;
    
    // Encode code to base64
    const encoded = btoa(encodeURIComponent(code));
    
    // Create shareable URL
    const url = `${window.location.origin}${window.location.pathname}?code=${encoded}&lang=${lang}`;
    
    // Copy to clipboard
    navigator.clipboard.writeText(url).then(() => {
        addOutput('Share URL copied to clipboard! 🎉', 'success');
        addOutput(`URL: ${url}`, 'info');
    }).catch(() => {
        // Fallback: show in output
        addOutput('Share URL:', 'info');
        addOutput(url, 'info');
    });
}

// Load code from URL
function loadFromURL() {
    const params = new URLSearchParams(window.location.search);
    const encodedCode = params.get('code');
    const lang = params.get('lang');
    
    if (encodedCode) {
        try {
            const code = decodeURIComponent(atob(encodedCode));
            editor.setValue(code);
            addOutput('Code loaded from URL', 'success');
        } catch (e) {
            addOutput('Failed to load code from URL', 'error');
        }
    }
    
    if (lang && (lang === 'wsl' || lang === 'swsl')) {
        currentLanguage = lang;
        document.getElementById('language-selector').value = lang;
        changeLanguage(lang);
    }
}

// Handle messages from parent window (for iframe embedding)
function handleMessage(event) {
    const { type, data } = event.data;
    
    switch (type) {
        case 'setCode':
            if (data && data.code) {
                editor.setValue(data.code);
                if (data.language) {
                    currentLanguage = data.language;
                    changeLanguage(data.language);
                }
            }
            break;
            
        case 'getCode':
            // Send code back to parent
            event.source.postMessage({
                type: 'code',
                data: {
                    code: editor.getValue(),
                    language: currentLanguage
                }
            }, event.origin);
            break;
            
        case 'setTheme':
            if (data && data.theme) {
                currentTheme = data.theme;
                const theme = currentTheme === 'dark' ? 'wsl-dark' : 'wsl-light';
                monaco.editor.setTheme(theme);
                document.body.setAttribute('data-theme', currentTheme);
            }
            break;
    }
}

// Export code (for download)
function exportCode() {
    const code = editor.getValue();
    const lang = currentLanguage;
    const filename = `workflow.${lang}`;
    
    const blob = new Blob([code], { type: 'text/plain' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = filename;
    a.click();
    URL.revokeObjectURL(url);
    
    addOutput(`Code exported as ${filename}`, 'success');
}

// Initialize when DOM is ready
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', init);
} else {
    init();
}

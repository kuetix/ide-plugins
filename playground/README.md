# WSL Playground - Interactive Web Editor

An embeddable, web-based playground for the Workflow Specific Language (WSL) and SimplifiedWSL (SWSL) with real-time syntax highlighting and validation.

## Features

- 🎨 **Syntax Highlighting** - Full syntax highlighting for both WSL and SimplifiedWSL
- 🌓 **Dark/Light Theme** - Toggle between light and dark themes
- 📝 **Multiple Examples** - Pre-built examples to get started quickly
- 🔗 **Shareable URLs** - Share your code via URL
- 📱 **Responsive Design** - Works on desktop, tablet, and mobile
- 🎯 **Embeddable** - Easy to embed in any website via iframe
- ⚡ **Real-time Validation** - Instant syntax validation as you type
- 🚀 **Zero Installation** - Works directly in the browser

## Quick Start

### Standalone Usage

1. Open `index.html` in your web browser
2. Select an example or start writing your own WSL code
3. Use the toolbar to change themes, clear the editor, or share your code

### Embedding in Your Website

#### Basic Embed

Add this iframe to your HTML:

```html
<iframe 
    src="https://your-domain.com/playground/embed.html"
    width="100%"
    height="600"
    frameborder="0"
    style="border: 1px solid #ddd; border-radius: 8px;"
></iframe>
```

#### Embed with Initial Code

Pass code via URL parameter:

```html
<iframe 
    src="https://your-domain.com/playground/embed.html?code=BASE64_ENCODED_CODE&lang=swsl"
    width="100%"
    height="600"
    frameborder="0"
></iframe>
```

#### Responsive Embed

```html
<div style="position: relative; padding-bottom: 56.25%; height: 0; overflow: hidden;">
    <iframe 
        src="https://your-domain.com/playground/embed.html"
        style="position: absolute; top: 0; left: 0; width: 100%; height: 100%; border: 0;"
    ></iframe>
</div>
```

## JavaScript API (for iframe communication)

### Send Code to Embedded Playground

```javascript
const iframe = document.querySelector('iframe');

// Set code in the playground
iframe.contentWindow.postMessage({
    type: 'setCode',
    data: {
        code: 'module example\n\nworkflow demo\n\naction.Call() -> .',
        language: 'swsl'
    }
}, '*');
```

### Get Code from Embedded Playground

```javascript
// Request code from playground
iframe.contentWindow.postMessage({
    type: 'getCode'
}, '*');

// Listen for response
window.addEventListener('message', (event) => {
    if (event.data.type === 'code') {
        console.log('Code:', event.data.data.code);
        console.log('Language:', event.data.data.language);
    }
});
```

### Change Theme

```javascript
iframe.contentWindow.postMessage({
    type: 'setTheme',
    data: { theme: 'dark' } // or 'light'
}, '*');
```

### Listen for Ready Event

```javascript
window.addEventListener('message', (event) => {
    if (event.data.type === 'ready') {
        console.log('Playground is ready!');
        // Now you can send messages to the iframe
    }
});
```

## File Structure

```
playground/
├── index.html              # Main playground page
├── embed.html              # Embeddable version
├── css/
│   └── playground.css      # Styles for the playground
├── js/
│   ├── playground.js       # Main playground logic
│   ├── wsl-language.js     # Monaco Editor language definitions
│   └── examples.js         # Example code snippets
└── README.md              # This file
```

## Examples Included

### SimplifiedWSL Examples

1. **Hello World** - Basic SimplifiedWSL syntax
2. **Payment Feature** - Feature-level workflow
3. **Solution Example** - Solution-level orchestration
4. **Validate Workflow** - Payment validation
5. **Action Chain** - Chained action execution
6. **Hierarchical Call** - Multi-level workflow calls
7. **Error Handling** - Error handling patterns

### Traditional WSL Examples

1. **Login Workflow** - Complete login workflow with state machine

## Customization

### Change Default Language

Edit `playground.js`:

```javascript
let currentLanguage = 'swsl'; // Change to 'wsl' for WSL
```

### Add Custom Examples

Edit `examples.js`:

```javascript
const examples = {
    my_example: {
        name: "My Example",
        language: "swsl",
        code: `// Your code here`
    }
};
```

### Customize Theme Colors

Edit `playground.css` CSS variables:

```css
:root {
    --accent-color: #0969da;  /* Change accent color */
    --bg-primary: #ffffff;    /* Background color */
    /* ... other variables ... */
}
```

## Browser Support

- ✅ Chrome 90+
- ✅ Firefox 88+
- ✅ Safari 14+
- ✅ Edge 90+

## Dependencies

- **Monaco Editor** (v0.45.0) - Microsoft's code editor (powers VS Code)
  - Loaded via CDN: `https://cdnjs.cloudflare.com/ajax/libs/monaco-editor/0.45.0/`

## Deployment

### GitHub Pages

1. Push the `playground` directory to your repository
2. Enable GitHub Pages in repository settings
3. Set source to `main` branch and `/playground` folder
4. Access at `https://yourusername.github.io/repository-name/`

### Static Hosting (Netlify, Vercel, etc.)

1. Deploy the `playground` directory
2. Configure build settings (none required - pure static)
3. Set publish directory to `playground`

### Local Development

Simply open `index.html` in your browser. For best results, use a local server:

```bash
# Python 3
python -m http.server 8000

# Node.js
npx serve

# PHP
php -S localhost:8000
```

Then open `http://localhost:8000/index.html`

## Integration Examples

### WordPress

```php
<div style="width: 100%; max-width: 1200px; margin: 0 auto;">
    <iframe 
        src="https://your-playground-url.com/embed.html"
        width="100%"
        height="600"
        frameborder="0"
        style="border: 1px solid #ddd; border-radius: 8px;"
    ></iframe>
</div>
```

### React

```jsx
import React from 'react';

function WSLPlayground() {
    return (
        <iframe
            src="https://your-playground-url.com/embed.html"
            width="100%"
            height="600"
            frameBorder="0"
            style={{ border: '1px solid #ddd', borderRadius: '8px' }}
        />
    );
}

export default WSLPlayground;
```

### Vue

```vue
<template>
    <iframe
        src="https://your-playground-url.com/embed.html"
        width="100%"
        height="600"
        frameborder="0"
        style="border: 1px solid #ddd; border-radius: 8px;"
    />
</template>
```

## Security Considerations

- The playground runs entirely in the browser (client-side)
- No code is sent to any server
- URL parameters use base64 encoding (not encryption)
- When embedding, consider using Content Security Policy (CSP)
- Validate any messages received via `postMessage` API

## Performance

- Lightweight: ~50KB total (excluding Monaco Editor CDN)
- Monaco Editor loads asynchronously
- No build step required
- Fast initial load time

## Troubleshooting

### Editor not loading

- Check browser console for errors
- Ensure CDN is accessible
- Try using a different CDN mirror

### Syntax highlighting not working

- Verify language is registered correctly
- Check Monaco Editor version compatibility
- Ensure `wsl-language.js` is loaded

### iframe not communicating

- Check `postMessage` origin restrictions
- Verify both pages are served over same protocol (http/https)
- Look for CORS errors in console

## Contributing

To add features or fix bugs:

1. Edit the appropriate files in `js/` or `css/`
2. Test in multiple browsers
3. Update documentation in this README
4. Submit a pull request

## License

This playground is part of the Kuetix IDE Plugins project.

## Links

- [Main Repository](https://github.com/kuetix/ide-plugins)
- [VS Code Extension](../vs-code-wsl/)
- [JetBrains Plugin](../jetbrains-wsl/)
- [WSL Documentation](../SIMPLIFIED_WSL.md)

## Support

For issues or questions:
- Open an issue on [GitHub](https://github.com/kuetix/ide-plugins/issues)
- Check the [documentation](../README.md)

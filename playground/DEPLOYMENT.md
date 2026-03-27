# Deploying WSL Playground

This guide explains how to deploy the WSL Playground to various hosting platforms.

## GitHub Pages (Recommended)

GitHub Pages is the easiest way to deploy the playground for free.

### Method 1: Using GitHub Pages Settings

1. **Push the code to GitHub** (already done if using this repository)

2. **Enable GitHub Pages:**
   - Go to your repository on GitHub
   - Click **Settings** → **Pages**
   - Under "Source", select the branch (e.g., `main`)
   - Select folder: `/ (root)` or configure to serve from `playground` directory
   - Click **Save**

3. **Access your playground:**
   - After a few minutes, your playground will be available at:
   - `https://[username].github.io/[repository-name]/playground/`

### Method 2: Using GitHub Actions

Create `.github/workflows/deploy-playground.yml`:

```yaml
name: Deploy Playground to GitHub Pages

on:
  push:
    branches: [ main ]
    paths:
      - 'playground/**'
  workflow_dispatch:

permissions:
  contents: read
  pages: write
  id-token: write

jobs:
  deploy:
    environment:
      name: github-pages
      url: ${{ steps.deployment.outputs.page_url }}
    runs-on: ubuntu-latest
    steps:
      - name: Checkout
        uses: actions/checkout@v3
      
      - name: Setup Pages
        uses: actions/configure-pages@v3
      
      - name: Upload artifact
        uses: actions/upload-pages-artifact@v2
        with:
          path: 'playground'
      
      - name: Deploy to GitHub Pages
        id: deployment
        uses: actions/deploy-pages@v2
```

## Netlify

Deploy to Netlify with one click or via CLI.

### Using Netlify UI

1. **Connect your repository:**
   - Go to [netlify.com](https://netlify.com)
   - Click "Add new site" → "Import an existing project"
   - Connect your GitHub repository

2. **Configure build settings:**
   - **Build command:** (leave empty - no build needed)
   - **Publish directory:** `playground`
   - Click **Deploy site**

3. **Access your playground:**
   - Your site will be available at `https://[random-name].netlify.app`
   - You can customize the domain in site settings

### Using Netlify CLI

```bash
# Install Netlify CLI
npm install -g netlify-cli

# Navigate to playground directory
cd playground

# Deploy
netlify deploy --prod --dir .
```

### netlify.toml Configuration

Create `netlify.toml` in the root:

```toml
[build]
  publish = "playground"
  command = ""

[[redirects]]
  from = "/*"
  to = "/index.html"
  status = 200
```

## Vercel

Deploy to Vercel for fast global CDN delivery.

### Using Vercel UI

1. **Import your repository:**
   - Go to [vercel.com](https://vercel.com)
   - Click "Add New" → "Project"
   - Import your GitHub repository

2. **Configure project:**
   - **Framework Preset:** Other
   - **Root Directory:** `playground`
   - **Build Command:** (leave empty)
   - **Output Directory:** (leave empty)
   - Click **Deploy**

3. **Access your playground:**
   - Your site will be available at `https://[project-name].vercel.app`

### Using Vercel CLI

```bash
# Install Vercel CLI
npm install -g vercel

# Navigate to playground directory
cd playground

# Deploy
vercel --prod
```

### vercel.json Configuration

Create `vercel.json` in playground directory:

```json
{
  "buildCommand": "",
  "outputDirectory": ".",
  "cleanUrls": true,
  "trailingSlash": false
}
```

## AWS S3 + CloudFront

For enterprise-grade deployment with AWS.

### Prerequisites
- AWS Account
- AWS CLI installed and configured

### Deployment Steps

1. **Create S3 Bucket:**
```bash
aws s3 mb s3://wsl-playground
```

2. **Configure bucket for static hosting:**
```bash
aws s3 website s3://wsl-playground --index-document index.html
```

3. **Upload files:**
```bash
cd playground
aws s3 sync . s3://wsl-playground --acl public-read
```

4. **Set bucket policy:**
```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Sid": "PublicReadGetObject",
      "Effect": "Allow",
      "Principal": "*",
      "Action": "s3:GetObject",
      "Resource": "arn:aws:s3:::wsl-playground/*"
    }
  ]
}
```

5. **Optional: Set up CloudFront for CDN**

## Custom Server

Deploy on your own server with any web server.

### Using Nginx

1. **Copy files to web server:**
```bash
scp -r playground/* user@server:/var/www/wsl-playground/
```

2. **Configure Nginx:**
```nginx
server {
    listen 80;
    server_name playground.yourdomain.com;
    
    root /var/www/wsl-playground;
    index index.html;
    
    location / {
        try_files $uri $uri/ /index.html;
    }
    
    # Security headers
    add_header X-Frame-Options "SAMEORIGIN" always;
    add_header X-Content-Type-Options "nosniff" always;
    add_header X-XSS-Protection "1; mode=block" always;
    
    # Cache static assets
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }
}
```

3. **Restart Nginx:**
```bash
sudo systemctl restart nginx
```

### Using Apache

1. **Copy files:**
```bash
scp -r playground/* user@server:/var/www/html/playground/
```

2. **Configure .htaccess:**
```apache
<IfModule mod_rewrite.c>
    RewriteEngine On
    RewriteBase /playground/
    RewriteRule ^index\.html$ - [L]
    RewriteCond %{REQUEST_FILENAME} !-f
    RewriteCond %{REQUEST_FILENAME} !-d
    RewriteRule . /playground/index.html [L]
</IfModule>

# Security headers
<IfModule mod_headers.c>
    Header set X-Frame-Options "SAMEORIGIN"
    Header set X-Content-Type-Options "nosniff"
    Header set X-XSS-Protection "1; mode=block"
</IfModule>

# Cache control
<IfModule mod_expires.c>
    ExpiresActive On
    ExpiresByType text/css "access plus 1 year"
    ExpiresByType application/javascript "access plus 1 year"
    ExpiresByType image/png "access plus 1 year"
    ExpiresByType image/svg+xml "access plus 1 year"
</IfModule>
```

## Docker

Deploy using Docker container.

### Dockerfile

Create `Dockerfile` in playground directory:

```dockerfile
FROM nginx:alpine

# Copy playground files
COPY . /usr/share/nginx/html/

# Copy custom nginx config
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
```

### nginx.conf

```nginx
server {
    listen 80;
    server_name localhost;
    
    root /usr/share/nginx/html;
    index index.html;
    
    location / {
        try_files $uri $uri/ /index.html;
    }
    
    # Security headers
    add_header X-Frame-Options "SAMEORIGIN" always;
    add_header X-Content-Type-Options "nosniff" always;
}
```

### Build and Run

```bash
cd playground

# Build Docker image
docker build -t wsl-playground .

# Run container
docker run -d -p 8080:80 wsl-playground

# Access at http://localhost:8080
```

## Environment-Specific Configurations

### Production Checklist

- [ ] Enable HTTPS/SSL
- [ ] Configure CDN for Monaco Editor (or self-host)
- [ ] Set up proper CORS headers
- [ ] Enable gzip/brotli compression
- [ ] Configure caching headers
- [ ] Set up monitoring/analytics
- [ ] Test on multiple browsers
- [ ] Test mobile responsiveness
- [ ] Set up error tracking (e.g., Sentry)

### Performance Optimization

1. **Enable Compression:**
```nginx
gzip on;
gzip_types text/plain text/css application/json application/javascript text/xml application/xml;
```

2. **Browser Caching:**
```nginx
location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg)$ {
    expires 1y;
    add_header Cache-Control "public, immutable";
}
```

3. **Content Security Policy:**
```nginx
add_header Content-Security-Policy "default-src 'self'; script-src 'self' 'unsafe-inline' 'unsafe-eval' https://cdnjs.cloudflare.com; style-src 'self' 'unsafe-inline' https://cdnjs.cloudflare.com; font-src 'self' data:; img-src 'self' data:; connect-src 'self';" always;
```

## Updating Your Deployment

### GitHub Pages
```bash
git add playground/
git commit -m "Update playground"
git push origin main
# GitHub Pages will auto-deploy
```

### Netlify/Vercel
```bash
git push origin main
# Automatic deployment via webhook
```

### Manual Server
```bash
cd playground
rsync -avz --delete . user@server:/var/www/wsl-playground/
```

## Troubleshooting

### Monaco Editor Not Loading

**Problem:** Editor appears blank or shows error.

**Solutions:**
1. Check CDN availability: `https://cdnjs.cloudflare.com/ajax/libs/monaco-editor/0.45.0/`
2. Try alternative CDN: `https://cdn.jsdelivr.net/npm/monaco-editor@0.45.0/`
3. Self-host Monaco Editor (download and include locally)

### CORS Issues with iframe

**Problem:** iframe communication doesn't work.

**Solutions:**
1. Ensure both parent and iframe use same protocol (http/https)
2. Check postMessage origin restrictions
3. Set proper CORS headers on server

### 404 Errors on Refresh

**Problem:** Refreshing non-root URLs returns 404.

**Solution:** Configure server to serve `index.html` for all routes (see server configs above)

## Support

For issues or questions about deployment:
- Open an issue on [GitHub](https://github.com/kuetix/ide-plugins/issues)
- Check the [README](README.md) for general documentation

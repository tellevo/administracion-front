const express = require('express');
const path = require('path');
const fs = require('fs').promises;

const app = express();
const PORT = process.env.PORT || 3000;

function isAsset(pathname) {
  return pathname.includes('.');
}

function cacheHeadersFor(res, pathname) {
  if (pathname.startsWith('/assets/')) {
    res.set('Cache-Control', 'public, max-age=31536000, immutable');
  } else if (pathname === '/' || pathname.endsWith('.html')) {
    res.set('Cache-Control', 'no-cache');
  }
}

app.use(async (req, res) => {
  const pathname = decodeURIComponent(req.path);

  try {
    // Try to serve static asset from dist/
    const staticPath = path.join(__dirname, 'dist', pathname === '/' ? 'index.html' : pathname);
    await fs.access(staticPath);
    cacheHeadersFor(res, pathname);
    return res.sendFile(staticPath);
  } catch (error) {
    // SPA fallback: if no explicit asset requested, return index.html
    if (!isAsset(pathname)) {
      try {
        const indexPath = path.join(__dirname, 'dist', 'index.html');
        await fs.access(indexPath);
        cacheHeadersFor(res, '/index.html');
        return res.sendFile(indexPath);
      } catch (indexError) {
        // Continue to 404
      }
    }

    return res.status(404).send('Not found');
  }
});

// Bind only to localhost; external traffic should come via Nginx reverse proxy
app.listen(PORT, '127.0.0.1', () => {
  console.log(`Server running on http://127.0.0.1:${PORT}`);
});

module.exports = app;

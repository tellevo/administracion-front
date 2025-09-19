import { serve } from 'bun';

function isAsset(pathname) {
  return pathname.includes('.');
}

function cacheHeadersFor(pathname) {
  const headers = new Headers();
  if (pathname.startsWith('/assets/')) {
    headers.set('Cache-Control', 'public, max-age=31536000, immutable');
  } else if (pathname === '/' || pathname.endsWith('.html')) {
    headers.set('Cache-Control', 'no-cache');
  }
  return headers;
}

serve({
  // Bind only to localhost; external traffic should come via Nginx reverse proxy
  hostname: '127.0.0.1',
  port: Number(process.env.PORT) || 3000,
  async fetch(req) {
    const url = new URL(req.url);
    const pathname = decodeURIComponent(url.pathname);

    // Try to serve static asset from dist/
    const staticPath = 'dist' + (pathname === '/' ? '/index.html' : pathname);
    const file = Bun.file(staticPath);
    if (await file.exists()) {
      return new Response(file, { headers: cacheHeadersFor(pathname) });
    }

    // SPA fallback: if no explicit asset requested, return index.html
    if (!isAsset(pathname)) {
      const index = Bun.file('dist/index.html');
      if (await index.exists()) {
        return new Response(index, { headers: cacheHeadersFor('/index.html') });
      }
    }

    return new Response('Not found', { status: 404 });
  },
});

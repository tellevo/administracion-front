import { serve } from 'bun';

serve({
  port: 3000, // Or your preferred port
  async fetch(req) {
    const url = new URL(req.url);
    let filePath = 'dist' + url.pathname;
    if (url.pathname === '/') filePath = 'dist/index.html';

    try {
      const file = Bun.file(filePath);
      return new Response(file);
    } catch (e) {
      return new Response('Not found', { status: 404 });
    }
  },
});

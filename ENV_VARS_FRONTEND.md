# Frontend Environment Variables

For the Vue.js frontend, you need to set the following environment variables at build time.

| Variable | Description | Production Value |
|----------|-------------|------------------|
| `VITE_API_URL` | URL of the backend API | `https://admin.tellevoapp.com/api` |

## How to set for Production
Create or edit `.env.production` in the root of the frontend project:

```properties
VITE_API_URL=https://admin.tellevoapp.com/api
```

Then run the build script:
```bash
npm run build
```

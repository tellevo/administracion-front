# Backend Environment Variables

The following environment variables should be set in your production environment (e.g., systemd service file, Docker container, or cloud provider settings).

| Variable | Description | Default (Dev) |
|----------|-------------|---------------|
| `DB_USERNAME` | PostgreSQL database username | `postgres` |
| `DB_PASSWORD` | PostgreSQL database password | `tellevo$` |
| `JDBC_URL` | JDBC Connection URL | `jdbc:postgresql://192.168.100.14:17432/tellevoappdb` |
| `JWT_SECRET` | Secret key for signing JWTs | `mySecretKey...` |
| `CORS_ALLOWED_ORIGINS` | Comma-separated list of allowed origins | (See application.properties) |
| `ADMIN_USER` | Default admin username | `admin@tellevoapp.cl` |
| `ADMIN_PASSWORD` | Default admin password | `admin123` |
| `SPRING_PROFILE` | Active Spring profile | `dev` |

## Example .env file (DO NOT COMMIT TO GIT)
```properties
DB_USERNAME=myuser
DB_PASSWORD=mypassword
JWT_SECRET=very_long_random_secret_key_at_least_32_chars
CORS_ALLOWED_ORIGINS=https://admin.tellevoapp.com
SPRING_PROFILE=prod
```

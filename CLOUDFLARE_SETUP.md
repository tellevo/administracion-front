# Cloudflare Security Configuration

To secure `admin.tellevoapp.com`, apply the following settings in your Cloudflare Dashboard.

## SSL/TLS
1.  **SSL/TLS Encryption Mode:** Set to **Full (Strict)** if your backend has a valid SSL certificate. If using a self-signed cert on the backend (not recommended for prod), use **Full**.
2.  **Edge Certificates:**
    *   **Always Use HTTPS:** **On**
    *   **Minimum TLS Version:** **1.2**
    *   **Automatic HTTPS Rewrites:** **On**

## Security / WAF
1.  **Bot Fight Mode:** **On** (under Security > Bots)
2.  **WAF Rules:**
    *   Create a rule to block access to sensitive paths if not needed publicly (e.g., `/actuator/*` if you had it enabled, though it seems disabled).
    *   **Rate Limiting:** Consider enabling if you see brute force attacks on `/api/login`.

## Network
1.  **WebSockets:** **On** (Required for your WebSocket functionality).
2.  **Onion Routing:** Off (usually not needed for corporate admin tools).

## Access (Optional but Recommended)
If this is an internal admin tool, consider using **Cloudflare Access** (Zero Trust) to put the entire `admin.tellevoapp.com` domain behind a login screen (Google Auth, GitHub, etc.) *before* they even hit your app.

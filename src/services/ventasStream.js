/**
 * Ventas Stream Service - WebSocket client for real-time ventas data
 *
 * Handles WebSocket connection to the backend proxy, manages reconnections,
 * and provides event-based API for real-time data updates.
 */

class VentasStreamService {
  constructor() {
    this.ws = null
    this.isConnected = false
    this.isConnecting = false
    this.reconnectAttempts = 0
    this.maxReconnectAttempts = 5
    this.reconnectDelay = 1000 // Start with 1 second

    // Event listeners
    this.listeners = {
      venta: [],
      status: [],
      error: []
    }

    // Configuration
    this.wsUrl = this.getWebSocketUrl()
    this.connectTimeout = 10000 // 10 seconds
    this.heartbeatInterval = 30000 // 30 seconds
    this.heartbeatTimer = null
  }

  /**
   * Generate WebSocket URL based on current location
   */
  getWebSocketUrl() {
    // DEBUG: Log environment variables for troubleshooting
    console.log('[WebSocket Config] Environment:', {
      PROD: import.meta.env.PROD,
      DEV: import.meta.env.DEV,
      VITE_BACKEND_HOST: import.meta.env.VITE_BACKEND_HOST,
      VITE_BACKEND_PORT: import.meta.env.VITE_BACKEND_PORT,
      VITE_ENV: import.meta.env.VITE_ENV,
      currentHostname: window.location.hostname,
      currentHost: window.location.host
    })

    // Determine backend host/port based on environment detection
    let backendHost, backendPort, useCustomBackend

     // If explicitly set in environment, use it
     if (import.meta.env.VITE_BACKEND_HOST) {
       backendHost = import.meta.env.VITE_BACKEND_HOST
       backendPort = import.meta.env.VITE_BACKEND_PORT || 8080
       useCustomBackend = true

       // For production domains, use nginx proxy instead of custom backend
       if (backendHost === 'admin.tellevoapp.com' || backendHost.includes('tellevoapp.com')) {
         useCustomBackend = false
       }
     }
    // Otherwise, detect based on current hostname and assume backend is on same server
    else {
      const currentHost = window.location.hostname

      console.log('[WebSocket Host Detection] Current host:', currentHost, 'Protocol:', window.location.protocol)

      // For production domains - handle various domain patterns
      if (currentHost === 'admin.tellevoapp.com' ||
          currentHost.includes('admin.tellevoapp.com') ||
          currentHost.endsWith('.tellevoapp.com') ||
          window.location.protocol === 'https:') {
        console.log('[WebSocket Host Detection] Detected HTTPS production domain:', currentHost)
        console.log('[WebSocket Host Detection] Will use WSS protocol')

        // Use nginx public HTTPS endpoint (port 443)
        backendHost = currentHost // Use current host to support subdomains
        backendPort = 443 // NGINX HTTPS port
        useCustomBackend = false // Let nginxdirect the connection
        console.log('[WebSocket Host Detection] Using nginx endpoint:', backendHost + ':' + backendPort)
      }
      // For localhost development, use localhost
      else if (currentHost === 'localhost' || currentHost === '127.0.0.1' || currentHost.startsWith('192.168.')) {
        backendHost = 'localhost'
        backendPort = import.meta.env.VITE_BACKEND_PORT || 8080
        useCustomBackend = true
        console.log('[WebSocket Host Detection] Using localhost for development')
      }
      // Handle case where hostname detection fails (empty string)
      else if (!currentHost || currentHost.trim() === '') {
        console.warn('[WebSocket Host Detection] Hostname detection failed, using production fallback')
        // Fallback to production server when hostname is not available
        backendHost = 'admin.tellevoapp.com'
        backendPort = 8080
        useCustomBackend = true
      }
      // For other domains, use production server (safety fallback)
      else {
        console.log('[WebSocket Host Detection] Unknown domain, using production fallback:', currentHost)
        backendHost = 'admin.tellevoapp.com'
        backendPort = 8080
        useCustomBackend = true
      }
    }

    // For localhost backends, always use WS:// protocol (backend likely runs on HTTP)
    // For production domains, check if backend supports secure connections or if reverse proxy handles SSL
    const useSecure = backendHost !== 'localhost' && backendHost !== '127.0.0.1' && window.location.protocol === 'https:' && this.shouldUseSecureWebSocket(backendHost)
    const protocol = useSecure ? 'wss:' : 'ws:'

    const resultUrl = useCustomBackend ? `${protocol}//${backendHost}:${backendPort}/ws/ventas` : `${protocol}//${window.location.host}/ws/ventas`

    console.log('[WebSocket Config] Decided:', {
      backendHost,
      backendPort,
      useCustomBackend,
      useSecure,
      resultUrl
    })

    // In development, use direct backend connection
    if (import.meta.env.DEV) {
      const url = `${protocol}//${backendHost}:${backendPort}/ws/ventas`
      console.log('[WebSocket Config] FINAL URL (dev):', url)
      return url
    }
    // In production HTTPS, use nginx proxy endpoint
    else if (window.location.protocol === 'https:') {
      // nginx handles SSL, so connect to nginx port without specifying port (443 default)
      const url = `${protocol}//${window.location.hostname}/ws/ventas`
      console.log('[WebSocket Config] FINAL URL (nginx proxy - WSS):', url)
      return url
    }
    // For other production cases, use configured backend
    else {
      const url = `${protocol}//${backendHost}:${backendPort}/ws/ventas`
      console.log('[WebSocket Config] FINAL URL (production):', url)
      return url
    }
  }

  /**
   * Initialize WebSocket connection
   */
  connect() {
    console.log('[WebSocket Service] connect() called, current URL:', this.wsUrl)
    console.log('[WebSocket Service] current state:', this.getConnectionInfo())

    if (this.isConnected || this.isConnecting) {
      console.warn('[WebSocket Service] Already connected or connecting, skipping')
      return
    }

    this.isConnecting = true
    this.updateStatus('connecting')

    try {
      console.log('[WebSocket Service] Creating new WebSocket connection to:', this.wsUrl)
      this.ws = new WebSocket(this.wsUrl)

    // Set connection timeout
    const timeoutId = setTimeout(() => {
      if (this.isConnecting) {
        console.error(`[WebSocket Service] Connection timeout after ${this.connectTimeout}ms`)
        console.error(`[WebSocket Service] URL: ${this.wsUrl}`)
        console.error(`[WebSocket Service] ReadyState: ${this.ws ? this.ws.readyState : 'undefined'}`)
        this.handleConnectionError(new Error('Connection timeout'))
      }
    }, this.connectTimeout)

    this.ws.onopen = (event) => {
      clearTimeout(timeoutId)
      console.log('[WebSocket Service] Connection opened successfully')
      this.handleConnectionOpen(event)
    }

    this.ws.onmessage = (event) => {
      console.log('[WebSocket Service] Message received:', event.data)
      this.handleMessage(event)
    }

    this.ws.onclose = (event) => {
      clearTimeout(timeoutId)
      console.log(`[WebSocket Service] Connection closed, code: ${event.code}, reason: ${event.reason}`)
      this.handleConnectionClose(event)
    }

    this.ws.onerror = (event) => {
      console.error('[WebSocket Service] Connection error occurred')
      clearTimeout(timeoutId)
      this.handleConnectionError(event)
    }

    } catch (error) {
      this.handleConnectionError(error)
    }
  }

  /**
   * Handle successful WebSocket connection
   */
  handleConnectionOpen(event) {
    console.log('WebSocket connected:', this.wsUrl)
    this.isConnected = true
    this.isConnecting = false
    this.reconnectAttempts = 0
    this.reconnectDelay = 1000 // Reset reconnect delay

    this.updateStatus('connected')
    this.startHeartbeat()

    // Notify listeners
    this.notifyListeners('status', 'connected')
  }

  /**
   * Handle incoming WebSocket messages
   */
  handleMessage(event) {
    try {
      const data = JSON.parse(event.data)

      // Validate data structure
      if (this.isValidVentaData(data)) {
        this.notifyListeners('venta', data)
      } else {
        console.warn('Invalid venta data received:', data)
      }
    } catch (error) {
      console.error('Error parsing WebSocket message:', error, event.data)
    }
  }

  /**
   * Handle WebSocket connection close
   */
  handleConnectionClose(event) {
    console.log('WebSocket closed:', event.code, event.reason)
    this.cleanup()

    // Try to reconnect unless it was a clean close
    if (event.code !== 1000 && this.reconnectAttempts < this.maxReconnectAttempts) {
      this.attemptReconnect()
    } else {
      this.updateStatus('disconnected')
      this.notifyListeners('status', 'disconnected')
    }
  }

  /**
   * Handle WebSocket errors
   */
  handleConnectionError(error) {
    console.error('WebSocket error:', error)
    this.cleanup()

    this.notifyListeners('error', error)

    if (this.reconnectAttempts < this.maxReconnectAttempts) {
      this.attemptReconnect()
    } else {
      this.updateStatus('disconnected')
    }
  }

  /**
   * Attempt to reconnect with exponential backoff
   */
  attemptReconnect() {
    if (this.reconnectAttempts >= this.maxReconnectAttempts) {
      console.error('Max reconnect attempts reached')
      return
    }

    this.reconnectAttempts++
    const delay = this.reconnectDelay * Math.pow(2, this.reconnectAttempts - 1) // Exponential backoff

    console.log(`Attempting reconnect ${this.reconnectAttempts}/${this.maxReconnectAttempts} in ${(delay / 1000).toFixed(1)}s`)

    setTimeout(() => {
      if (!this.isConnected) {
        this.connect()
      }
    }, delay)
  }

  /**
   * Force reconnection
   */
  reconnect() {
    console.log('Manual reconnection requested')
    this.reconnectAttempts = 0 // Reset counter for manual reconnect
    this.disconnect()
    setTimeout(() => this.connect(), 500)
  }

  /**
   * Disconnect WebSocket
   */
  disconnect() {
    this.cleanup()

    if (this.ws) {
      this.ws.close(1000, 'Client disconnect')
    }
  }

  /**
   * Clean up connection resources
   */
  cleanup() {
    this.isConnected = false
    this.isConnecting = false

    if (this.heartbeatTimer) {
      clearInterval(this.heartbeatTimer)
      this.heartbeatTimer = null
    }

    if (this.ws) {
      // Remove event handlers to prevent memory leaks
      this.ws.onopen = null
      this.ws.onmessage = null
      this.ws.onclose = null
      this.ws.onerror = null
      this.ws = null
    }
  }

  /**
   * Start heartbeat to maintain connection
   */
  startHeartbeat() {
    this.heartbeatTimer = setInterval(() => {
      if (this.isConnected && this.ws && this.ws.readyState === WebSocket.OPEN) {
        // Send a ping frame or simple message to keep connection alive
        try {
          this.ws.send(JSON.stringify({ type: 'ping', timestamp: Date.now() }))
        } catch (error) {
          console.error('Heartbeat failed:', error)
          this.handleConnectionError(error)
        }
      }
    }, this.heartbeatInterval)
  }

  /**
   * Update connection status
   */
  updateStatus(status) {
    // Could emit global status updates here if needed
    console.log('Connection status:', status)
  }

  /**
   * Event listener management
   */
  onStatusChange(callback) {
    this.listeners.status.push(callback)
  }

  onCandidato(callback) {
    this.listeners.venta.push(callback)
  }

  onError(callback) {
    this.listeners.error.push(callback)
  }

  /**
   * Notify all listeners of an event
   */
  notifyListeners(eventType, data) {
    this.listeners[eventType].forEach(callback => {
      try {
        callback(data)
      } catch (error) {
        console.error(`Error in ${eventType} listener:`, error)
      }
    })
  }

  /**
   * Validate venta data structure
   */
  isValidVentaData(data) {
    return data &&
           typeof data.id === 'number' &&
           typeof data.email === 'string' &&
           typeof data.nombre_empresa === 'string' &&
           data.fecha_envio &&
           !isNaN(new Date(data.fecha_envio).getTime())
  }

  /**
   * Determine if secure WebSocket (WSS) should be used
   * Some production setups use reverse proxies that handle SSL termination
   */
  shouldUseSecureWebSocket(backendHost) {
    // Environment override - if set to false, always use insecure WebSocket
    if (import.meta.env.VITE_WEBSOCKET_SECURE === 'false') {
      return false
    }

    // Host-specific overrides for production deployments
    const nonSecureHosts = [
      // HTTPS hosts removed - now use secure WebSocket by default for HTTPS
      // 'admin.tellevoapp.com', // Now uses WSS for HTTPS production
    ]

    // For specified hosts, force insecure WebSocket
    if (nonSecureHosts.includes(backendHost)) {
      console.log('[WebSocket Config] Forcing insecure WebSocket for host:', backendHost)
      return false
    }

    // Default: use secure for non-localhost HTTPS connections
    return true
  }

  /**
   * Force protocol override (useful for debugging connection issues)
   */
  setForcedProtocol(protocol) {
    console.log('[WebSocket Config] Forcing protocol override:', protocol)
    // Store preference in localStorage for persistence
    localStorage.setItem('websocketForcedProtocol', protocol)

    // Update URL if forcing non-secure
    if (protocol === 'ws') {
      this.wsUrl = this.wsUrl.replace(/^wss:/, 'ws:')
    } else {
      this.wsUrl = this.wsUrl.replace(/^ws:/, 'wss:')
    }

    console.log('[WebSocket Config] Updated URL:', this.wsUrl)
  }

  /**
   * Get connection info for debugging
   */
  getConnectionInfo() {
    return {
      url: this.wsUrl,
      connected: this.isConnected,
      connecting: this.isConnecting,
      reconnectAttempts: this.reconnectAttempts,
      readyState: this.ws ? this.ws.readyState : -1,
      protocol: this.wsUrl.startsWith('wss:') ? 'wss' : 'ws',
      forcedProtocol: localStorage.getItem('websocketForcedProtocol')
    }
  }
}

// Singleton instance
export const ventasStreamService = new VentasStreamService()

// Named export for consistency
export { VentasStreamService }

// Default export
export default ventasStreamService

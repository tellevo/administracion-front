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
    const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:'
    const host = window.location.host // Includes port if non-standard

    // In development, connect to backend port 8080
    // In production, usually same host/port
    if (import.meta.env.DEV) {
      return `${protocol}//${window.location.hostname}:8080/ws/ventas`
    } else {
      return `${protocol}//${host}/ws/ventas`
    }
  }

  /**
   * Initialize WebSocket connection
   */
  connect() {
    if (this.isConnected || this.isConnecting) {
      console.warn('WebSocket already connected or connecting')
      return
    }

    this.isConnecting = true
    this.updateStatus('connecting')

    try {
      this.ws = new WebSocket(this.wsUrl)

      // Set connection timeout
      const timeoutId = setTimeout(() => {
        if (this.isConnecting) {
          console.error('WebSocket connection timeout')
          this.handleConnectionError(new Error('Connection timeout'))
        }
      }, this.connectTimeout)

      this.ws.onopen = (event) => {
        clearTimeout(timeoutId)
        this.handleConnectionOpen(event)
      }

      this.ws.onmessage = (event) => {
        this.handleMessage(event)
      }

      this.ws.onclose = (event) => {
        clearTimeout(timeoutId)
        this.handleConnectionClose(event)
      }

      this.ws.onerror = (event) => {
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

  onVenta(callback) {
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
   * Get connection info for debugging
   */
  getConnectionInfo() {
    return {
      url: this.wsUrl,
      connected: this.isConnected,
      connecting: this.isConnecting,
      reconnectAttempts: this.reconnectAttempts,
      readyState: this.ws ? this.ws.readyState : -1
    }
  }
}

// Singleton instance
export const ventasStreamService = new VentasStreamService()

// Named export for consistency
export { VentasStreamService }

// Default export
export default ventasStreamService

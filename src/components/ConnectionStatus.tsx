"use client"

import { useState, useEffect } from "react"
import { Card, CardContent } from "@/components/ui/card"
import { CheckCircle, Wifi, WifiOff } from "lucide-react"
import { checkBackendHealth } from "../services/api"

export function ConnectionStatus() {
  const [isConnected, setIsConnected] = useState<boolean | null>(null)
  const [isChecking, setIsChecking] = useState(false)
  const [lastChecked, setLastChecked] = useState<Date | null>(null)

  const checkConnection = async () => {
    setIsChecking(true)
    try {
      const healthy = await checkBackendHealth()
      setIsConnected(healthy)
      setLastChecked(new Date())
    } catch (error) {
      console.error("Connection check error:", error)
      setIsConnected(false)
      setLastChecked(new Date())
    } finally {
      setIsChecking(false)
    }
  }

  useEffect(() => {
    checkConnection()
    // Check connection every 30 seconds
    const interval = setInterval(checkConnection, 30000)
    return () => clearInterval(interval)
  }, [])

  if (isConnected === null && isChecking) {
    return (
      <Card className="mb-4">
        <CardContent className="flex items-center space-x-2 p-4">
          <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-blue-600"></div>
          <span className="text-sm text-gray-600">Checking backend connection...</span>
        </CardContent>
      </Card>
    )
  }

  if (!isConnected) {
    return (
      <Card className="mb-4 border-red-200 bg-red-50">
        <CardContent className="flex items-center justify-between p-4">
          <div className="flex items-center space-x-2">
            <WifiOff className="h-5 w-5 text-red-500" />
            <div>
              <p className="text-sm font-medium text-red-800">Backend Server Disconnected</p>
              <p className="text-xs text-red-600">Please ensure the Spring Boot application is running on port 8080</p>
              {lastChecked && (
                <p className="text-xs text-red-500 mt-1">Last checked: {lastChecked.toLocaleTimeString()}</p>
              )}
            </div>
          </div>
          <button
            onClick={checkConnection}
            disabled={isChecking}
            className="px-3 py-1 text-xs bg-red-100 text-red-700 rounded hover:bg-red-200 disabled:opacity-50"
          >
            {isChecking ? "Checking..." : "Retry"}
          </button>
        </CardContent>
      </Card>
    )
  }

  return (
    <Card className="mb-4 border-green-200 bg-green-50">
      <CardContent className="flex items-center justify-between p-4">
        <div className="flex items-center space-x-2">
          <Wifi className="h-4 w-4 text-green-500" />
          <span className="text-sm text-green-800">Backend server connected</span>
          <CheckCircle className="h-4 w-4 text-green-500" />
        </div>
        {lastChecked && (
          <span className="text-xs text-green-600">Last checked: {lastChecked.toLocaleTimeString()}</span>
        )}
      </CardContent>
    </Card>
  )
}

"use client"

import { useState } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Database, Play } from "lucide-react"

interface MockDataProviderProps {
  onUseMockData: () => void
}

export function MockDataProvider({ onUseMockData }: MockDataProviderProps) {
  const [isLoading, setIsLoading] = useState(false)

  const handleUseMockData = async () => {
    setIsLoading(true)
    // Simulate loading time
    await new Promise((resolve) => setTimeout(resolve, 1000))
    onUseMockData()
    setIsLoading(false)
  }

  return (
    <Card className="mb-4 border-blue-200 bg-blue-50">
      <CardHeader>
        <CardTitle className="flex items-center space-x-2 text-blue-800">
          <Database className="w-5 h-5" />
          <span>Demo Mode Available</span>
        </CardTitle>
      </CardHeader>
      <CardContent className="space-y-3">
        <p className="text-sm text-blue-700">
          Can't connect to the backend? Try the demo mode with sample data to explore the interface.
        </p>
        <Button
          onClick={handleUseMockData}
          disabled={isLoading}
          className="flex items-center space-x-2"
          variant="outline"
        >
          <Play className="w-4 h-4" />
          <span>{isLoading ? "Loading Demo..." : "Use Demo Data"}</span>
        </Button>
      </CardContent>
    </Card>
  )
}

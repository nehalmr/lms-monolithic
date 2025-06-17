"use client"

import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Badge } from "@/components/ui/badge"
import { Terminal, Database, Globe, CheckCircle } from "lucide-react"

export function StartupGuide() {
  return (
    <Card className="mb-6">
      <CardHeader>
        <CardTitle className="flex items-center space-x-2">
          <Terminal className="w-5 h-5" />
          <span>Getting Started</span>
        </CardTitle>
      </CardHeader>
      <CardContent className="space-y-4">
        <p className="text-sm text-gray-600">
          To use the Library Management System, please ensure both frontend and backend are running:
        </p>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div className="space-y-2">
            <div className="flex items-center space-x-2">
              <Database className="w-4 h-4 text-blue-600" />
              <span className="font-medium">Backend (Spring Boot)</span>
              <Badge variant="outline">Port 8080</Badge>
            </div>
            <div className="text-sm text-gray-600 ml-6">
              <code className="bg-gray-100 px-2 py-1 rounded text-xs">mvn spring-boot:run</code>
            </div>
          </div>

          <div className="space-y-2">
            <div className="flex items-center space-x-2">
              <Globe className="w-4 h-4 text-green-600" />
              <span className="font-medium">Frontend (React)</span>
              <Badge variant="outline">Port 3000</Badge>
            </div>
            <div className="text-sm text-gray-600 ml-6">
              <code className="bg-gray-100 px-2 py-1 rounded text-xs">npm start</code>
            </div>
          </div>
        </div>

        <div className="flex items-center space-x-2 text-sm text-green-600">
          <CheckCircle className="w-4 h-4" />
          <span>Once both services are running, the connection status will show as connected above.</span>
        </div>
      </CardContent>
    </Card>
  )
}

"use client"

import { useState } from "react"
import { BookManagement } from "./components/BookManagement"
import { MemberManagement } from "./components/MemberManagement"
import { BorrowingManagement } from "./components/BorrowingManagement"
import { Dashboard } from "./components/Dashboard"
import { Button } from "@/components/ui/button"
import { Book, Users, ArrowLeftRight, BarChart3 } from "lucide-react"
import { ConnectionStatus } from "./components/ConnectionStatus"

type ActiveTab = "dashboard" | "books" | "members" | "borrowing"

function App() {
  const [activeTab, setActiveTab] = useState<ActiveTab>("dashboard")

  const renderContent = () => {
    switch (activeTab) {
      case "dashboard":
        return <Dashboard />
      case "books":
        return <BookManagement />
      case "members":
        return <MemberManagement />
      case "borrowing":
        return <BorrowingManagement />
      default:
        return <Dashboard />
    }
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <header className="bg-white shadow-sm border-b">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center py-6">
            <div className="flex items-center">
              <Book className="w-8 h-8 text-blue-600 mr-3" />
              <h1 className="text-2xl font-bold text-gray-900">Library Management System</h1>
            </div>
          </div>
        </div>
      </header>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <ConnectionStatus />
        <div className="flex space-x-1 mb-8">
          <Button
            variant={activeTab === "dashboard" ? "default" : "outline"}
            onClick={() => setActiveTab("dashboard")}
            className="flex items-center space-x-2"
          >
            <BarChart3 className="w-4 h-4" />
            <span>Dashboard</span>
          </Button>
          <Button
            variant={activeTab === "books" ? "default" : "outline"}
            onClick={() => setActiveTab("books")}
            className="flex items-center space-x-2"
          >
            <Book className="w-4 h-4" />
            <span>Books</span>
          </Button>
          <Button
            variant={activeTab === "members" ? "default" : "outline"}
            onClick={() => setActiveTab("members")}
            className="flex items-center space-x-2"
          >
            <Users className="w-4 h-4" />
            <span>Members</span>
          </Button>
          <Button
            variant={activeTab === "borrowing" ? "default" : "outline"}
            onClick={() => setActiveTab("borrowing")}
            className="flex items-center space-x-2"
          >
            <ArrowLeftRight className="w-4 h-4" />
            <span>Borrowing</span>
          </Button>
        </div>

        <main>{renderContent()}</main>
      </div>
    </div>
  )
}

export default App

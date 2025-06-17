"use client"

import { useState, useEffect } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Book, Users, ArrowLeftRight, AlertTriangle } from "lucide-react"
import { api, checkBackendHealth } from "../services/api"
import { StartupGuide } from "./StartupGuide"
import { MockDataProvider } from "./MockDataProvider"

interface DashboardStats {
  totalBooks: number
  availableBooks: number
  totalMembers: number
  activeTransactions: number
  overdueBooks: number
}

const mockStats: DashboardStats = {
  totalBooks: 10,
  availableBooks: 7,
  totalMembers: 8,
  activeTransactions: 5,
  overdueBooks: 1,
}

export function Dashboard() {
  const [stats, setStats] = useState<DashboardStats>({
    totalBooks: 0,
    availableBooks: 0,
    totalMembers: 0,
    activeTransactions: 0,
    overdueBooks: 0,
  })
  const [loading, setLoading] = useState(true)
  const [showStartupGuide, setShowStartupGuide] = useState(false)
  const [usingMockData, setUsingMockData] = useState(false)

  const fetchStats = async () => {
    try {
      setLoading(true)

      // Check if backend is available first
      const isBackendHealthy = await checkBackendHealth()
      if (!isBackendHealthy) {
        throw new Error("Backend server is not available")
      }

      const [books, members, transactions, overdueTransactions] = await Promise.allSettled([
        api.get("/books"),
        api.get("/members"),
        api.get("/borrowing"),
        api.get("/borrowing/overdue"),
      ])

      // Handle successful responses
      const booksData = books.status === "fulfilled" ? books.value.data : []
      const membersData = members.status === "fulfilled" ? members.value.data : []
      const transactionsData = transactions.status === "fulfilled" ? transactions.value.data : []
      const overdueData = overdueTransactions.status === "fulfilled" ? overdueTransactions.value.data : []

      const availableBooks = booksData.filter((book: any) => book.availableCopies > 0).length
      const activeTransactions = transactionsData.filter((t: any) => t.status === "BORROWED").length

      setStats({
        totalBooks: booksData.length,
        availableBooks,
        totalMembers: membersData.length,
        activeTransactions,
        overdueBooks: overdueData.length,
      })
      setShowStartupGuide(false)
      setUsingMockData(false)
    } catch (error) {
      console.error("Error fetching dashboard stats:", error)
      setShowStartupGuide(true)
      // Set default stats on error
      setStats({
        totalBooks: 0,
        availableBooks: 0,
        totalMembers: 0,
        activeTransactions: 0,
        overdueBooks: 0,
      })
    } finally {
      setLoading(false)
    }
  }

  const handleUseMockData = () => {
    setStats(mockStats)
    setUsingMockData(true)
    setShowStartupGuide(false)
  }

  useEffect(() => {
    fetchStats()
  }, [])

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-blue-600"></div>
      </div>
    )
  }

  return (
    <div className="space-y-6">
      <div>
        <h2 className="text-3xl font-bold text-gray-900">Dashboard</h2>
        <p className="text-gray-600 mt-2">
          Overview of your library management system
          {usingMockData && <span className="text-blue-600 font-medium"> (Demo Mode)</span>}
        </p>
        {showStartupGuide && (
          <>
            <StartupGuide />
            <MockDataProvider onUseMockData={handleUseMockData} />
          </>
        )}
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Total Books</CardTitle>
            <Book className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{stats.totalBooks}</div>
            <p className="text-xs text-muted-foreground">{stats.availableBooks} available</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Total Members</CardTitle>
            <Users className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{stats.totalMembers}</div>
            <p className="text-xs text-muted-foreground">Registered members</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Active Borrowings</CardTitle>
            <ArrowLeftRight className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{stats.activeTransactions}</div>
            <p className="text-xs text-muted-foreground">Currently borrowed</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Overdue Books</CardTitle>
            <AlertTriangle className="h-4 w-4 text-red-500" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-red-600">{stats.overdueBooks}</div>
            <p className="text-xs text-muted-foreground">Need attention</p>
          </CardContent>
        </Card>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <Card>
          <CardHeader>
            <CardTitle>Quick Actions</CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="text-sm text-gray-600">• Add new books to the collection</div>
            <div className="text-sm text-gray-600">• Register new members</div>
            <div className="text-sm text-gray-600">• Process book borrowing and returns</div>
            <div className="text-sm text-gray-600">• Manage overdue books and fines</div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>System Status</CardTitle>
          </CardHeader>
          <CardContent className="space-y-4">
            <div className="flex items-center justify-between">
              <span className="text-sm text-gray-600">Database Connection</span>
              <span className={`text-sm font-medium ${usingMockData ? "text-blue-600" : "text-green-600"}`}>
                {usingMockData ? "Demo Mode" : "Active"}
              </span>
            </div>
            <div className="flex items-center justify-between">
              <span className="text-sm text-gray-600">API Service</span>
              <span className={`text-sm font-medium ${usingMockData ? "text-blue-600" : "text-green-600"}`}>
                {usingMockData ? "Demo Mode" : "Running"}
              </span>
            </div>
            <div className="flex items-center justify-between">
              <span className="text-sm text-gray-600">Notification Service</span>
              <span className={`text-sm font-medium ${usingMockData ? "text-blue-600" : "text-green-600"}`}>
                {usingMockData ? "Demo Mode" : "Enabled"}
              </span>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}

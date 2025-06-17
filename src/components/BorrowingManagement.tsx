"use client"

import type React from "react"
import { useState, useEffect } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Label } from "@/components/ui/label"
import { Badge } from "@/components/ui/badge"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { Plus, ArrowLeftRight, Calendar, AlertTriangle, AlertCircle } from "lucide-react"
import { api } from "../services/api"

interface BorrowingTransaction {
  transactionId?: number
  book: {
    bookId: number
    title: string
    author: string
  }
  member: {
    memberId: number
    name: string
    email: string
  }
  borrowDate: string
  dueDate: string
  returnDate?: string
  status: "BORROWED" | "RETURNED" | "OVERDUE"
}

interface BookOption {
  bookId: number
  title: string
  author: string
  availableCopies: number
}

interface MemberOption {
  memberId: number
  name: string
  email: string
}

export function BorrowingManagement() {
  const [transactions, setTransactions] = useState<BorrowingTransaction[]>([])
  const [books, setBooks] = useState<BookOption[]>([])
  const [members, setMembers] = useState<MemberOption[]>([])
  const [showBorrowForm, setShowBorrowForm] = useState(false)
  const [loading, setLoading] = useState(true)
  const [selectedBookId, setSelectedBookId] = useState<string>("")
  const [selectedMemberId, setSelectedMemberId] = useState<string>("")
  // Add error state
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    fetchData()
  }, [])

  const fetchData = async () => {
    try {
      setError(null)
      const [transactionsRes, booksRes, membersRes] = await Promise.allSettled([
        api.get("/borrowing"),
        api.get("/books/available"),
        api.get("/members/active"),
      ])

      // Handle results even if some fail
      setTransactions(transactionsRes.status === "fulfilled" ? transactionsRes.value.data : [])
      setBooks(booksRes.status === "fulfilled" ? booksRes.value.data : [])
      setMembers(membersRes.status === "fulfilled" ? membersRes.value.data : [])

      // Check if any requests failed
      const failedRequests = [transactionsRes, booksRes, membersRes].filter((res) => res.status === "rejected")
      if (failedRequests.length > 0) {
        setError("Some data could not be loaded. Please check your connection.")
      }
    } catch (error: any) {
      console.error("Error fetching data:", error)
      setError(error.message || "Failed to fetch data. Please check if the backend server is running.")
      setTransactions([])
      setBooks([])
      setMembers([])
    } finally {
      setLoading(false)
    }
  }

  const handleBorrowBook = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!selectedBookId || !selectedMemberId) return

    try {
      await api.post(`/borrowing/borrow?bookId=${selectedBookId}&memberId=${selectedMemberId}`)
      setSelectedBookId("")
      setSelectedMemberId("")
      setShowBorrowForm(false)
      fetchData()
    } catch (error) {
      console.error("Error borrowing book:", error)
      alert(
        "Error borrowing book. Please check if the member has reached the borrowing limit or if the book is available.",
      )
    }
  }

  const handleReturnBook = async (transactionId: number) => {
    try {
      await api.post(`/borrowing/return/${transactionId}`)
      fetchData()
    } catch (error) {
      console.error("Error returning book:", error)
    }
  }

  const getStatusBadgeVariant = (status: string) => {
    switch (status) {
      case "BORROWED":
        return "default"
      case "RETURNED":
        return "secondary"
      case "OVERDUE":
        return "destructive"
      default:
        return "outline"
    }
  }

  const isOverdue = (dueDate: string, status: string) => {
    return status === "BORROWED" && new Date(dueDate) < new Date()
  }

  const getDaysOverdue = (dueDate: string) => {
    const today = new Date()
    const due = new Date(dueDate)
    const diffTime = today.getTime() - due.getTime()
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
    return diffDays > 0 ? diffDays : 0
  }

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-blue-600"></div>
      </div>
    )
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h2 className="text-3xl font-bold text-gray-900">Borrowing Management</h2>
          <p className="text-gray-600 mt-2">Manage book borrowing and returns</p>
        </div>
        <Button onClick={() => setShowBorrowForm(true)} className="flex items-center space-x-2">
          <Plus className="w-4 h-4" />
          <span>Borrow Book</span>
        </Button>
      </div>

      {error && (
        <Card className="border-red-200 bg-red-50">
          <CardContent className="flex items-center space-x-2 p-4">
            <AlertCircle className="h-5 w-5 text-red-500" />
            <div>
              <p className="text-sm font-medium text-red-800">Connection Error</p>
              <p className="text-xs text-red-600">{error}</p>
            </div>
            <Button size="sm" variant="outline" onClick={fetchData}>
              Retry
            </Button>
          </CardContent>
        </Card>
      )}

      {showBorrowForm && (
        <Card>
          <CardHeader>
            <CardTitle>Borrow Book</CardTitle>
          </CardHeader>
          <CardContent>
            <form onSubmit={handleBorrowBook} className="space-y-4">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <Label htmlFor="book-select">Select Book</Label>
                  <Select value={selectedBookId} onValueChange={setSelectedBookId}>
                    <SelectTrigger>
                      <SelectValue placeholder="Choose a book" />
                    </SelectTrigger>
                    <SelectContent>
                      {books.map((book) => (
                        <SelectItem key={book.bookId} value={book.bookId.toString()}>
                          {book.title} by {book.author} ({book.availableCopies} available)
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>
                <div>
                  <Label htmlFor="member-select">Select Member</Label>
                  <Select value={selectedMemberId} onValueChange={setSelectedMemberId}>
                    <SelectTrigger>
                      <SelectValue placeholder="Choose a member" />
                    </SelectTrigger>
                    <SelectContent>
                      {members.map((member) => (
                        <SelectItem key={member.memberId} value={member.memberId.toString()}>
                          {member.name} ({member.email})
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>
              </div>
              <div className="flex space-x-2">
                <Button type="submit" disabled={!selectedBookId || !selectedMemberId}>
                  Borrow Book
                </Button>
                <Button type="button" variant="outline" onClick={() => setShowBorrowForm(false)}>
                  Cancel
                </Button>
              </div>
            </form>
          </CardContent>
        </Card>
      )}

      <div className="grid grid-cols-1 gap-6">
        {transactions.map((transaction) => (
          <Card key={transaction.transactionId} className="hover:shadow-lg transition-shadow">
            <CardHeader className="pb-3">
              <div className="flex items-start justify-between">
                <div className="flex items-center space-x-2">
                  <ArrowLeftRight className="w-5 h-5 text-blue-600" />
                  <div>
                    <CardTitle className="text-lg">{transaction.book.title}</CardTitle>
                    <p className="text-sm text-gray-600">by {transaction.book.author}</p>
                  </div>
                </div>
                <div className="flex items-center space-x-2">
                  {isOverdue(transaction.dueDate, transaction.status) && (
                    <AlertTriangle className="w-5 h-5 text-red-500" />
                  )}
                  <Badge variant={getStatusBadgeVariant(transaction.status)}>{transaction.status}</Badge>
                  {transaction.status === "BORROWED" && (
                    <Button
                      size="sm"
                      onClick={() => transaction.transactionId && handleReturnBook(transaction.transactionId)}
                    >
                      Return Book
                    </Button>
                  )}
                </div>
              </div>
            </CardHeader>
            <CardContent className="space-y-3">
              <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
                <div>
                  <p className="text-sm font-medium text-gray-700">Member</p>
                  <p className="text-sm text-gray-600">{transaction.member.name}</p>
                  <p className="text-xs text-gray-500">{transaction.member.email}</p>
                </div>
                <div>
                  <p className="text-sm font-medium text-gray-700">Borrow Date</p>
                  <p className="text-sm text-gray-600 flex items-center">
                    <Calendar className="w-4 h-4 mr-1" />
                    {new Date(transaction.borrowDate).toLocaleDateString()}
                  </p>
                </div>
                <div>
                  <p className="text-sm font-medium text-gray-700">Due Date</p>
                  <p
                    className={`text-sm flex items-center ${
                      isOverdue(transaction.dueDate, transaction.status) ? "text-red-600" : "text-gray-600"
                    }`}
                  >
                    <Calendar className="w-4 h-4 mr-1" />
                    {new Date(transaction.dueDate).toLocaleDateString()}
                    {isOverdue(transaction.dueDate, transaction.status) && (
                      <span className="ml-2 text-xs">({getDaysOverdue(transaction.dueDate)} days overdue)</span>
                    )}
                  </p>
                </div>
                {transaction.returnDate && (
                  <div>
                    <p className="text-sm font-medium text-gray-700">Return Date</p>
                    <p className="text-sm text-gray-600 flex items-center">
                      <Calendar className="w-4 h-4 mr-1" />
                      {new Date(transaction.returnDate).toLocaleDateString()}
                    </p>
                  </div>
                )}
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      {transactions.length === 0 && (
        <Card>
          <CardContent className="text-center py-8">
            <ArrowLeftRight className="w-12 h-12 text-gray-400 mx-auto mb-4" />
            <p className="text-gray-500">No borrowing transactions found. Start by borrowing some books!</p>
          </CardContent>
        </Card>
      )}
    </div>
  )
}

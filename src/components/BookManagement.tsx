"use client"

import type React from "react"
import { useState, useEffect } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Badge } from "@/components/ui/badge"
import { Plus, Search, Edit2, Trash2, AlertCircle } from "lucide-react"
import { api } from "../services/api"

interface BookType {
  bookId?: number
  title: string
  author: string
  genre: string
  isbn: string
  yearPublished: number
  availableCopies: number
  totalCopies: number
}

export function BookManagement() {
  const [books, setBooks] = useState<BookType[]>([])
  const [searchTerm, setSearchTerm] = useState("")
  const [showAddForm, setShowAddForm] = useState(false)
  const [editingBook, setEditingBook] = useState<BookType | null>(null)
  const [loading, setLoading] = useState(true)
  const [newBook, setNewBook] = useState<BookType>({
    title: "",
    author: "",
    genre: "",
    isbn: "",
    yearPublished: new Date().getFullYear(),
    availableCopies: 1,
    totalCopies: 1,
  })

  // Add error state
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    fetchBooks()
  }, [])

  const fetchBooks = async () => {
    try {
      setError(null)
      const response = await api.get("/books")
      setBooks(response.data)
    } catch (error: any) {
      console.error("Error fetching books:", error)
      setError(error.message || "Failed to fetch books. Please check if the backend server is running.")
      setBooks([]) // Set empty array on error
    } finally {
      setLoading(false)
    }
  }

  const handleAddBook = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      await api.post("/books", newBook)
      setNewBook({
        title: "",
        author: "",
        genre: "",
        isbn: "",
        yearPublished: new Date().getFullYear(),
        availableCopies: 1,
        totalCopies: 1,
      })
      setShowAddForm(false)
      fetchBooks()
    } catch (error) {
      console.error("Error adding book:", error)
    }
  }

  const handleUpdateBook = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!editingBook?.bookId) return

    try {
      await api.put(`/books/${editingBook.bookId}`, editingBook)
      setEditingBook(null)
      fetchBooks()
    } catch (error) {
      console.error("Error updating book:", error)
    }
  }

  const handleDeleteBook = async (bookId: number) => {
    if (window.confirm("Are you sure you want to delete this book?")) {
      try {
        await api.delete(`/books/${bookId}`)
        fetchBooks()
      } catch (error) {
        console.error("Error deleting book:", error)
      }
    }
  }

  const handleSearch = async () => {
    if (!searchTerm.trim()) {
      fetchBooks()
      return
    }

    try {
      const response = await api.get(`/books/search?keyword=${encodeURIComponent(searchTerm)}`)
      setBooks(response.data)
    } catch (error) {
      console.error("Error searching books:", error)
    }
  }

  const filteredBooks = books.filter(
    (book) =>
      book.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
      book.author.toLowerCase().includes(searchTerm.toLowerCase()) ||
      book.genre.toLowerCase().includes(searchTerm.toLowerCase()),
  )

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
          <h2 className="text-3xl font-bold text-gray-900">Book Management</h2>
          <p className="text-gray-600 mt-2">Manage your library's book collection</p>
        </div>
        <Button onClick={() => setShowAddForm(true)} className="flex items-center space-x-2">
          <Plus className="w-4 h-4" />
          <span>Add Book</span>
        </Button>
      </div>

      <div className="flex space-x-4">
        <div className="flex-1">
          <Input
            placeholder="Search books by title, author, or genre..."
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
            onKeyPress={(e) => e.key === "Enter" && handleSearch()}
          />
        </div>
        <Button onClick={handleSearch} variant="outline">
          <Search className="w-4 h-4" />
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
            <Button size="sm" variant="outline" onClick={fetchBooks}>
              Retry
            </Button>
          </CardContent>
        </Card>
      )}

      {showAddForm && (
        <Card>
          <CardHeader>
            <CardTitle>Add New Book</CardTitle>
          </CardHeader>
          <CardContent>
            <form onSubmit={handleAddBook} className="space-y-4">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <Label htmlFor="title">Title</Label>
                  <Input
                    id="title"
                    value={newBook.title}
                    onChange={(e) => setNewBook({ ...newBook, title: e.target.value })}
                    required
                  />
                </div>
                <div>
                  <Label htmlFor="author">Author</Label>
                  <Input
                    id="author"
                    value={newBook.author}
                    onChange={(e) => setNewBook({ ...newBook, author: e.target.value })}
                    required
                  />
                </div>
                <div>
                  <Label htmlFor="genre">Genre</Label>
                  <Input
                    id="genre"
                    value={newBook.genre}
                    onChange={(e) => setNewBook({ ...newBook, genre: e.target.value })}
                    required
                  />
                </div>
                <div>
                  <Label htmlFor="isbn">ISBN</Label>
                  <Input
                    id="isbn"
                    value={newBook.isbn}
                    onChange={(e) => setNewBook({ ...newBook, isbn: e.target.value })}
                    required
                  />
                </div>
                <div>
                  <Label htmlFor="year">Year Published</Label>
                  <Input
                    id="year"
                    type="number"
                    value={newBook.yearPublished}
                    onChange={(e) => setNewBook({ ...newBook, yearPublished: Number.parseInt(e.target.value) })}
                    required
                  />
                </div>
                <div>
                  <Label htmlFor="copies">Total Copies</Label>
                  <Input
                    id="copies"
                    type="number"
                    min="1"
                    value={newBook.totalCopies}
                    onChange={(e) => {
                      const copies = Number.parseInt(e.target.value)
                      setNewBook({
                        ...newBook,
                        totalCopies: copies,
                        availableCopies: copies,
                      })
                    }}
                    required
                  />
                </div>
              </div>
              <div className="flex space-x-2">
                <Button type="submit">Add Book</Button>
                <Button type="button" variant="outline" onClick={() => setShowAddForm(false)}>
                  Cancel
                </Button>
              </div>
            </form>
          </CardContent>
        </Card>
      )}

      {editingBook && (
        <Card>
          <CardHeader>
            <CardTitle>Edit Book</CardTitle>
          </CardHeader>
          <CardContent>
            <form onSubmit={handleUpdateBook} className="space-y-4">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <Label htmlFor="edit-title">Title</Label>
                  <Input
                    id="edit-title"
                    value={editingBook.title}
                    onChange={(e) => setEditingBook({ ...editingBook, title: e.target.value })}
                    required
                  />
                </div>
                <div>
                  <Label htmlFor="edit-author">Author</Label>
                  <Input
                    id="edit-author"
                    value={editingBook.author}
                    onChange={(e) => setEditingBook({ ...editingBook, author: e.target.value })}
                    required
                  />
                </div>
                <div>
                  <Label htmlFor="edit-genre">Genre</Label>
                  <Input
                    id="edit-genre"
                    value={editingBook.genre}
                    onChange={(e) => setEditingBook({ ...editingBook, genre: e.target.value })}
                    required
                  />
                </div>
                <div>
                  <Label htmlFor="edit-isbn">ISBN</Label>
                  <Input
                    id="edit-isbn"
                    value={editingBook.isbn}
                    onChange={(e) => setEditingBook({ ...editingBook, isbn: e.target.value })}
                    required
                  />
                </div>
                <div>
                  <Label htmlFor="edit-year">Year Published</Label>
                  <Input
                    id="edit-year"
                    type="number"
                    value={editingBook.yearPublished}
                    onChange={(e) => setEditingBook({ ...editingBook, yearPublished: Number.parseInt(e.target.value) })}
                    required
                  />
                </div>
                <div>
                  <Label htmlFor="edit-total-copies">Total Copies</Label>
                  <Input
                    id="edit-total-copies"
                    type="number"
                    min="1"
                    value={editingBook.totalCopies}
                    onChange={(e) => setEditingBook({ ...editingBook, totalCopies: Number.parseInt(e.target.value) })}
                    required
                  />
                </div>
              </div>
              <div className="flex space-x-2">
                <Button type="submit">Update Book</Button>
                <Button type="button" variant="outline" onClick={() => setEditingBook(null)}>
                  Cancel
                </Button>
              </div>
            </form>
          </CardContent>
        </Card>
      )}

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {filteredBooks.map((book) => (
          <Card key={book.bookId} className="hover:shadow-lg transition-shadow">
            <CardHeader className="pb-3">
              <div className="flex items-start justify-between">
                <div className="flex items-center space-x-2">
                  {/* Book icon is removed to avoid redeclaration */}
                  <CardTitle className="text-lg">{book.title}</CardTitle>
                </div>
                <div className="flex space-x-1">
                  <Button size="sm" variant="outline" onClick={() => setEditingBook(book)}>
                    <Edit2 className="w-4 h-4" />
                  </Button>
                  <Button size="sm" variant="outline" onClick={() => book.bookId && handleDeleteBook(book.bookId)}>
                    <Trash2 className="w-4 h-4" />
                  </Button>
                </div>
              </div>
            </CardHeader>
            <CardContent className="space-y-2">
              <p className="text-sm text-gray-600">
                <strong>Author:</strong> {book.author}
              </p>
              <p className="text-sm text-gray-600">
                <strong>Genre:</strong> {book.genre}
              </p>
              <p className="text-sm text-gray-600">
                <strong>ISBN:</strong> {book.isbn}
              </p>
              <p className="text-sm text-gray-600">
                <strong>Year:</strong> {book.yearPublished}
              </p>
              <div className="flex justify-between items-center pt-2">
                <Badge variant={book.availableCopies > 0 ? "default" : "destructive"}>
                  {book.availableCopies}/{book.totalCopies} Available
                </Badge>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      {filteredBooks.length === 0 && (
        <Card>
          <CardContent className="text-center py-8">
            {/* Book icon is removed to avoid redeclaration */}
            <p className="text-gray-500">No books found. Add some books to get started!</p>
          </CardContent>
        </Card>
      )}
    </div>
  )
}

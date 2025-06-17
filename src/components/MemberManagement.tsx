"use client"

import type React from "react"
import { useState, useEffect } from "react"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Badge } from "@/components/ui/badge"
import { Plus, Search, Edit2, Trash2, User, AlertCircle } from "lucide-react"
import { api } from "../services/api"

interface Member {
  memberId?: number
  name: string
  email: string
  phone: string
  address: string
  membershipStatus: "ACTIVE" | "SUSPENDED" | "EXPIRED"
  registrationDate?: string
}

export function MemberManagement() {
  const [members, setMembers] = useState<Member[]>([])
  const [searchTerm, setSearchTerm] = useState("")
  const [showAddForm, setShowAddForm] = useState(false)
  const [editingMember, setEditingMember] = useState<Member | null>(null)
  const [loading, setLoading] = useState(true)
  const [newMember, setNewMember] = useState<Member>({
    name: "",
    email: "",
    phone: "",
    address: "",
    membershipStatus: "ACTIVE",
  })

  // Add error state
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    fetchMembers()
  }, [])

  const fetchMembers = async () => {
    try {
      setError(null)
      const response = await api.get("/members")
      setMembers(response.data)
    } catch (error: any) {
      console.error("Error fetching members:", error)
      setError(error.message || "Failed to fetch members. Please check if the backend server is running.")
      setMembers([])
    } finally {
      setLoading(false)
    }
  }

  const handleAddMember = async (e: React.FormEvent) => {
    e.preventDefault()
    try {
      await api.post("/members", newMember)
      setNewMember({
        name: "",
        email: "",
        phone: "",
        address: "",
        membershipStatus: "ACTIVE",
      })
      setShowAddForm(false)
      fetchMembers()
    } catch (error) {
      console.error("Error adding member:", error)
    }
  }

  const handleUpdateMember = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!editingMember?.memberId) return

    try {
      await api.put(`/members/${editingMember.memberId}`, editingMember)
      setEditingMember(null)
      fetchMembers()
    } catch (error) {
      console.error("Error updating member:", error)
    }
  }

  const handleDeleteMember = async (memberId: number) => {
    if (window.confirm("Are you sure you want to delete this member?")) {
      try {
        await api.delete(`/members/${memberId}`)
        fetchMembers()
      } catch (error) {
        console.error("Error deleting member:", error)
      }
    }
  }

  const handleSearch = async () => {
    if (!searchTerm.trim()) {
      fetchMembers()
      return
    }

    try {
      const response = await api.get(`/members/search?name=${encodeURIComponent(searchTerm)}`)
      setMembers(response.data)
    } catch (error) {
      console.error("Error searching members:", error)
    }
  }

  const filteredMembers = members.filter(
    (member) =>
      member.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      member.email.toLowerCase().includes(searchTerm.toLowerCase()),
  )

  const getStatusBadgeVariant = (status: string) => {
    switch (status) {
      case "ACTIVE":
        return "default"
      case "SUSPENDED":
        return "destructive"
      case "EXPIRED":
        return "secondary"
      default:
        return "outline"
    }
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
          <h2 className="text-3xl font-bold text-gray-900">Member Management</h2>
          <p className="text-gray-600 mt-2">Manage library members and their profiles</p>
        </div>
        <Button onClick={() => setShowAddForm(true)} className="flex items-center space-x-2">
          <Plus className="w-4 h-4" />
          <span>Add Member</span>
        </Button>
      </div>

      <div className="flex space-x-4">
        <div className="flex-1">
          <Input
            placeholder="Search members by name or email..."
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
            <Button size="sm" variant="outline" onClick={fetchMembers}>
              Retry
            </Button>
          </CardContent>
        </Card>
      )}

      {showAddForm && (
        <Card>
          <CardHeader>
            <CardTitle>Add New Member</CardTitle>
          </CardHeader>
          <CardContent>
            <form onSubmit={handleAddMember} className="space-y-4">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <Label htmlFor="name">Name</Label>
                  <Input
                    id="name"
                    value={newMember.name}
                    onChange={(e) => setNewMember({ ...newMember, name: e.target.value })}
                    required
                  />
                </div>
                <div>
                  <Label htmlFor="email">Email</Label>
                  <Input
                    id="email"
                    type="email"
                    value={newMember.email}
                    onChange={(e) => setNewMember({ ...newMember, email: e.target.value })}
                    required
                  />
                </div>
                <div>
                  <Label htmlFor="phone">Phone</Label>
                  <Input
                    id="phone"
                    value={newMember.phone}
                    onChange={(e) => setNewMember({ ...newMember, phone: e.target.value })}
                    required
                  />
                </div>
                <div>
                  <Label htmlFor="address">Address</Label>
                  <Input
                    id="address"
                    value={newMember.address}
                    onChange={(e) => setNewMember({ ...newMember, address: e.target.value })}
                    required
                  />
                </div>
              </div>
              <div className="flex space-x-2">
                <Button type="submit">Add Member</Button>
                <Button type="button" variant="outline" onClick={() => setShowAddForm(false)}>
                  Cancel
                </Button>
              </div>
            </form>
          </CardContent>
        </Card>
      )}

      {editingMember && (
        <Card>
          <CardHeader>
            <CardTitle>Edit Member</CardTitle>
          </CardHeader>
          <CardContent>
            <form onSubmit={handleUpdateMember} className="space-y-4">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <Label htmlFor="edit-name">Name</Label>
                  <Input
                    id="edit-name"
                    value={editingMember.name}
                    onChange={(e) => setEditingMember({ ...editingMember, name: e.target.value })}
                    required
                  />
                </div>
                <div>
                  <Label htmlFor="edit-email">Email</Label>
                  <Input
                    id="edit-email"
                    type="email"
                    value={editingMember.email}
                    onChange={(e) => setEditingMember({ ...editingMember, email: e.target.value })}
                    required
                  />
                </div>
                <div>
                  <Label htmlFor="edit-phone">Phone</Label>
                  <Input
                    id="edit-phone"
                    value={editingMember.phone}
                    onChange={(e) => setEditingMember({ ...editingMember, phone: e.target.value })}
                    required
                  />
                </div>
                <div>
                  <Label htmlFor="edit-address">Address</Label>
                  <Input
                    id="edit-address"
                    value={editingMember.address}
                    onChange={(e) => setEditingMember({ ...editingMember, address: e.target.value })}
                    required
                  />
                </div>
                <div>
                  <Label htmlFor="edit-status">Membership Status</Label>
                  <select
                    id="edit-status"
                    value={editingMember.membershipStatus}
                    onChange={(e) =>
                      setEditingMember({
                        ...editingMember,
                        membershipStatus: e.target.value as "ACTIVE" | "SUSPENDED" | "EXPIRED",
                      })
                    }
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    <option value="ACTIVE">Active</option>
                    <option value="SUSPENDED">Suspended</option>
                    <option value="EXPIRED">Expired</option>
                  </select>
                </div>
              </div>
              <div className="flex space-x-2">
                <Button type="submit">Update Member</Button>
                <Button type="button" variant="outline" onClick={() => setEditingMember(null)}>
                  Cancel
                </Button>
              </div>
            </form>
          </CardContent>
        </Card>
      )}

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {filteredMembers.map((member) => (
          <Card key={member.memberId} className="hover:shadow-lg transition-shadow">
            <CardHeader className="pb-3">
              <div className="flex items-start justify-between">
                <div className="flex items-center space-x-2">
                  <User className="w-5 h-5 text-blue-600" />
                  <CardTitle className="text-lg">{member.name}</CardTitle>
                </div>
                <div className="flex space-x-1">
                  <Button size="sm" variant="outline" onClick={() => setEditingMember(member)}>
                    <Edit2 className="w-4 h-4" />
                  </Button>
                  <Button
                    size="sm"
                    variant="outline"
                    onClick={() => member.memberId && handleDeleteMember(member.memberId)}
                  >
                    <Trash2 className="w-4 h-4" />
                  </Button>
                </div>
              </div>
            </CardHeader>
            <CardContent className="space-y-2">
              <p className="text-sm text-gray-600">
                <strong>Email:</strong> {member.email}
              </p>
              <p className="text-sm text-gray-600">
                <strong>Phone:</strong> {member.phone}
              </p>
              <p className="text-sm text-gray-600">
                <strong>Address:</strong> {member.address}
              </p>
              {member.registrationDate && (
                <p className="text-sm text-gray-600">
                  <strong>Registered:</strong> {new Date(member.registrationDate).toLocaleDateString()}
                </p>
              )}
              <div className="flex justify-between items-center pt-2">
                <Badge variant={getStatusBadgeVariant(member.membershipStatus)}>{member.membershipStatus}</Badge>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      {filteredMembers.length === 0 && (
        <Card>
          <CardContent className="text-center py-8">
            <User className="w-12 h-12 text-gray-400 mx-auto mb-4" />
            <p className="text-gray-500">No members found. Add some members to get started!</p>
          </CardContent>
        </Card>
      )}
    </div>
  )
}

import axios from "axios"

const API_BASE_URL = process.env.REACT_APP_API_URL || "http://localhost:8080/api"

export const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000, // 10 second timeout
  headers: {
    "Content-Type": "application/json",
  },
})

// Request interceptor
api.interceptors.request.use(
  (config) => {
    console.log("Making request to:", `${config.baseURL}${config.url}`)
    return config
  },
  (error) => {
    console.error("Request error:", error)
    return Promise.reject(error)
  },
)

// Response interceptor with retry logic
api.interceptors.response.use(
  (response) => {
    return response
  },
  async (error) => {
    const originalRequest = error.config

    if (error.code === "ECONNREFUSED" || error.code === "ERR_NETWORK") {
      console.error("Backend server is not running. Please start the Spring Boot application on port 8080.")
      return Promise.reject(
        new Error(
          "Backend server is not available. Please ensure the Spring Boot application is running on port 8080.",
        ),
      )
    }

    if (error.response?.status === 500 && !originalRequest._retry) {
      originalRequest._retry = true
      // Wait 1 second before retry
      await new Promise((resolve) => setTimeout(resolve, 1000))
      return api(originalRequest)
    }

    console.error("API Error:", error.response?.data || error.message)
    return Promise.reject(error)
  },
)

// Health check function using axios instead of fetch
export const checkBackendHealth = async (): Promise<boolean> => {
  try {
    // Use axios with a shorter timeout for health checks
    const healthApi = axios.create({
      timeout: 5000,
      headers: {
        "Content-Type": "application/json",
      },
    })

    const response = await healthApi.get(`${API_BASE_URL}/health`)
    return response.status === 200
  } catch (error) {
    console.error("Backend health check failed:", error)
    return false
  }
}

export default api

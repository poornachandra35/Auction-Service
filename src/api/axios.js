// import axios from "axios";

// const API = axios.create({
//   baseURL: "http://localhost:8080",
// });

// // Attach JWT automatically
// API.interceptors.request.use((req) => {
//   const token = localStorage.getItem("token");
//   if (token) {
//     req.headers.Authorization = `Bearer ${token}`;
//   }
//   return req;
// });

// export default API;

// src/api/axios.js
import axios from "axios";

const API = axios.create({
  baseURL: "http://localhost:8080"
});

// attach token automatically
API.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  const userId = localStorage.getItem("userId");

  if (token) config.headers.Authorization = `Bearer ${token}`;
  if (userId) config.headers.userId = userId;

  return config;
});

export default API;
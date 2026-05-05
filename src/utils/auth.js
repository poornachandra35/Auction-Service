// src/utils/auth.js

export const getUser = () => {
  const token = localStorage.getItem("token");

  if (!token) return null;

  try {
    const payload = JSON.parse(atob(token.split(".")[1]));

    return {
      userId: payload.userId,
      email: payload.sub,
      name: payload.name,
      role: payload.role
    };
  } catch (e) {
    return null;
  }
};
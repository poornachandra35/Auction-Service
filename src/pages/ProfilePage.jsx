import { useEffect, useState } from "react";
import API from "../api/axios";

export default function ProfilePage() {
  const [user, setUser] = useState({});
  const userId = localStorage.getItem("userId");

  useEffect(() => {
    API.get(`/api/users/${userId}`)
      .then(res => setUser(res.data));
  }, []);

  return (
    <div className="dashboard">
      <h2>Edit Profile</h2>

      <input value={user.name || ""} readOnly />
      <input value={user.email || ""} readOnly />

      <p style={{ color: "gray" }}>
        (Editing basic info not supported in backend yet)
      </p>
    </div>
  );
}
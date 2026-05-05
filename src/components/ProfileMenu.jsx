import { useState } from "react";
import { useNavigate } from "react-router-dom";

export default function ProfileMenu({ name }) {
  const [open, setOpen] = useState(false);
  const navigate = useNavigate();

  const firstLetter = name.charAt(0).toUpperCase();

  return (
    <div className="profile-wrapper">
      <div
        className="profile-circle"
        onClick={() => setOpen(!open)}
      >
        {firstLetter}
      </div>

      {open && (
        <div className="profile-dropdown">
            <p onClick={() => window.location.href="/analytics"}>
  View Analytics
</p>
          <p onClick={() => navigate("/profile")}>Edit Profile</p>
          <p onClick={() => navigate("/preferences")}>Preferences</p>
          <p
            onClick={() => {
              localStorage.clear();
              window.location.href = "/";
            }}
          >
            Logout
          </p>
        </div>
      )}
    </div>
  );
}
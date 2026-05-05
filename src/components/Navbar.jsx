import { useState } from "react";
import ProfileMenu from "./ProfileMenu";
import "../styles/navbar.css";

export default function Navbar() {
  const name = localStorage.getItem("name") || "U";

  return (
    <div className="navbar">
      <h2>Auction App</h2>

      <ProfileMenu name={name} />
    </div>
  );
}
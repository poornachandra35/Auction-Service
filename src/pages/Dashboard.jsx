// src/pages/Dashboard.jsx
import SellerDashboard from "./SellerDashboard";
import BuyerDashboard from "./BuyerDashboard";

export default function Dashboard() {
  const role = localStorage.getItem("role");

  if (role === "SELLER") return <SellerDashboard />;
  return <BuyerDashboard />;
}
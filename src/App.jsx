import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Login from "./pages/Login";
import Register from "./pages/Register";
import ForgotPassword from "./pages/ForgotPassword";
import Dashboard from "./pages/Dashboard";
import ItemDetails from "./pages/ItemDetails";
import Auction from "./pages/Auction";
import CreateAuction from "./pages/CreateAuction";
import ProfilePage from "./pages/ProfilePage";
import PreferencesPage from "./pages/PreferencesPage";
import SellerAnalytics from "./pages/SellerAnalytics";
// ✅ Protected Route
function PrivateRoute({ children }) {
  const token = localStorage.getItem("token");
  return token ? children : <Navigate to="/" />;
}

export default function App() {
  return (
    <BrowserRouter>
      <Routes>

        {/* PUBLIC */}
       
        <Route path="/" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/forgot-password" element={<ForgotPassword />} />
<Route path="/profile" element={<ProfilePage />} />
<Route path="/preferences" element={<PreferencesPage />} />
 <Route path="/analytics" element={<SellerAnalytics />} />
        {/* PROTECTED */}
        <Route
          path="/items"
          element={
            <PrivateRoute>
              <Dashboard />
            </PrivateRoute>
          }
        />

        <Route
          path="/items/:id"
          element={
            <PrivateRoute>
              <ItemDetails />
            </PrivateRoute>
          }
        />

        <Route
          path="/auction/:id"
          element={
            <PrivateRoute>
              <Auction />
            </PrivateRoute>
          }
        />

        <Route
          path="/create-auction"
          element={
            <PrivateRoute>
              <CreateAuction />
            </PrivateRoute>
          }
        />

      </Routes>
    </BrowserRouter>
  );
}
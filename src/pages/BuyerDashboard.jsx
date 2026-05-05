import { useEffect, useState } from "react";
import API from "../api/axios";
import noImage from "../assets/no-image.png";
import "../styles/dashboard.css";

export default function BuyerDashboard() {

  const [auctions, setAuctions] = useState([]);
  const [items, setItems] = useState([]);

  const [category, setCategory] = useState("");
  const [status, setStatus] = useState("ALL");
  const [sort, setSort] = useState("");

  const [showSidebar, setShowSidebar] = useState(false);
  const [darkMode, setDarkMode] = useState(true);

  const [zoomImage, setZoomImage] = useState(null);

  // 🔥 NEW STATES (ONLY ADDITION)
  const [activeBid, setActiveBid] = useState(null);
  const [bidAmount, setBidAmount] = useState("");

  useEffect(() => {
    loadData();
  }, []);

  const loadData = () => {
    API.get("/api/auctions").then(res => setAuctions(res.data));
    API.get("/api/items").then(res => setItems(res.data));
  };

  const getItem = (id) =>
    items.find(i => Number(i.id) === Number(id));

  const getTimeLeft = (time) => {
    const diff = new Date(time) - new Date();
    if (diff <= 0) return "Ended";

    const min = Math.floor(diff / 60000);
    const sec = Math.floor((diff % 60000) / 1000);

    return `${min}m ${sec}s`;
  };

  // 🔥 UPDATED BID FUNCTION (prompt removed only)
  const placeBid = async (auction, amountFromUI = null) => {
    try {
      const userId = localStorage.getItem("userId");

      if (!userId) {
        alert("Please login first");
        return;
      }

      const amount = amountFromUI;

      if (!amount || isNaN(amount)) {
        alert("Enter valid amount");
        return;
      }

      await API.post("/api/auctions/bid", {
        auctionId: auction.id,
        userId: Number(userId),
        amount: Number(amount)
      });

      // 🔥 close UI after success
      setActiveBid(null);
      setBidAmount("");

      loadData();

    } catch (err) {
      alert(err?.response?.data || "Bid failed");
    }
  };

  let filtered = auctions.filter(a => {
    const item = getItem(a.itemId);
    if (!item) return false;

    if (category && !item.title.toLowerCase().includes(category.toLowerCase()))
      return false;

    if (status !== "ALL" && a.status !== status)
      return false;

    return true;
  });

  if (sort === "LOW_HIGH") {
    filtered.sort((a, b) => a.basePrice - b.basePrice);
  } else if (sort === "HIGH_LOW") {
    filtered.sort((a, b) => b.basePrice - a.basePrice);
  }

  return (
    <div className={darkMode ? "dashboard dark" : "dashboard light"}>

      {/* HEADER */}
      <div className="dashboard-header">
        <h2>Buyer Dashboard</h2>

        <div className="header-actions">
          <button onClick={() => setShowSidebar(true)}>☰</button>
          <button onClick={() => setDarkMode(!darkMode)}>
            {darkMode ? "🌙" : "☀️"}
          </button>
          <ProfileMenu />
        </div>
      </div>

      {/* SIDEBAR */}
      {showSidebar && (
        <div className="overlay" onClick={() => setShowSidebar(false)}>
          <div className="sidebar" onClick={(e) => e.stopPropagation()}>
            <h3>Filters</h3>

            <input placeholder="Search..."
              onChange={(e) => setCategory(e.target.value)} />

            <button onClick={() => setStatus("ALL")}>All</button>
            <button onClick={() => setStatus("ACTIVE")}>Active</button>
            <button onClick={() => setStatus("CREATED")}>Upcoming</button>
            <button onClick={() => setStatus("ENDED")}>Ended</button>

            <select onChange={(e) => setSort(e.target.value)}>
              <option value="">Sort</option>
              <option value="LOW_HIGH">Low → High</option>
              <option value="HIGH_LOW">High → Low</option>
            </select>
          </div>
        </div>
      )}

      {/* ZOOM */}
      {zoomImage && (
        <div className="zoom-overlay" onClick={() => setZoomImage(null)}>
          <img src={zoomImage} className="zoom-img" />
        </div>
      )}

      {/* CARDS */}
      <div className="card-grid">

        {filtered.map(a => {
          const item = getItem(a.itemId);
          if (!item) return null;

          const imageUrl = item.imageUrl
            ? `http://localhost:8082/${item.imageUrl}`
            : noImage;

          return (
            <div
              key={a.id}
              className={`card ${activeBid === a.id ? "active" : ""}`}
            >

              <div className="image-container">
                <img
                  src={imageUrl}
                  onError={(e) => (e.target.src = noImage)}
                />

                <div
                  className="zoom-btn"
                  onClick={() => setZoomImage(imageUrl)}
                >
                  🔍
                </div>
              </div>

              <p><b>{item.title}</b></p>

              <p className={`status ${a.status.toLowerCase()}`}>
                {a.status}
              </p>

              {a.status === "CREATED" && (
                <p className="timer blue">
                  ⏳ Starts in: {getTimeLeft(a.startTime)}
                </p>
              )}

              {a.status === "ACTIVE" && (
                <p className="timer red">
                  🔴 Ends in: {getTimeLeft(a.endTime)}
                </p>
              )}

              <p>₹ {a.basePrice}</p>
              <p>Highest: ₹ {a.currentHighestBid}</p>

              {/* 🔥 ONLY UI CHANGE HERE */}
              {a.status === "ACTIVE" && (
                <>
                  {activeBid === a.id ? (
                    <div className="bid-box">
                      <input
                        placeholder="Enter bid"
                        value={bidAmount}
                        onChange={(e) => setBidAmount(e.target.value)}
                      />

                      <button onClick={() => placeBid(a, bidAmount)}>
                        Submit
                      </button>

                      <button onClick={() => setActiveBid(null)}>
                        Cancel
                      </button>
                    </div>
                  ) : (
                    <button onClick={() => setActiveBid(a.id)}>
                      Place Bid
                    </button>
                  )}
                </>
              )}

            </div>
          );
        })}

      </div>
    </div>
  );
}

/* PROFILE */
function ProfileMenu() {
  const [open, setOpen] = useState(false);

  const name = localStorage.getItem("name") || "User";
  const firstLetter = name.charAt(0).toUpperCase();

  const logout = () => {
    localStorage.clear();
    window.location.href = "/";
  };

  return (
    <div className="profile-wrapper">
      <div className="profile-circle" onClick={() => setOpen(!open)}>
        {firstLetter}
      </div>

      {open && (
        <div className="profile-dropdown">
          <p onClick={logout}>Logout</p>
        </div>
      )}
    </div>
  );
}
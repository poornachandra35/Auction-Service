import { useEffect, useState } from "react";
import API from "../api/axios";
import CreateItem from "../components/CreateItem";
import noImage from "../assets/no-image.png";
import "../styles/dashboard.css";

export default function SellerDashboard() {
  const [auctions, setAuctions] = useState([]);
  const [items, setItems] = useState([]);
  const [filtered, setFiltered] = useState([]);

  const [editingItem, setEditingItem] = useState(null);
  const [editData, setEditData] = useState({});
  const [editImage, setEditImage] = useState(null);

  const [showCreate, setShowCreate] = useState(false);

  const [category, setCategory] = useState("");
  const [status, setStatus] = useState("ALL");
  const [maxPrice, setMaxPrice] = useState(100000);
  const [sort, setSort] = useState("");

  const [page, setPage] = useState(1);
  const itemsPerPage = 6;

  // 🔥 NEW STATES
  const [toast, setToast] = useState(null);
  const [confirmDelete, setConfirmDelete] = useState(null);

  const userId = localStorage.getItem("userId");

  useEffect(() => {
    loadData();
  }, []);

  useEffect(() => {
    filterData();
  }, [category, status, maxPrice, sort, auctions, items]);

  useEffect(() => {
    const interval = setInterval(() => {
      setAuctions(prev => [...prev]);
    }, 1000);
    return () => clearInterval(interval);
  }, []);

  // 🔥 TOAST FUNCTION
  const showToast = (msg, type = "success") => {
    setToast({ msg, type });
    setTimeout(() => setToast(null), 3000);
  };

  const loadData = () => {
    API.get(`/api/auctions/seller/${userId}`)
      .then(res => {
        setAuctions(res.data);
        setFiltered(res.data);
      });

    API.get("/api/items")
      .then(res => setItems(res.data));
  };

  const getItem = (itemId) =>
    items.find(i => Number(i.id) === Number(itemId));

  const getCountdown = (time) => {
    const diff = new Date(time) - new Date();
    if (diff <= 0) return "0m 0s";

    const mins = Math.floor(diff / 60000);
    const secs = Math.floor((diff % 60000) / 1000);
    return `${mins}m ${secs}s`;
  };

  const filterData = () => {
    let result = auctions.filter(a => {
      const item = getItem(a.itemId);
      if (!item) return false;

      const matchStatus =
        status === "ALL" || a.status === status;

      const matchCategory =
        !category ||
        item.category.toLowerCase().includes(category.toLowerCase());

      const matchPrice = item.basePrice <= maxPrice;

      return matchStatus && matchCategory && matchPrice;
    });

    if (sort === "LOW_HIGH") {
      result.sort((a, b) => a.basePrice - b.basePrice);
    } else if (sort === "HIGH_LOW") {
      result.sort((a, b) => b.basePrice - a.basePrice);
    }

    setFiltered(result);
    setPage(1);
  };

  const start = (page - 1) * itemsPerPage;
  const paginated = filtered.slice(start, start + itemsPerPage);

  // ✅ UPDATED UPDATE FUNCTION (FULL FORM)
  const updateItem = async (id) => {
    try {
      const formData = new FormData();

      const dto = {
        title: editData.title,
        description: editData.description,
        category: editData.category,
        basePrice: editData.basePrice,
        auctionStartTime: editData.auctionStartTime,
        auctionDurationMinutes: editData.auctionDurationMinutes || 10
      };

      formData.append(
        "data",
        new Blob([JSON.stringify(dto)], { type: "application/json" })
      );

      if (editImage) {
        formData.append("image", editImage);
      }

      await API.put(`/api/items/${id}`, formData, {
        headers: { userId }
      });

      showToast("Updated successfully ✅");
      setEditingItem(null);
      loadData();

    } catch (err) {
      console.error(err.response?.data);
      showToast("Update failed ❌", "error");
    }
  };

  const startEdit = (item) => {
    setEditingItem(item.id);
    setEditData(item);
  };

  // 🔥 DELETE WITH CONFIRMATION
  const deleteItem = async () => {
    try {
      await API.delete(`/api/items/${confirmDelete}`, {
        headers: { userId }
      });

      showToast("Deleted successfully 🗑️");
      setConfirmDelete(null);
      loadData();

    } catch (err) {
      console.error(err);
      showToast("Delete failed ❌", "error");
    }
  };

  return (
    <div className="dashboard">
      <h2>Seller Dashboard</h2>

      {/* 🔥 TOAST */}
      {toast && (
        <div className={`toast ${toast.type}`}>
          {toast.msg}
        </div>
      )}

      {/* 🔥 CONFIRM MODAL */}
      {confirmDelete && (
        <div className="modal">
          <div className="modal-box">
            <p>Are you sure you want to delete?</p>
            <button onClick={deleteItem}>Yes</button>
            <button onClick={() => setConfirmDelete(null)}>Cancel</button>
          </div>
        </div>
      )}

      {/* ORIGINAL UI */}
      <div style={{ textAlign: "right", marginBottom: "10px" }}>
        <button
          className="btn btn-update"
          onClick={() => window.location.href = "/analytics"}
        >
          📊 View Analytics
        </button>
      </div>

      <div style={{ textAlign: "center", marginBottom: "20px" }}>
        <button
          className="btn btn-update"
          onClick={() => setShowCreate(!showCreate)}
        >
          {showCreate ? "Close" : "Create Auction"}
        </button>
      </div>

      {showCreate && (
        <div className="auth-container">
          <CreateItem onSuccess={loadData} />
        </div>
      )}

      {/* FILTER (UNCHANGED) */}
      <div style={{ textAlign: "center", marginBottom: "20px" }}>
        <input
          placeholder="Category"
          onChange={(e) => setCategory(e.target.value)}
        />

        <div>
          <label>Max Price: ₹{maxPrice}</label>
          <input
            type="range"
            min="0"
            max="100000"
            value={maxPrice}
            onChange={(e) => setMaxPrice(e.target.value)}
          />
        </div>

        <button onClick={() => setStatus("ALL")}>All</button>
        <button onClick={() => setStatus("CREATED")}>Created</button>
        <button onClick={() => setStatus("ACTIVE")}>Active</button>
        <button onClick={() => setStatus("ENDED")}>Ended</button>

        <select onChange={(e) => setSort(e.target.value)}>
          <option value="">Sort</option>
          <option value="LOW_HIGH">Low → High</option>
          <option value="HIGH_LOW">High → Low</option>
        </select>
      </div>

      {/* CARDS */}
      <div className="card-grid">
        {paginated.map(a => {
          const item = getItem(a.itemId);
          if (!item) return null;

          const imageUrl = item.imageUrl
            ? `http://localhost:8082/${item.imageUrl}`
            : noImage;

          return (
            <div key={a.id} className="card">
              <img src={imageUrl} onError={(e) => (e.target.src = noImage)} />

              <p><b>{item.title}</b></p>

              <p>
                <span style={{
                  background:
                    a.status === "ACTIVE"
                      ? "#22c55e"
                      : a.status === "CREATED"
                      ? "#3b82f6"
                      : "#ef4444",
                  padding: "4px 8px",
                  borderRadius: "6px",
                  color: "white"
                }}>
                  {a.status}
                </span>
              </p>

              {a.status === "CREATED" && (
                <p style={{ color: "#3b82f6", fontWeight: "bold" }}>
                  ⏳ Starts in: {getCountdown(a.startTime)}
                </p>
              )}

              {a.status === "ACTIVE" && (
                <p style={{ color: "red", fontWeight: "bold" }}>
                  🔴 Ends in: {getCountdown(a.endTime)}
                </p>
              )}

              {a.status === "ENDED" && (
                <p style={{ color: "gray" }}>Auction Ended</p>
              )}

              {/* 🔥 FULL EDIT FORM */}
              {editingItem === item.id ? (
                <>
                  <input value={editData.title} onChange={(e) => setEditData({ ...editData, title: e.target.value })} />
                  <textarea value={editData.description} onChange={(e) => setEditData({ ...editData, description: e.target.value })} />
                  <input value={editData.category} onChange={(e) => setEditData({ ...editData, category: e.target.value })} />
                  <input type="number" value={editData.basePrice} onChange={(e) => setEditData({ ...editData, basePrice: e.target.value })} />
                  <input type="datetime-local" onChange={(e) => setEditData({ ...editData, auctionStartTime: e.target.value })} />
                  <input type="number" placeholder="Duration" onChange={(e) => setEditData({ ...editData, auctionDurationMinutes: e.target.value })} />
                  <input type="file" onChange={(e) => setEditImage(e.target.files[0])} />

                  <button onClick={() => updateItem(item.id)}>Save</button>
                </>
              ) : (
                <>
                  <p>₹ {a.basePrice}</p>
                  <p>Highest: ₹{a.currentHighestBid}</p>

                  <div className="btn-group">
                    <button className="btn btn-update" onClick={() => startEdit(item)}>
                      Update
                    </button>

                    <button
                      className="btn btn-delete"
                      onClick={() => setConfirmDelete(a.itemId)}
                    >
                      Delete
                    </button>
                  </div>
                </>
              )}
            </div>
          );
        })}
      </div>

      {/* PAGINATION */}
      <div style={{ textAlign: "center", marginTop: "20px" }}>
        <button disabled={page === 1} onClick={() => setPage(page - 1)}>
          Prev
        </button>

        <span style={{ margin: "0 10px" }}>Page {page}</span>

        <button
          disabled={start + itemsPerPage >= filtered.length}
          onClick={() => setPage(page + 1)}
        >
          Next
        </button>
      </div>
    </div>
  );
}
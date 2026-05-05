import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import API from "../api/axios";

export default function ItemDetails() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [item, setItem] = useState(null);
  const [auction, setAuction] = useState(null);

  const [editMode, setEditMode] = useState(false);
  const [editForm, setEditForm] = useState({});

  // 🔁 Fetch item + auction
  useEffect(() => {
    fetchItem();
    fetchAuction();
  }, []);

  const fetchItem = async () => {
    try {
      const res = await API.get(`/api/items/${id}`);
      setItem(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  const fetchAuction = async () => {
    try {
      const res = await API.get(`/api/auctions/${id}`);
      setAuction(res.data);
    } catch (err) {
      console.error(err);
    }
  };

  // 📝 Handle form change
  const handleEditChange = (e) => {
    setEditForm({ ...editForm, [e.target.name]: e.target.value });
  };

  // 🟡 Open edit form
  const openEdit = () => {
    setEditForm({
      title: item.title,
      description: item.description,
      category: item.category,
      basePrice: item.basePrice,
      startTime: auction.startTime,
      duration: 120,
    });
    setEditMode(true);
  };

 //---------------------------------------------------------------------
  const submitUpdate = async () => {
    try {
      // 🔹 Update Item
      await API.put(
        `/api/items/${auction.itemId}`,
        {
          title: editForm.title,
          description: editForm.description,
          category: editForm.category,
          basePrice: Number(editForm.basePrice),
          auctionStartTime: editForm.startTime,
          auctionDurationMinutes: Number(editForm.duration),
        },
        {
          headers: {
            userId: 1,
          },
        }
      );

      // 🔹 Calculate end time
      const start = new Date(editForm.startTime);
      const end = new Date(start.getTime() + editForm.duration * 60000);

      // 🔹 Update Auction
      await API.put(
        `/api/auctions/${auction.id}`,
        {
          itemId: auction.itemId,
          sellerId: auction.sellerId,
          basePrice: Number(editForm.basePrice),
          startTime: start.toISOString(),
          endTime: end.toISOString(),
        },
        {
          headers: {
            userId: 1,
          },
        }
      );

      alert("Updated Successfully!");
      setEditMode(false);
      fetchItem();
      fetchAuction();

    } catch (err) {
      console.error(err);
      alert(err.response?.data?.message || "Update Failed");
    }
  };

//---------------------------------------------------------------------------
  const deleteAuction = async () => {
    if (!window.confirm("Are you sure?")) return;

    try {
      await API.delete(`/api/auctions/${auction.id}`);
      await API.delete(`/api/items/${auction.itemId}`, {
        headers: { userId: 1 },
      });

      alert("Deleted Successfully!");
      navigate("/items");

    } catch (err) {
      console.error(err);
      alert(err.response?.data?.message || "Delete Failed");
    }
  };
//-----------------------------------------------------------
  // ⏳ Loading
  if (!item || !auction) {
    return <div className="p-6">Loading...</div>;
  }

  return (
    <div className="p-6 bg-gray-100 min-h-screen">
      <div className="bg-white p-6 rounded-xl shadow-lg max-w-xl mx-auto">

        <h1 className="text-2xl font-bold mb-2">{item.title}</h1>

        <p className="text-gray-600 mb-4">{item.description}</p>

        <p className="text-lg font-semibold text-blue-500">
          ₹{auction.basePrice}
        </p>

        <p className="text-sm text-gray-500 mb-2">
          Status: {auction.status}
        </p>

        {/* Go to Auction */}
        <button
          className="w-full bg-blue-500 text-white py-2 rounded mt-4"
          onClick={() => navigate(`/auction/${auction.id}`)}
        >
          Go to Auction
        </button>

        {/* ACTION BUTTONS */}
        {auction.status === "CREATED" && (
          <>
            <button
              onClick={openEdit}
              className="w-full bg-yellow-500 text-white py-2 rounded mt-2"
            >
              Update Auction
            </button>

            <button
              onClick={deleteAuction}
              className="w-full bg-red-500 text-white py-2 rounded mt-2"
            >
              Delete Auction
            </button>
          </>
        )}

        {/* EDIT FORM */}
        {editMode && (
          <div className="mt-4 p-4 border rounded bg-gray-50">

            <h3 className="font-bold mb-2">Edit Auction</h3>

            <input
              name="title"
              value={editForm.title}
              onChange={handleEditChange}
              className="w-full p-2 border mb-2"
              placeholder="Title"
            />

            <input
              name="description"
              value={editForm.description}
              onChange={handleEditChange}
              className="w-full p-2 border mb-2"
              placeholder="Description"
            />

            <input
              name="category"
              value={editForm.category}
              onChange={handleEditChange}
              className="w-full p-2 border mb-2"
              placeholder="Category"
            />

            <input
              type="number"
              name="basePrice"
              value={editForm.basePrice}
              onChange={handleEditChange}
              className="w-full p-2 border mb-2"
              placeholder="Base Price"
            />

            <input
              type="datetime-local"
              name="startTime"
              onChange={handleEditChange}
              className="w-full p-2 border mb-2"
            />

            <input
              type="number"
              name="duration"
              value={editForm.duration}
              onChange={handleEditChange}
              className="w-full p-2 border mb-2"
              placeholder="Duration (minutes)"
            />

            <button
              onClick={submitUpdate}
              className="w-full bg-green-500 text-white py-2 rounded"
            >
              Submit Update
            </button>

            <button
              onClick={() => setEditMode(false)}
              className="w-full bg-gray-400 text-white py-2 rounded mt-2"
            >
              Cancel
            </button>

          </div>
        )}

        {auction.status !== "CREATED" && (
          <p className="text-red-500 mt-4">
            Cannot edit or delete active/ended auction
          </p>
        )}

      </div>
    </div>
  );
}
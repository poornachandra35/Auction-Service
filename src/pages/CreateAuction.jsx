import { useState } from "react";
import API from "../api/axios";
import { useNavigate } from "react-router-dom";
import { getUser } from "../utils/auth";

export default function CreateAuction() {
  const navigate = useNavigate();
  const user = getUser();

  const [form, setForm] = useState({
    title: "",
    description: "",
    category: "",
    basePrice: "",
    startTime: "",
    duration: "",
  });

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async () => {
    
      // ❗ Basic validation
      if (
        !form.title ||
        !form.description ||
        !form.category ||
        !form.basePrice ||
        !form.startTime ||
        !form.duration
      ) {
        alert("Please fill all fields");
        return;
      }

    //----------------------------------------------------------------------------------
      const itemRes = await API.post(
        "/api/items",
        {
          title: form.title,
          description: form.description,
          category: form.category,
          basePrice: Number(form.basePrice),
          sellerId: user.id,
          auctionStartTime: form.startTime,
          auctionDurationMinutes: Number(form.duration),
        },
        {
          headers: {
            userId: 1, // ✅ REQUIRED HEADER
          },
        }
      );

      const itemId = itemRes.data.id;

      // ✅ STEP 2: CALCULATE END TIME
      const start = new Date(form.startTime);
      const end = new Date(start.getTime() + form.duration * 60000);

      // ✅ STEP 3: CREATE AUCTION
      await API.post(
        "/api/auctions",
        {
          itemId: itemId,
          sellerId: user.id,
          basePrice: Number(form.basePrice),
          startTime: form.startTime,
          endTime: end.toISOString(),
        },
        {
          headers: {
            userId: 1, // ✅ REQUIRED HEADER
          },
        }
      );

      alert("Auction Created Successfully!");
      navigate("/items");

    
    
  };

  return (
    <div className="p-6 bg-gray-100 min-h-screen flex justify-center">
      <div className="bg-white p-6 rounded-xl shadow w-[400px]">

        <h2 className="text-xl font-bold mb-4">Create Auction</h2>

        <input
          name="title"
          placeholder="Title"
          onChange={handleChange}
          className="w-full p-2 border mb-2"
        />

        <input
          name="description"
          placeholder="Description"
          onChange={handleChange}
          className="w-full p-2 border mb-2"
        />

        <input
          name="category"
          placeholder="Category (e.g. ELECTRONICS)"
          onChange={handleChange}
          className="w-full p-2 border mb-2"
        />

        <input
          name="basePrice"
          type="number"
          placeholder="Base Price"
          onChange={handleChange}
          className="w-full p-2 border mb-2"
        />

        <input
          type="datetime-local"
          name="startTime"
          onChange={handleChange}
          className="w-full p-2 border mb-2"
        />

        <input
          type="number"
          name="duration"
          placeholder="Duration (minutes)"
          onChange={handleChange}
          className="w-full p-2 border mb-2"
        />

        <button
          onClick={handleSubmit}
          className="w-full bg-green-500 text-white py-2 rounded"
        >
          Create Auction
        </button>

      </div>
    </div>
  );
}
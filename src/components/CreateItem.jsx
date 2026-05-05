import { useState } from "react";
import API from "../api/axios";

export default function CreateItem({ onSuccess }) {
  const [data, setData] = useState({
    title: "",
    description: "",
    category: "",
    basePrice: "",
    auctionStartTime: "",
    auctionDurationMinutes: ""
  });

  const [image, setImage] = useState(null);

  const submit = async () => {
    try {
      const formData = new FormData();

      formData.append(
        "data",
        new Blob([JSON.stringify(data)], { type: "application/json" })
      );

      if (image) {
        formData.append("image", image);
      }

      await API.post("/api/items", formData);

      alert("Auction Created ✅");

      if (onSuccess) onSuccess();

    } catch (err) {
      console.error(err);
      alert("Error creating item ❌");
    }
  };

  return (
    <div className="glass-card form-animate">

      <h2>Create Auction</h2>

      <input
        placeholder="Title"
        onChange={(e) => setData({ ...data, title: e.target.value })}
      />

      <input
        placeholder="Description"
        onChange={(e) => setData({ ...data, description: e.target.value })}
      />

      <input
        placeholder="Category"
        onChange={(e) => setData({ ...data, category: e.target.value })}
      />

      <input
        type="number"
        placeholder="Base Price"
        onChange={(e) => setData({ ...data, basePrice: e.target.value })}
      />

      <input
        type="datetime-local"
        onChange={(e) =>
          setData({ ...data, auctionStartTime: e.target.value })
        }
      />

      <input
        type="number"
        placeholder="Duration (minutes)"
        onChange={(e) =>
          setData({ ...data, auctionDurationMinutes: e.target.value })
        }
      />

      <input
        type="file"
        onChange={(e) => setImage(e.target.files[0])}
      />

      <button onClick={submit}>
        Create Auction
      </button>

    </div>
  );
}
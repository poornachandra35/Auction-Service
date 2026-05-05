import API from "../api/axios";
import { useState } from "react";
import noImage from "../assets/no-image.png";

export default function ItemCard({ item }) {
  const [bid, setBid] = useState("");

  const placeBid = () => {
    API.post("/api/auctions/bid", {
      auctionId: item.id,
      userId: localStorage.getItem("userId"),
      amount: Number(bid)
    }).then(() => alert("Bid placed"));
  };

  // ✅ safe image
  const imageUrl = item.imageUrl
    ? `http://localhost:8082/${item.imageUrl}`
    : noImage;

  return (
    <div className="card">
      <img
        src={imageUrl}
        alt={item.title}
        width="150"
        height="150"
        style={{ objectFit: "cover", borderRadius: "8px" }}
        onError={(e) => (e.target.src = noImage)} // 🔥 fallback if broken
      />

      <h3>{item.title}</h3>
      <p>₹{item.basePrice}</p>

      <input
        placeholder="Your bid"
        onChange={(e) => setBid(e.target.value)}
      />

      <button onClick={placeBid}>Bid</button>
    </div>
  );
}
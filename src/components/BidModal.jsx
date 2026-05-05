import { useState } from "react";
import API from "../api/axios";

export default function BidModal({ auction, onClose }) {
  const [amount, setAmount] = useState("");

  const placeBid = async () => {
    try {
      await API.post("/api/auctions/bid", {
        auctionId: auction.id,
        userId: localStorage.getItem("userId"),
        amount: Number(amount),
      });

      alert("Bid placed!");
      onClose();
    } catch {
      alert("Bid failed");
    }
  };

  return (
    <div className="modal">
      <div className="modal-content">
        <h3>Place Bid</h3>

        <input
          type="number"
          placeholder="Enter amount"
          onChange={(e) => setAmount(e.target.value)}
        />

        <button onClick={placeBid}>Submit</button>
        <button onClick={onClose}>Cancel</button>
      </div>
    </div>
  );
}
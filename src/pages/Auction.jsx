import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getUser } from "../utils/auth";
import API from "../api/axios";

export default function Auction() {
  const { id } = useParams();
  const user = getUser();

  const [auction, setAuction] = useState(null);
  const [item, setItem] = useState(null);
  const [bidAmount, setBidAmount] = useState("");
  const [timeLeft, setTimeLeft] = useState("");

  // 🔁 Initial fetch
  useEffect(() => {
    fetchAuction();
  }, []);

  // 🔄 Auto refresh (every 5 sec)
  useEffect(() => {
    const interval = setInterval(() => {
      fetchAuction();
    }, 5000);

    return () => clearInterval(interval);
  }, []);

  // ⏳ Timer update every second
  useEffect(() => {
    if (!auction) return;

    const interval = setInterval(() => {
      updateTimer();
    }, 1000);

    return () => clearInterval(interval);
  }, [auction]);

  // 📡 Fetch auction + item
  const fetchAuction = async () => {
    try {
      const res = await API.get(`/api/auctions/${id}`);
      setAuction(res.data);

      const itemRes = await API.get(`/api/items/${res.data.itemId}`);
      setItem(itemRes.data);

    } catch (err) {
      console.error(err);
    }
  };

  // ⏳ Timer logic
  const updateTimer = () => {
    const now = new Date().getTime();
    let targetTime;

    if (auction.status === "CREATED") {
      targetTime = new Date(auction.startTime).getTime();
    } else if (auction.status === "ACTIVE") {
      targetTime = new Date(auction.endTime).getTime();
    } else {
      setTimeLeft("Auction Ended");
      return;
    }

    const diff = targetTime - now;

    if (diff <= 0) {
      setTimeLeft("Time Up");
      return;
    }

    const minutes = Math.floor(diff / 60000);
    const seconds = Math.floor((diff % 60000) / 1000);

    setTimeLeft(`${minutes}m ${seconds}s`);
  };

  // 💰 Place bid
  const placeBid = async () => {
    try {
      if (!bidAmount) {
        alert("Enter bid amount");
        return;
      }

      if (isNaN(bidAmount)) {
        alert("Enter valid number");
        return;
      }

      if (Number(bidAmount) <= auction.currentHighestBid) {
        alert("Bid must be higher than current price");
        return;
      }

      await API.post("/api/auctions/bid", {
        auctionId: auction.id,
        userId: user?.id || user?.userId, // supports both formats
        amount: Number(bidAmount),
      });

      alert("Bid Placed!");
      setBidAmount("");
      fetchAuction();

    } catch (err) {
        console.log("Token:", localStorage.getItem("token"));
console.log("Decoded User:", user);
  console.error("FULL ERROR:", err);
  console.error("RESPONSE:", err.response?.data);

  alert(err.response?.data?.message || "Bid Failed");
}
  };

  // ⏳ Loading state
  if (!auction || !item) {
    return <p className="p-6">Loading...</p>;
  }

  return (
    <div className="p-6 bg-gray-100 min-h-screen flex justify-center items-center">
      <div className="bg-white p-6 rounded-xl shadow-lg w-[400px]">

        <h2 className="text-2xl font-bold mb-2">
          {item.title}
        </h2>

        <p className="text-gray-600 mb-2">
          Status: <b>{auction.status}</b>
        </p>

        {/* ⏳ Timer */}
        <p className="text-blue-500 font-semibold mb-4">
          {timeLeft}
        </p>

        {/* 💰 Current Price */}
        <p className="mb-4 text-lg font-bold text-green-600 animate-pulse">
          ₹{auction.currentHighestBid}
        </p>

        {/* 🔒 Status-based UI */}

        {auction.status === "ENDED" && (
          <p className="text-red-500 font-bold">Auction Closed</p>
        )}

        {auction.status === "CREATED" && (
          <p className="text-yellow-500">
            Auction not started yet
          </p>
        )}

        {auction.status === "ACTIVE" && (
          <>
            <input
              placeholder="Enter bid amount"
              value={bidAmount}
              className="w-full p-2 border rounded mb-3"
              onChange={(e) => setBidAmount(e.target.value)}
            />

            <button
              onClick={placeBid}
              className="w-full bg-blue-500 text-white py-2 rounded hover:bg-blue-600 transition"
            >
              Place Bid
            </button>
          </>
        )}
      </div>
    </div>
  );
}
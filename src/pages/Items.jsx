import { useEffect, useState } from "react";
import API from "../api/axios";
import { motion } from "framer-motion";
import { useNavigate } from "react-router-dom";

export default function Items() {
  const [items, setItems] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    fetchItems();
  }, []);
//------------------------------------------------------------------
  const fetchItems = async () => {
    try {
      const res = await API.get("/api/items");
      setItems(res.data);
//       {
//   data: [...],      ACTUAL DATA
//   status: 200,
//   statusText: "OK",
//   headers: {...},
// }
    } catch (err) {
      console.error(err);
    }
  };
  //----------------------------------------------------------------------

  return (
    <div className="p-6 bg-gray-100 min-h-screen">

      <h1 className="text-3xl font-bold mb-6">Dashboard</h1>

      {/* 🔥 NEW BUTTONS */}
      <div className="flex gap-4 mb-6">
        <button
          onClick={() => navigate("/items")}
          className="bg-blue-500 text-white px-4 py-2 rounded"
        >
          Show Items
        </button>

        <button
          onClick={() => navigate("/create-auction")}
          className="bg-green-500 text-white px-4 py-2 rounded"
        >
          Create Auction
        </button>
      </div>

      {/* ITEMS LIST */}
      <div className="grid grid-cols-3 gap-6">
        {items.map((item) => (
          <motion.div
            key={item.id}
            whileHover={{ scale: 1.05 }}
            onClick={() => navigate(`/items/${item.id}`)}
            className="bg-white p-4 rounded-xl shadow cursor-pointer"
          >
            <h2 className="text-xl font-semibold">{item.title}</h2>
            <p>{item.description}</p>
            <p className="text-blue-500 font-bold">₹{item.basePrice}</p>
          </motion.div>
        ))}
      </div>
    </div>
  );
}
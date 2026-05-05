// import { useState, useEffect } from "react";

// export default function SearchFilter({ items, auctions, getItem, onFilter }) {
//   const [category, setCategory] = useState("");
//   const [status, setStatus] = useState("ALL");
//   const [maxPrice, setMaxPrice] = useState(100000);
//   const [sort, setSort] = useState("");

//   useEffect(() => {
//     filterData();
//   }, [category, status, maxPrice, sort, items, auctions]);

//   const filterData = () => {
//     let result = auctions.filter(a => {
//       const item = getItem(a.itemId);
//       if (!item) return false;

//       const matchStatus =
//         status === "ALL" || a.status === status;

//       const matchCategory =
//         !category ||
//         item.category.toLowerCase().includes(category.toLowerCase());

//       const matchPrice = item.basePrice <= maxPrice;

//       return matchStatus && matchCategory && matchPrice;
//     });

//     // 🔽 SORTING
//     if (sort === "LOW_HIGH") {
//       result.sort((a, b) => a.basePrice - b.basePrice);
//     } else if (sort === "HIGH_LOW") {
//       result.sort((a, b) => b.basePrice - a.basePrice);
//     }

//     onFilter(result);
//   };

//   return (
//     <div className="filter-bar">

//   {/* CATEGORY */}
//   <input
//     placeholder="Search category..."
//     onChange={(e) => setCategory(e.target.value)}
//   />

//   {/* PRICE SLIDER */}
//   <div className="filter-slider">
//     <label>Max ₹{maxPrice}</label>
//     <input
//       type="range"
//       min="0"
//       max="100000"
//       value={maxPrice}
//       onChange={(e) => setMaxPrice(e.target.value)}
//     />
//   </div>

//   {/* STATUS */}
//   <div className="filter-buttons">
//     <button onClick={() => setStatus("ALL")}>All</button>
//     <button onClick={() => setStatus("CREATED")}>Created</button>
//     <button onClick={() => setStatus("ACTIVE")}>Active</button>
//     <button onClick={() => setStatus("ENDED")}>Ended</button>
//   </div>

//   {/* SORT */}
//   <select onChange={(e) => setSort(e.target.value)}>
//     <option value="">Sort</option>
//     <option value="LOW_HIGH">Low → High</option>
//     <option value="HIGH_LOW">High → Low</option>
//   </select>

// </div>
//   );
// }
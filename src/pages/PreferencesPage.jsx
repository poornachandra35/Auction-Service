import { useEffect, useState } from "react";
import API from "../api/axios";

export default function PreferencesPage() {
  const userId = localStorage.getItem("userId");

  const [data, setData] = useState({
    preferredCategories: "",
    minBudget: "",
    maxBudget: "",
    location: ""
  });

  // 🔥 LOAD EXISTING
  useEffect(() => {
    API.get(`/api/users/${userId}/preferences`)
      .then(res => {
        setData({
          preferredCategories: res.data.preferredCategories?.join(",") || "",
          minBudget: res.data.minBudget || "",
          maxBudget: res.data.maxBudget || "",
          location: res.data.location || ""
        });
      })
      .catch(() => {});
  }, []);

  // 🔥 SAVE
  const save = async () => {
    try {
      await API.post("/api/users/preferences", {
        preferredCategories: data.preferredCategories.split(","),
        minBudget: Number(data.minBudget),
        maxBudget: Number(data.maxBudget),
        location: data.location
      });

      alert("Preferences saved ✅");
    } catch {
      alert("Failed ❌");
    }
  };

  return (
    <div className="dashboard">
      <h2>User Preferences</h2>

      <input
        placeholder="Categories (comma separated)"
        value={data.preferredCategories}
        onChange={(e) =>
          setData({ ...data, preferredCategories: e.target.value })
        }
      />

      <input
        placeholder="Min Budget"
        type="number"
        value={data.minBudget}
        onChange={(e) =>
          setData({ ...data, minBudget: e.target.value })
        }
      />

      <input
        placeholder="Max Budget"
        type="number"
        value={data.maxBudget}
        onChange={(e) =>
          setData({ ...data, maxBudget: e.target.value })
        }
      />

      <input
        placeholder="Location"
        value={data.location}
        onChange={(e) =>
          setData({ ...data, location: e.target.value })
        }
      />

      <button onClick={save}>Save</button>
    </div>
  );
}
import { useEffect, useState } from "react";
import "../styles/dashboard.css";

import {
  BarChart, Bar, XAxis, YAxis, Tooltip, ResponsiveContainer,
  PieChart, Pie, Cell,
  LineChart, Line,
} from "recharts";

export default function SellerAnalytics() {

  const [data, setData] = useState(null);
  const sellerId = localStorage.getItem("userId");

  useEffect(() => {
    fetch(`http://localhost:9000/analytics/seller/${sellerId}`)
      .then(res => res.json())
      .then(setData);
  }, []);

  if (!data) return <h2>Loading analytics...</h2>;

  // ===== DATA FORMATTERS =====
  const categoryData = Object.entries(data.categoryAnalysis)
    .map(([k, v]) => ({ name: k, value: v }));

  const stateData = Object.entries(data.stateDistribution)
    .map(([k, v]) => ({ name: k, value: v }));

  const timeData = Object.entries(data.timeTrend)
    .map(([k, v]) => ({ hour: k, bids: v }));

  const forecastData = data.revenueForecast.map((v, i) => ({
    step: i,
    value: v
  }));

  const COLORS = ["#4f46e5", "#06b6d4", "#22c55e", "#f59e0b"];

  return (
    <div className="dashboard">

      <h2>📊 Seller Analytics Dashboard</h2>

      {/* ===== KPI CARDS ===== */}
      <div className="analytics-grid">

        <div className="analytics-card">
          <h4>Total Bids</h4>
          <p>{data.summary.totalBids}</p>
        </div>

        <div className="analytics-card">
          <h4>Users</h4>
          <p>{data.summary.uniqueUsers}</p>
        </div>

        <div className="analytics-card">
          <h4>Highest Bid</h4>
          <p>₹ {data.summary.highestBid}</p>
        </div>

        <div className="analytics-card">
          <h4>Performance</h4>
          <p>{data.summary.performanceScore.toFixed(2)}</p>
        </div>

      </div>

      {/* ===== CATEGORY BAR ===== */}
      <div className="chart-box">
        <h3>Category Comparison</h3>

        <ResponsiveContainer width="100%" height={300}>
          <BarChart data={categoryData}>
            <XAxis dataKey="name" />
            <YAxis />
            <Tooltip />
            <Bar dataKey="value" />
          </BarChart>
        </ResponsiveContainer>
      </div>

      {/* ===== STATE PIE ===== */}
      <div className="chart-box">
        <h3>State Distribution</h3>

        <ResponsiveContainer width="100%" height={300}>
          <PieChart>
            <Pie data={stateData} dataKey="value" nameKey="name">
              {stateData.map((_, i) => (
                <Cell key={i} fill={COLORS[i % COLORS.length]} />
              ))}
            </Pie>
            <Tooltip />
          </PieChart>
        </ResponsiveContainer>
      </div>

      {/* ===== TIME TREND ===== */}
      <div className="chart-box">
        <h3>Bidding Time Trend</h3>

        <ResponsiveContainer width="100%" height={300}>
          <LineChart data={timeData}>
            <XAxis dataKey="hour" />
            <YAxis />
            <Tooltip />
            <Line dataKey="bids" />
          </LineChart>
        </ResponsiveContainer>
      </div>

      {/* ===== FORECAST ===== */}
      <div className="chart-box">
        <h3>Revenue Forecast</h3>

        <ResponsiveContainer width="100%" height={300}>
          <LineChart data={forecastData}>
            <XAxis dataKey="step" />
            <YAxis />
            <Tooltip />
            <Line dataKey="value" />
          </LineChart>
        </ResponsiveContainer>
      </div>

      {/* ===== HEATMAP (SIMPLE) ===== */}
      <div className="chart-box">
        <h3>Bidding Heatmap</h3>

        {Object.entries(data.heatmap).map(([hour, auctions]) => (
          <div key={hour} className="heat-row">
            <span>{hour}:00</span>

            {Object.values(auctions).map((v, i) => (
              <div
                key={i}
                className="heat-cell"
                style={{ opacity: Math.min(v / 10, 1) }}
              />
            ))}
          </div>
        ))}
      </div>

      {/* ===== BIDDER RANKING ===== */}
      <div className="chart-box">
        <h3>Top Bidders</h3>

        <table className="analytics-table">
          <thead>
            <tr>
              <th>User Id</th>
                <th>Rank</th>
               <th>Name</th>
             
              <th>Probability</th>
            </tr>
          </thead>

          <tbody>
            {data.bidderPrediction.slice(0, 5).map((b, i) => (
              
              <tr key={i}>
               <center><td>{b.userId}</td></center>
               <td>{b.rank}</td>
               <td>{b.userName}</td>
               
                <td>{(b.probability * 100).toFixed(2)}%</td>
               
              </tr>
            ))}
          </tbody>
        </table>
      </div>

    </div>
  );
}
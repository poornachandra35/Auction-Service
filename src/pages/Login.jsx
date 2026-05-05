import { useState, useRef, useEffect } from "react";
import API from "../api/axios";
import { parseJwt } from "../utils/jwt";

export default function Login() {
  const [data, setData] = useState({ email: "", password: "" });
  const [coins, setCoins] = useState([]);
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);

  const buttonRef = useRef(null);
  const audioRef = useRef(null);

  useEffect(() => {
    audioRef.current = new Audio("/coin.mp3");
    audioRef.current.volume = 0.5;
  }, []);

  const playSound = () => {
    audioRef.current.currentTime = 0;
    audioRef.current.play().catch(() => {});
  };

  const triggerCoins = () => {
    const rect = buttonRef.current.getBoundingClientRect();
    const cx = rect.left + rect.width / 2;
    const cy = rect.top + rect.height / 2;

    const newCoins = Array.from({ length: 12 }, (_, i) => {
      const angle = (i / 12) * 2 * Math.PI;
      return {
        id: Date.now() + i,
        x: cx,
        y: cy,
        dx: Math.cos(angle) * 140,
        dy: Math.sin(angle) * 140
      };
    });

    setCoins(newCoins);
    playSound();

    setTimeout(() => setCoins([]), 1200);
  };

  const login = async () => {
    try {
      setLoading(true);
      const res = await API.post("/api/auth/login", data);

      const token = res.data;
      const decoded = parseJwt(token);

      // ✅ EXISTING
      localStorage.setItem("token", token);
      localStorage.setItem("userId", decoded.userId);

      // ✅ ADDED (based on backend JWT)
      localStorage.setItem("email", decoded.sub);
      localStorage.setItem("role", decoded.role);
      localStorage.setItem("name", decoded.name);

      triggerCoins();

      setTimeout(() => setSuccess(true), 300);
      setTimeout(() => (window.location.href = "/items"), 2200);
    } catch {
      alert("Login Failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-container">
      {!success ? (
        <div className="glass-card form-animate">
          <h2>Welcome Back</h2>

          <input
            placeholder="Email"
            onChange={(e) => setData({ ...data, email: e.target.value })}
          />

          <input
            type="password"
            placeholder="Password"
            onChange={(e) => setData({ ...data, password: e.target.value })}
          />

          <button ref={buttonRef} onClick={login}>
            {loading ? <div className="spinner" /> : "Login"}
          </button>

          <p className="link">
            <a href="/forgot-password">Forgot Password?</a>
          </p>

          <p className="link">
            Don’t have an account? <a href="/register">Register</a>
          </p>
        </div>
      ) : (
        <div className="success-wrapper">
          <div className="checkmark-circle">
            <div className="checkmark"></div>
          </div>
        </div>
      )}

      {coins.map((c) => (
        <div
          key={c.id}
          className="coin"
          style={{
            left: c.x,
            top: c.y,
            "--dx": `${c.dx}px`,
            "--dy": `${c.dy}px`
          }}
        />
      ))}
    </div>
  );
}



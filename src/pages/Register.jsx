import { useState, useRef, useEffect } from "react";
import API from "../api/axios";

export default function Register() {
  const [form, setForm] = useState({
    name: "",
    email: "",
    password: "",
    phone: "",
    role: "BUYER",
    location: "",
    otp: ""
  });

  const [errors, setErrors] = useState({});
  const [otpSent, setOtpSent] = useState(false);
  const [loading, setLoading] = useState(false);
  const [coins, setCoins] = useState([]);
  const [generalError, setGeneralError] = useState("");

  const buttonRef = useRef(null);
  const audioRef = useRef(null);

  // 🔊 preload sound
  useEffect(() => {
    audioRef.current = new Audio("/coin.mp3");
    audioRef.current.volume = 0.5;
  }, []);

  const playSound = () => {
    if (!audioRef.current) return;
    audioRef.current.currentTime = 0;
    audioRef.current.play().catch(() => {});
  };

  // ===== HANDLE INPUT =====
  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setErrors({ ...errors, [e.target.name]: "" });
    setGeneralError("");
  };

  // ===== VALIDATION =====
  const validate = () => {
    let err = {};
    if (!form.name) err.name = "Name required";
    if (!form.email) err.email = "Email required";
    if (!form.password) err.password = "Password required";
    if (!form.phone) err.phone = "Phone required";
    if (!form.role) err.role = "Role required";
    if (!form.location) err.location = "Location required";
    if (otpSent && !form.otp) err.otp = "OTP required";
    return err;
  };

  // ===== ERROR HANDLER =====
  const extractError = (err) => {
    const data = err?.response?.data;
    if (!data) return "Something went wrong";
    if (typeof data === "string") return data;
    if (data.message) return data.message;
    if (data.error) return data.error;
    return "Something went wrong";
  };

  // ===== COIN ANIMATION (FIXED) =====
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
        dx: Math.cos(angle) * (120 + Math.random() * 40),
        dy: Math.sin(angle) * (120 + Math.random() * 40)
      };
    });

    setCoins(newCoins);
    playSound();

    setTimeout(() => setCoins([]), 1200);
  };

  // ===== SEND OTP =====
  const sendOtp = async () => {
    const err = validate();
    if (err.email) return setErrors(err);

    try {
      setLoading(true);
      await API.post("/api/auth/send-otp", { email: form.email });
      setOtpSent(true);
    } catch (err) {
      setGeneralError(extractError(err));
    } finally {
      setLoading(false);
    }
  };

  // ===== REGISTER =====
  const register = async () => {
    const err = validate();
    setErrors(err);
    if (Object.keys(err).length > 0) return;

    try {
      setLoading(true);
      await API.post("/api/auth/register", form);

      triggerCoins(); // ✅ FIXED animation

      setTimeout(() => {
        window.location.href = "/";
      }, 1500);

    } catch (err) {
      setGeneralError(extractError(err));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-container">
      <div className="glass-card form-animate">

        <h2>Create Account</h2>

        {generalError && <p className="error">{generalError}</p>}
 {errors.name && <p className="error">{errors.name}</p>}
        <input name="name" placeholder="Name" onChange={handleChange} />
       
{errors.email && <p className="error">{errors.email}</p>}
        <input name="email" placeholder="Email" onChange={handleChange} />

        {errors.password && <p className="error">{errors.password}</p>}
        <input type="password" name="password" placeholder="Password" onChange={handleChange} />
        {errors.phone && <p className="error">{errors.phone}</p>}
        <input name="phone" placeholder="Phone" onChange={handleChange} />
  {errors.role && <p className="error">{errors.role}</p>}      
<select name="role" value={form.role} onChange={handleChange}>
  <option value="BUYER">Buyer</option>
  <option value="SELLER">Seller</option>
</select>
{errors.location && <p className="error">{errors.location}</p>}
        <input name="location" placeholder="Location" onChange={handleChange} />
        

        {!otpSent ? (
          <button ref={buttonRef} onClick={sendOtp}>
            {loading ? <div className="spinner" /> : "Send OTP"}
          </button>
        ) : (
          <>
            <input name="otp" placeholder="OTP" onChange={handleChange} />
            {errors.otp && <p className="error">{errors.otp}</p>}

            <button ref={buttonRef} onClick={register}>
              {loading ? <div className="spinner" /> : "Register"}
            </button>
          </>
        )}

        <p className="link">
          Already have an account? <a href="/">Login</a>
        </p>
      </div>

      {/* 🪙 COINS */}
      {coins.map((c) => (
        <div
          key={c.id}
          className="coin"
          style={{
            left: `${c.x}px`,
            top: `${c.y}px`,
            "--dx": `${c.dx}px`,
            "--dy": `${c.dy}px`
          }}
        />
      ))}
    </div>
  );
}
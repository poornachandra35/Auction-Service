import { useState } from "react";
import API from "../api/axios";

export default function ForgotPassword() {
  const [step, setStep] = useState(1);
  const [data, setData] = useState({
    email: "",
    otp: "",
    newPassword: ""
  });

  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState("");

  const handleChange = (e) => {
    setData({ ...data, [e.target.name]: e.target.value });
    setError("");
  };

  const sendOtp = async () => {
    if (!data.email) return setError("Email is required");

    try {
      setLoading(true);
      await API.post("/api/auth/send-otp", { email: data.email });
      setStep(2);
    } catch {
      setError("Failed to send OTP");
    } finally {
      setLoading(false);
    }
  };

  const verifyOtp = async () => {
    if (!data.otp) return setError("OTP is required");

    try {
      setLoading(true);
      await API.post("/api/auth/verify-otp", {
        email: data.email,
        otp: data.otp
      });
      setStep(3);
    } catch {
      setError("Invalid OTP");
    } finally {
      setLoading(false);
    }
  };

  const resetPassword = async () => {
    if (!data.newPassword) return setError("Password required");

    try {
      setLoading(true);
      await API.post("/api/auth/reset-password", data);

      setSuccess(true);

      setTimeout(() => {
        window.location.href = "/";
      }, 1800);
    } catch {
      setError("Reset failed");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-container">
      {!success ? (
        <div className="glass-card form-animate">
          <h2>Reset Password</h2>

          {step === 1 && (
            <>
              <input
                name="email"
                placeholder="Email"
                onChange={handleChange}
              />
              <button onClick={sendOtp}>
                {loading ? <div className="spinner" /> : "Send OTP"}
              </button>
            </>
          )}

          {step === 2 && (
            <>
              <input
                name="otp"
                placeholder="OTP"
                onChange={handleChange}
              />
              <button onClick={verifyOtp}>
                {loading ? <div className="spinner" /> : "Verify OTP"}
              </button>
            </>
          )}

          {step === 3 && (
            <>
              <input
                type="password"
                name="newPassword"
                placeholder="New Password"
                onChange={handleChange}
              />
              <button onClick={resetPassword}>
                {loading ? <div className="spinner" /> : "Reset Password"}
              </button>
            </>
          )}

          {error && <p className="error">{error}</p>}

          <p className="link">
            <a href="/">Back to Login</a>
          </p>
        </div>
      ) : (
        <div className="success-wrapper">
          <div className="checkmark-circle">
            <div className="checkmark"></div>
          </div>
        </div>
      )}
    </div>
  );
}
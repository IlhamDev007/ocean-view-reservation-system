// OceanView small UX helpers
document.addEventListener("DOMContentLoaded", () => {
  // Auto-hide alert messages after 4 seconds
  document.querySelectorAll(".alert").forEach(a => {
    setTimeout(() => a.style.display = "none", 4000);
  });
});
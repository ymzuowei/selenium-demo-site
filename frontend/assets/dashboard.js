// Check login (auth_token cookie)
window.onload = () => {
  const token = document.cookie.split("; ").find(row => row.startsWith("auth_token="));
  if (!token) {
    window.location.href = "/login.html";
  }
};

// Sidebar toggle
const sidebar = document.getElementById('sidebar');
const toggleBtn = document.getElementById('sidebarToggle');
toggleBtn.addEventListener('click', () => {
  sidebar.classList.toggle('collapsed');
});

// Card expand/collapse
document.querySelectorAll('.card-header').forEach(header => {
  header.addEventListener('click', () => {
    const content = header.nextElementSibling;
    content.classList.toggle('show');

    // Rotate arrow
    const arrow = header.querySelector('.arrow');
    arrow.classList.toggle('down');
  });
});

// User menu toggle
const userBtn = document.getElementById('userBtn');
const userDropdown = document.getElementById('userDropdown');
userBtn.addEventListener('click', () => {
  userDropdown.classList.toggle('show');
});

// Logout button logic
document.getElementById('logoutBtn').addEventListener('click', () => {
  document.cookie = "auth_token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
  window.location.href = "/login.html";
});

// Settings button (just example alert)
document.getElementById('settingsBtn').addEventListener('click', () => {
  alert("Settings clicked!");
});

// Close user dropdown if click outside
document.addEventListener('click', e => {
  if (!userBtn.contains(e.target) && !userDropdown.contains(e.target)) {
    userDropdown.classList.remove('show');
  }
});
document.getElementById('loginForm').addEventListener('submit', async function(e) {
  e.preventDefault();
  document.getElementById('msg').textContent = '';
  document.getElementById('error').textContent = '';

  const email = document.getElementById('email').value;
  const password = document.getElementById('password').value;

  try {
    const response = await fetch('https://reqres.in/api/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'x-api-key': 'reqres-free-v1'
      },
      body: JSON.stringify({ email, password })
    });

    const data = await response.json();

    if (response.ok && data.token) {
      // Save token in cookie (expire in 1 day)
      document.cookie = `auth_token=${data.token}; path=/; max-age=${60 * 60 * 24}`;
      document.getElementById('msg').textContent = 'Login successful!';
    } else {
      document.getElementById('error').textContent = data.error || 'Login failed';
    }
  } catch (err) {
    document.getElementById('error').textContent = 'Network error';
  }
});
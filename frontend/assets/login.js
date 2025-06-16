document.getElementById('loginForm').addEventListener('submit', function(e){
  e.preventDefault();
  const u=document.getElementById('username').value;
  const p=document.getElementById('password').value;
  if(u==='demo' && p==='123456'){
    document.cookie='auth=1';
    window.location='dashboard.html';
  } else {
    document.getElementById('error').innerText='Invalid credentials';
  }
});

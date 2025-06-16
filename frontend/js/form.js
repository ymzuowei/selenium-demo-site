document.addEventListener('DOMContentLoaded', function () {
  const form = document.getElementById('regForm');
  if (form) {
    form.addEventListener('submit', handleSubmit);
  }
});

let handleSubmit = function(e){
  e.preventDefault();
  let valid=true;
  const email=document.getElementById('email').value;
  const phone=document.getElementById('phone').value;
  if(!email.includes('@')){
    document.getElementById('emailErr').innerText='Invalid email';
    valid=false;
  }
  if(phone.length<10){
    document.getElementById('phoneErr').innerText='Phone too short';
    valid=false;
  }
  if(valid){
    document.getElementById('msg').innerText='Submitted!';
  }
};
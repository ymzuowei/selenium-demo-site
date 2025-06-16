document.addEventListener('DOMContentLoaded', function () {
  const form = document.getElementById('regForm');
  if (form) {
    form.addEventListener('submit', handleSubmit);
  }
});

let handleSubmit = function(e) {
  e.preventDefault();
  let valid = true;

  const email = document.getElementById('email').value;
  const phone = document.getElementById('phone').value;
  const country = document.getElementById('country').value;
  const agree = document.getElementById('agree').checked;
  const gender = document.querySelector('input[name="gender"]:checked');

  // Reset error messages
  document.getElementById('emailErr').innerText = '';
  document.getElementById('phoneErr').innerText = '';
  document.getElementById('countryErr').innerText = '';
  document.getElementById('agreeErr').innerText = '';
  document.getElementById('genderErr').innerText = '';
  document.getElementById('msg').innerText = '';

  // Email validation
  if (!email.includes('@')) {
    document.getElementById('emailErr').innerText = 'Invalid email';
    valid = false;
  }

  // Phone validation
  if (phone.length < 10) {
    document.getElementById('phoneErr').innerText = 'Phone too short';
    valid = false;
  }

  // Country selection
  if (country === '') {
    document.getElementById('countryErr').innerText = 'Please select a country';
    valid = false;
  }

  // Agreement checkbox
  if (!agree) {
    document.getElementById('agreeErr').innerText = 'You must agree to terms';
    valid = false;
  }

  // Gender selection
  if (!gender) {
    document.getElementById('genderErr').innerText = 'Select a gender';
    valid = false;
  }

  // Final submit
  if (valid) {
    document.getElementById('msg').innerText = 'Submitted!';
  }
};

function checkEmailUnique() {
  const email = document.getElementById("email").value;
  const msgElem = document.getElementById("emailStatus");
  msgElem.innerText = "Checking...";

  // Simulate AJAX call
  setTimeout(() => {
    if (email === "taken@example.com") {
      msgElem.innerText = "Email already registered";
    } else {
      msgElem.innerText = "Email available";
    }
  }, 1500); // simulate network delay
}

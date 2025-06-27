# Selenium Demo Site

A personal project to showcase automation testing capabilities using Selenium, JUnit 5, and modern web development practices.

---

## ✨ Highlights

* Full end-to-end test coverage for an e-commerce-like frontend
* Responsive layout testing (desktop and mobile)
* Cross-browser automation including 360 Secure Browser (Chrome-based)
* GitHub Actions CI for Docker-based test execution
* Page Object Model with reusable base classes and utilities

---

## 📚 Project Structure

```
selenium-demo-site/
├── frontend/                  # Static HTML/CSS/JS frontend
│   ├── cart.html              # Shopping cart with quantity editing
│   ├── checkout.html          # Checkout process with address & payment
│   ├── confirmation.html      # Order summary and finalization
│   ├── dashboard.html         # Protected dashboard with sidebar/cards
│   ├── forms/form.html        # Registration form with validation
│   ├── login.html             # Login with reqres API auth
│   └── product.html           # Product listing page
│
├── tests/                    # Selenium + JUnit test cases
│   └── src/test/java/com/demo
│       ├── base/              # BaseWebTest.java, config, driver management
│       ├── pages/             # Page Object classes (LoginPage, CartPage...)
│       ├── tests/             # Test classes (LoginTests, CartTests...)
│       └── util/              # ScreenshotUtil, ImageAssertions, etc.
│
├── .github/workflows/ci.yml  # GitHub Actions pipeline
├── document/slides.html      # Tailwind-based presentation
└── README.md                 # —→ YOU ARE HERE
```

---

## 🚀 Features Tested

* **Login Flow:** Auth with [reqres.in](https://reqres.in/) API, cookie storage (`auth_token`)
* **Form Validation:** Inputs for email, phone, gender, country, agreement
* **Dashboard Interactions:** Sidebar toggle, card expand/collapse, logout
* **Product Listing:** Lazy images, video elements, downloadable PDFs
* **Shopping Cart:** Add/update/remove items, persistent localStorage cart
* **Checkout:** Address + payment method selection, order confirmation
* **Responsive UI:** Tested at desktop (1280x960) and mobile (375x667)

---

## ⚖️ Testing Stack

* **Selenium WebDriver** with **JUnit 5**
* **Page Object Model** design for maintainable code
* **BaseWebTest.java** for setup (Chrome, headless, mobile, screenshots)
* **Custom Utilities:**

  * `ScreenshotUtil`: Screenshots on failure or step
  * `ImageAssertions`: Lazy image checks
  * `FileAssertions`: Valid download link validation
* **360 Secure Browser Support:**

  * Via setting binary path to v132 Chrome-based binary

---

## 📅 CI/CD via GitHub Actions

* Triggers on `push`
* Docker builds the frontend
* Starts a local container (port 8080)
* Installs Chrome and compatible ChromeDriver
* Executes all Selenium tests via `mvn test`

```yaml
- name: Build frontend Docker image
  run: docker build -t demo-site frontend

- name: Run frontend container
  run: docker run -d -p 8080:80 demo-site

- name: Run Selenium tests
  run: cd tests && mvn test
```

---

## 🔎 Example Test Case

```java
@Test
public void testSingleItemSubtotalCalculation() {
    productPage.addToCart(0);
    handleAlertIfPresent();
    cartPage.open();
    WebElement row = cartPage.getCartRows().get(0);
    double price = cartPage.getPrice(row);
    double subtotal = cartPage.getSubtotal(row);
    int qty = cartPage.getSelectedQuantity(row);
    assertEquals(qty * price, subtotal, 0.01);
}
```

---

## 📊 Planned Enhancements

* Add payment integration (Stripe/PayPal test mode)
* Expand real-device mobile browser testing (e.g. Android/iOS)
* Automated test reporting (HTML, Markdown summary)
* CI migration to Jenkins/GitLab for advanced pipelines
* Add Lighthouse or JMeter-based performance tests
* Add API-level tests for login, checkout, etc.
* Accessibility (a11y) test integration

---

## 📄 License

This project is released under the **MIT License**. It is free and open-source—feel free to use, modify, and share it without restriction.

---

## 👨‍💻 Author

This project serves as a demonstration of modern QA practices using browser automation, CI/CD, and modular design for scalable web testing.

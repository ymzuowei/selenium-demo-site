# selenium-demo-site

A comprehensive demonstration project showcasing robust automation testing using Selenium, featuring:

* **Login Validation:** Secure access with API integration, cookie/session handling, and error management.

* **Form Validation:** Real-time feedback, email uniqueness checks, and comprehensive input validation (email, phone, country, agreement, gender).

* **Dashboard Interactions:** Dynamic sidebar, collapsible cards, and interactive user menu with logout functionality.

* **Product & E-commerce Flow:** Product display (images, videos, manuals), shopping cart (add, remove, update items, subtotal/total calculation, persistence), and a complete checkout process to order confirmation.

* **Responsive Layout:** Ensuring optimal viewing and usability across desktop and mobile viewports.

* **Key Achievement: Targeted Browser Compatibility (360 Browser)**

## Technologies & Methodology

This project utilizes modern tools and best practices for high-quality test automation:

* **Selenium WebDriver:** For browser automation.

* **JUnit 5:** For reliable and repeatable test execution.

* **Page Object Model (POM):** Implemented for maintainable, scalable, and reusable test code by abstracting page elements and interactions into dedicated classes (e.g., `LoginPage.java`, `CartPage.java`, `ProductPage.java`).

* **Dedicated Test Base Classes:** `BaseTest.java` and `BaseMobileTest.java` provide consistent browser setup (including specific configuration for 360 Browser on desktop) and automatic screenshot capture.

* **Custom Utilities:** Helper classes like `ScreenshotUtil`, `ImageAssertions`, and `FileAssertions` enhance test capabilities.

* **Docker:** For one-step, reproducible deployment of the frontend application, ensuring consistent test environments.

* **GitHub Actions:** For automated Continuous Integration/Continuous Deployment (CI/CD), triggering tests on every code push and providing immediate feedback.

## Quick Start

Follow these steps to get the demo site running and tests executing:

1.  **Build Docker Image:**

    ```bash
    docker build -t demo-site frontend
    ```

2.  **Run Docker Container:**

    ```bash
    docker run -d -p 8080:80 demo-site
    ```

3.  **Run Tests (Java/Maven):**

    * Ensure you have Java, Maven, and ChromeDriver (configured for 360 Browser if applicable) installed.

    * Navigate to the `tests` directory:

        ```bash
        cd tests
        ```

    * Execute tests:

        ```bash
        mvn test
        ```

4.  **Automated CI:** Pushing code to GitHub will automatically trigger the configured CI tests via GitHub Actions.

Once set up, the project effectively demonstrates robust login failure handling, comprehensive form validation, and adaptive responsive page layouts.

## Future Improvements (Roadmap)

To further scale and enhance the automation testing capabilities:

* **Expand Payment Gateway Testing:** Introduce dedicated integration tests for external payment services and dynamic promotions/discount codes.

* **Broader Cross-Browser & Device Testing:** Extend test coverage beyond the currently configured browsers to include Firefox, Safari, Edge, and various mobile device emulations or real devices.

* **Integrate with Advanced CI/CD Tools:** Transition to more sophisticated CI/CD platforms like Jenkins or GitLab CI for enhanced reporting and parallel test execution.

* **Automated Testing Report Functionality:** Implement tools or custom solutions to generate comprehensive and easily digestible test reports.

* **Performance Testing Integration:** Add basic performance checks using tools like Lighthouse or JMeter to identify bottlenecks.

* **Accessibility (A11y) Testing:** Incorporate automated accessibility checks (e.g., using Axe-core) for enhanced inclusivity.

* **API Testing:** Implement dedicated API tests for backend services to improve test execution speed and robustness.
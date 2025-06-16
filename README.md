# selenium-demo-site

A demonstration project combining frontend and Selenium Java, featuring:

- Login validation, cookie/session handling, form validation, and responsive layout  
- Automated testing using Selenium Page Object + JUnit  
- One-step deployment and testing with Docker + GitHub Actions  

## Quick Start

1. `docker build -t demo-site frontend`  
2. `docker run -d -p 8080:80 demo-site`  
3. Install Java/Maven/ChromeDriver, then run `cd tests && mvn test`  
4. Push to GitHub to automatically trigger CI tests  

Once set up, it demonstrates login failure handling, form validation, and responsive page layouts.

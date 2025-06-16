# selenium-demo-site

前端+Selenium Java 演示项目，包括：
- 登录校验、cookie session、表单验证、响应式支持
- Selenium Page Object + JUnit 自动化测试
- Docker + GitHub Actions 一键部署与测试

## 快速开始
1. \`docker build -t demo-site frontend\`
2. \`docker run -d -p 8080:80 demo-site\`
3. 安装 Java/Maven/ChromeDriver，运行 \`cd tests && mvn test\`
4. 推送至 GitHub 后自动触发 CI 测试

成功后可展示登录失败、表单校验、页面响应式布局等


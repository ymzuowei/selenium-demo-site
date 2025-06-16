package com.demo.pages;
import org.openqa.selenium.*;
public class LoginPage {
  WebDriver driver;
  By user=By.id("username"), pass=By.id("password"), submit=By.tagName("button"), error=By.id("error");
  public LoginPage(WebDriver d){driver=d;}
  public void login(String u, String p){driver.findElement(user).sendKeys(u);driver.findElement(pass).sendKeys(p);driver.findElement(submit).click();}
  public String getError(){return driver.findElement(error).getText();}
}

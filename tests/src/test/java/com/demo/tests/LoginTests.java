package com.demo.tests;
import com.demo.base.BaseTest;
import com.demo.pages.LoginPage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class LoginTests extends BaseTest {
  @Test public void testInvalidLogin(){
    driver.get("http://localhost:8080/login.html");
    LoginPage lp=new LoginPage(driver);
    lp.login("wrong","wrong");
    assertEquals("Invalid credentials", lp.getError());
  }
}

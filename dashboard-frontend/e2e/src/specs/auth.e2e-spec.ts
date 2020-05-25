import { LoginPage } from "../pages/login.po";
import { browser, ExpectedConditions } from 'protractor';

describe('Login component: ', () => {  
  let loginPO: LoginPage;

  beforeAll(() => {
    loginPO = new LoginPage();
  });

  it('invalid login', () => {    
    loginPO.navigateTo("home");
    expect(browser.getCurrentUrl()).toEqual("http://localhost:4200/login?returnUrl=%2Fhome");
    loginPO.getLoginInput().sendKeys('wronglogin');
    loginPO.getPasswordInput().sendKeys('password');
    loginPO.getKeyInput().sendKeys('395a4f34a0fec5df4b644cc480c8d861');
    loginPO.getLoginConfirmButton().click();
    
    expect(browser.getCurrentUrl()).not.toEqual("http://localhost:4200/home");
    expect(loginPO.getLogoutButton().isPresent()).toBeFalsy();
    expect(loginPO.getWrongCredentialsText().isPresent()).toBeTruthy();    
  });

  it('invalid password', () => {
    loginPO.navigateTo("home");
    expect(browser.getCurrentUrl()).toEqual("http://localhost:4200/login?returnUrl=%2Fhome");
    loginPO.getLoginInput().sendKeys('login');
    loginPO.getPasswordInput().sendKeys('wrongpassword');
    loginPO.getKeyInput().sendKeys('395a4f34a0fec5df4b644cc480c8d861');
    loginPO.getLoginConfirmButton().click();
    
    expect(browser.getCurrentUrl()).not.toEqual("http://localhost:4200/home");
    expect(loginPO.getLogoutButton().isPresent()).toBeFalsy();
    expect(loginPO.getWrongCredentialsText().isPresent()).toBeTruthy();
  });

  it('invalid key', () => {
    loginPO.navigateTo("home");
    expect(browser.getCurrentUrl()).toEqual("http://localhost:4200/login?returnUrl=%2Fhome");
    loginPO.getLoginInput().sendKeys('login');
    loginPO.getPasswordInput().sendKeys('password');
    loginPO.getKeyInput().sendKeys('21aa4f34a0fec5df4b644cc480c8da37');
    loginPO.getLoginConfirmButton().click();
    
    expect(browser.getCurrentUrl()).not.toEqual("http://localhost:4200/home");
    expect(loginPO.getLogoutButton().isPresent()).toBeFalsy();
    expect(loginPO.getWrongCredentialsText().isPresent()).toBeTruthy();
  });

  it('successfull login', async () => {
    loginPO.navigateTo("home");
    expect(browser.getCurrentUrl()).toEqual("http://localhost:4200/login?returnUrl=%2Fhome");    
    loginPO.getLoginInput().sendKeys('login');
    loginPO.getPasswordInput().sendKeys('password');
    loginPO.getKeyInput().sendKeys('395a4f34a0fec5df4b644cc480c8d861');
    loginPO.getLoginConfirmButton().click();
    
    expect(browser.getCurrentUrl()).toEqual("http://localhost:4200/home");
    expect(loginPO.getLogoutButton().isPresent()).toBeTruthy();
    expect(loginPO.getWrongCredentialsText().isPresent()).toBeFalsy();
    let token = await browser.executeScript("return window.localStorage.getItem(\"token\");");
    expect(token).not.toBeNull();
  });

  it('logout', async () => {
    loginPO.navigateTo("home");
    browser.wait(ExpectedConditions.elementToBeClickable(loginPO.getLogoutButton()));
    loginPO.getLogoutButton().click();
    expect(loginPO.getLogoutButton().isPresent()).toBeFalsy();
    
    let token = await browser.executeScript("return window.localStorage.getItem(\"token\");");
    expect(token).toBeNull();
    expect(browser.getCurrentUrl()).toEqual("http://localhost:4200/login");
  });
});

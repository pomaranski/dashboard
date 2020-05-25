import { LoginPage } from "../pages/login.po";
import { InstancesPage } from "../pages/instances.po";
import { browser, ExpectedConditions } from 'protractor';

describe('Instances CRUD operations: ', () => {  
  let loginPO: LoginPage;
  let instancesPO: InstancesPage;

  beforeAll(() => {
    loginPO = new LoginPage();
    instancesPO = new InstancesPage();
    
    instancesPO.navigateTo("home");
    loginPO.logIn();
  });

  it('should add instance', () => {        
    browser.wait(ExpectedConditions.elementToBeClickable(instancesPO.getAddInstanceButton()), 5000);
    instancesPO.getAddInstanceButton().click();

    instancesPO.getNameInput().sendKeys('TestInstance');
    instancesPO.getHttpUriInput().sendKeys('http://instanceuri.com');
    instancesPO.getInstanceLoginInput().sendKeys('instancelogin');
    instancesPO.getInstancePasswordInput().sendKeys('instancepassword');
    instancesPO.getSSHUriInput().sendKeys('ssh://user@0.0.0.0');
    instancesPO.getHostLoginInput().sendKeys('ssh-user');
    instancesPO.getHostPasswordInput().sendKeys('sshpassword');
    instancesPO.getSubmitInstanceButton().click();

    expect(instancesPO.getByDataAutomation("TestInstance").isPresent()).toBeTruthy();
    expect(browser.getCurrentUrl()).toEqual("http://localhost:4200/home");
  });

  it('should delete instance', () => {    
    instancesPO.navigateTo("home");

    browser.wait(ExpectedConditions.elementToBeClickable(instancesPO.getRemoveInstanceButton("TestInstance")));
    instancesPO.getRemoveInstanceButton("TestInstance").click();

    browser.wait(ExpectedConditions.elementToBeClickable(instancesPO.getConfirmRemovalButton()));
    browser.sleep(500);
    instancesPO.getConfirmRemovalButton().click();

    expect(instancesPO.getByDataAutomation("TestInstance").isPresent()).toBeFalsy();
  });

  afterAll(() => {
    loginPO.logOut();
  });
});

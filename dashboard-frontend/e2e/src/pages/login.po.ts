import { BasePage } from "./base.po";
import { by, element, ElementFinder, browser } from 'protractor';

export class LoginPage extends BasePage {    

  getLoginInput(): ElementFinder {
      return this.getByDataAutomation("login-input");
  }

  getPasswordInput(): ElementFinder {
      return this.getByDataAutomation("password-input");
  }

  getKeyInput(): ElementFinder {
      return this.getByDataAutomation("key-input");
  }

  getWrongCredentialsText(): ElementFinder {
      return this.getByDataAutomation("wrong-logincredentials-text");
  }

  getLogoutButton(): ElementFinder {
      return this.getByDataAutomation("logout-button");
  }

  getLoginConfirmButton(): ElementFinder {
      return this.getByDataAutomation("submit-login-button");
  }

  login(): void {
    this.getLoginInput().sendKeys('login');
    this.getPasswordInput().sendKeys('password');
    this.getKeyInput().sendKeys('395a4f34a0fec5df4b644cc480c8d861');
    this.getLoginConfirmButton().click();
  }
}

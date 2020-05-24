import { BasePage } from './base.po'
import { element, by, ElementFinder, ElementArrayFinder } from 'protractor';

export class InstancesPage extends BasePage {
    getAddInstanceButton(): ElementFinder {
        return this.getByDataAutomation("add-instance-button");
    }

    getNameInput(): ElementFinder {
        return this.getByDataAutomation("name-input");
    }

    getHttpUriInput(): ElementFinder {
        return this.getByDataAutomation("http-uri-input");
    }

    getInstanceLoginInput(): ElementFinder {
        return this.getByDataAutomation("instance-login-input");
    }

    getInstancePasswordInput(): ElementFinder {
        return this.getByDataAutomation("instance-password-input");
    }

    getSSHUriInput(): ElementFinder {
        return this.getByDataAutomation("ssh-uri-input");
    }

    getHostLoginInput(): ElementFinder {
        return this.getByDataAutomation("host-login-input");
    }

    getHostPasswordInput(): ElementFinder {
        return this.getByDataAutomation("host-password-input");
    }

    getSubmitInstanceButton(): ElementFinder {
        return this.getByDataAutomation("submit-instance-button");
    }

    getRemoveInstanceButton(name: string): ElementFinder {
        return this.getByDataAutomation(name).element(by.css('[data-automation = "remove-button"]'));
    }

    getConfirmRemovalButton(): ElementFinder {
        return this.getByDataAutomation("confirm-modal").element(by.css('[data-automation = "confirm-button"'));
    }
}
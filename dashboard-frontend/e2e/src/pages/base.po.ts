import { browser, by, element, ElementFinder } from 'protractor';

export class BasePage {
    navigateTo(url: string): void {
        browser.get(browser.baseUrl + url);
    }

    getByDataAutomation(name: string): ElementFinder {
        return element(by.css('[data-automation = "' + name + '"]'));
    }
}
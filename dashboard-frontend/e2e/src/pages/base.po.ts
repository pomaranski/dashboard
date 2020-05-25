import { browser, by, element, ElementFinder, ExpectedConditions } from 'protractor';

export class BasePage {
    navigateTo(url: string): void {
        browser.get(browser.baseUrl + url);
    }

    getByDataAutomation(name: string): ElementFinder {
        return element(by.css('[data-automation = "' + name + '"]'));
    }

    waitToastDisappear(): void {
        browser.wait(ExpectedConditions.invisibilityOf(element(by.css(".ngx-toastr"))), 6000);
    }
}
import { BasePage } from "./base.po";
import { by, element, ElementFinder, browser, ElementArrayFinder } from 'protractor';

export class AdminPage extends BasePage {

    getUnassignedLink(): ElementFinder {
        return this.getByDataAutomation("unassigned-instances-navigation-link");
    }

    getUnassignedInstances(): ElementArrayFinder {
        return this.getByDataAutomation("unassigned-instances-page").element(by.css("ul")).all(by.css("li"));
    }
}

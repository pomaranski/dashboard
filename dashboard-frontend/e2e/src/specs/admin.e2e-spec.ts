import { LoginPage } from "../pages/login.po";
import { AdminPage } from "../pages/admin.po";
import { browser, ExpectedConditions } from 'protractor';

describe('Admin instances operations: ', () => {  
  let loginPO: LoginPage;
  let adminPO: AdminPage;

  beforeAll(() => {
    loginPO = new LoginPage();
    adminPO = new AdminPage();
    
    adminPO.navigateTo("home");
    loginPO.logIn("admin");
  });

  it('should list all unassigned instances', async () => {
    adminPO.getUnassignedLink().click();
    let no_instances: number = await adminPO.getUnassignedInstances().count();

    expect(no_instances).toEqual(2);
  });

  afterAll(() => {
    loginPO.logOut();
  });
});

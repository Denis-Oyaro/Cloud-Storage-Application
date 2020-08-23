package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.datasource.url=jdbc:h2:mem:test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CloudStorageApplicationCredentialTests {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private String baseUrl;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
        baseUrl = "http://localhost:" + port;

        // register new user
        String firstname = "testFirstName";
        String lastname = "testLastName";
        String username = "testUsername";
        String password = "testPassword";
        driver.get(baseUrl + "/signup");
        SignupPage signupPage = new SignupPage(driver);
        signupPage.registerNewUser(firstname, lastname, username, password);

        // login user
        driver.get(baseUrl + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginUser(username, password);
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void createAndVerifyDisplayCredentialTest() {
        // create credential
        String url = "https://www.test.com";
        String username = "testUsername";
        String password = "pass12345";
        driver.get(baseUrl + "/home");
        HomePage homePage = new HomePage(driver);
        homePage.createCredential(url,username,password);

        // Verify that credential is displayed on home page,
        // and displayed password is encrypted
        driver.get(baseUrl + "/home");
        homePage = new HomePage(driver);
        Assertions.assertTrue(homePage.isCredentialDisplayedAndPasswordEncrypted(url,
                username,password));
    }

    @Test
    public void editExistingCredentialAndVerifyNewChangesTest() {
        //Create credential
        String oldUrl = "https://www.oldUrl.ca";
        String oldUsername = "oldUsername";
        String oldPassword = "oldPassword";
        driver.get(baseUrl + "/home");
        HomePage homePage = new HomePage(driver);
        homePage.createCredential(oldUrl,oldUsername,oldPassword);

        //verify credential viewable and viewed password unencrypted
        driver.get(baseUrl + "/home");
        homePage = new HomePage(driver);
        Assertions.assertTrue(homePage.isCredentialViewableAndPasswordUnencrypted(oldUrl,
                oldUsername,oldPassword));

        //Edit credential
        String newUrl = "https://www.newUrl.ug";
        String newUsername = "newUsername";
        String newPassword = "newPassword";
        driver.get(baseUrl + "/home");
        homePage = new HomePage(driver);
        homePage.editCredentials(oldUrl,oldUsername,oldPassword,newUrl,newUsername,
                newPassword);

        //verify new changes are displayed
        driver.get(baseUrl + "/home");
        homePage = new HomePage(driver);
        Assertions.assertTrue(homePage.isCredentialDisplayedAndPasswordEncrypted(newUrl,
                newUsername,newPassword));
    }

    @Test
    public void deleteExistingCredentialAndVerifyNolongerDisplayedTest() {

        //Create credentials for existing user
        String url1 = "http://url1";
        String username1 = "username1";
        String password1 = "password1";
        String url2 = "http://url2";
        String username2 = "username2";
        String password2 = "password2";
        driver.get(baseUrl + "/home");
        HomePage homePage = new HomePage(driver);
        homePage.createCredential(url1,username1,password1);
        driver.get(baseUrl + "/home");
        homePage = new HomePage(driver);
        homePage.createCredential(url2,username2,password2);

        //verify credential 1 is displayed
        driver.get(baseUrl + "/home");
        homePage = new HomePage(driver);
        Assertions.assertTrue(homePage.isCredentialDisplayedAndPasswordEncrypted(url1,
                username1,password1));

        // delete credential 1
        driver.get(baseUrl + "/home");
        homePage = new HomePage(driver);
        homePage.deleteCredential(url1,username1,password1);

        //verify credential 1 no longer displayed
        driver.get(baseUrl + "/home");
        homePage = new HomePage(driver);
        Assertions.assertFalse(homePage.isCredentialDisplayedAndPasswordEncrypted(url1,
                username1,password1));
    }
}

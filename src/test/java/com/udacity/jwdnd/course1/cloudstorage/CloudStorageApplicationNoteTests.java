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
class CloudStorageApplicationNoteTests {

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
        signupPage.registerNewUser(firstname,lastname,username,password);

        // login user
        driver.get(baseUrl + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginUser(username,password);
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void createAndDisplayNoteTest() {
        // create note
        String noteTitle = "Test title";
        String noteDescription = "Hello, World! This is a test note description.";
        driver.get(baseUrl + "/home");
        HomePage homePage = new HomePage(driver);
        homePage.createNote(noteTitle,noteDescription);

        // Verify that note is displayed on home page
        driver.get(baseUrl + "/home");
        homePage = new HomePage(driver);
        Assertions.assertTrue(homePage.isNoteDisplayed(noteTitle,noteDescription));
    }

    @Test
    public void editExistingNoteAndVerifyNewChangesTest() {
        //Create Note
        String oldNoteTitle = "old note title";
        String oldNoteDescription = "old note description.";
        driver.get(baseUrl + "/home");
        HomePage homePage = new HomePage(driver);
        homePage.createNote(oldNoteTitle,oldNoteDescription);

        //verify old note displayed
        driver.get(baseUrl + "/home");
        homePage = new HomePage(driver);
        Assertions.assertTrue(homePage.isNoteDisplayed(oldNoteTitle,oldNoteDescription));

        //Edit existing note
        String newNoteTitle = "new note title";
        String newNoteDescription = "new note description";
        driver.get(baseUrl + "/home");
        homePage = new HomePage(driver);
        homePage.editNote(oldNoteTitle,newNoteTitle,oldNoteDescription,newNoteDescription);

        //verify new changes are displayed
        driver.get(baseUrl + "/home");
        homePage = new HomePage(driver);
        Assertions.assertTrue(homePage.isNoteDisplayed(newNoteTitle,newNoteDescription));
    }

    @Test
    public void deleteExistingNoteAndVerifyNolongerDisplayedTest() {

        //Create Notes for existing user
        String noteTitle1 = "test title 1";
        String noteDescription1 = "test description 1";
        String noteTitle2 = "test title 2";
        String noteDescription2 = "test description 2";
        driver.get(baseUrl + "/home");
        HomePage homePage = new HomePage(driver);
        homePage.createNote(noteTitle1,noteDescription1);
        driver.get(baseUrl + "/home");
        homePage = new HomePage(driver);
        homePage.createNote(noteTitle2,noteDescription2);

        //verify note 1 displayed
        driver.get(baseUrl + "/home");
        homePage = new HomePage(driver);
        Assertions.assertTrue(homePage.isNoteDisplayed(noteTitle1,noteDescription1));

        // delete note 1
        driver.get(baseUrl + "/home");
        homePage = new HomePage(driver);
        homePage.deleteNote(noteTitle1,noteDescription1);

        //verify note 1 no longer displayed
        driver.get(baseUrl + "/home");
        homePage = new HomePage(driver);
        Assertions.assertFalse(homePage.isNoteDisplayed(noteTitle1,noteDescription1));
    }
}
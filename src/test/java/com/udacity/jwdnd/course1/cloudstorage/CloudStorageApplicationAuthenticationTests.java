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
class CloudStorageApplicationAuthenticationTests {

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
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void unAuthorizedCanOnlyAccessLoginAndSignupTest() {
		// can access login page
		driver.get(baseUrl + "/login");
		Assertions.assertEquals("Login", driver.getTitle());

		// can access signup page
		driver.get(baseUrl + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());

		// cannot access home page
		driver.get(baseUrl + "/home");
		Assertions.assertNotEquals("Home", driver.getTitle());
	}

	@Test
	public void registeredAndLoggedInUserCanAccessHomeTest() {
		// register new user
		String firstname = "Denis";
		String lastname = "Oyaro";
		String username = "DenOy";
		String password = "testPassword";
		driver.get(baseUrl + "/signup");
		SignupPage signupPage = new SignupPage(driver);
		signupPage.registerNewUser(firstname,lastname,username,password);

		// login user
		driver.get(baseUrl + "/login");
		LoginPage loginPage = new LoginPage(driver);
		loginPage.loginUser(username,password);

		// access home as logged in user
		driver.get(baseUrl + "/home");
		Assertions.assertEquals("Home", driver.getTitle());

		// logout user
		HomePage homePage = new HomePage(driver);
		homePage.logoutUser();

		// cannot access home after logout
		driver.get(baseUrl + "/home");
		Assertions.assertNotEquals("Home", driver.getTitle());

	}

}

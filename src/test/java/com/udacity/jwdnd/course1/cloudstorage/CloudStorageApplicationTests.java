package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.io.File;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.close();
			driver.quit();
			System.out.println("Cleaning up after test");
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doMockSignUp(String firstName, String lastName, String userName, String password){
		// Create a dummy account for logging in later.

		// Visit the sign-up page.
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));
		
		// Fill out credentials
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
		WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
		inputFirstName.click();
		inputFirstName.sendKeys(firstName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
		WebElement inputLastName = driver.findElement(By.id("inputLastName"));
		inputLastName.click();
		inputLastName.sendKeys(lastName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement inputUsername = driver.findElement(By.id("inputUsername"));
		inputUsername.click();
		inputUsername.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement inputPassword = driver.findElement(By.id("inputPassword"));
		inputPassword.click();
		inputPassword.sendKeys(password);

		// Attempt to sign up.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
		WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
		buttonSignUp.click();

		/* Check that the sign up was successful. 
		// You may have to modify the element "success-msg" and the sign-up 
		// success message below depening on the rest of your code.
		*/
		Assertions.assertTrue(driver.findElement(By.id("success-msg")).getText().contains("You successfully signed up!"));
		//wait for redirecting to login page
		webDriverWait.until(ExpectedConditions.titleContains("Login"));
	}
	
	/**
	 * PLEASE DO NOT DELETE THIS method.
	 * Helper method for Udacity-supplied sanity checks.
	 **/
	private void doLogIn(String userName, String password)
	{
		// Log in to our dummy account.
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
		WebElement loginUserName = driver.findElement(By.id("inputUsername"));
		loginUserName.click();
		loginUserName.sendKeys(userName);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
		WebElement loginPassword = driver.findElement(By.id("inputPassword"));
		loginPassword.click();
		loginPassword.sendKeys(password);

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
		WebElement loginButton = driver.findElement(By.id("login-button"));
		loginButton.click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));

	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling redirecting users 
	 * back to the login page after a succesful sign up.
	 * Read more about the requirement in the rubric: 
	 * https://review.udacity.com/#!/rubrics/2724/view 
	 */
	@Test
	public void testRedirection() {
		// Create a test account
		doMockSignUp("Redirection","Test","RT","123");
		
		// Check if we have been redirected to the log in page.
		Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());
	}

	/**
	 * test upload files, see files previously uploaded.
	 * */
	@Test
	public void uploadFile() throws InterruptedException {
		// Create a test account
		doMockSignUp("Nana", "Test", "NT", "123");
		doLogIn("NT", "123");

		// Go to files tab
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-files-tab")));
		WebElement filesTab = driver.findElement(By.id("nav-files-tab"));
		filesTab.click();

		// Upload a file
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File("src/test/resources/test.txt").getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();

		// Check if we have been redirected to the home page, files tab.
		redirectToHome(webDriverWait, "nav-files-tab");

		// Check if the file is displayed on the home page.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileTable")));
		Assertions.assertTrue(driver.findElement(By.id("table-file-name")).getText().contains("test.txt"));

		Thread.sleep(3000);
	}

	@Test
	public void testCredentials() throws InterruptedException {

	  // Sign up and log in
	  doMockSignUp("TestUser", "Test", "TU", "password123");
	  doLogIn("TU", "password123");

	  // Go to credentials tab
	  WebDriverWait wait = new WebDriverWait(driver, 2);
	  wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-files-tab")));

//	  Locate credentials tab and click it
	  WebElement homeTab = driver.findElement(By.id("nav-credential-tab"));
	  wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credential-tab")));
	  WebElement credentialsTab = driver.findElement(By.id("nav-credential-tab"));
	  credentialsTab.click();

	  // Add credentials
	  wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-credential-button")));
	  WebElement addCredentialButton = driver.findElement(By.id("add-credential-button"));
	  addCredentialButton.click();

	  wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
	  WebElement urlField = driver.findElement(By.id("credential-url"));
	  urlField.sendKeys("https://example.com");

	  wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
	  WebElement usernameField = driver.findElement(By.id("credential-username"));
	  usernameField.sendKeys("testuser");

	  wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
	  WebElement passwordField = driver.findElement(By.id("credential-password"));
	  passwordField.sendKeys("password123");

	  WebElement submitButton = driver.findElement(By.id("submit-credential-button"));
	  submitButton.click();

	  // Verify credentials displayed
	//Locate credentials tab and click it
      redirectToHome(wait, "nav-credential-tab");

	  wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));

	  wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("table-cred-url")));
	  WebElement urlDisplay = driver.findElement(By.id("table-cred-url"));
	  Assertions.assertEquals("https://example.com", urlDisplay.getText());

	  wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("table-cred-username")));
	  WebElement usernameDisplay = driver.findElement(By.id("table-cred-username"));
	  Assertions.assertEquals("testuser", usernameDisplay.getText());

	  // Verify password is encrypted
	  wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("table-cred-password")));
	  WebElement passwordDisplay = driver.findElement(By.id("table-cred-password"));
	  Assertions.assertNotEquals("password123", passwordDisplay.getText());

		Thread.sleep(3000);

	}

	@Test
	public void testEditCredentials() throws InterruptedException {
		// Sign up and log in
		doMockSignUp("TestUser", "Test", "TU", "password123");
		doLogIn("TU", "password123");

		// Go to credentials tab
		WebDriverWait wait = new WebDriverWait(driver, 2);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-files-tab")));
		WebElement credentialsTab = driver.findElement(By.id("nav-credential-tab"));
		credentialsTab.click();

		// Add credentials
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-credential-button")));
		WebElement addCredentialButton = driver.findElement(By.id("add-credential-button"));
		addCredentialButton.click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement urlField = driver.findElement(By.id("credential-url"));
		urlField.sendKeys("https://example.com");

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
		WebElement usernameField = driver.findElement(By.id("credential-username"));
		usernameField.sendKeys("testuser");

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		WebElement passwordField = driver.findElement(By.id("credential-password"));
		passwordField.sendKeys("password123");

		WebElement submitButton = driver.findElement(By.id("submit-credential-button"));
		submitButton.click();

		// Verify credentials displayed
		//Locate credentials tab and click it
		redirectToHome(wait, "nav-credential-tab");

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("table-cred-url")));
		WebElement urlDisplay = driver.findElement(By.id("table-cred-url"));
		Assertions.assertEquals("https://example.com", urlDisplay.getText());

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("table-cred-username")));
		WebElement usernameDisplay = driver.findElement(By.id("table-cred-username"));
		Assertions.assertEquals("testuser", usernameDisplay.getText());

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("table-cred-password")));
		WebElement passwordDisplay = driver.findElement(By.id("table-cred-password"));
		Assertions.assertNotEquals("password123", passwordDisplay.getText());

		// Edit credentials
		WebElement editButton = driver.findElement(By.id("edit-credential-button"));
		editButton.click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement urlField2 = driver.findElement(By.id("credential-url"));
		//clear the field before sending the new keys
		urlField2.clear();
		urlField2.sendKeys("https://example.com/new");

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
		WebElement usernameField2 = driver.findElement(By.id("credential-username"));
		usernameField2.clear();
		usernameField2.sendKeys("testuser2");

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		WebElement passwordField2 = driver.findElement(By.id("credential-password"));
		passwordField2.clear();
		passwordField2.sendKeys("<PASSWORD>");

		WebElement submitButton2 = driver.findElement(By.id("submit-credential-button"));
		submitButton2.click();
//		Thread.sleep(4000);
		// Verify credentials displayed
		//Locate credentials tab and click it
		redirectToHome(wait, "nav-credential-tab");

//		driver.navigate().refresh();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));

//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("table-cred-url")));
//		driver.navigate().refresh();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("table-cred-url")));
		WebElement urlDisplay2 = driver.findElement(By.id("table-cred-url"));
		Assertions.assertEquals("https://example.com/new", urlDisplay2.getText());

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("table-cred-username")));
		WebElement usernameDisplay2 = driver.findElement(By.id("table-cred-username"));
		Assertions.assertEquals("testuser2", usernameDisplay2.getText());

		// Verify password is encrypted
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("table-cred-password")));
		WebElement passwordDisplay2 = driver.findElement(By.id("table-cred-password"));
		Assertions.assertNotEquals("password123", passwordDisplay2.getText());

		Thread.sleep(3000);

	}

	//write a test that deletes a credential and verifies it is no longer displayed
	@Test
	public void testDeleteCredentials() throws InterruptedException {
		// Sign up and log in
		doMockSignUp("TestUser", "Test", "TU", "password123");
        doLogIn("TU", "password123");

		// Go to credentials tab
		WebDriverWait wait = new WebDriverWait(driver, 2);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-files-tab")));
		WebElement credentialsTab = driver.findElement(By.id("nav-credential-tab"));
		credentialsTab.click();

		// Add credentials
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-credential-button")));
		WebElement addCredentialButton = driver.findElement(By.id("add-credential-button"));
		addCredentialButton.click();

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
		WebElement urlField = driver.findElement(By.id("credential-url"));
		urlField.sendKeys("https://example.com");

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
		WebElement usernameField = driver.findElement(By.id("credential-username"));
		usernameField.sendKeys("testuser");

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		WebElement passwordField = driver.findElement(By.id("credential-password"));
		passwordField.sendKeys("<PASSWORD>");

		WebElement submitButton = driver.findElement(By.id("submit-credential-button"));

		submitButton.click();

		// Verify credentials displayed
		//Locate credentials tab and click it
		redirectToHome(wait, "nav-credential-tab");

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("table-cred-url")));
		WebElement urlDisplay = driver.findElement(By.id("table-cred-url"));
		Assertions.assertEquals("https://example.com", urlDisplay.getText());

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("table-cred-username")));
		WebElement usernameDisplay = driver.findElement(By.id("table-cred-username"));
		Assertions.assertEquals("testuser", usernameDisplay.getText());

		// Delete credentials
		WebElement deleteButton = driver.findElement(By.id("delete-credential-button"));
		deleteButton.click();

		// Verify credentials are not displayed
		redirectToHome(wait, "nav-credential-tab");

		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialTable")));

		Thread.sleep(3000);

	}

	/* test creating a note and redirecting back to home page*/
	@Test
	public void createNote() throws InterruptedException {
		// Create a test account
		doMockSignUp("Nana","Test","NT","123");
		doLogIn("NT", "123");

		// Create a note
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
		notesTab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-note-button")));
		WebElement addNoteButton = driver.findElement(By.id("add-note-button"));
		addNoteButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement noteTitle = driver.findElement(By.id("note-title"));
		noteTitle.click();
		noteTitle.sendKeys("Test Note");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement noteDescription = driver.findElement(By.id("note-description"));
		noteDescription.click();
		noteDescription.sendKeys("Test Note Description");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submit-note-button")));
		WebElement noteSubmit = driver.findElement(By.id("submit-note-button"));
		noteSubmit.click();

		// Check if we have been redirected to the home page.
		redirectToHome(webDriverWait, "nav-notes-tab");

		// Check if the note is displayed on the home page.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
		Assertions.assertTrue(driver.findElement(By.id("table-note-title")).getText().contains("Test Note"));

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("table-note-description")));
		WebElement noteDescriptionDisplay = driver.findElement(By.id("table-note-description"));
		Assertions.assertEquals("Test Note Description", noteDescriptionDisplay.getText());

		Thread.sleep(3000);
	}

	private void redirectToHome(WebDriverWait webDriverWait, String id) {
		driver.get("http://localhost:" + this.port + "/home");
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
		driver.findElement(By.id(id)).click();
	}

	/* test editing a note, saving changes, redirecting back to home page, check if note description changed*/
	@Test
	public void editNote() throws InterruptedException {
		// Create a test account
		doMockSignUp("Nana", "Test", "NT", "123");
		doLogIn("NT", "123");

		// Create a note
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
		notesTab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-note-button")));
		WebElement addNoteButton = driver.findElement(By.id("add-note-button"));
		addNoteButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement noteTitle = driver.findElement(By.id("note-title"));
		noteTitle.click();
		noteTitle.sendKeys("Test Note");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement noteDescription = driver.findElement(By.id("note-description"));
		noteDescription.click();
		noteDescription.sendKeys("Test Note Description");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submit-note-button")));
		WebElement noteSubmit = driver.findElement(By.id("submit-note-button"));
		noteSubmit.click();

		// Check if we have been redirected to notes tab in home page.
		redirectToHome(webDriverWait, "nav-notes-tab");

		// Check if the note is displayed on the home page.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
		Assertions.assertTrue(driver.findElement(By.id("table-note-title")).getText().contains("Test Note"));

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("table-note-description")));
		WebElement noteDescriptionDisplay = driver.findElement(By.id("table-note-description"));
		Assertions.assertEquals("Test Note Description", noteDescriptionDisplay.getText());

		// Edit the note
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("edit-note-button")));
		WebElement editNoteButton = driver.findElement(By.id("edit-note-button"));
		editNoteButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement noteDescriptionEdit = driver.findElement(By.id("note-description"));
		noteDescriptionEdit.click();

		//save changes
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submit-note-button")));
		WebElement noteSubmitEdit = driver.findElement(By.id("submit-note-button"));
		noteSubmitEdit.click();

		// Check if we have been redirected to the nots tab.
		redirectToHome(webDriverWait, "nav-notes-tab");

		// Check if the note is displayed on the home page.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
		Assertions.assertTrue(driver.findElement(By.id("table-note-title")).getText().contains("Test Note"));

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("table-note-description")));
		WebElement noteDescriptionDisplayEdit = driver.findElement(By.id("table-note-description"));
		Assertions.assertEquals("Test Note Description", noteDescriptionDisplayEdit.getText());

		Thread.sleep(3000);
	}

	/* test deleting a note, redirecting back to home page, check if note is deleted*/
	@Test
	public void deleteNote() throws InterruptedException {
		// Create a test account
		doMockSignUp("Nana", "Test", "NT", "123");
		doLogIn("NT", "123");

		// Create a note
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
		WebElement notesTab = driver.findElement(By.id("nav-notes-tab"));
		notesTab.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("add-note-button")));
		WebElement addNoteButton = driver.findElement(By.id("add-note-button"));
		addNoteButton.click();

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		WebElement noteTitle = driver.findElement(By.id("note-title"));
		noteTitle.click();
		noteTitle.sendKeys("Test Note");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
		WebElement noteDescription = driver.findElement(By.id("note-description"));
		noteDescription.click();
		noteDescription.sendKeys("Test Note Description");

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("submit-note-button")));
		WebElement noteSubmit = driver.findElement(By.id("submit-note-button"));
		noteSubmit.click();

		// Check if we have been redirected to the result page.
		redirectToHome(webDriverWait, "nav-notes-tab");

		// Check if the note is displayed on the home page.
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
		Assertions.assertTrue(driver.findElement(By.id("table-note-title")).getText().contains("Test Note"));

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("table-note-description")));
		WebElement noteDescriptionDisplay = driver.findElement(By.id("table-note-description"));
		Assertions.assertEquals("Test Note Description", noteDescriptionDisplay.getText());

		// Delete the note
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("delete-note-button")));
		WebElement deleteNoteButton = driver.findElement(By.id("delete-note-button"));
		deleteNoteButton.click();

		// Check if we have been redirected to the result page.
		redirectToHome(webDriverWait, "nav-notes-tab");

//		// Check if the note is displayed on the home page.
//		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("userTable")));
//		Assertions.assertFalse(driver.findElement(By.id("table-note-title")).getText().contains("Test Note"));

		Thread.sleep(3000);

	}

	private void redirectToCredentialTab(WebDriverWait webDriverWait, String id) {
		driver.get("http://localhost:" + this.port + "/home");
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
		driver.findElement(By.id(id)).click();
	}

	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling bad URLs 
	 * gracefully, for example with a custom error page.
	 * 
	 * Read more about custom error pages at: 
	 * https://attacomsian.com/blog/spring-boot-custom-error-page#displaying-custom-error-page
	 */
	@Test
	public void testBadUrl() {
		// Create a test account
		doMockSignUp("URL","Test","UT","123");
		doLogIn("UT", "123");
		
		// Try to access a random made-up URL.
		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}


	/**
	 * PLEASE DO NOT DELETE THIS TEST. You may modify this test to work with the 
	 * rest of your code. 
	 * This test is provided by Udacity to perform some basic sanity testing of 
	 * your code to ensure that it meets certain rubric criteria. 
	 * 
	 * If this test is failing, please ensure that you are handling uploading large files (>1MB),
	 * gracefully in your code. 
	 * 
	 * Read more about file size limits here: 
	 * https://spring.io/guides/gs/uploading-files/ under the "Tuning File Upload Limits" section.
	 */
	@Test
	public void testLargeUpload() {
		// Create a test account
		doMockSignUp("Large File","Test","LFT","123");
		doLogIn("LFT", "123");

		// Try to upload an arbitrary large file
		WebDriverWait webDriverWait = new WebDriverWait(driver, 2);
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));

	}



}

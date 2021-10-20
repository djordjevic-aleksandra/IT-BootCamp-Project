package tests;

import java.awt.AWTException;
import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ProfileTest extends BasicTest {

	@Test(priority = 0)
	public void editProfileTest() throws InterruptedException {
		driver.get(loginPageUrl);
		locationPopupPage.closeLocationPopupBox();
		Thread.sleep(2000);
		String email = "customer@dummyid.com";
		String password = "12345678a";
		loginPage.logIn(email, password);
		Assert.assertTrue(notificationSistemPage.returnMessage().contains("Login Successfull"),
				"[ERROR] Message 'Login Successfull' is now shown");
		notificationSistemPage.waitMessageToDispear();
		driver.get(profilePageUrl);
		Thread.sleep(2000);
		profilePage.setUserInformation("John", "Smith", "12", "064123456789", "85087", "United States", "Arizona",
				"Phoenix");
		Assert.assertTrue(notificationSistemPage.returnMessage().contains("Setup Successful"),
				"[ERROR] Message 'Setup Successfull' is not shown");
		notificationSistemPage.waitMessageToDispear();
		authPage.logOut();
		Assert.assertTrue(notificationSistemPage.returnMessage().contains("Logout Successfull!"),
				"[ERROR] Message ''Logot Successfull! is not shown");
		notificationSistemPage.waitMessageToDispear();

	}

	@Test(priority = 1)
	public void changeProfileImageTest() throws InterruptedException, IOException, AWTException {
		driver.get(loginPageUrl);
		locationPopupPage.closeLocationPopupBox();
		Thread.sleep(2000);

		String email = "customer@dummyid.com";
		String password = "12345678a";
		loginPage.logIn(email, password);

		Assert.assertTrue(notificationSistemPage.returnMessage().contains("Login Successfull"),
				"[ERROR] Message 'Login Successfull' is now shown");
		notificationSistemPage.waitMessageToDispear();

		driver.get(profilePageUrl);
		Thread.sleep(2000);

		profilePage.UploadPhoto();

		Assert.assertTrue(notificationSistemPage.returnMessage().contains("Profile Image Uploaded Successfully"),
				"[ERROR] Message 'Profile Image Uploaded Successfully' is not shown");
		notificationSistemPage.waitMessageToDispear();
		// Remove profile picture from user profile
		profilePage.removePhoto();
		// Verify that message 'Profile Image Deleted Successfully' is shown after
		// profile picture is removed successfully
		Assert.assertTrue(notificationSistemPage.returnMessage().contains("Profile Image Deleted Successfully"),
				"[ERROR] Message 'Profile Image Deleted Successfully' is not shown");
		notificationSistemPage.waitMessageToDispear();
	}
}

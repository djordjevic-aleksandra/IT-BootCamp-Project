package tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class MealItemTest extends BasicTest {

	@Test(priority = 0)
	public void addMealToCartTest() throws InterruptedException {
		driver.get(lscqComboUrl);
		locationPopupPage.closeLocationPopupBox();

		mealPage.addMealToTheCart("3");

		Assert.assertTrue(notificationSistemPage.returnMessage().contains("The Following Errors Occurred:"),
				"[ERROR] First error message 'The Following Errors Occurred:' is not shown");
		Assert.assertTrue(notificationSistemPage.returnMessage().contains("Please Select Location"),
				"[ERROR] Second error message ' Please Select Location' is not shown");
		notificationSistemPage.waitMessageToDispear();

		String location = "City Center - Albany";
		locationPopupPage.openLocationPopupBox();
		locationPopupPage.setLocation(location);
		Thread.sleep(1000);

		mealPage.addMealToTheCart("3");

		Assert.assertTrue(notificationSistemPage.returnMessage().contains("Meal Added To Cart"),
				"[ERROR] Message 'Meal Added To Cart' is not shown");
		notificationSistemPage.waitMessageToDispear();
	}

	@Test(priority = 1)
	public void addMealToFavoriteTest() throws InterruptedException {

		driver.get(lscqComboUrl);
		locationPopupPage.closeLocationPopupBox();

		mealPage.addMealToTheFavorites();

		Assert.assertTrue(notificationSistemPage.returnMessage().contains("Please login first!"),
				"[ERROR] Message 'Please login first!' is not shown");
		notificationSistemPage.waitMessageToDispear();

		driver.get(loginPageUrl);

		Thread.sleep(2000);

		String email = "customer@dummyid.com";
		String password = "12345678a";
		loginPage.logIn(email, password);
		notificationSistemPage.waitMessageToDispear();

		driver.get(lscqComboUrl);
		Thread.sleep(500);

		mealPage.addMealToTheFavorites();
		Thread.sleep(1000);
		Assert.assertTrue(notificationSistemPage.returnMessage().contains("Product has been added to your favorites."),
				"[ERROR] User added meal to favorites without login");
		notificationSistemPage.waitMessageToDispear();

	}

	@Test(priority = 2)
	public void clearCartTest() throws InterruptedException, IOException {
		SoftAssert softAssert = new SoftAssert();
		driver.get(mealsPageUrl);
		locationPopupPage.closeLocationPopupBox();
		String location = "City Center - Albany";
		locationPopupPage.openLocationPopupBox();
		locationPopupPage.setLocation(location);
		Thread.sleep(1000);

		File file = new File("data/Data.xlsx");
		FileInputStream fis = new FileInputStream(file);
		XSSFWorkbook wb = new XSSFWorkbook(fis);

		XSSFSheet meals = wb.getSheet("Meals");

		for (int i = 1; i <= meals.getLastRowNum(); i++) {

			String mealUrl = meals.getRow(i).getCell(0).getStringCellValue();

			driver.get(mealUrl);
			mealPage.addMealToTheCart("3");
			softAssert.assertTrue(notificationSistemPage.returnMessage().contains("Meal Added To Cart"),
					"[ERROR] Message 'Meal Added To Cart' is not shown");
			notificationSistemPage.waitMessageToDispear();

		}
		softAssert.assertAll();
		Thread.sleep(1000);
		cartSummaryPage.clearAllFromTheCart();
		Assert.assertTrue(notificationSistemPage.returnMessage().contains("All meals removed from Cart successfully"),
				"[ERROR] Message'All meals removed from Cart successfully' is not shown");
		notificationSistemPage.waitMessageToDispear();
		wb.close();
	}
}

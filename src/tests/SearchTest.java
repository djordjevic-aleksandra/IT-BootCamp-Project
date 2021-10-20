package tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class SearchTest extends BasicTest {

	@Test(priority = 0)
	public void searchResultsTest() throws InterruptedException, IOException {
		SoftAssert softAssert = new SoftAssert();

		driver.get(mealsPageUrl);
		// Set location "City Center - Albany"
		String location = "City Center - Albany";
		locationPopupPage.setLocation(location);
		Thread.sleep(2000);

		DataFormatter formatter = new DataFormatter();
		File file = new File("data/Data.xlsx");
		FileInputStream fis = new FileInputStream(file);
		XSSFWorkbook wb = new XSSFWorkbook(fis);
		XSSFSheet MealSearchResults = wb.getSheet("Meal Search Results");

		for (int i = 1; i <= 6; i++) {

			// Locations and URls from the 'Data.xlsx' file
			String locationFromData = MealSearchResults.getRow(i).getCell(0).getStringCellValue();
			String mealUrls = MealSearchResults.getRow(i).getCell(1).getStringCellValue();

			// Open URLs from the 'Data.xlsx' file
			driver.get(mealUrls);
			Thread.sleep(5000);
			// Open location pop up box and set location from the 'Data.xlsx' file
			locationPopupPage.openLocationPopupBox();
			Thread.sleep(5000);
			locationPopupPage.setLocation(locationFromData);
			Thread.sleep(2000);
			// Number of result data from the 'Data.xlsx' file
			String numberOfTheResultsData = formatter.formatCellValue(MealSearchResults.getRow(i).getCell(2));

			softAssert.assertEquals(searchResultPage.returnNumberOfTheResults(), numberOfTheResultsData,
					"[ERROR] The number of results is not  the same as the number of results from the 'Data.xlsx' file");
			Thread.sleep(1000);

			for (int j = 0; j < searchResultPage.getResult().size(); j++) {
				String name = formatter.formatCellValue(MealSearchResults.getRow(i).getCell(j + 3));

				softAssert.assertTrue(searchResultPage.getNamesOfMeals().get(j).contains(name),
						"[ERROR] The names of results are not  the same as the names of results from the 'Data.xlsx' file");
			}

		}
		softAssert.assertAll();
		wb.close();

	}

}

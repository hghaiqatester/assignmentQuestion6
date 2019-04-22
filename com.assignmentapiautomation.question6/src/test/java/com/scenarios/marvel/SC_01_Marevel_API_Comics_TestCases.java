package com.scenarios.marvel;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.Matchers.containsString;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.equalTo;

public class SC_01_Marevel_API_Comics_TestCases {

	public static Properties prop;
	String response;
	int statusCode;

	@BeforeTest
	public void configSetUp() {
		try {
			prop = new Properties();
			FileInputStream ip = new FileInputStream(System.getProperty("user.dir")
					+ "//src//main//java//com//warehouse//configuration//configuration.properties");
			prop.load(ip);
		}

		catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Test Method - verifyComicsAPI_Valid_ResponseCode
	 * 
	 * To Verify the request's status code as 200 when request for fetching
	 * comics data is made with correct and authorized parameters.
	 */

	// @Test
	public void verifyComicsAPI_Valid_ResponseCode() {
		RestAssured.baseURI = "http://gateway.marvel.com";
		given().param("ts", "1").param("apikey", "0b1355f40d115eb81e1c1768ad3df9a5")
				.param("hash", "4fc1415adbda1a1d5a9c267e05c277a5").header("content-type ", "application/json").when()
				.get("/v1/public/comics").then().assertThat().statusCode(200);
		// .and()
		// .body(containsString("Category 632977 was not found"));
	}
	
	/*
	 * Test Method - fetchAllComics_Ids_Titles
	 * 
	 * To Verify the request's status code as 200 as well list of all comics id's and it's titles
	 * when request for comics data is made with correct and authorized parameters.
	 */

	// @Test
	public void fetchAllComics_Ids_Titles() {
		response = get(
				"http://gateway.marvel.com/v1/public/comics?ts=1&apikey=0b1355f40d115eb81e1c1768ad3df9a5&hash=4fc1415adbda1a1d5a9c267e05c277a5")
						.asString();
		statusCode = get(
				"http://gateway.marvel.com/v1/public/comics?ts=1&apikey=0b1355f40d115eb81e1c1768ad3df9a5&hash=4fc1415adbda1a1d5a9c267e05c277a5")
						.statusCode();
		if (statusCode == 200) {
			JsonPath jp = new JsonPath(response);
			String attrTxt = jp.getString("attributionText");
			System.out.println("Attribution texyt is" + attrTxt);
			List<Integer> ids = from(response).getList("data.results.id");
			List<String> titles = from(response).getList("data.results.title");
			for (Integer id : ids) {
				System.out.println("Id is " + id);
			}
			for (String title : titles) {
				System.out.println("Title is" + title);
			}
		}
	}

	/*
	 * Test Method - verifyComicTitle
	 * 
	 * To Verify the request's status code as 200 as well correct comic title corresponding to it's id
	 *  when request for fetching comics data is made with correct and authorized parameters.
	 */
	// @Test
	public void verifyComicTitle() {
		response = get(
				"http://gateway.marvel.com/v1/public/comics?ts=1&apikey=0b1355f40d115eb81e1c1768ad3df9a5&hash=4fc1415adbda1a1d5a9c267e05c277a5")
						.asString();
		statusCode = get(
				"http://gateway.marvel.com/v1/public/comics?ts=1&apikey=0b1355f40d115eb81e1c1768ad3df9a5&hash=4fc1415adbda1a1d5a9c267e05c277a5")
						.statusCode();
		if (statusCode == 200) {
			JsonPath jp = new JsonPath(response);
			List<Integer> ids = from(response).getList("data.results.id");
			System.out.println("ids are    " + ids);
			for (Integer id : ids) {
				if (id == 16239) {
					String comicTitle = from(response).getString("data.results.title");
					if (comicTitle.equalsIgnoreCase("Ultimate X-Men (Spanish Language Edition) (2000) #3"))
						System.out.println("Correct comic is shown corresponding to comic id");
					else
						System.out.println("Correct comic is shown corresponding to comic id");
				}
			}

		}
	}

	/*
	 * Test Method - verifyComicTitle_particular_resource
	 * 
	 * To Verify the request's status code as 200 as well correct comic title corresponding to it's id
	 *  when request for fetching comics data is made with correct and authorized parameters for particular resource id.
	 */
	@Test
	public void verifyComicTitle_particular_resource() {
		RestAssured.baseURI = "http://gateway.marvel.com";

		Response res = given().param("ts", "1").param("apikey", "0b1355f40d115eb81e1c1768ad3df9a5")
				.param("hash", "4fc1415adbda1a1d5a9c267e05c277a5").header("content-type ", "application/json").when()
				.get("v1/public/series/454").then().assertThat().statusCode(200).extract().response();

		String title = res.jsonPath().getString("data.results[0].title");

		System.out.println("Title is " + title);
		if (res.jsonPath().getString("data.results.title").equalsIgnoreCase("Amazing Spider-Man (1999 - 2013)"))
			System.out.println("Correct comic is fetched successfully");
		else
			System.out.println("Incorrect comic is fetched");

	}

	/*
	 * Test Method - verifyStatusCode_Invalid_APIKey
	 * 
	 * To Verify the request's status code as 401 when request is sent with invalid API key
	 */

	@Test
	public void verifyStatusCode_Invalid_APIKey() {
		RestAssured.baseURI = "http://gateway.marvel.com";
		given().param("ts", "1").param("apikey", "0b1355f40d115eb81e1c")
				.param("hash", "4fc1415adbda1a1d5a9c267e05c277a5").header("content-type ", "application/json").when()
				.get("/v1/public/comics").then().assertThat().statusCode(401);

	}

	/*
	 * Test Method - verifyStatusCode_APIKey_Missing
	 * 
	 * To Verify the request's status code as 401 when API key is missing in request
	 */

	@Test
	public void verifyStatusCode_APIKey_Missing() {
		RestAssured.baseURI = "http://gateway.marvel.com";
		given().param("ts", "1").param("hash", "4fc1415adbda1a1d5a9c267e05c277a5")
				.header("content-type ", "application/json").when().get("/v1/public/comics").then().assertThat()
				.statusCode(409);

	}

	/*
	 * Test Method - verifyStatusCode_APIKey_Missing
	 * 
	 * To Verify the request's status code as 401 when Hash key is missing in request
	 */

	//@Test
	public void verifyStatusCode_HashKey_Missing() {
		RestAssured.baseURI = "http://gateway.marvel.com";
		given().param("ts", "1").param("apikey", "0b1355f40d115eb81e1c1768ad3df9a5")
				.header("content-type ", "application/json").when().get("/v1/public/comics").then().assertThat()
				.statusCode(409);

	}

}

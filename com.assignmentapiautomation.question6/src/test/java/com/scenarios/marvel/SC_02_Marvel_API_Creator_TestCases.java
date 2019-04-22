package com.scenarios.marvel;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.*;
import java.util.List;

import static io.restassured.RestAssured.*;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import static io.restassured.path.json.JsonPath.*;

public class SC_02_Marvel_API_Creator_TestCases {
	
	
	/*
	 * Test Method - verifyComicsAPI_Valid_ResponseCode
	 * 
	 * To Verify the request's status code as 200 when request for fetching
	 * creator data is made with correct and authorized parameters.
	 */

	private String response;
	private int statusCode;
	private String creatorFirstName;
	private String creatorLastName;
	private String creatorFullName;
	private String comicTitle;


	//@Test
	public void verifyCreatorsAPI_Valid_ResponseCode() {
		RestAssured.baseURI = "http://gateway.marvel.com";
		given().param("ts", "1").param("apikey", "0b1355f40d115eb81e1c1768ad3df9a5")
				.param("hash", "4fc1415adbda1a1d5a9c267e05c277a5").header("content-type ", "application/json").when()
				.get("v1/public/creators/32").then().assertThat().statusCode(200);

	}
	
	
	/*
	 * Test Method - verifyCreator_Details
	 * 
	 * To Verify creator's first name,last name, full name when request is made for creator id 32
	 */
	
	//@Test
	public void verifyCreator_Details()
	{
		response = get("http://gateway.marvel.com/v1/public/creators/32?ts=1&apikey=0b1355f40d115eb81e1c1768ad3df9a5&hash=4fc1415adbda1a1d5a9c267e05c277a5")
						.asString();
		statusCode = get(
				"http://gateway.marvel.com/v1/public/creators/32?ts=1&apikey=0b1355f40d115eb81e1c1768ad3df9a5&hash=4fc1415adbda1a1d5a9c267e05c277a5")
						.statusCode();
		if (statusCode == 200) {
			JsonPath jp = new JsonPath(response);
			creatorFirstName = jp.getString("data.results[0].firstName");
			creatorLastName=jp.getString("data.results[0].lastName");
			creatorFullName=jp.getString("data.results[0].fullName");
			System.out.println("Creator first name is -->" + creatorFirstName);
			System.out.println("Creator last name is -->" + creatorLastName);
			System.out.println("Creator full name is -->" + creatorFullName);
			if(creatorFirstName.equalsIgnoreCase("Steve") && creatorLastName.equalsIgnoreCase("Ditko")&& creatorFullName.equalsIgnoreCase("Steve Ditko"))
				System.out.println("Correct details are shown for creator ID 32");
			else
				System.out.println("Incorrect details are shown for creator ID 32");
			
		}
		else
			System.out.println("API service is not reachable");
	}
	
	@Test
	public void verifyCreator_ParticularComics()
	{
		response = get("http://gateway.marvel.com/v1/public/comics/60533?ts=1&apikey=0b1355f40d115eb81e1c1768ad3df9a5&hash=4fc1415adbda1a1d5a9c267e05c277a5")
						.asString();
		statusCode = get(
				"http://gateway.marvel.com/v1/public/comics/60533?ts=1&apikey=0b1355f40d115eb81e1c1768ad3df9a5&hash=4fc1415adbda1a1d5a9c267e05c277a5")
						.statusCode();
		if (statusCode == 200) {
			JsonPath jp = new JsonPath(response);
			comicTitle = jp.getString("data.results[0].title");
			int creatorID=jp.getInt("data.results[0].id");
			System.out.println("Creator id is --> " + creatorID +" and its corresponding comic title is -->" + comicTitle);
			
			if(creatorID==60533 && comicTitle.equalsIgnoreCase("Alpha Flight by John Byrne Omnibus (Hardcover)"))
				System.out.println("Correct details are shown for creator ID 32");
			else
				System.out.println("Incorrect details are shown for creator ID 32");
			
		}
		else
			System.out.println("API service is not reachable");
	}
}

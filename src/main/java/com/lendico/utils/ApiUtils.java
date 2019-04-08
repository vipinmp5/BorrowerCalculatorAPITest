package com.lendico.utils;




import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;

/**
 * Created by Vipin Alias Neo De Van
 */
public class ApiUtils {
	// Global Setup Variables
	public static String path;
	public static String jsonPathTerm;

	// Sets Base URI
	public static void setBaseURI() {
		RestAssured.baseURI = PropertyManager.getInstance().getBaseUrl();
	}

	
	// Sets base path
	public static void setBasePath(String basePathTerm) {
		RestAssured.basePath = basePathTerm;
	}

	// Reset Base URI (after test)
	public static void resetBaseURI() {
		RestAssured.baseURI = null;
	}

	// Reset base path
	public static void resetBasePath() {
		RestAssured.basePath = null;
	}

	// Sets ContentType
	public static void setContentType(ContentType Type) {
		given().contentType(Type);
	}

	// Returns response by given path
	public static Response postResponsebyBodyCalculateAnnuity(String loanAmount, String nominalRate, int duration) {
		return given().accept("application/json").contentType(ContentType.JSON).body("{\"loanAmount\": \""+loanAmount+"\", \"nominalRate\": \""+nominalRate+"\", \"duration\": "+duration+"}").when().post("calc-annuity");
	}
	
	public static Response postResponsebyBodyGeneratePlan(String loanAmount, String nominalRate, int duration, String date) {
		return given().accept("application/json").contentType(ContentType.JSON).body("{\"loanAmount\": \""+loanAmount+"\", \"nominalRate\": \""+nominalRate+"\", \"duration\": "+duration+",  \"startDate\": \""+date+"\"}").when().post("generate-plan");
	}

	// Returns response
	public static Response getResponse() {
		return get();
	}
	
	public static JsonPath getJsonPath(Response res) {
		String json = res.asString();
		return new JsonPath(json);
	}
	

}
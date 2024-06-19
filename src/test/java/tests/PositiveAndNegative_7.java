package tests;

import org.testng.annotations.Test;


import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import resources.EcomUtils_4;
import resources.TestDataBuild;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

public class PositiveAndNegative_7 extends EcomUtils_4   {

	public static Response responseRaw;
	public static String token;
	public static String nameTestCase;

  @BeforeMethod
  public void initializeTestCaseName( Method method ) {
	 
	  nameTestCase = method.getName();
	  System.out.println("Before Method");
  }
  
  
  @Test(priority =1 , dataProvider = "getTokenAPITestData" , groups = { "JsonDataTests"})
  public void callGetTokenAPINegative(HashMap<String, String> eachRow) throws FileNotFoundException, IOException {

		responseRaw = 	given()
							.log().all()
							.spec(requestSpecification())
							.formParams(eachRow)
						.when()
							.post(getValue("endpointGetToken"))
							//.post("/oauth2/resourceOwner/token")
						.then()
							.log().all()
							.spec(responseSpecification(401))
							.assertThat().statusCode(401)
							.extract()
							.response();
							
	
	JsonPath  jsonpath_auth_response = convertResponseToJsonPath(responseRaw);
	token = jsonpath_auth_response.getString("msg");
	Assert.assertTrue(token.equalsIgnoreCase("Authentication failed"));
  }

  @Test(priority =2 , dataProvider = "getTokenAPITestData", groups = {"Regression", "JsonDataTests"})
  public void callGetTokenAPIPositive(HashMap<String, String> eachRow) throws FileNotFoundException, IOException {

		responseRaw = 	given()
							.log().all()
							.spec(requestSpecification())
							.formParams(eachRow)
						.when()
							.post(getValue("endpointGetToken"))
							//.post("/oauth2/resourceOwner/token")
						.then()
							.log().all()
							.spec(responseSpecification(200))
							.assertThat().statusCode(200)
							.extract()
							.response();
							
	
	JsonPath  jsonpath_auth_response = convertResponseToJsonPath(responseRaw);
	token = jsonpath_auth_response.getString("access_token");
	Assert.assertTrue(token.length() >0);
  }

  
  
  @DataProvider(name = "getTokenAPITestData")
	public Iterator<HashMap<String, String>> getJsonData(Method m) throws IOException {
	  
	  return TestDataBuild.readJsonData(System.getProperty("user.dir")+ getValue("jsonDataFile")
	  									, m.getName())
			  				.iterator();
		
	}

  

  



 

  


}

package tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import pojo.GetCourseResponse;
import pojo.WebAutomation;
import resources.EcomUtils_4;
import resources.TestDataBuild;


public class TestsForEachAPI_5  extends EcomUtils_4{


/*
 * applied specs(EcomUtils)
 * applied POJO classes
 * applied propfile
 * applied formdata through hashMap(TestDataBuild) and Excel Test data file
 * applied LoggingFilter to spec of request and response  (EcomUtils_4)
 * applied  a single var to hold raw json responses
 * applied user-defined method to convert from Response > JsonPath > String
 * applied separate Test methods for each of the APIs
 * applied to a Test the dataProvider built from another Test
 */
	public static Response responseRaw;
	public static String token;
	public static List<String> listTokens = new ArrayList();;
	
	@BeforeMethod
	public void intitialize() {
		System.out.println("I'm before method");
		
		
	}
	
	@Test (priority =1, dataProvider = "getCoursesData",alwaysRun = true,  groups = {"Regression"})
	public void callGetTokenAPI(ArrayList<String> eachRow) throws IOException {

			responseRaw = 	given()
								.log().all()
								.spec(requestSpecification())
								.formParams(TestDataBuild.getFormatData(eachRow))
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
		listTokens.add(token);
		System.out.println("******size tokenList" +listTokens.size());
	}
		
	@Test(priority =2, dataProvider = "getTokens" ,alwaysRun = true , groups = {"Regression"}) 	
	public void callGetCourseDetailsAPI(String tokens) throws FileNotFoundException, IOException {
		responseRaw 			= 	given()
										.log().all()
										.spec(requestSpecification())
										.queryParam("access_token", tokens)
									.when()
										.get(getValue("endpointGetCourseDetails"))
										//.get("/getCourseDetails")
									.then()
										.log().all()
										.spec(responseSpecification(401))
										.body( "instructor", equalTo("RahulShetty"))
										.header( "Content-Length", Integer::parseInt, greaterThan(100))
										.extract()
										.response();
		GetCourseResponse getCourseResponse  = responseRaw.as(GetCourseResponse.class);
		
		/*
		 * print price all courses.
		 */
		double sum = 0;
		for( WebAutomation eachCourse: getCourseResponse.getCourses().getWebAutomation()) {
			 sum = sum + Double.parseDouble( eachCourse.getPrice());
			 
		}
		System.out.println("sum is " + sum);
	}
	
	@DataProvider(name = "getCoursesData")
	public Iterator<List<String>> createDataAsArrayList(Method m) throws IOException {
		String testcase =m.getName(),  testDataFile =getValue("testDataFile");
			 return TestDataBuild.readExcelData(testcase, testDataFile).iterator();
		
	}
	
	@DataProvider(name = "getTokens")
	public  Iterator<String> tokenDataProvider() {
			
		return listTokens.iterator();
	}
}

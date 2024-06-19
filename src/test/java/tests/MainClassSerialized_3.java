package tests;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;
import pojo.GetCourseResponse;
import pojo.WebAutomation;
import resources.EcomUtils;
import resources.TestDataBuild;


/*
 * applied specs(EcomUtils)
 * applied POJO classes  on the response
 * applied propfile
 * applied formdata through hashMap(TestDataBuild) and Test data file
 * 
 */
public class MainClassSerialized_3 extends EcomUtils {

	@Test (priority =1, dataProvider = "getCoursesData")
	public void test(ArrayList<String> eachRow) throws IOException {

		String auth_response = 	given()
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
								.response()
								.asString();

		JsonPath jasonauth_response = new JsonPath(auth_response);
		String token = jasonauth_response.getString("access_token");
		
		GetCourseResponse getCourseResponse = new GetCourseResponse();
		
		
		getCourseResponse = 	given()
										.log().all()
										.spec(requestSpecification())
										.queryParam("access_token", token)
									.when()
										.get(getValue("endpointGetCourseDetails"))
										//.get("/getCourseDetails")
									.then()
										.log().all()
										.spec(responseSpecification(401))
										.body( "instructor", equalTo("RahulShetty"))
										.header( "Content-Length", Integer::parseInt, greaterThan(100))
										.extract()
										.response()
										.as(GetCourseResponse.class);
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
	public Iterator<List<String>> createDataAsArrayList() throws IOException {
		String testcase ="getCourses",  testDataFile =getValue("testDataFile");
			 return TestDataBuild.readExcelData(testcase, testDataFile).iterator();
		
	}
	
}

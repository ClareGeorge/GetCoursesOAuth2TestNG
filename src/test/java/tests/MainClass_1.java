package tests;

import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;


/*
 * Intial test without any specs, utils or props or test data
 */
public class MainClass_1 {

	
	@Test
	public void MainTest() {
		
		String auth_response = 	given()
								.log().all()
								.urlEncodingEnabled(false)
								.formParam("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
								.formParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
								.formParam("grant_type", "client_credentials")
								.formParam("scope", "trust")
							.when()
								.post("https://rahulshettyacademy.com/oauthapi/oauth2/resourceOwner/token")
							.then()
								.log().all()
								.assertThat().statusCode(200)
								.extract()
								.response()
								.asString();
		
		JsonPath jasonauth_response = new JsonPath(auth_response);
		String token = jasonauth_response.getString("access_token");
		
		String response_getCourse = 	given()
										.log().all()
										.urlEncodingEnabled(false)
										.queryParam("access_token", token)
									.when()
										.get("https://rahulshettyacademy.com/oauthapi/getCourseDetails")
									.then()
										.log().all()
										.assertThat().statusCode(401)
										.extract()
										.response()
										.asString();
		
	}
}

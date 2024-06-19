package tests;

import org.testng.annotations.Test;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import resources.EcomUtils;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

/*
 * applied specs(utils)
 * no propfile
 * no POJO classes
 */
public class MainClassRefined_2 extends EcomUtils{

	@Test
	public void test() {

		String auth_response = 	given()
								.log().all()
								.spec(requestSpecification())
								.formParam("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
								.formParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
								.formParam("grant_type", "client_credentials")
								.formParam("scope", "trust")
							.when()
								.post("/oauth2/resourceOwner/token")
							.then()
								.log().all()
								.spec(responseSpecification(200))
								.assertThat().statusCode(200)
								.extract()
								.response()
								.asString();

		JsonPath jasonauth_response = new JsonPath(auth_response);
		String token = jasonauth_response.getString("access_token");
		
		
		String response_getCourse = 	given()
										.log().all()
										.spec(requestSpecification())
										.queryParam("access_token", token)
									.when()
										.get("/getCourseDetails")
									.then()
										.log().all()
										.spec(responseSpecification(401))
										.body( "instructor", equalTo("RahulShetty"))
										.header( "Content-Length", Integer::parseInt, greaterThan(100))
										.extract()
										.response()
										.asString();
	
	}
	
}

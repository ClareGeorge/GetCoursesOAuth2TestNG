package resources;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.cookie.CookieFilter;
import io.restassured.filter.session.SessionFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class EcomUtils {

	public static RequestSpecification reqspec ;
	public static ResponseSpecification resspec ;
	public static int statusCode =200;
	public RequestSpecification requestSpecification() {
		
		CookieFilter cookieFilter = new CookieFilter();
		SessionFilter sessionFilter = new SessionFilter();
		
		reqspec		 = new RequestSpecBuilder()
						.setUrlEncodingEnabled(false)
						.setRelaxedHTTPSValidation()
						.setBaseUri("https://rahulshettyacademy.com/oauthapi")
						//.setContentType(ContentType.ANY)
						.addFilter(cookieFilter)
						.addFilter(sessionFilter)
						//.addHeder("authorization", token)
				 		.build();
		return reqspec;
		
		
	}
	public ResponseSpecification responseSpecification(int statusCode) {
		
		resspec = new ResponseSpecBuilder()
					.expectContentType(ContentType.JSON)
					
					.expectStatusCode(statusCode)
				  	.build();
		
		return resspec;
	
	}
	public  String getValue(String key) throws IOException {
		
		FileInputStream fis = new FileInputStream(".\\src\\test\\java\\resources\\global.properties");
		Properties props = new Properties();
		props.load(fis);
		String value = props.getProperty(key);
		return value ;
		
		
	}
	

	/*
	 * public static void main (String[] args) throws IOException {
	 * 
	 * System.out.println(getValue("endpointGetToken"));; }
	 */
}

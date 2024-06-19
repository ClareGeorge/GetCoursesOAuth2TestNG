package resources;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Properties;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.cookie.CookieFilter;
import io.restassured.filter.session.SessionFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class EcomUtils_4 {

	public static RequestSpecification reqspec ;
	public static ResponseSpecification resspec ;
	public static int statusCode =200;
	public static  PrintStream log;
	public RequestSpecification requestSpecification() throws FileNotFoundException, IOException {
	
		if(reqspec == null) {
			CookieFilter cookieFilter = new CookieFilter();
			SessionFilter sessionFilter = new SessionFilter();
			log = new PrintStream(new FileOutputStream(getValue("logFile")));
			
			reqspec		 = new RequestSpecBuilder()
							.setUrlEncodingEnabled(false)
							.setRelaxedHTTPSValidation()
							.setBaseUri(getValue("BaseUri"))
							//.setContentType(ContentType.ANY)
							.addFilter(cookieFilter)
							.addFilter(sessionFilter)
							//.addHeder("authorization", token)
							.addFilter(RequestLoggingFilter.logRequestTo(log))
							.addFilter(ResponseLoggingFilter.logResponseTo(log))
							.build();
			
			return reqspec;
		}
		log.print("***************************************************************************\n");
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
	
	/*Response > String > JsonPath */
	public JsonPath convertResponseToJsonPath(Response response) {
		return new JsonPath(response.asString());
	}


	
}

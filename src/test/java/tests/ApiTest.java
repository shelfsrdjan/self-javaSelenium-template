package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static io.restassured.RestAssured.given;
public class ApiTest {

    private static String apiKey = "f399f648-e6ca-49b2-8cbd-0f657b3db451"; // ovde upises svoj APIkey

    @Test
    public void api()
    {
        RestAssured.baseURI ="https://pro-api.coinmarketcap.com/v1/cryptocurrency";

        RequestSpecification httpRequest = given();
        httpRequest.contentType("application/json");
        httpRequest.header("X-CMC_PRO_API_KEY", apiKey);

        Response response = httpRequest.request(Method.GET, "/info?id=3843");
        String responseBody = response.getBody().asString();
        System.out.println("Response Body is =>  " + responseBody);

        JSONObject jsonObjectStatus = new JSONObject(responseBody).getJSONObject("status");
        System.out.println(jsonObjectStatus.toString());
        String error_message = jsonObjectStatus.get("error_message").toString();
        System.out.println(error_message);
        Assert.assertEquals(error_message,"null");

        JSONObject jsonObjectData = new JSONObject(responseBody).getJSONObject("data");
        System.out.println(jsonObjectData.toString());
        JSONObject jsonObjectID = jsonObjectData.getJSONObject("3843");
        System.out.println(jsonObjectID.toString());
        String symbol = jsonObjectID.getString("symbol");
        System.out.println(symbol);
        Assert.assertEquals(symbol,"BOLT");
        String dateAdded = jsonObjectID.getString("date_added");
        System.out.println(dateAdded);
        Assert.assertEquals(dateAdded, "2019-04-05T00:00:00.000Z");
        String logo = jsonObjectID.getString("logo");
        System.out.println(logo);
        Assert.assertEquals(logo, "https://s2.coinmarketcap.com/static/img/coins/64x64/3843.png");

        JSONObject jsonObjectURLs = jsonObjectData.getJSONObject("3843").getJSONObject("urls");
        System.out.println(jsonObjectURLs.toString());
        JSONArray urlsArray = jsonObjectURLs.getJSONArray("source_code");
        String source_code = urlsArray.get(0).toString();
        System.out.println(source_code);
        Assert.assertEquals("https://github.com/SyQic-Ops/bolt",source_code);

        JSONArray websiteArray = jsonObjectURLs.getJSONArray("website");
        String website = websiteArray.get(0).toString();
        System.out.println(website);
        Assert.assertEquals("https://bolt.global/",website);
    }

    @Test
    public void apiGET()
    {
        RestAssured.baseURI ="https://jsonplaceholder.typicode.com/";

        RequestSpecification httpRequest = given();
        Response response = httpRequest.request(Method.GET, "/users");

        String responseBody = response.getBody().asString();
        System.out.println("Response Body is =>  " + responseBody);

        System.out.println("status code:"+response.getStatusCode());
        Assert.assertEquals("Status code is not 200",200,response.getStatusCode());
    }

    @Test
    public void apiPOST()
    {
        // JSON Object
        Map<String,Object> bookingOne = new HashMap<String,Object>();
        bookingOne.put("id", "11");
        bookingOne.put("name", "Mahajan Trololo");
        bookingOne.put("username", "Bret");
        bookingOne.put("email", "example@gmail.com");
        bookingOne.put("phone", "1-770-736-8031 x56442");
        bookingOne.put("website", "example@gmail.com");

        Map<String,String> bookingDatesMapForAmod = new HashMap<>();
        bookingDatesMapForAmod.put("street", "Kulas Light");
        bookingDatesMapForAmod.put("suite", "Apt. 556");
        bookingDatesMapForAmod.put("city", "Gwenborough");
        bookingDatesMapForAmod.put("zipcode", "92998-3874");
        bookingOne.put("address", bookingDatesMapForAmod);

        Map<String,String> geoAdressInfo = new HashMap<>();
        geoAdressInfo.put("lat","-37.3159");
        geoAdressInfo.put("lng","81.1496");
        bookingDatesMapForAmod.put("geo", String.valueOf(geoAdressInfo));
        bookingOne.put("geo",geoAdressInfo);

        Map<String,String> infoCompany = new HashMap<>();
        infoCompany.put("name","Romaguera-Crona");
        infoCompany.put("catchPhrase","Multi-layered client-server neural-net");
        infoCompany.put("bs","harness real-time e-markets");
        bookingOne.put("company",infoCompany);


        // Creating JSON array to add both JSON objects
        List<Map<String,Object>> jsonArrayPayload = new ArrayList<>();

        jsonArrayPayload.add(bookingOne);

        System.out.println(bookingOne.toString());

        //GIVEN
        RestAssured
                .given()
                .baseUri("https://jsonplaceholder.typicode.com/users")
                .contentType(ContentType.JSON)
//                .body(jsonArrayPayload)
                .log()
                .all()
                // WHEN
                .when()
                .post()
                // THEN
                .then()
                .assertThat()
                // Asserting status code as 500 as it does not accept json array payload
                .statusCode(201)
                .log()
                .all();


    }
}

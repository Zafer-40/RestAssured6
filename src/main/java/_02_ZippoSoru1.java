import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.*;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class _02_ZippoSoru1 {
    // Soru : "http://api.zippopotam.us/us/90210"  endpoint indne dönen
    // place dizisinin ilk elemanının state değerinin  "California"
    // olduğunu doğrulayınız
    @Test
    public void test() {
        given()


                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .statusCode(200)
                .body("places[0].state", equalTo("California"))
        ;
    }

    // Soru : "http://api.zippopotam.us/tr/01000"  endpoint in dönen
    // place dizisinin herhangi bir elemanında  "Dörtağaç Köyü" değerinin
    // olduğunu doğrulayınız
    @Test
    public void test2() {
        given()

                .when()
                .get("http://api.zippopotam.us/tr/01000")

                .then()
                //.log().body()
                .body("places.'place name'", hasItem("Dörtağaç Köyü"))
                .statusCode(200)

        ;
    }

    // Soru : "http://api.zippopotam.us/us/90210"  endpoint in dönen
    // places dizisinin dizi uzunluğunun 1 olduğunu doğrulayınız.
    @Test
    public void test3() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                //.log().body()
                .body("places", hasSize(1)) // places in item sayisi 1 e esit mi?
        //.body("places.size()",equalTo(1))//  2. yontem
        ;
    }

    @Test
    public void combiningTest() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .statusCode(200)
                .body("places[0].state", equalTo("California"))
                .body("places[0].'place name'", equalTo("Beverly Hills"))
        ;
    }

    @Test
    public void pathParamTest() {
        //pathParam endpointe deger atiyor
        given()
                .pathParam("ulke", "us")
                .pathParam("postaKod", 90210)
                .log().uri() //request link i yazdirdik

                .when()
                .get("http://api.zippopotam.us/{ulke}/{postaKod}")

                .then()
                .statusCode(200)
        ;
    }

    @Test
    public void queryParamTest() {
        //https://gorest.co.in/public/v1/users?page=3
        given()
                .queryParam("page", 3)
                .log().uri()

                .when()
                .get("https://gorest.co.in/public/v1/users")

                .then()
                .statusCode(200)
                .log().body()
        ;
    }

    @Test
    public void soru() {
        // https://gorest.co.in/public/v1/users?page=3
        // bu linkteki 1 den 10 kadar sayfaları çağırdığınızda response daki donen page degerlerinin
        // çağrılan page nosu ile aynı olup olmadığını kontrol ediniz.
        for (int i = 1; i <= 10; i++) {
            given()
                    .queryParam("page", i)
                    .log().uri()
                    .when()
                    .get("https://gorest.co.in/public/v1/users")

                    .then()
                    .statusCode(200)
                    .body("meta.pagination.page",equalTo(i))
            ;
        }
    }

    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;

    @BeforeClass
    public void setup(){
        baseURI = "https://gorest.co.in/public/v1";

        requestSpec= new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .log(LogDetail.URI)  // log().uri()
                .build();

        responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)  // statusCode(200)
                .log(LogDetail.BODY)
                .expectContentType(ContentType.JSON)
                .build();
    }

    @Test
    public void requestResponseSpecificationn(){
        given()
                .param("page",1)
                .spec(requestSpec)

                .when()
                .get("/users") // http hok ise baseUri baş tarafına gelir.

                .then()
                .spec(responseSpec)
        ;
    }

    @Test
    public void extractingJsonPath() {
        String countryName=
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .extract().path("country")
        ;
        System.out.println("country = " + countryName);
        Assert.assertEquals(countryName,"United States");
    }

    @Test
    public void extractPathSoru(){
        // Soru : "http://api.zippopotam.us/us/90210"  endpoint indne dönen
        // place dizisinin ilk elemanının state değerinin  "California"
        // olduğunu testNG Assertion ile doğrulayınız
        String stateName=
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .extract().path("places[0].state")
                ;
        System.out.println("state Name = " + stateName);
        Assert.assertEquals(stateName,"California");
    }

    @Test
    public void extractPathSoru2(){
        // Soru : "http://api.zippopotam.us/us/90210"  endpoint indne dönen
        // place dizisinin ilk elemanının place name değerinin  "Beverly Hills"
        // olduğunu testNG Assertion ile doğrulayınız
        String stateName=
                given()

                        .when()
                        .get("http://api.zippopotam.us/us/90210")
                        .then()
                        .extract().path("places[0].'place name'")
                ;
        System.out.println("Place Name = " + stateName);
        Assert.assertEquals(stateName,"Beverly Hills");
    }

    @Test
    public void extractPathSoru3(){
        // Soru : "https://gorest.co.in/public/v1/users"  endpoint in den dönen
        // limit bilgisinin 10 olduğunu testNG ile doğrulayınız.
        int limit=
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        .extract().path("meta.pagination.limit")

                ;
        System.out.println("Limit No = " + limit);
        Assert.assertEquals(limit,10);
    }

    @Test
    public void extractPathSoru4(){
    // TUM ID LERI ALDIK
        List<Integer> idler =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        .extract().path("data.id")
                ;
        System.out.println("idler = " + idler);
    }

    @Test
    public void extractPathSoru5(){
    // TUM NAME LERI ALDIK
        List<String> names =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        .extract().path("data.name")
                ;
        System.out.println("names = " + names);
    }

    @Test
    public void extractingJsonPathResponsAll(){
    //RESPONSE ILE TUM VERILERE ULASIP ICINDEN ISTEDIKLERINI ALABILIYORSUN
        Response donenData =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        .extract().response()
                ;
        List<Integer> idler = donenData.path("data.id");
        List<String> nameler = donenData.path("data.name");
        int limit = donenData.path("meta.pagination.limit");

        System.out.println("limit = " + limit);
        System.out.println("idler = " + idler);
        System.out.println("nameler = " + nameler);

        Assert.assertTrue(nameler.contains("Mahesh Menon"));
        Assert.assertTrue(idler.contains(5599126));
        Assert.assertTrue(limit==10);
    }
}

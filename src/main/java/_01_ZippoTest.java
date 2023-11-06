import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class _01_ZippoTest {

    @Test
    public void test1() {

        given()
                // HAZIRLIK ISLEMLERI KODLARI
                .when()
                // ENDPOINT, METHOD U VERIP ISTEK GONDERILIYOR

                .then()
        // ASSERTION, TEST, DATA ISLEMLERI
        ;
    }

    @Test
    public void test2() {
        given()
                // HAZIRLIK KISMI BOS
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body() // DONEN BODY JSON DATA, LOG().ALL() : GIDIP GELEN HERSEY
                .statusCode(200); // TEST KISMI OLDUGUNDAN ASSERTION STATUS KOD 200 MU?
        ;
    }

    @Test
    public void contentTypeTest() {
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body() // DONEN BODY JSON DATA, LOG().ALL() : GIDIP GELEN HERSEY
                .statusCode(200) // TEST KISMI OLDUGUNDAN ASSERTION STATUS KOD 200 MU?
                .contentType(ContentType.JSON) // DONEN DATANIN TIPI JSON MI
        ;
    }

    @Test
    public void test3() {
        //https://jsonpathfinder.com/ BODY ICINDEN VERI ALDIGIMIZ SITE
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                //.log().body()
                .statusCode(200)
                .body("country", equalTo("United States"))// assertion
        ;
    }
}

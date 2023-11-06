import Model.Location;
import Model.Place;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

public class _03_ApiTestPOJO {
    // POJO : JSON nesnesi Location nesnesi

    @Test
    public void extractJsonAll_POJO(){
        //DONEN BODY BILGISINI LOCATION CLASS KALIBIYLA CEVIR
        Location locationNesnesi=
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .extract().body().as(Location.class)// Location kalibina gore
        ;
        System.out.println
                ("locationNesnesi.getCountry() = " + locationNesnesi.getCountry());

        System.out.println
                ("locationNesnesi.getPlaces() = " + locationNesnesi.getPlaces());
        for (Place p : locationNesnesi.getPlaces())
            System.out.println("p = " + p);
    }

    @Test
    public void soru(){
        // http://api.zippopotam.us/tr/01000
        // endpointinden dönen verilerden "Dörtağaç Köyü" ait bilgileri yazdırınız

        Location locationNesnesi=
                given()
                        .when()
                        .get("http://api.zippopotam.us/tr/01000")

                        .then()
                        .extract().body().as(Location.class)
                ;
        for (Place p :locationNesnesi.getPlaces()){
            if (p.getPlacename().equalsIgnoreCase("Dörtağaç Köyü")){
                System.out.println("p = " + p);
            }
        }

    }
}

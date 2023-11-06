import Model.Location;
import Model.Place;
import Model.User;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.*;

public class _05_PathAndJsonPath {
    @Test
    public void extractingPath(){ // POST CODE STRING OLDUGU ICIN STRINGE ESITLEDIK YOKSA CALISMAZ
        String postCode=
        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .extract().path("'post code'")
        ;
        System.out.println("postCode = " + postCode);
    }

    @Test
    public void extractingJSONPath(){ // POST CODE STRINGTI JSONPATH ILE ONU INTEGER A CEVIRDIK VE INT E ESITLEDIK
        int postCode=
                given()

                        .when()
                        .get("http://api.zippopotam.us/us/90210")
                        .then()
                        .extract().jsonPath().getInt("'post code'")
                ;
        System.out.println("postCode = " + postCode);
    }

    @Test
    public void getZipCode(){
        Response response=
                given()

                        .when()
                        .get("http://api.zippopotam.us/us/90210")
                        .then()
                        .log().body()
                        .extract().response()
                ;
        Location locationPathAs=response.as(Location.class); //BUTUN CLASSLARI YAZMALISIN
        System.out.println(locationPathAs.getPlaces());
        System.out.println("===============================");

        List<Place>places = response.jsonPath().getList("places", Place.class);
        System.out.println(places);

        // Daha önceki örneklerde (as) Clas dönüşümleri için tüm yapıya karşılık gelen
        // gereken tüm classları yazarak dönüştürüp istediğimiz elemanlara ulaşıyorduk.

        // Burada ise(JsonPath) aradaki bir veriyi clasa dönüştürerek bir list olarak almamıza
        // imkan veren JSONPATH i kullandık.Böylece tek class ile veri alınmış oldu
        // diğer class lara gerek kalmadan

        // path : class veya tip dönüşümüne imkan veremeyen direk veriyi verir. List<String> gibi
        // jsonPath : class dönüşümüne ve tip dönüşümüne izin vererek , veriyi istediğimiz formatta verir.
    }
    @Test
    public void getUserV1(){
        // https://gorest.co.in/public/v1/users  endpointte dönen Sadece Data Kısmını POJO
        // dönüşümü ile alarak yazdırınız.
        List<User> dataUser=
        given()

                .when()
                .get("https://gorest.co.in/public/v1/users")
                .then()
                .extract().jsonPath().getList("data", User.class);
    ;
        System.out.println(dataUser.get(0).getEmail());
        System.out.println("*********************************");
        for (User user : dataUser)
            System.out.println(user);
    }


}

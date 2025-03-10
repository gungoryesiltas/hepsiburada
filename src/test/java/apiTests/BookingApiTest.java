package apiTests;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.*;
import static io.restassured.RestAssured.given;

public class BookingApiTest {
    private static String token;
    private static int bookingId;
    private static Faker faker = new Faker();
    private static String firstName;
    private static String lastName;

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

        // Rastgele ad ve soyad oluştur
        firstName = faker.name().firstName();
        lastName = faker.name().lastName();
        createAuthToken();
        createBooking();
    }

    @Test(priority = 1)
    public void createAuthToken() {
        JSONObject requestBody = new JSONObject();
        requestBody.put("username", "admin");
        requestBody.put("password", "password123");

        Response response = given()
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .when()
                .post("/auth")
                .then()
                .extract().response();

        token = response.jsonPath().getString("token");

        // Token başarıyla alındığını kontrol et
        Assert.assertNotNull(token, "Token oluşturulamadı!");
        System.out.println("Token alındı: " + token);
    }

    @Test(priority = 2, dependsOnMethods = "createAuthToken")
    public void createBooking() {
        JSONObject bookingDates = new JSONObject();
        bookingDates.put("checkin", "2025-01-01");
        bookingDates.put("checkout", "2025-01-10");

        JSONObject requestBody = new JSONObject();
        requestBody.put("firstname", firstName);
        requestBody.put("lastname", lastName);
        requestBody.put("totalprice", faker.number().numberBetween(100, 500));
        requestBody.put("depositpaid", true);
        requestBody.put("bookingdates", bookingDates);
        requestBody.put("additionalneeds", "Breakfast");

        Response response = given()
                .header("Content-Type", "application/json")
                .body(requestBody.toString())
                .when()
                .post("/booking")
                .then()
                .extract().response();

        // Booking ID'yi al ve global değişkene ata
        bookingId = response.jsonPath().getInt("bookingid");

        // Booking ID'nin doğru şekilde alındığını kontrol et
        Assert.assertNotNull(bookingId, "Booking ID alınamadı!");
        System.out.println("Booking oluşturuldu. ID: " + bookingId);
    }

    @Test(priority = 3)
    public void getBookingByName() {
        // Booking'leri firstname ve lastname ile sorgula
        System.out.println("firstName = " + firstName);
        System.out.println("lastName = " + lastName);
        Response response = given()
                .queryParam("firstname", firstName)
                .queryParam("lastname", lastName)
                .when()
                .get("/booking")
                .then()
                .extract().response();

        // Booking ID'leri çıktısını al
        System.out.println("Booking ID'leri: " + response.asString());

        // Status kodunun doğru olup olmadığını kontrol et
        Assert.assertTrue(response.statusCode() == 200, "Booking ID alınamadı!");
    }

    @Test(priority = 4)
    public void getBookingById() {
        // Global bookingId'yi kullanarak booking detayını al
        System.out.println("Booking ID: " + bookingId);
        Response response = given()
                .when()
                .get("/booking/" + bookingId)
                .then()
                .extract().response();

        // Booking detaylarını yazdır
        System.out.println("Booking Detayları: " + response.asString());

        // Status kodunun doğru olduğunu kontrol et
        Assert.assertEquals(response.statusCode(), 200, "Booking detayı getirilemedi!");
    }

    @Test(priority = 5)
    public void updateBooking() {
        JSONObject bookingDates = new JSONObject();
        bookingDates.put("checkin", "2025-02-01");
        bookingDates.put("checkout", "2025-02-10");
        System.out.println("bookingId = " + bookingId);
        System.out.println("token = " + token);
        JSONObject requestBody = new JSONObject();
        requestBody.put("firstname", "Updated_" + firstName);
        requestBody.put("lastname", lastName);
        requestBody.put("totalprice", 222);
        requestBody.put("depositpaid", false);
        requestBody.put("bookingdates", bookingDates);
        requestBody.put("additionalneeds", "Lunch");

        Response response = given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .cookie("token", token)
                .body(requestBody.toString())
                .when()
                .put("/booking/" + bookingId)
                .then()
                .extract().response();

        // Güncellenen Booking bilgisini yazdır
        System.out.println("Updated Booking: " + response.asString());

        // Status kodunun doğru olduğunu kontrol et
        Assert.assertEquals(response.statusCode(), 200, "Booking güncellenemedi!");
    }

    @Test(priority = 6, dependsOnMethods = "getBookingById")
    public void patchBooking() {
        JSONObject requestBody = new JSONObject();
        requestBody.put("firstname", "Patched_" + firstName);
        requestBody.put("lastname", lastName);

        Response response = given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .cookie("token", token)
                .body(requestBody.toString())
                .when()
                .patch("/booking/" + bookingId)
                .then()
                .extract().response();

        // Patchlenen booking bilgisini yazdır
        System.out.println("Patched Booking: " + response.asString());

        // Status kodunun doğru olduğunu kontrol et
        Assert.assertEquals(response.statusCode(), 200, "Booking patch edilemedi!");
    }

    @Test(priority = 7)
    public void deleteBooking() {
        Response response = given()
                .header("Content-Type", "application/json")
                .cookie("token", token)
                .when()
                .delete("/booking/" + bookingId)
                .then()
                .extract().response();

        // Silinen Booking'in yanıtını yazdır
        System.out.println("Delete Response: " + response.asString());

        // Status kodunun doğru olduğunu kontrol et
        Assert.assertEquals(response.statusCode(), 201, "Booking silinemedi!");
    }
}

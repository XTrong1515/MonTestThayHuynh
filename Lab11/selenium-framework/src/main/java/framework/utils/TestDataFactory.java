package framework.utils;

import com.github.javafaker.Faker;

import java.util.Locale;
import java.util.Map;
public class TestDataFactory {
    private static final Faker faker = new Faker(Locale.forLanguageTag("vi"));

    public static String randomFirstName() {
        return faker.name().firstName();
    }

    public static String randomLastName() {
        return faker.name().lastName();
    }

    public static String randomPostalCode() {
        return faker.number().digits(5);
    }

    public static String randomEmail() {
        return faker.internet().emailAddress();
    }

    // Sinh một bộ dữ liệu checkout hoàn chỉnh
    public static Map<String, String> randomCheckoutData() {
        return Map.of(
            "firstName", randomFirstName(),
            "lastName", randomLastName(),
            "postalCode", randomPostalCode()
        );
    }
}

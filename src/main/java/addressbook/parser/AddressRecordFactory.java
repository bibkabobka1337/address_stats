package addressbook.parser;

import addressbook.model.AddressRecord;

final class AddressRecordFactory {

    private AddressRecordFactory() {
    }

    static AddressRecord create(String city, String street, String house, String floor) {
        return new AddressRecord(
                requireText(city, "city"),
                requireText(street, "street"),
                parseNumber(house, "house"),
                parseNumber(floor, "floor"));
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Пустое значение поля " + fieldName);
        }
        return value.strip();
    }

    private static int parseNumber(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Пустое значение поля " + fieldName);
        }
        try {
            return Integer.parseInt(value.strip());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Некорректное значение поля " + fieldName + ": " + value, e);
        }
    }
}

package addressbook.parser;

import addressbook.model.AddressRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CsvAddressParser implements AddressParser {

    private static final char DELIMITER = ';';
    private static final char QUOTE = '"';
    private static final int FIELDS_COUNT = 4;

    @Override
    public List<AddressRecord> parse(Path file) throws IOException {
        List<AddressRecord> records = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            reader.readLine();
            int lineNumber = 1;
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.isBlank()) {
                    continue;
                }
                records.add(parseLine(line, lineNumber));
            }
        }
        return records;
    }

    private AddressRecord parseLine(String line, int lineNumber) throws AddressParseException {
        List<String> fields = splitFields(line);
        if (fields.size() != FIELDS_COUNT) {
            throw new AddressParseException(
                    "CSV, строка " + lineNumber + ": ожидалось полей " + FIELDS_COUNT
                            + ", найдено " + fields.size());
        }
        try {
            return AddressRecordFactory.create(
                    fields.get(0), fields.get(1), fields.get(2), fields.get(3));
        } catch (IllegalArgumentException e) {
            throw new AddressParseException(
                    "CSV, строка " + lineNumber + ": " + e.getMessage(), e);
        }
    }

    private List<String> splitFields(String line) {
        List<String> fields = new ArrayList<>(FIELDS_COUNT);
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char symbol = line.charAt(i);
            if (symbol == QUOTE) {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == QUOTE) {
                    current.append(QUOTE);
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (symbol == DELIMITER && !inQuotes) {
                fields.add(current.toString());
                current.setLength(0);
            } else {
                current.append(symbol);
            }
        }
        fields.add(current.toString());
        return fields;
    }
}

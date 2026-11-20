package addressbook.parser;

import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

public class AddressParserFactory {

    private final Map<String, Supplier<AddressParser>> parsersByExtension = Map.of(
            "csv", CsvAddressParser::new,
            "xml", XmlAddressParser::new);

    public AddressParser create(Path file) throws AddressParseException {
        String extension = extensionOf(file);
        Supplier<AddressParser> supplier = parsersByExtension.get(extension);
        if (supplier == null) {
            throw new AddressParseException(
                    "Неподдерживаемый формат файла: \"" + extension + "\". Поддерживаются: csv, xml");
        }
        return supplier.get();
    }

    private String extensionOf(Path file) {
        String name = file.getFileName().toString();
        int dot = name.lastIndexOf('.');
        return dot < 0 ? "" : name.substring(dot + 1).toLowerCase(Locale.ROOT);
    }
}

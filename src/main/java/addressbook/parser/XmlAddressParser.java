package addressbook.parser;

import addressbook.model.AddressRecord;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class XmlAddressParser implements AddressParser {

    private static final String ITEM_ELEMENT = "item";
    private static final String CITY_ATTRIBUTE = "city";
    private static final String STREET_ATTRIBUTE = "street";
    private static final String HOUSE_ATTRIBUTE = "house";
    private static final String FLOOR_ATTRIBUTE = "floor";

    private final XMLInputFactory inputFactory = createInputFactory();

    @Override
    public List<AddressRecord> parse(Path file) throws IOException {
        List<AddressRecord> records = new ArrayList<>();
        try (InputStream input = Files.newInputStream(file)) {
            XMLStreamReader reader = inputFactory.createXMLStreamReader(input);
            try {
                while (reader.hasNext()) {
                    if (reader.next() == XMLStreamConstants.START_ELEMENT
                            && ITEM_ELEMENT.equals(reader.getLocalName())) {
                        records.add(readItem(reader));
                    }
                }
            } finally {
                reader.close();
            }
        } catch (XMLStreamException e) {
            throw new AddressParseException("XML: некорректный файл: " + e.getMessage(), e);
        }
        return records;
    }

    private AddressRecord readItem(XMLStreamReader reader) throws AddressParseException {
        try {
            return AddressRecordFactory.create(
                    reader.getAttributeValue(null, CITY_ATTRIBUTE),
                    reader.getAttributeValue(null, STREET_ATTRIBUTE),
                    reader.getAttributeValue(null, HOUSE_ATTRIBUTE),
                    reader.getAttributeValue(null, FLOOR_ATTRIBUTE));
        } catch (IllegalArgumentException e) {
            int line = reader.getLocation().getLineNumber();
            throw new AddressParseException("XML, строка " + line + ": " + e.getMessage(), e);
        }
    }

    private static XMLInputFactory createInputFactory() {
        XMLInputFactory factory = XMLInputFactory.newFactory();
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        return factory;
    }
}

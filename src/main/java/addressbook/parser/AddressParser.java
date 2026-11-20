package addressbook.parser;

import addressbook.model.AddressRecord;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface AddressParser {

    List<AddressRecord> parse(Path file) throws IOException;
}

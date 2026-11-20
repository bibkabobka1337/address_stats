package addressbook.parser;

import java.io.IOException;

public class AddressParseException extends IOException {

    public AddressParseException(String message) {
        super(message);
    }

    public AddressParseException(String message, Throwable cause) {
        super(message, cause);
    }
}

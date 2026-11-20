package addressbook;

import addressbook.console.ConsoleApplication;
import addressbook.console.ConsoleReportPrinter;
import addressbook.parser.AddressParserFactory;
import addressbook.stats.FileProcessingService;
import addressbook.stats.StatisticsCalculator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.Charset;

public class Main {

    public static void main(String[] args) {
        Charset inputCharset = detectCharset("stdin.encoding", "stdout.encoding", "native.encoding");
        Charset outputCharset = detectCharset("stdout.encoding", "native.encoding");

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in, inputCharset));
        PrintStream output = new PrintStream(System.out, true, outputCharset);

        FileProcessingService processingService =
                new FileProcessingService(new AddressParserFactory(), new StatisticsCalculator());
        ConsoleApplication application =
                new ConsoleApplication(input, output, processingService, new ConsoleReportPrinter(output));

        application.run();
    }

    private static Charset detectCharset(String... propertyNames) {
        for (String propertyName : propertyNames) {
            String charsetName = System.getProperty(propertyName);
            if (charsetName != null && Charset.isSupported(charsetName)) {
                return Charset.forName(charsetName);
            }
        }
        return Charset.defaultCharset();
    }
}

package addressbook;

import addressbook.console.ConsoleApplication;
import addressbook.console.ConsoleReportPrinter;
import addressbook.parser.AddressParserFactory;
import addressbook.stats.FileProcessingService;
import addressbook.stats.StatisticsCalculator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        PrintStream output = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        FileProcessingService processingService =
                new FileProcessingService(new AddressParserFactory(), new StatisticsCalculator());
        ConsoleApplication application =
                new ConsoleApplication(input, output, processingService, new ConsoleReportPrinter(output));

        application.run();
    }
}

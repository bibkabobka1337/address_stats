package addressbook.stats;

import addressbook.model.AddressRecord;
import addressbook.parser.AddressParserFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

public class FileProcessingService {

    private final AddressParserFactory parserFactory;
    private final StatisticsCalculator calculator;

    public FileProcessingService(AddressParserFactory parserFactory, StatisticsCalculator calculator) {
        this.parserFactory = parserFactory;
        this.calculator = calculator;
    }

    public FileReport process(Path file) throws IOException {
        long start = System.nanoTime();
        List<AddressRecord> records = parserFactory.create(file).parse(file);
        AddressStatistics statistics = calculator.calculate(records);
        Duration elapsed = Duration.ofNanos(System.nanoTime() - start);
        return new FileReport(statistics, elapsed);
    }
}

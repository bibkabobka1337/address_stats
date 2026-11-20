package addressbook.console;

import addressbook.model.AddressRecord;
import addressbook.stats.AddressStatistics;
import addressbook.stats.FileReport;

import java.io.PrintStream;
import java.util.Map;

public class ConsoleReportPrinter {

    private static final int MIN_FLOOR = 1;
    private static final int MAX_FLOOR = 5;
    private static final String CITY_HEADER = "Город";
    private static final int COUNT_COLUMN_WIDTH = 8;

    private final PrintStream output;

    public ConsoleReportPrinter(PrintStream output) {
        this.output = output;
    }

    public void print(FileReport report) {
        AddressStatistics statistics = report.statistics();
        output.println();
        printDuplicates(statistics.duplicates());
        output.println();
        printFloorCounts(statistics.floorCountsByCity());
        output.println();
        output.println("Время обработки файла: " + report.processingTime().toMillis() + " мс");
        output.println();
    }

    private void printDuplicates(Map<AddressRecord, Integer> duplicates) {
        output.println("Дублирующиеся записи:");
        if (duplicates.isEmpty()) {
            output.println("  не найдены");
            return;
        }
        duplicates.forEach((record, count) -> output.println(
                "  " + record.city() + ", " + record.street() + ", д. " + record.house()
                        + ", этажей: " + record.floor() + " — повторений: " + count));
    }

    private void printFloorCounts(Map<String, Map<Integer, Integer>> floorCountsByCity) {
        output.println("Количество зданий по этажности в каждом городе:");
        if (floorCountsByCity.isEmpty()) {
            output.println("  нет данных");
            return;
        }
        int cityColumnWidth = cityColumnWidth(floorCountsByCity);
        output.println(formatHeader(cityColumnWidth));
        floorCountsByCity.forEach((city, counts) -> output.println(formatRow(city, counts, cityColumnWidth)));
    }

    private int cityColumnWidth(Map<String, Map<Integer, Integer>> floorCountsByCity) {
        int width = CITY_HEADER.length();
        for (String city : floorCountsByCity.keySet()) {
            width = Math.max(width, city.length());
        }
        return width;
    }

    private String formatHeader(int cityColumnWidth) {
        StringBuilder header = new StringBuilder(String.format("  %-" + cityColumnWidth + "s", CITY_HEADER));
        for (int floor = MIN_FLOOR; floor <= MAX_FLOOR; floor++) {
            header.append(String.format("%" + COUNT_COLUMN_WIDTH + "s", floor + " эт."));
        }
        return header.toString();
    }

    private String formatRow(String city, Map<Integer, Integer> counts, int cityColumnWidth) {
        StringBuilder row = new StringBuilder(String.format("  %-" + cityColumnWidth + "s", city));
        for (int floor = MIN_FLOOR; floor <= MAX_FLOOR; floor++) {
            row.append(String.format("%" + COUNT_COLUMN_WIDTH + "d", counts.getOrDefault(floor, 0)));
        }
        return row.toString();
    }
}

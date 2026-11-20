package addressbook.console;

import addressbook.stats.FileProcessingService;
import addressbook.stats.FileReport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public class ConsoleApplication {

    private static final String EXIT_COMMAND = "exit";
    private static final String PROMPT = "> ";

    private final BufferedReader input;
    private final PrintStream output;
    private final FileProcessingService processingService;
    private final ConsoleReportPrinter reportPrinter;

    public ConsoleApplication(BufferedReader input,
                              PrintStream output,
                              FileProcessingService processingService,
                              ConsoleReportPrinter reportPrinter) {
        this.input = input;
        this.output = output;
        this.processingService = processingService;
        this.reportPrinter = reportPrinter;
    }

    public void run() {
        output.println("Введите путь до файла-справочника (csv или xml).");
        output.println("Для завершения работы введите \"" + EXIT_COMMAND + "\" или нажмите Ctrl+D (Ctrl+Z в Windows).");
        while (true) {
            output.print(PROMPT);
            output.flush();
            String line = readLine();
            if (line == null) {
                break;
            }
            String command = line.strip();
            if (command.isEmpty()) {
                continue;
            }
            if (EXIT_COMMAND.equalsIgnoreCase(command)) {
                break;
            }
            handleFile(command);
        }
        output.println("Работа завершена.");
    }

    private String readLine() {
        try {
            return input.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    private void handleFile(String command) {
        try {
            Path file = Path.of(unquote(command));
            FileReport report = processingService.process(file);
            reportPrinter.print(report);
        } catch (NoSuchFileException e) {
            output.println("Файл не найден: " + e.getFile());
        } catch (IOException | RuntimeException e) {
            output.println("Не удалось обработать файл: " + e.getMessage());
        } catch (OutOfMemoryError e) {
            output.println("Недостаточно памяти для обработки файла.");
        }
    }

    private String unquote(String value) {
        if (value.length() >= 2 && value.startsWith("\"") && value.endsWith("\"")) {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }
}

package addressbook.stats;

import java.time.Duration;

public record FileReport(AddressStatistics statistics, Duration processingTime) {
}

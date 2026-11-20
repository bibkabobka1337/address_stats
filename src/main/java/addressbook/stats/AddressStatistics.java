package addressbook.stats;

import addressbook.model.AddressRecord;

import java.util.Map;

public record AddressStatistics(
        Map<AddressRecord, Integer> duplicates,
        Map<String, Map<Integer, Integer>> floorCountsByCity) {
}

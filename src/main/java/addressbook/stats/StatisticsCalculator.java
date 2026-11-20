package addressbook.stats;

import addressbook.model.AddressRecord;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class StatisticsCalculator {

    private static final Comparator<Map.Entry<AddressRecord, Integer>> DUPLICATES_ORDER =
            Map.Entry.<AddressRecord, Integer>comparingByValue().reversed()
                    .thenComparing(entry -> entry.getKey().city())
                    .thenComparing(entry -> entry.getKey().street())
                    .thenComparingInt(entry -> entry.getKey().house())
                    .thenComparingInt(entry -> entry.getKey().floor());

    public AddressStatistics calculate(List<AddressRecord> records) {
        Map<AddressRecord, Integer> occurrences = countOccurrences(records);
        return new AddressStatistics(
                findDuplicates(occurrences),
                countFloorsByCity(occurrences));
    }

    private Map<AddressRecord, Integer> countOccurrences(List<AddressRecord> records) {
        Map<AddressRecord, Integer> occurrences = new HashMap<>(records.size() * 2);
        for (AddressRecord record : records) {
            occurrences.merge(record, 1, Integer::sum);
        }
        return occurrences;
    }

    private Map<AddressRecord, Integer> findDuplicates(Map<AddressRecord, Integer> occurrences) {
        Map<AddressRecord, Integer> duplicates = new LinkedHashMap<>();
        occurrences.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .sorted(DUPLICATES_ORDER)
                .forEach(entry -> duplicates.put(entry.getKey(), entry.getValue()));
        return duplicates;
    }

    private Map<String, Map<Integer, Integer>> countFloorsByCity(
            Map<AddressRecord, Integer> occurrences) {
        Map<String, Map<Integer, Integer>> floorCountsByCity = new TreeMap<>();
        for (AddressRecord building : occurrences.keySet()) {
            floorCountsByCity
                    .computeIfAbsent(building.city(), city -> new TreeMap<>())
                    .merge(building.floor(), 1, Integer::sum);
        }
        return floorCountsByCity;
    }
}

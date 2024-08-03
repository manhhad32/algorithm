import java.util.*;

public class AprioriAlgorithm {

    public static void main(String[] args) {
        List<Set<String>> transactions = Arrays.asList(
            new HashSet<>(Arrays.asList("milk", "bread", "butter")),
            new HashSet<>(Arrays.asList("beer", "bread")),
            new HashSet<>(Arrays.asList("milk", "bread", "beer", "butter")),
            new HashSet<>(Arrays.asList("bread", "butter")),
            new HashSet<>(Arrays.asList("milk", "butter"))
        );

        double minSupport = 0.6;

        List<Set<String>> frequentItemsets = findFrequentItemsets(transactions, minSupport);

        for (Set<String> itemset : frequentItemsets) {
            System.out.println(itemset);
        }
    }

    public static List<Set<String>> findFrequentItemsets(List<Set<String>> transactions, double minSupport) {
        int minSupportCount = (int) Math.ceil(minSupport * transactions.size());
        List<Set<String>> currentFrequentItemsets = findFrequentOneItemsets(transactions, minSupportCount);
        List<Set<String>> allFrequentItemsets = new ArrayList<>(currentFrequentItemsets);

        while (!currentFrequentItemsets.isEmpty()) {
            List<Set<String>> candidateItemsets = generateCandidates(currentFrequentItemsets);
            currentFrequentItemsets = getFrequentItemsets(transactions, candidateItemsets, minSupportCount);
            allFrequentItemsets.addAll(currentFrequentItemsets);
        }

        return allFrequentItemsets;
    }

    private static List<Set<String>> findFrequentOneItemsets(List<Set<String>> transactions, int minSupportCount) {
        Map<String, Integer> itemCounts = new HashMap<>();

        for (Set<String> transaction : transactions) {
            for (String item : transaction) {
                itemCounts.put(item, itemCounts.getOrDefault(item, 0) + 1);
            }
        }

        List<Set<String>> frequentOneItemsets = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : itemCounts.entrySet()) {
            if (entry.getValue() >= minSupportCount) {
                frequentOneItemsets.add(new HashSet<>(Collections.singletonList(entry.getKey())));
            }
        }

        return frequentOneItemsets;
    }

    private static List<Set<String>> generateCandidates(List<Set<String>> frequentItemsets) {
        List<Set<String>> candidates = new ArrayList<>();

        for (int i = 0; i < frequentItemsets.size(); i++) {
            for (int j = i + 1; j < frequentItemsets.size(); j++) {
                Set<String> candidate = new HashSet<>(frequentItemsets.get(i));
                candidate.addAll(frequentItemsets.get(j));

                if (candidate.size() == frequentItemsets.get(0).size() + 1) {
                    candidates.add(candidate);
                }
            }
        }

        return candidates;
    }

    private static List<Set<String>> getFrequentItemsets(List<Set<String>> transactions, List<Set<String>> candidates, int minSupportCount) {
        List<Set<String>> frequentItemsets = new ArrayList<>();
        Map<Set<String>, Integer> itemsetCounts = new HashMap<>();

        for (Set<String> transaction : transactions) {
            for (Set<String> candidate : candidates) {
                if (transaction.containsAll(candidate)) {
                    itemsetCounts.put(candidate, itemsetCounts.getOrDefault(candidate, 0) + 1);
                }
            }
        }

        for (Map.Entry<Set<String>, Integer> entry : itemsetCounts.entrySet()) {
            if (entry.getValue() >= minSupportCount) {
                frequentItemsets.add(entry.getKey());
            }
        }

        return frequentItemsets;
    }
}

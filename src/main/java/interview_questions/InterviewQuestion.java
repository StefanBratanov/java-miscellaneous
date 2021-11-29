package interview_questions;

import java.util.*;
import java.util.stream.Collectors;

public class InterviewQuestion {

    public static void main(String[] args) {
        InterviewQuestion iq = new InterviewQuestion();

        String[] shakespeare = new String[]{
                "My cat is big",
                "My dog is small",
                "My dog is afraid of a cat"
        };
        iq.init(shakespeare);

        Integer[] output = iq.find("Lion is a big cat");

        System.out.println(Arrays.toString(output));

    }

    void init(String[] initialSentences) {
        for (int i = 0; i < initialSentences.length; i++) {
            List<String> words = Arrays.stream(initialSentences[i].split("\\s+")).collect(Collectors.toList());

            final int index = i;
            words.forEach(word -> {
                List<Integer> indexes = wordsMap.computeIfAbsent(word, k -> new ArrayList<>());
                indexes.add(index);
            });
        }

        System.out.println(wordsMap);
    }

    private final Map<String, List<Integer>> wordsMap = new HashMap<>();

    private final Map<Integer, Integer> occurencesByIndex = new HashMap<>();

    private final Set<Integer> output = new HashSet<>();

    Integer[] find(String newSentence) {
        List<String> wordsInSentence = Arrays.stream(newSentence.split("\\s+")).collect(Collectors.toList());

        for (String wordInSentence : wordsInSentence) {
            List<Integer> indexesForWord = wordsMap.get(wordInSentence);
            if (Objects.isNull(indexesForWord)) {
                continue;
            }
            indexesForWord.forEach(index -> {
                Integer occurence = occurencesByIndex.computeIfAbsent(index, k -> 0);
                int updatedOccurence = occurence + 1;
                if (updatedOccurence >= 3) {
                    output.add(index);
                } else {
                    occurencesByIndex.put(index, updatedOccurence);
                }
            });
        }

        return output.toArray(new Integer[0]);

    }

}

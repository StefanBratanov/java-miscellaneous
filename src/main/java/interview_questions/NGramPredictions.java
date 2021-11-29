package interview_questions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.math.RoundingMode.HALF_UP;

public class NGramPredictions {

    private static String testCase = "Mary had a little lamb its fleece was white as snow;\n" +
            "And everywhere that Mary went, the lamb was sure to go.\n" +
            "It followed her to school one day, which was against the rule;\n" +
            "It made the children laugh and play, to see a lamb at school.\n" +
            "And so the teacher turned it out, but still it lingered near,\n" +
            "And waited patiently about till Mary did appear.\n" +
            "\"Why does the lamb love Mary so?\" the eager children cry;\"Why, Mary loves the lamb, you know\" the teacher did reply.\"";

    public static void main(String[] args) throws IOException {

        InputStreamReader reader = new InputStreamReader(System.in, StandardCharsets.UTF_8);
        BufferedReader in = new BufferedReader(reader);
        String line;
        while ((line = in.readLine()) != null) {
            String[] splittedLine = line.split(",");
            int nGram = Integer.parseInt(splittedLine[0].trim());
            String text = splittedLine[1].trim();
            List<String> predictions = new LinkedList<>();
            String[] splitText = testCase.split("\\s+");
            for (int i = 0; i < splitText.length; i++) {
                String currentWord = stripOffNonAlphanumeric(splitText[i]);
                if (currentWord.equalsIgnoreCase(text)) {
                    int currentIndex = i + 1;
                    for (int ii = 0; ii < nGram - 1; ii++) {
                        if (currentIndex >= splitText.length) {
                            break;
                        }
                        String wordAfterUserText = stripOffNonAlphanumeric(splitText[currentIndex]);
                        predictions.add(wordAfterUserText);
                        currentIndex++;
                    }
                }
            }
            String output = getOutputFromPredictions(predictions);
            System.out.println(output);
        }
    }

    private static String getOutputFromPredictions(List<String> predictions) {
        Map<String, Long> occurencesByPrediction = predictions.stream()
                .collect(Collectors.groupingBy(String::toLowerCase,
                        Collectors.counting()));
        return occurencesByPrediction.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(entry -> entry.getKey() + "," + convertToPercentageAndPad(entry.getValue(), predictions.size()))
                .collect(Collectors.joining(";"));
    }

    private static BigDecimal convertToPercentageAndPad(long occurences, int totalNumberOfWords) {
        return new BigDecimal(occurences).divide(new BigDecimal(totalNumberOfWords), HALF_UP)
                .setScale(3, HALF_UP);
    }

    private static String stripOffNonAlphanumeric(String word) {
        return word.replaceAll("[^a-zA-Z0-9]", "").trim();
    }


}

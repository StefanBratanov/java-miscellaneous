package monty;

import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Paradox {

    private static final String SHEEP = "Sheep";
    private static final String CAR = "Car";

    public static void main(String[] args) {
        List<String> choices = Arrays.asList(SHEEP, SHEEP, CAR);

        int numberOfRuns = 100000000;

        List<String> yourChoices = Lists.newArrayList();
        List<String> otherChoices = Lists.newArrayList();

        for (int i = 0; i < numberOfRuns; i++) {

            shuffleList(choices);

            int randomChoice = randomChoice();
            int otherSheepIndex = getOtherSheepIndex(choices, randomChoice);

            String yourChoice = choices.get(randomChoice);
            String otherChoice = getOtherChoice(choices, otherSheepIndex, randomChoice);

            yourChoices.add(yourChoice);
            otherChoices.add(otherChoice);
        }

        long yourChoiceCarOccurence = calculateCarOccurence(yourChoices);
        long otherChoiceCarOccurence = calculateCarOccurence(otherChoices);

        System.out.println("Percentage for your choice: " + ((yourChoiceCarOccurence * 100) / numberOfRuns));
        System.out.println("Percentage for other choice: " + ((otherChoiceCarOccurence * 100) / numberOfRuns));
    }

    private static long calculateCarOccurence(List<String> choices) {
        return choices.stream()
                .filter(choice -> choice.equals(CAR))
                .count();
    }

    private static int randomChoice() {
        Random random = new Random();
        return random.ints(0, 3)
                .findFirst()
                .getAsInt();
    }

    private static String getOtherChoice(List<String> choices, int otherSheepIndex, int yourChoiceIndex) {
        for (int i = 0; i < choices.size(); i++) {
            if (i != otherSheepIndex && i != yourChoiceIndex) {
                return choices.get(i);
            }
        }
        throw new IllegalStateException("There is no other choice");
    }

    private static int getOtherSheepIndex(List<String> choices, int excludeIndex) {
        for (int i = 0; i < choices.size(); i++) {
            if (i != excludeIndex && choices.get(i).equals(SHEEP)) {
                return i;
            }
        }
        throw new IllegalStateException("There is no other sheep if you exclude index: " + excludeIndex);
    }

    private static void shuffleList(List<String> list) {
        for (int i = 0; i < 10; i++) {
            Collections.shuffle(list);
        }
    }
}

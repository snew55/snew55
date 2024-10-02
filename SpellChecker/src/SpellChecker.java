import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class SpellChecker {

    private HashMap<String, Integer> wordOccurrences;
    private HashSet<String> dictionaryWords;

    public SpellChecker() {
        this.wordOccurrences = calculateWordOccurrences("input.txt");
        this.dictionaryWords = loadHashSet("words.txt");
    }

    public HashSet<String> loadHashSet(String file) {

        HashSet<String> set = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                set.add(line.toLowerCase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return set;
    }

    public static HashMap<String, Integer> calculateWordOccurrences(String file) {

        HashMap<String, Integer> occurrences = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] words = line.toLowerCase().replaceAll("[.,!?]", "").split("\\s+");
                for (String word : words) {
                    if (!word.isEmpty()) {
                        occurrences.put(word, occurrences.getOrDefault(word, 0) + 1);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return occurrences;
    }

    public void printMenu() {

        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        while (choice != 4) {

            System.out.println("Document Spell Check Program");
            System.out.println("----------------------------");
            System.out.println("1: Spell Check your document (input.txt)");
            System.out.println("2: Print word frequency alphabetically");
            System.out.println("3: Print word frequency from greatest to least");
            System.out.println("4: Exit program");
            System.out.print("Your choice? ");

            try {
                choice = Integer.parseInt(scanner.nextLine());

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number 1-4.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.println(spellCheck("input.txt"));
                    break;
                case 2:
                    printFrequencyAlphabetically();
                    break;
                case 3:
                    printFrequencyByCount();
                    break;
                case 4:
                    System.out.println("Exiting program.");
                    break;
                default:
                    System.out.println("Invalid choice. Please choose a valid option.");
            }
        }
        scanner.close();
    }

    public String spellCheck(String file) {
        StringBuilder output = new StringBuilder();
        try (BufferedReader read = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = read.readLine()) != null) {
                String[] wordsInLine = line.split(" ");
                for (String word : wordsInLine) {

                    String leadingPunctuation = word.replaceAll("[a-zA-Z0-9].*$", "");
                    String trailingPunctuation = word.replaceAll("^.*[a-zA-Z0-9]", "");
                    String cleanWord = word.replaceAll("^[.,!?]+|[.,!?]+$", "");
                    String lowerCaseWord = cleanWord.toLowerCase();

                    if (!dictionaryWords.contains(lowerCaseWord)) {
                        output.append(leadingPunctuation).append("<").append(cleanWord).append(">").append(trailingPunctuation).append(" ");
                    }
                    else {
                        output.append(word).append(" ");
                    }
                }
                output.append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.toString();
    }


    private void printFrequencyAlphabetically() {

        TreeMap<String, Integer> sorted = new TreeMap<>(wordOccurrences);
        sorted.forEach((key, value) -> System.out.println(key + ": " + value));
    }

    private void printFrequencyByCount() {

        TreeMap<Integer, Set<String>> freqMap = new TreeMap<>(Collections.reverseOrder());
        wordOccurrences.forEach((word, count) -> {
            freqMap.computeIfAbsent(count, k -> new HashSet<>()).add(word);
        });

        freqMap.forEach((count, words) -> {
            System.out.println(count + ": " + String.join(", ", words));
        });
    }
}

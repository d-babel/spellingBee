import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        makeWords("", letters);
    }

    private void makeWords(String prefix, String remaining) {
        if (!prefix.isEmpty()) {
            words.add(prefix);
        }
        if (remaining.isEmpty()) {
            return;
        }
        for (int i = 0; i < remaining.length(); i++) {
            String newPrefix = prefix + remaining.charAt(i);
            String newRemaining = remaining.substring(0, i) + remaining.substring(i + 1);
            makeWords(newPrefix, newRemaining);
        }
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        words = mergeSort(words);
    }

    // Merge sort
    private ArrayList<String> mergeSort(ArrayList<String> list) {
        if (list.size() <= 1) {
            return list;
        }

        int mid = list.size() / 2;
        // Left
        ArrayList<String> left = new ArrayList<>();
        for (int i = 0; i < mid; i++) {
            left.add(list.get(i));
        }
        // Right
        ArrayList<String> right = new ArrayList<>();
        for (int j = mid; j < list.size(); j++) {
            right.add(list.get(j));
        }
        // Sort
        left = mergeSort(left);
        right = mergeSort(right);

        return merge(left, right);
    }

    // Merges into single sorted list
    private ArrayList<String> merge(ArrayList<String> left, ArrayList<String> right) {
        ArrayList<String> merged = new ArrayList<>();
        int i = 0;
        int j = 0;

        while (i < left.size() && j < right.size()) {
            // Which element goes first
            if (left.get(i).compareTo(right.get(j)) <= 0) {
                merged.add(left.get(i));
                i++;
            } else {
                merged.add(right.get(j));
                j++;
            }
        }
        // Remaining
        while (i < left.size()) {
            merged.add(left.get(i));
            i++;
        }
        // Remaining
        while (j < right.size()) {
            merged.add(right.get(j));
            j++;
        }
        return merged;
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        // Binary search through words
        int i = 0;
        while (i < words.size()) {
            // Remove
            if (!found(words.get(i))) {
                words.remove(i);
            } else {
                i++;
            }
        }
    }

    // Binary search if word exists
    private boolean found(String s) {
        int left = 0;
        int right = DICTIONARY_SIZE - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            int comparison = s.compareTo(DICTIONARY[mid]);
            if (comparison == 0) {
                return true;
            } else if (comparison < 0) {
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return false;
    }

    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}

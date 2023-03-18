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
 * @author Zach Blick, Jackson Deutch
 *
 * Written on March 17, 2023 for CS2 @ Menlo School
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
        permute("", letters);
    }

    // Permutes through letters and creates every possible combination
    public void permute(String word, String letters)
    {
        // Begin by adding the word that we pass in to words list
        words.add(word);

        // Base case
        if (letters.length() == 0)
        {
            return;
        }

        // Recursive step
        // Add each letter to word and then create a new recursive step for each word we make
        // Essentially a new branch (a new word) for each letter we add on
        // We then remove the letter from letters for no duplicate letters in word
        for (int i = 0; i < letters.length(); i++)
        {
            permute(word + letters.charAt(i), letters.substring(0,i) + letters.substring(i+1));
        }

    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort()
    {
        words = mergeSort(words, 0, words.size() - 1);
    }

    /* Merge sort is recursive algorithm that sorts the words ArrayList
     lexicographically and then returns the sorted ArrayList */
    public ArrayList<String> mergeSort(ArrayList<String> words, int low, int high)
    {
        // Base case
        if (high - low == 0)
        {
            ArrayList<String> newArr = new ArrayList<>();
            newArr.add(words.get(low));
            return newArr;
        }

        // Create midpoint
        // The midpoint is used to split up the array to allow for easier sorting
        int mid = (high + low) / 2;

        // Recursive step
        ArrayList<String> arrA = mergeSort(words, low, mid);
        ArrayList<String> arrB = mergeSort(words, mid + 1, high);
        return merge(arrA, arrB);
    }

    /* Merge is a supporting algorithm that that merges
     two ArrayLists of Strings together and then returns the combined list */
    public ArrayList<String> merge(ArrayList<String> arr1, ArrayList<String> arr2)
    {
        // Declare new ArrayList
        ArrayList<String> combinedList = new ArrayList<>();

        int i = 0;
        int j = 0;
        while (i < arr1.size() && j < arr2.size())
        {
            // Compare the leading values in arr1 and arr2
            // Whichever one is smaller lexicographically gets added to the combined list
            // We then increment the index we point to in that list
            int compResult = arr1.get(i).compareTo(arr2.get(j));
            if (compResult > 0)
            {
                combinedList.add(arr2.get(j));
                j++;
            }
            else
            {
                combinedList.add(arr1.get(i));
                i++;
            }
        }

        /* While loops to add the rest of the elements to the combined list
         since one list will still have remaining elements leftover after comps */
        while (i < arr1.size())
        {
            combinedList.add(arr1.get(i));
            i++;
        }
        while (j < arr2.size())
        {
            combinedList.add(arr2.get(j));
            j++;
        }
        return combinedList;
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
        for (int i = 0; i < words.size(); i++)
        {
            if(!binarySearch(words.get(i), 0, DICTIONARY_SIZE))
            {
                words.remove(i);
                i--;
            }
        }
    }

    // Returns true if the target word is found in dictionary, false if it is not
    public boolean binarySearch(String target, int low, int high)
    {
        int mid = (high + low) / 2;

        // Base cases
        if (low > high)
        {
            return false;
        }
        else
        {
            // Check if the midpoint in dictionary is our target word
            // If so, we know the target word is a valid word
            if (target.equals(DICTIONARY[mid]))
            {
                return true;
            }
        }

        // Recursion steps
        if (DICTIONARY[mid].compareTo(target) > 0)
        {
            /* If the word we look at in dictionary is lexicographically
            greater than target then we know the target
            word must be to the left of that midpoint, which allows us to recurse */
            return binarySearch(target, low, mid - 1);
        }
        else
        {
            return binarySearch(target, mid + 1, high);
        }
    }

    // Prints all valid words to wordList.txt
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

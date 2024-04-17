// CS 265 Final
// 4/30/2024
// Written by Alex Boehne, Kaitlyn Hoffenberger, Owen Ring, and Jenna Wolf

import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        ArrayList<String> argList = new ArrayList<>(Arrays.asList(args)); // Cast args to arraylist

        Scanner input = new Scanner(System.in); // Scanner obj

        String compedPass = "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824"; // hello in SHA256
        int palindromes = 0; // Palindrome counter

        comparePassword(compedPass, input); // Doesn't advance program until correct password

        FileReader f; // Init FileReader

        // Change the text file directory if used on different machine (specified on CLI with arg -p <PATH>)
        if (argList.contains("-p")) { // Checks for -p and sets filepath to following arg
            f = new FileReader(argList.get((argList.indexOf("-p") + 1)));
        } else {
            f = new FileReader("/home/faker/IdeaProjects/jTest/src/text.txt");
        }

        int min = 1; // min len variable
        int max = 2147483647; // max len variable

        if (argList.contains("-min")) { // Min parameter
            min = Integer.parseInt(argList.get((argList.indexOf("-min") + 1)));
        }

        if (argList.contains("-max")) { // Max parameter
            max = Integer.parseInt(argList.get((argList.indexOf("-max") + 1)));
        }


        long start = System.currentTimeMillis(); // Start system timer

        ArrayList<String> stList = new ArrayList<>(List.of(splitFile(f))); // Puts text file in string array

        if (argList.contains("-w")) { // Individual word parameter
            stList.clear(); // Clear list
            for (String arg : argList) { // Iterate through CLI words
                if (!arg.equals("-w")) {
                    stList.add(arg);
                }
            }
        }

        ArrayList<String> palindromeArray = new ArrayList<>(); // Init array of palindromes

        // Iterates through all words in stored list
        for (String word : stList) {
            if (word.length() < 2 && min == 1 && max == 2147483647) { // Exclude len 1 words
                continue;
            } else if (min != 1 || max != 2147483647) { // Check for min/max args
                if (word.length() < min || word.length() > max) {
                    continue;
                }
            }
            if (calcPalindrome(word)) { // Adds to palindrome list
                palindromeArray.add(word);
                palindromes++;
            }
        }

        long end = System.currentTimeMillis(); // End system timer
        long time = end - start; // Calc run time

        System.out.println(palindromes + " palindromes found in " + time + " ms, out of " + stList.size() + " words."); // Print output

        // List all palindromes found
        System.out.println("Would you like to list the palindromes found? (y/N)");
        String response = input.next();
        if (response.equals("y")) {
            System.out.println("\nPalindromes found:");
            for (String word : palindromeArray) {
                System.out.println(word);
            }
        }
    }

    // Function to determine if word is palindrome
    public static boolean calcPalindrome(String word) {
        if (word.charAt(0) != word.charAt(word.length() - 1)) { // If first and last characters don't match, not a palindrome
            return false;
        } else { // Recursively check the substring excluding first and last characters
            if(word.length() <= 2) {
                return true;
            }
            else {
                return calcPalindrome(word.substring(1, word.length() - 1));
            }
        }
    }

    //  Digests input string into SHA-256 hex byte array
    public static byte[] getSHA(String input) throws NoSuchAlgorithmException {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        return sha.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    //  Turns hex array into 256-byte len string
    public static String toHex(byte[] hash) {
        BigInteger num = new BigInteger(1, hash); // Generate BigInteger
        StringBuilder hexString = new StringBuilder(num.toString(16)); // Create StringBuilder in hex

        while (hexString.length() < 64) { // Pad string with 0s if 'len < 64'
            hexString.insert(0, '0');
        }
        return hexString.toString(); // Returns hashed string
    }
    //  Complete method for hashing string with SHA256
    public static String toHash(String h) throws NoSuchAlgorithmException {
        return toHex(getSHA(h));
    }

    public static boolean comparePassword(String rPass, Scanner i) throws NoSuchAlgorithmException {
        while (true) {
            System.out.print("Please input the password: ");
            String password = i.nextLine();

            if (toHash(password).equals(rPass)) {
                System.out.println("Your password is correct!\n");
                return true;
            } else {
                System.out.println("Your password is incorrect!");
            }
        }
    }

    public static String[] splitFile(FileReader fr) throws IOException {
        // list that holds strings of a file
        ArrayList<String> listOfStrings = new ArrayList<>();


        StringBuilder s = new StringBuilder();
        char ch;

        // Reads file
        while (fr.ready()) {
            ch = (char) fr.read();

            // Used to specify the delimiters
            if (ch == '\n' || ch == ' ' || ch == ',') {

                // Storing each string in arraylist
                listOfStrings.add(s.toString());

                // clearing content in string
                s = new StringBuilder();
            } else {
                s.append(ch);
            }
        }
        if (s.length() > 0) {

            // Appends last line of strings
            listOfStrings.add(s.toString());
        }
        // storing the data in arraylist to array (faster read times)
        return listOfStrings.toArray(new String[0]);
    }
}
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        ArrayList<String> argList = new ArrayList<String>(Arrays.asList(args)); // Cast args to arraylist

        Scanner input = new Scanner(System.in); // Scanner obj

        String compedPass = "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824"; // hello in SHA256
        int palindromes = 0; // Pallindrome counter

        comparePassword(compedPass, input); // Doesn't advance program until correct password

        // Init FileReader
        FileReader f = null;

        // Change the text file directory if used on different machine (specified on CLI with arg -p <PATH>)
        if (argList.contains("-p")) { // Checks for -p and sets filepath to following arg
            f = new FileReader(argList.get((argList.indexOf("-p") + 1)));
        } else {
            f = new FileReader("/home/faker/IdeaProjects/jTest/src/text.txt");
        }

        int min = 0; // min len variable
        int max = 2147483647; // max len variable

        if (argList.contains("-min")) { // Min parameter
            min = Integer.parseInt(argList.get((argList.indexOf("-min") + 1)));
        }

        if (argList.contains("-max")) { // Max parameter
            max = Integer.parseInt(argList.get((argList.indexOf("-max") + 1)));
        }


        long start = System.currentTimeMillis(); //

        String[] stList = splitFile(f); // Puts text file in string array

        ArrayList<Character> charArray = new ArrayList<>();
        ArrayList<String> palindromeArray = new ArrayList<>();

        // Iterates through all words in stored list
        for (String word : stList) {
            if (word.length() < 2 && min == 0 && max == 0) { // Exclude len 1 words
                continue;
            } else if (min != 0 && max != 2147483647) { // Check for min/max args
                if (word.length() < min || word.length() > max) {
                    continue;
                }
            }
            for (Character c : word.toCharArray()) { // Turn word into character array
                charArray.add(c);
            }
            if (calcPalindrome(charArray)) { // Adds to palindrome list
                palindromeArray.add(word);
                palindromes++;
            }
            charArray.clear(); // Clears charArray
        }

        long end = System.currentTimeMillis();
        long time = end - start; // Calc run time

        System.out.println(palindromes + " palindromes found in " + time + " ms"); // Print output

        // List all pallindromes found
        System.out.println("Would you like to list the palindromes found? (y/N)");
        String response = input.next();
        if (response.equals("y")) {
            System.out.println("\nPalindromes found:");
            for (String word : palindromeArray) {
                System.out.println(word);
            }
        }
    }

    // Function to determine if word is pallindrome
    public static boolean calcPalindrome(ArrayList<Character> t) {
        for (int i = 0; i <= Math.ceil(t.size() / 2); i++) {
            // Converts characters to lowercase
            Character c = Character.toLowerCase(t.get(i));
            Character c2 = Character.toLowerCase(t.get(t.size() - 1 - i));

            if (!c.equals(c2)) { // Checks for 2 letters at opposite ends
                return false;
            }
        }
        return true;
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
        ArrayList<String> listOfStrings = new ArrayList<String>();


        String s = "";
        char ch;

        // Reads file
        while (fr.ready()) {
            ch = (char) fr.read();

            // Used to specify the delimiters
            if (ch == '\n' || ch == ' ' || ch == ',') {

                // Storing each string in arraylist
                listOfStrings.add(s);

                // clearing content in string
                s = "";
            } else {
                s += ch;
            }
        }
        if (!s.isEmpty()) {

            // Appends last line of strings
            listOfStrings.add(s);
        }
        // storing the data in arraylist to array (faster read times)
        return listOfStrings.toArray(new String[0]);
    }
}
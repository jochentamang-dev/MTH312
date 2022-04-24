package Ciphers;

import java.util.Arrays;
import java.util.Scanner;

public class playFair {
    /**
     * playFair takes key and message to create a playfair square.
     * Playfair square is then used to encrypt and decrypt messages.
     * 
     */

    public static void main(String[] args) {
        // scanner for input
        Scanner S = new Scanner(System.in);
        // phrase to be used as a key
        System.out.print("ENTER PHRASE TO BE USED AS A KEY: ");
        String key = S.nextLine().toUpperCase().replaceAll("[^a-zA-Z]", "");
        // letter to be removed from the key
        System.out.print("\nENTER A LETTER TO BE REMOVED: ");
        char removed = S.nextLine().charAt(0);
        key = key.replaceAll(Character.toString(removed), "");
        //message formatting
        System.out.print("\nENTER YOUR MESSAGE TO BE ENCRYPTED (please no " + removed +"): " );
        String message = S.nextLine().toUpperCase().replaceAll("[^a-zA-Z0-9]", "");
        message = message.replaceAll(Character.toString(removed), "");
        message = formatMessage(message);
        System.out.println( "\nYour message: "+ message);
        // Setting up the board
        key = addMissingLetter(noRepeat(key));
        System.out.println("\nYour key: " + key + "\n\nplayfair square:");
        char[][] board = fillBoard(key);
        outputTable(board);
        //encryption and decryption
        System.out.println( "\nCipher text: "+ encrypt(message, board));
        System.out.println("\nDecrypted text: " + decrypt(encrypt(message, board), board));

    }
    /** method to remove repeated letters in the key*/
    public static String noRepeat(String key) {
        /** returned later as a key without duplicate */
        String output = "";
        /** Loops through the key to check for duplicate */
        for (int i = 0; i < key.length(); i++) {
            int j;
            for (j = 0; j < i; j++) {
                if (key.charAt(i) == key.charAt(j)) {
                    break;
                }
            }
            if (j == i) {
                output += key.charAt(i);
            }
        }
        return output;
    }
    /** method to add missing letters to the key */
    public static String addMissingLetter(String key) {
        /** counter variable to check from A - Z */
        char Ascii = 'A';
         /** checks from A - Z for missing letters */
        while (Ascii <= 'Z') {
            boolean exists = false;
            for (int i = 0; i < key.length(); i++) {
                if (Ascii == key.charAt(i)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                key += Ascii;
            }
            Ascii++;
        }
        return key;
    }
    /** method builds the playfair square */
    public static char[][] fillBoard(String key) {
        /** empty playfair square 5 x 5 */
        char[][] board = new char[5][5];
        int count = 0;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                board[i][j] = key.charAt(count);
                count++;
            }
        }
        return board;

    }
    /** method to format message (i.e. take care of repeats in the pair of letter in a block 
     * and adding "X" to single character at the end ) */
    public static String formatMessage(String Message) {
        for (int i = 0; i < Message.length() - 1; i += 2) {
            if (Message.charAt(i) == Message.charAt(i + 1))
                Message = Message.substring(0, i + 1) + 'X' + Message.substring(i + 1);
        }
        if ((double) Message.length() % 2 != 0) {
            Message += "X";
        }
        return Message;
    }
    /**
     * method to encrypt the message using playfair square
     * @param message clear text that will be encrypted
     * @param board playfair square
     * @return clear text after encryption
     */
    public static String encrypt(String message, char[][] board) {
        String encrypted = "";
        /** loops through every other character in the clear text */
        for (int i = 0; i < message.length() - 1; i += 2) {
            /* location[o] holds row of first chaarcter in a block, [1] -> column for first character,
            [2] -> row for second character, [3] -> column for the second character */
            int[] location = search(board, message.charAt(i), message.charAt(i + 1));
            /* if two character are in the same row */
            if (location[0] == location[2]) {
                location[1] = (location[1] + 1) % 5;
                location[3] = (location[3] + 1) % 5;
            }
            /* if both characters are in the same column */
            else if (location[1] == location[3]) {
                location[0] = (location[0] + 1) % 5;
                location[2] = (location[2] + 1) % 5;
            }

            /* if both the characters are in different rows
             and columns */
            else {
                int temp = location[1];
                location[1] = location[3];
                location[3] = temp;
            }
            encrypted = encrypted + board[location[0]][location[1]]
                    + board[location[2]][location[3]];
        }

        return encrypted;
    }
    /** method to search for locations in playfair swuare (row and column index) 
     * of two characters in a block */
    public static int[] search(char board[][], char a, char b) {
        int[] location = new int[4];
        /** loops through each row in the square */
        for (int i = 0; i < 5; i++) {
            /** loops through each column in the square */
            for (int j = 0; j < 5; j++) {
                /** location of first character */
                if (board[i][j] == a) {
                    location[0] = i;
                    location[1] = j;
                    /** location of second character */
                } else if (board[i][j] == b) {
                    location[2] = i;
                    location[3] = j;
                }
            }
        }
        return location;
    }
    /**
     * Method to decrypt the cipher text using playfair square 
     * @param message the cipher text
     * @param board playfair square
     * @return the decrypted clear text
     */
    public static String decrypt(String message, char[][] board) {
        String decrypted = "";
        /**loops through every other character in the cipher text */
        for (int i = 0; i < message.length() - 1; i += 2) {
            // location[o] holds row first value, [1] -> column for first value
            int[] location = search(board, message.charAt(i), message.charAt(i + 1));
            // two characters in a block in same row
            if (location[0] == location[2]) {
                location[1] = (location[1] + 4) % 5;
                location[3] = (location[3] + 4) % 5;
            }

            // if both characters are in the same column
            else if (location[1] == location[3]) {
                location[0] = (location[0] + 4) % 5;
                location[2] = (location[2] + 4) % 5;
            }

            // if both characters are in different rows and columns
            else {
                int temp = location[1];
                location[1] = location[3];
                location[3] = temp;
            }
            decrypted = decrypted + board[location[0]][location[1]] + board[location[2]][location[3]];
        }

        return decrypted;
    }

    /** method to output the table */
    public static void outputTable(char[][] table) {
        for (char[] row : table) {
            System.out.println(Arrays.toString(row));
        }
    }
}

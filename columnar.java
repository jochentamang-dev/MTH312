package Ciphers;

import java.util.Arrays;
import java.util.Scanner;
/**
 * columnar takes a key and string and uses transposition to encrypt
 * the message. The reciever will recieve a encrypted message and the   * key.
 */

public class columnar {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.print("Enter your secret key:");
        /* key for our cipher (only alphabet allowed) */
        String key = s.nextLine().toUpperCase().replaceAll("[^a-zA-Z0-9]", "");
        /* due to error, removes duplicate in the key */
        key = removeDuplicate(key.toCharArray(), key.length());
        System.out.print("\nEnter your secret message: ");
        /* message to be encrypted letter, only letters allowed */
        String message = s.nextLine().toLowerCase().replaceAll("[^a-zA-Z0-9]", "");
        /* empty columnar table */
        char[][] table = new char[(int) Math.ceil(message.length() / (double) key.length()) + 1][key.length()];
        /* builds columnar table */
        table = buildTable(table, message, key);
        System.out.println("\nHERE IS THE COLUMNAR TABLE PRIOR TO TRANSPOSITION:");
        outputTable(table);
        /* sorts columar table column by alphabetical order */
        table = sortTable(table);
        System.out.println("\nHere is the table after sorting by alphabetical order and TRANSPOSITION:");
        outputTable(table);
        System.out.println("\nThis is what the receiver gets: ");
        outputTableAsMessage(table);
        System.out.println("\n\nThis is the decrypted message:");
        /* decryption */
        outputTableAsMessage(decrypt(table, key));
    }

    /** method to remove duplicate from a string */
    public static String removeDuplicate(char[] str, int length) {
        // Used as index in the new string
        int index = 0;
        // Traverse through all characters
        for (int i = 0; i < length; i++) {

            int j;
            // Check if str[i] is present before it
            for (j = 0; j < i; j++) {
                if (str[i] == str[j]) {
                    break;
                }
            }
            // If not present, then add it to
            // result.
            if (j == i) {
                str[index++] = str[i];
            }
        }
        return String.valueOf(Arrays.copyOf(str, index));
    }
    /** function to output the table as a message */
    public static void outputTableAsMessage(char[][] table) {
        //goes through each row left to right and outputs the character
        for (int row = 1; row < table.length; row++) {
            for (char x : table[row]) {
                if (x == ' ') {
                    continue;
                }
                System.out.print(x);
            }
        }
    }
    /** function to output the table */
    public static void outputTable(char[][] table) {
        for (char[] row : table) {
            System.out.println(Arrays.toString(row));
        }
    }
    /**
     * builds the columnar table
     * @param table empty columnar table, same table is returned after the function fills the table
     * @param message message to be encrypted later
     * @param key key for the encryption
     * @return columnar table
     */
    public static char[][] buildTable(char[][] table, String message, String key) {
        /** variable to track each character in the message */
        int messageCounter = 0;
        /** loops through every row of the table */
        for (int i = 0; i < table.length; i++) {
            /** loops through every column of the table */
            for (int j = 0; j < table[0].length; j++) {
                /** if it is the first row, then table will hold the key */
                if (i == 0) 
                {
                    table[i][j] = key.charAt(j);
                } 
                /** for every other row, maps each character in the message to the table */
                else 
                {
                    table[i][j] = message.charAt(messageCounter);
                    if (messageCounter == message.length() - 1) 
                    {
                        return table;
                    }
                    messageCounter++;
                }
            }
        }
        return table;
    }
    /**
     * function to sort the column of a columnar table by alphabetical order of the key.
     * VERY similar to bubble sort
     * @param table the filled columnar table
     * @return columnar table in alphabetical order 
     */
    public static char[][] sortTable(char[][] table) {
        /** empty array that is same size as table array */
        char[][] tableSorted = new char[table.length][table[0].length];
        /** copies table array onto tableSorted array */
        for (int i = 0; i < tableSorted.length; i++) {
            System.arraycopy(table[i], 0, tableSorted[i], 0, tableSorted[i].length);
        }
        /** loops through every column of the table */
        for (int i = 0; i < tableSorted[0].length; i++) {
            /** loops through every column after the first (i) column */
            for (int j = i + 1; j < tableSorted[0].length; j++) {
                /** if the previous column's first-row character is "bigger" than the current column's, then
                 * switches the position and every value of previous column with the current column.
                  */
                if ((int) tableSorted[0][i] > (int) table[0][j]) {
                    char[] column = getColumn(tableSorted, tableSorted.length, i);
                    switchColumns(tableSorted, j, i, column);
                }
            }
        }
        return tableSorted;
    }
    /** function that returns an array that holds all the index of one column */
    public static char[] getColumn(char[][] table, int rows, int column) {
        char[] columnArray = new char[rows];
        /** loops through "every" row */
        for (int i = 0; i < rows; i++) {
            /** adds the value in one particular column and each row to columnArray */
            columnArray[i] = table[i][column];
        }
        return columnArray;
    }
    /** function that switches two column */
    private static void switchColumns(char[][] table, int fIndex, int sIndex, char[] cSwitch) 
    {
        /**loops through every row */
        for (int i = 0; i < table.length; i++) 
        {
            /** column "sIndex" gets assigned the column "fIndex" values */
            table[i][sIndex] = table[i][fIndex];
            /** column "fIndex" gets assigned the value in cSwitch, which we got from getColumn() method  */
            table[i][fIndex] = cSwitch[i];
        }
    }
    /**
     * decryption for the ciphertext
     * @param table columnar transposition table after transposition
     * @param key key for our cipher
     * @return the decrypted (or original) columnar table prior after undoing transposition
     */
    public static char[][] decrypt(char[][] table, String key) {
        /** empty table with same size as our columnar table */
        char[][] tableDecrypt = new char[table.length][table[0].length];
        /** loops through each column in the transposition table */
        for (int i = 0; i < tableDecrypt[0].length; i++) {
            /** finds the actual or original position of that particular column */
            int oPosition = findColumn(table, key.charAt(i));
            /** adds/moves that particular column values to its original column index */
            addToColumn(table, tableDecrypt, oPosition, i);

        }
        return tableDecrypt;
    }
    /**
     * a function that adds values to a particular column of a table
     * @param table columnar table after transposition
     * @param empty "empty" columnar table
     * @param fIndex original position/column index of a column
     * @param sIndex current position/column index of a column
     */
    public static void addToColumn(char[][] table, char[][] empty, int fIndex, int sIndex) 
    {
        /** loops through each row in the columnar table */
        for (int i = 0; i < table.length; i++) 
        {
            /** original column index of empty table recieves all the values of current column index of columnar table */
            empty[i][sIndex] = table[i][fIndex];
        }

    }
    /** 
     * function for finding the original location of a column.
     * c represents a character in the key
     */
    public static int findColumn(char[][] table, char c) {
        /** loops through each column in the table */
        for (int i = 0; i < table[0].length; i++) 
        {
            /** if the value of first row and a particular column i matches
             * c or a character in the key, then that is the original location of a column.
             */
            if (table[0][i] == c) {
                return i;
            }
        }
        return -1;
    }
}

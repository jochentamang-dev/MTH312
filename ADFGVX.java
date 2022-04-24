package Ciphers;

import java.util.Arrays;
import java.util.Scanner;
/**
 * ADFGVX takes message and key and uses substitution 
 * and transposition to encrypt the message. The reciever recieves the * encrypted message and needs the key to decrypt.
 */
public class ADFGVX {
    /** the translation table */
    static char[][] translationTable = { { ' ', 'A', 'D', 'F', 'G', 'V', 'X' },
            { 'A', 'O', 'R', '9', 'E', '5', 'X' },
            { 'D', 'P', 'L', 'K', 'S', 'Z', 'A' },
            { 'F', 'B', '6', 'J', 'G', 'W', '3' },
            { 'G', '4', 'U', 'N', 'V', 'Q', 'T' },
            { 'V', '0', 'H', 'M', 'C', '8', 'D' },
            { 'X', 'I', 'F', '1', '2', '7', 'Y' } };

    public static void main(String[] args)
    {
        /**output and input */
        Scanner s = new Scanner(System.in); 
        System.out.println("Here is the translation table used:");
        outputTable(translationTable);
        System.out.print("\nFor this code, we won't have repeated letter in the key.\n\nENTER YOUR KEY: ");
        String key = s.nextLine().toUpperCase().replaceAll("[^a-zA-Z0-9]", "");
        /** no duplicate due to duplicate character error */
        key = removeDuplicate(key.toCharArray(), key.length());
        System.out.print("\nENTER YOUR MESSAGE: ");
        String message = s.nextLine().toUpperCase().replaceAll("[^a-zA-Z0-9]", "");   
        /** encrypted array that holds the transposition table */
        char[][] encrypted = encrypt(message, key);
        System.out.println("\nHERE IS YOUR TABLE BEFORE TRANSPOSITION\n");
        columnar.outputTable(encrypted);
        System.out.println("\nHERE IS YOUR TABLE AFTER TRANSPOSITION\n");
        /** encrypted array now holds the encrypted message after transposition */
        encrypted = TRANSPOSITION(encrypted, key);
        columnar.outputTable(encrypted);
        String encrypt = outputEncrypt(encrypted);
        /** holds the decrypted transposition table in original order */
        char[][] decryptedTable = decrypt(encrypted, key);
        String decryptedMessage = decryptTable(decryptedTable);
        System.out.println("\nHERE IS THE ENCRYPTED MESSAGE:\n" + encrypt + "\n\nHERE IS THE DECRYPTED MESSAGE:\n" + decryptedMessage);

    }
    /**
     * this method creates a decrypted transposition table from transposition table (creates the transposition table in original letter) and returns the decrypted message.
     * @param table transposition table
     * @return the decrypted message
     */
    public static String decryptTable(char[][] table) {
        /** returned later */
        String message = "";
        /** holds the encrypted message, not the table */
        String encrypted = "";
        /** maps the encrypted message to encrypted variable
         * from the transposition table
         */
        for (int r = 1; r < table.length; r++) {
            for (int c = 0; c < table[0].length; c++) {
                encrypted += table[r][c];
            }
        }
        /** loops through two character in encrypted message at a time */
        for (int i = 0; i < encrypted.length() - 1; i += 2) {
            char one = encrypted.charAt(i);
            char two = encrypted.charAt(i + 1);
            /** gets the intersected character by looking at the first row and first column */
            message += translationTable[getRowCol(one, two)[0]][getRowCol(one, two)[1]];

        }
        return message;

    }
    /** method returns an array that holds the row and column of two letter. The array is used to find the intersection of the two letters */
    public static int[] getRowCol(char one, char two) {
        int[] RC = new int[2];
        /**loops through translationTable first row */
        for (int r = 0; r < translationTable.length; r++) {
            /**index of the row */
            if (one == translationTable[0][r]) {
                RC[0] = r;
                break;
            }
        }
        /** loops through the first column of transposition table */
        for (int c = 0; c < translationTable[0].length; c++) {
            /** index of the column */
            if (two == translationTable[c][0]) {
                RC[1] = c;
                break;
            }
        }
        return RC;
    }
    /**
     * This method undo the transposition for transposition table.
     * @param table transposition table in alphabetical order
     * @param key key of the ADFGVX cipher
     * @return original transpositiion table
     */
    public static char[][] decrypt(char[][] table, String key) {
        int[] position = new int[key.length()];
        /**loops through the column of transposition table */
        for (int c = 0; c < table[0].length; c++) {
            /** loops through each character in the key */
            for (int i = 0; i < key.length(); i++) {
                /** gets the column index of a key character */
                if (table[0][c] == key.charAt(i)) {
                    position[c] = i;
                    break;
                }
            }
        }
        /**goes to transposition 2 */
        table = TRANSPOSITION2(table, position);
        return table;
    }
    /**
     * This method is used for transposition step in 
     * the encryption.
     */
    public static char[][] TRANSPOSITION(char[][] table, String key) {
        /** sorts key to alphabetical letter */
        key = sortString(key);
        /** holds the column index of each column */
        int[] position = new int[key.length()];
        /** loops through the column */
        for (int c = 0; c < table[0].length; c++) {
            /** loops through the key string */
            for (int i = 0; i < key.length(); i++) {
                /** checks if the first row and c column is same as the key position/index. Then, assigns the original position to the position array */
                if (table[0][c] == key.charAt(i)) {
                    position[c] = i;
                    break;
                }
            }
        }
        /** sends to TRANSPOSITION2 method to transpose the columns */
        table = TRANSPOSITION2(table, position);
        return table;
    }
    /** this method transposes the column of table 2d array by looking at the position array */
    public static char[][] TRANSPOSITION2(char[][] table, int[] position) {
        /** empty table2 */
        char[][] table2 = new char[table.length][table[0].length];
        /** loops through each row and column of the transposition table */
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[0].length; j++) {
                /** adds the column to the original index/position */
                table2[i][position[j]] = table[i][j];
            }
        }
        return table2;
    }
    /**
     * This method takes message and a key, and performs translation. * Also puts the translated message into transposition table.
     * @param message message that will be encrypted
     * @param key secret key used for encryption (transposition)
     * @return transposition table without transposition
     */
    public static char[][] encrypt(String message, String key) {
        String encrypt = "";
        /** holds the row, column index */
        int[] coord = new int[2];
        /** loops through the message character by character */
        for (int i = 0; i < message.length(); i++) {
            /** gets the position of the character */
            coord = getCoord(message.charAt(i));
            /** adds the corresponding digraph from translation table to the encrypt string */
            encrypt = encrypt + translationTable[coord[0]][0] + translationTable[0][coord[1]];
        }
        /** used to keep track of encrypted character below */
        int Counter = 0;
        /** table used for the transposition later */
        char[][] table = new char[(int) Math.ceil(encrypt.length() / (double) key.length()) + 1][key.length()];
        /** loops through the row of the table */
        for (int r = 0; r < table.length; r++) {
            /** loops through the column of the table */
            for (int c = 0; c < table[0].length; c++) {
                /** if it is the first row, then adds the key on top (first row) */
                if (r == 0) {
                    table[r][c] = key.charAt(c);
                } 
                /** for every other row adds the translated character to the table. */
                else 
                {
                    table[r][c] = encrypt.charAt(Counter);
                    /** if the loop is done going through the translated character, then returns the transpositon table. */
                    if (Counter == encrypt.length() - 1) 
                    {
                        return table;
                    }
                    Counter++;
                }
            }
        }
        return table;
    }
    /**
     * This method returns array that holds the row, column index of * character in the translation table
     */
    public static int[] getCoord(char c) {
        /** holds the row, column index */
        int[] coord = new int[2];
        /** loops through the rows and column of translation table.
         * Skips the first row and first column, since that's not used * this portion.
         */
        for (int i = 1; i < translationTable.length; i++) {
            for (int j = 1; j < translationTable[0].length; j++) {
                /** finds the row and column of a character and returns it. */
                if (translationTable[i][j] == c) {
                    coord[0] = i;
                    coord[1] = j;
                    return coord;
                }
            }
        }
        return coord;
    }

    /** sorts the string by alphabetical order */
    public static String sortString(String key) {
        char temp[] = key.toCharArray();
        Arrays.sort(temp);
        return new String(temp);
    }

    /** changes the transpotion table into a encrypted message */
    public static String outputEncrypt(char[][] table) {
        /** returned later */
        String encrypted = "";
        /** loops through the rows and column of the transposition table and add each value to the encrypted string. */
        for (int c = 0; c < table[0].length; c++) {
            for (int r = 1; r < table.length; r++) {
                encrypted = encrypted + table[r][c];
            }
        }
        return encrypted;

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

    /** method to output the table */
    public static void outputTable(char[][] table) {
        for (char[] row : table) {
            System.out.println(Arrays.toString(row));
        }
    }

}

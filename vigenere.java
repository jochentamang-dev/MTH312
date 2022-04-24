package Ciphers;

import java.util.Arrays;
import java.util.Scanner;

/**
 * vigenere takes the message and key phrase and encrypts the message with key
 * and the vigere table. 
 */
public class vigenere {
  public static void main(String[] args) {
    //builds the vigenere square
    char vSquare[][] = builtTable();
    System.out.println("Vigenere square: ");
    outputTable(vSquare);
    //input
    Scanner S = new Scanner(System.in);
    System.out.print("\nEnter a message to be encrypted: ");
    String message = S.nextLine().toUpperCase().replaceAll("[^a-zA-Z]", "");
    System.out.print("\nEnter a key for the encryption: ");
    String key = S.nextLine().toUpperCase().replaceAll("[^a-zA-Z]", "");
    System.out.println("\nMessage: " + message + "\n\nKey: " + key);
    //build message and key array
    char messageAndkey[][] = buildEncryptDecrypt(message, key);
    String cipherText = encrypt(messageAndkey, vSquare);
    //outputs
    System.out.println("\nHere is the substitution mapping table:");
    outputTable(messageAndkey);
    System.out.println("\nHere is the cipher text: " + cipherText);
    System.out.println( "\nHere is the decrypted text: "+ decrypt(vSquare, cipherText, key));

  }
  /**
   * method for building the vigenere Table
   * @return the vigenere Table
   */
  public static char[][] builtTable() {
    /** empty 2d array, 26 x 26  */
    char[][] vTable = new char[26][26];
    char start = 'B';
    //loops through the rows and columns of vTable
    for (int i = 0; i < 26; i++) {
      //goes row by row and fills the array
      char rowCounter = start;
      for (int j = 0; j < 26; j++) {
        vTable[i][j] = rowCounter;
        rowCounter = (char) ((((int) rowCounter - 64) % 26) + 65);
      }
      start = (char) ((((int) start - 64) % 26) + 65);
    }
    return vTable;
  }
  /**
   * Method for building array with 2 rows. First row holds the message and second 
   * row holds the corresponding key
   * @param message clear text
   * @param key key phrase
   * @return array that holds the message and corresponding key characters
   */
  public static char[][] buildEncryptDecrypt(String message, String key) {
    //2d array with 2 row and length of message
    char messageAndkey[][] = new char[2][message.length()];
    int counMessage = 0;
    int countKey = 0;
    //loops through every row and column of messageAndKey array
    for (int i = 0; i < 2; i++) {
      for (int j = 0; j < message.length(); j++) 
      {
        //fills the first row with message
        if (i == 0) 
        {
          messageAndkey[i][j] = message.charAt(counMessage);
          counMessage++;
        } 
        //fills the second row with key
        else 
        {
          messageAndkey[i][j] = key.charAt(countKey);
          countKey = (countKey + 1) % key.length();
        }

      }
    }

    return messageAndkey;
  }
  /**
   * Method that substitutes characters of message for corresponding 
   * character in the vigere square.
   * @param messageAndKey array that holds message and key
   * @param board vigenere square
   * @return cipher text
   */
  public static String encrypt(char[][] messageAndKey, char[][] board) {
    //returned later as cipher text
    String encryptedString = "";
    //loops through the column of messageAndKey array
    for (int i = 0; i < messageAndKey[0].length; i++) {
      //gets the corresponding message and key from the array
      char message = messageAndKey[0][i];
      char key = messageAndKey[1][i];
      //finds the point of intersection on the vigenere square
      char encryptedChar = findChar(board, message, key);
      encryptedString += encryptedChar;
    }
    return encryptedString;
  }
  /**
   * Method for finding intersection and corresponding character on the 
   * vigenere square
   * @param board vigenere square
   * @param message a character of the message
   * @param key a corresponding key of the message
   * @return the corresponding character for substituion on the vigenere square
   */
  public static char findChar(char[][] board, char message, char key) {
    //column index of the message character on last row of the vigenere square
    int column = (int) message - 65;
    //loops through the first row of the vigenere square
    for (int i = 0; i < 26; i++) {
      //finds the row index of the key character on the first column of vSquare
      if (board[i][0] == key) {
        return board[i][column];
      }
    }
    return ' ';
  }
  /**
   * Method for decrypting the cipher text.
   * @param vTable vigenere square
   * @param message ciphertext
   * @param key key phrase
   * @return clear/original text
   */
  public static String decrypt( char[][] vTable, String message, String key)
  {
    //used later to find the corresponding letter in the clear text from the
    //cipher text
    int row,col;
    String clear= "";
    //counter to keep track of the key
    int kCounter = 0;
    //loops through the message
    for(int i = 0; i < message.length() ; i++)
    {
      //row index of the corresponding key character
      row = findRow(vTable, key.charAt(kCounter));
      //column index of the corresponding clear character
      col = findCol(vTable, message.charAt(i), row);
      //location of the corresponding clear character
      clear+=vTable[vTable.length - 1][col];
      kCounter = (kCounter + 1) % key.length();
    }
    return clear;
  }
  /** 
   * method for finding row index of a character in the first column
   */
  public static int findRow(char[][] vTable, char c)
  {
    //loops through every row in the first column and returns the
    //row index of the character c
    for(int r = 0; r < vTable.length; r++)
      {
        if(vTable[r][0] == c)
        {
          return r;
        }
      }
      return -1;
  }
  /**
   * method for finding column index of a character in the given row r
   */
  public static int findCol(char[][] vTable, char c,int r)
  {
    //loops through the column of vigenere square
    for(int col = 0; col < vTable[0].length; col++)
      {
        //find the column index of character c in a particular row
        if(vTable[r][col] == c)
        {
          return col;
        }
      }
      return -1;
  }
  /** method to output the table */
  public static void outputTable(char[][] table) {
    for (char[] row : table) {
      System.out.println(Arrays.toString(row));
    }
  }
}

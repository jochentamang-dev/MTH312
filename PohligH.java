package Ciphers;

import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;
/**
 * PohligHellman takes a message, prime number, and e value. It outputs 
 * the encrypted message as numbers 
 * as well as the decrypted message after translation.
 */

public class PohligH {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.print("Enter a message: ");
        String message = s.nextLine().replaceAll(" ", "");
        System.out.print("\nEnter your prime number: ");
        int prime = s.nextInt();
        //checking if prime
        while (!checkPrime(prime)) {
            System.out.print("\n" + prime + " is not a prime.\nEnter a different number: ");
            prime = s.nextInt();
        }
        int primeUno = prime - 1;
        System.out.print("\nChoose an integer e where 0 < e < prime - 1\nand gcf(e,prime-1) == 1: ");
        int e = s.nextInt();
        //checking if e if gcf of e and prime - 1 is 1
        while (egcf(e, primeUno)[0] != 1) {
            System.out.print("\ngcf of " + e + " and " + primeUno + " is not 1.\nEnter again: ");
            e = s.nextInt();
        }
        //encrypted message
        String eMessage = encrypt(message, BigInteger.valueOf(e), BigInteger.valueOf(prime));
        System.out.println("\nMessage:\n" + eMessage);
        //dvalue
        int d = egcf(prime - 1, e)[2];
        System.out.println("\nd = " + d);
        //decrypted message
        System.out.println("\n" + decrypt(eMessage, BigInteger.valueOf(d), BigInteger.valueOf(prime)));
    }
    /**
     * A method for decrypting cipher numbers.
     * @param eMessage encrypted message
     * @param d d value
     * @param prime prime number
     * @return decrypted/clear text
     */

    public static String decrypt(String eMessage, BigInteger d, BigInteger prime) {
        //returned later as the clear text
        String message = "";
        //counter
        int j = 0;
        //loops through the encrypted text
        for (int i = 0; i < eMessage.length(); i++) {
            //check if there is a space and gets the substring from j index to the i index
            if (eMessage.charAt(i) == ' ') {
                int x = Integer.parseInt("" + eMessage.substring(j, i));
                //modular exponentation
                BigInteger b = (BigInteger.valueOf(x)).modPow(d, prime);
                //comvering from int to char
                int AsciiNum = b.intValue();
                char AsciiChar = (char) AsciiNum;
                //adds to the message string
                message += AsciiChar;
                j = i + 1;
            //check if we are currently at the last index
            } else if (i == eMessage.length() - 1) {
                //gets the string from previous space to the last index
                int x = Integer.parseInt("" + eMessage.substring(j, i + 1));
                //modular exponentation
                BigInteger b = (BigInteger.valueOf(x)).modPow(d, prime);
                int AsciiNum = b.intValue();
                char AsciiChar = (char) AsciiNum;
                //adds to the message string
                message += AsciiChar;
            }
        }
        return message;

    }
    /**
     * method for encrypting the message
     */
    public static String encrypt(String message, BigInteger e, BigInteger prime) {
        //returned later as the cipher numbers
        String encryption = "";
        //loops through the message
        for (int i = 0; i < message.length(); i++) {
            //gets the ASCII value of a character, then performs modular exponentation using e and prime number
            encryption += BigInteger.valueOf((int) message.charAt(i)).modPow(e, prime) + " ";
        }
        return encryption;
    }
    /**
     * extended euclidean algorithm for finding gcf and inverses
     */
    public static int[] egcf(int x, int y) {
        int m1[] = new int[] { x, 1, 0 };
        int m2[] = new int[] { y, 0, 1 };
        int result[] = new int[3];
        // place holder
        result[0] = -1;
        // loops until m1 - m2 reaches zero
        while (result[0] != 0) {
            // the multiplier for m2
            int multiplier = m1[0] / m2[0];
            for (int i = 0; i < 3; i++) {
                // temp matrix for second matrix multiplied
                int temp[] = new int[3];
                temp[i] = multiplier * m2[i];
                result[i] = m1[i] - temp[i];
            }
            // swap matrixes
            for (int i = 0; i < 3; i++) {
                m1[i] = m2[i];
                m2[i] = result[i];
            }
        }
        return m1;
    }
    /**
     * method for checking if number is a prime
     */
    public static boolean checkPrime(int p) {
        //if the number is 0 or 1
        if (p == 0 || p == 1)
            return false;
        //if it is 2
        else if (p == 2)
            return true;
        else {
            //loops through every number / 2
            for (int i = 2; i <= p / 2; i++) {
                //check if the number is divisible
                if (p % i == 0)
                    return false;
            }
        }
        //passes all the false statement, so the number is a prime
        return true;
    }
}

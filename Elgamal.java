package Ciphers;

import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;
/** 
 * Elgamal takes (p,g,b) --> (p,g,B)
 * For encryption: r nonce --> R
 * For decryption: R, prime, b 
 * THIS PROGRAM IS NOT MADE TO HANDLE SUPER LARGE NUMBERS AS THIS
 * IS JUST A DEMONSTRATION OF Elgamal
 */

public class Elgamal {
    public static void main(String[] args) {
        /**Inputs, etc. */
        Scanner s = new Scanner(System.in);
        Random rand = new Random(); // generate a random number
        //generate random prime
        int prime = rand.nextInt(100000) + 1;
        while (!checkPrime(prime)) {          
            prime = rand.nextInt(100000) + 1;
        }
        System.out.print("this is your 'large' prime: " + prime);
        System.out.print("\nEnter your message: ");
        char[] message = s.nextLine().toCharArray();
        // char[] message = (s.nextLine().replaceAll("[^0-9]", "")).toCharArray();
        System.out.print("\n\nEnter a value for g (a primtive root of prime): ");
        int g = s.nextInt();
        while (egcf(g, prime)[0] != 1) {
            System.out.print(g + " is not a primitive root of p.\nEnter again: ");
            g = s.nextInt();
        }
        System.out.print("\nEnter your b [secret], where 1 < b < prime: ");
        int b = s.nextInt();
        // calculating big B (modular exponentation) --> (g ^ b) % p
        int B = (BigInteger.valueOf(g).modPow(BigInteger.valueOf(b), BigInteger.valueOf(prime))).intValue();
        System.out.println("\np: " + prime + "\ng: " + g + "\nB: " + B);
        System.out.print("\nEnter your r nonce, where 0 < r < prime: ");
        String nonce = s.next();
        BigInteger r = new BigInteger(nonce);
        //calculating R from nonce r --> (modular exponentation) (g^r) % p
        BigInteger R = BigInteger.valueOf(g).modPow(r, BigInteger.valueOf(prime));
        System.out.println("\nR: " + R);
        /** encryption */
        int[] eText = encrypt(message, B, r, prime);
        System.out.println("\nencrypted number: " + sValue(eText));        
        /** decryption section **/
        int[] cText = decrypt(eText, b, prime, R);
        System.out.println("\ndecrypted number: " + sValue(cText));        
        System.out.println("\ndecrypted message: " + findChar(cText));        
    }
    /** 
     * method for converting array of int to string using ASCII table
     */
    public static String findChar(int[] array)
    {
        String str = "";
        //converts int to ascii value
        for(int i = 0; i<array.length;i++)
        {
            str = str +  (char) array[i];
        }
        return str;
    }
    /** 
     * method used for outputting int array as a single string
     * */
    public static String sValue(int[] array)
    {
        String str = "";
        //adds int value to string str
        for(int i = 0; i < array.length; i++)
        {
            str+= String.valueOf(array[i]);
        }
        return str;
    }
    /**
     * method for decrypting
     * @param eMessage encrypted message
     * @param b bvalue
     * @param prime prime
     * @param R big R
     * @return decrypted int array
     */
    public static int[] decrypt(int[] eMessage, int b, int prime, BigInteger R)
    {
        //returned later
        int [] clear = new int[eMessage.length];
        //loop through the message
        for(int i = 0; i < eMessage.length;i++)
        {
        BigInteger exponenet = (BigInteger.valueOf(prime).subtract(BigInteger.ONE)).subtract(BigInteger.valueOf(b));
        BigInteger n = BigInteger.valueOf(eMessage[i]);
        //calculating c clear text --> (n * ((R^(p-1-b)) % prime)) % prime  
        clear[i] = ((n.multiply(R.modPow(exponenet, BigInteger.valueOf(prime)))).mod(BigInteger.valueOf(prime))).intValue();
        }
        return clear;
    }
    /**
     * 
     * @param message message
     * @param B big B
     * @param r nonce
     * @param prime prime
     * @return encrypted array of numbers
     */
    public static int[] encrypt(char[] message, int B, BigInteger r, int prime)
    {
        int[] ciphertext = new int[message.length];
        for(int i = 0; i < message.length;i++)
        {
            BigInteger m = BigInteger.valueOf((int) message[i]);
            ciphertext[i] = ((m.multiply(BigInteger.valueOf(B).modPow(r, BigInteger.valueOf(prime)))).mod(BigInteger.valueOf(prime))).intValue();
        }
        //calculating n encrypted text --> (m * ((B^r) % prime)) % prime 
       return ciphertext;
    }

    //** method for checking if a number is prime */
    public static boolean checkPrime(int p) {
        //if 1 or 2 then not prime
        if (p == 0 || p == 1)
            return false;
        //if 2 then prime
        else if (p == 2)
            return true;
        else {
            //checks through a number/2 to see if any number divides into the input (p).
            for (int i = 2; i <= p / 2; i++) {
                if (p % i == 0)
                    return false;
            }
        }
        //returns true since it passes all the " prime" checks
        return true;
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
}

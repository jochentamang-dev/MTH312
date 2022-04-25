package Ciphers;

import java.math.BigInteger;
import java.util.Scanner;
/** 
 * Elgamal takes (p,g,b) --> (p,g,B)
 * For encryption: r nonce --> R
 * For decryption: R, prime, b 
 * IMPORTANT: THIS PROGRAM DOES NOT DO THE ENCODING OF MESSAGE.
 * THE MESSAGE SHOULD BE ENCODED PRIOR TO RUNNING THIS PROGRAM.
 * THIS PROGRAM IS NOT MADE TO HANDLE SUPER LARGE NUMBERS AS THIS
 * IS JUST A DEMONSTRATION OF Elgamal
 */

public class Elgamal {
    public static void main(String[] args) {
        /**Inputs, etc. */
        Scanner s = new Scanner(System.in);
        System.out.print("Enter your message as numbers: ");
        String message = s.nextLine().replaceAll("[^0-9]", "");
        System.out.print("\nEnter a prime number: ");
        int prime = s.nextInt();
        // checking if prime
        while (!checkPrime(prime)) {
            System.out.println("\n" + prime + " is not a prime.\nEnter a different number: ");
            prime = s.nextInt();
        }
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

        /** encryption section **/
        BigInteger m = new BigInteger(message);
        //calculating n encrypted text --> (m * ((B^r) % prime)) % prime 
        BigInteger n = (m.multiply(BigInteger.valueOf(B).modPow(r, BigInteger.valueOf(prime)))).mod(BigInteger.valueOf(prime));
        System.out.println("\nencrypted ciphertext: " + n);        
        /** decryption section **/
        //exponent value for decryption --> prime - 1 -b
        BigInteger exponenet = (BigInteger.valueOf(prime).subtract(BigInteger.ONE)).subtract(BigInteger.valueOf(b));
        //calculating c clear text --> (n * ((R^(p-1-b)) % prime)) % prime  
        BigInteger c = (n.multiply(R.modPow(exponenet, BigInteger.valueOf(prime)))).mod(BigInteger.valueOf(prime));
        System.out.println("\nclear text: " + c);

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

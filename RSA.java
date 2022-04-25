package Ciphers;

//for handling large numbers in java
import java.math.BigInteger;
//for generate large prime numbers
import java.security.SecureRandom;
import java.util.Arrays;
//random generator
import java.util.Random;
import java.util.Scanner;
/**
 * RSA generates somewhat large prime numbers for the user.
 * User can choose the value for d, which will be used for public key
 * and decryption.
 */

public class RSA {
    //bit length of prime numbers
    final static int BIT_LENGTH = 256;
    public static void main(String[] args)
    {
        //input
        Scanner s = new Scanner(System.in);
        Random rand = new SecureRandom();
        //generate random prime number with 128 bit length
        //can change this to have your own prime | number p = BigInteger.valueOf(some int);
        BigInteger p = new BigInteger(BIT_LENGTH / 2, 100, rand);
        BigInteger q = new BigInteger(BIT_LENGTH / 2, 100, rand);
        System.out.println("p:\n" + p +"\n\nq:\n" + q);
        //the M value --> p * q
        BigInteger M = p.multiply(q); 
        //totient tM value (p-1) * (q-1)
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        System.out.println("\nEnter your message: ");
        String message = s.nextLine();
        System.out.print("\nphi:" + phi + "\n\nEnter a value for d, where 1 < d < phi - 1: ");
        BigInteger d = BigInteger.valueOf(s.nextInt());
        //calculates the nearest d value if gcf(d, tM) != 1
        while((phi.gcd(d)).intValue() != 1)
        {
            if( d.mod(BigInteger.TWO).intValue() != 1)
            {
                d = d.add(BigInteger.valueOf(1));
            }
            d = d.add(BigInteger.valueOf(2));
        }
        System.out.println("\ncalculated d value: " + d);
        //d modulo inverse of tM totient
        BigInteger e = d.modInverse(phi); 
        System.out.println("\nPublic key (M = " + M + ", e = " + e + ")");
        BigInteger [] encrypted = encrypt(M,e, message);
        System.out.println("\nYour message as list of encrypted number: " + Arrays.deepToString(encrypted));
        System.out.println("\nYour message after decrypting: " +decrypt( M, d, encrypted));
        
    }
    /** method for finding gcf of two numbers*/
    public static BigInteger gcd(BigInteger a, BigInteger b)
    {
        // if a < b
        if(a.compareTo(b) == -1) return gcd(b,a);
        //else if a % b == 0
        else if (a.mod(b).equals(BigInteger.ZERO)) return b;
        else return gcd(b, a.mod(b));
    }
    /**
     * encryption method for RSA
     * @param M public key M
     * @param e public key e
     * @param message clear text
     * @return array of encrypted message as number
     */
    public static BigInteger[] encrypt(BigInteger M, BigInteger e, String message)
    {
        //returned later as encrypted text
        BigInteger[] encrypt = new BigInteger[message.length()];
        BigInteger m;
        //loops through the message
        for(int i = 0; i < message.length(); i++)
        {
            //current character ASCII value
            m = BigInteger.valueOf((int) message.charAt(i));
            //adds to the array list
            //calculating (m^e) mod M
            encrypt[i] = m.modPow(e, M);
        }
        return encrypt;
    }
    /**
     * decryption method for RSA
     * @param M the public key M
     * @param d the privat key d
     * @param encrypted cipher text
     * @return clear text
     */
    public static String decrypt(BigInteger M, BigInteger d, BigInteger[] encrypted)
    {
        //returned later as clear text
        String message = "";
        for(int i = 0; i < encrypted.length; i++)
        {
            //calculation: (n^d) mod M
            BigInteger x = encrypted[i].modPow(d, M);
            //converts into to char or ASCII
            message+= (char) (x.intValue());
        }
        return message;
    }
}

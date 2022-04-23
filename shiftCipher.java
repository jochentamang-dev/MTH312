package Ciphers;
/** importing Scanner to take user input */
import java.util.Scanner;
public class shiftCipher{
    public static void main(String[] args)
    {
        /** scanner to take user input (message) */
        Scanner S = new Scanner(System.in);
        System.out.println("Enter your message: ");
        /** the message for program to encode or decode. The message is turned to uppercase and removed all characters (including spaces and numbers) that are not letters.*/
        String message = S.nextLine().toUpperCase().replaceAll("[^a-zA-Z]", ""); 
        System.out.println("Enter the number for shift: ");
        /** the number of shift that was done or needs to be done to the message */
        int shift = S.nextInt() % 26;
        System.out.println("Do you want to encode or decode the message? E for encode; D for decode");
        /** option to either encode or decode a message */
        char choice = S.next().toUpperCase().charAt(0);
        /** if the choice is to encode, sends the message and number of shifts to encoding method */
        if(choice == 'E')
        {
            System.out.println(encode(message, shift));
        }
        /** if the choice is to decode, sends the message and number of shifts to deconding method */
         else if (choice == 'D')
         {
             System.out.println(decode(message, shift));
         }
    }
    /*******************************************
     * Encodes the message by number of shifts specified. The method loops around if char is greater
     * than 'Z'
     * @param message the message that needs to be encoded
     * @param shift the number of shift for the encoding
     * @return encodedMessage the message after encoded by the shift
     *******************************************/
    public static String encode(String message, int shift)
    {
        //initalize the encoded message
        String encodedMessage = "";
        //loops through each character in the original message
        for(int i =0; i < message.length(); i++ )
        {
            //track the current character
            char current = message.charAt(i);
            current+= shift;
            //if the current character goes beyond 'Z' when shifted, loops back to 'A'
            if(current > 'Z')
            {
                current-=26;
                //adds the letter to encodedMessage string
                encodedMessage+= current;
            }
            //else adds the character to encodedMessage string
            else
            {
                encodedMessage+= current;
            }
        }
        return encodedMessage;
    }
    /*******************************************
     * Decodes the cipher text by number of shift
     * @param message the encoded messaged or cipher text
     * @param shift number of shift used in encoding
     * @return decodedMessage the original message prior to encoding
     *******************************************/
    public static String decode(String message, int shift)
    {
        //initalizing decoded message
        String decodedMessage = "";
        //loops through each character in the encryptedMessage
        for(int i = 0; i < message.length(); i++)
        {
            //keeps track of current character
            char current = message.charAt(i);
            current-=shift;
            //if current character goes beyond 'A', then loops back to 'Z'
            if(current < 'A')
            {
                current+=26;
                decodedMessage+=current;
            }
            else
            {
                decodedMessage+=current;
            } 
        }
        return decodedMessage;
    }
}
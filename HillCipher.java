package Ciphers;

import java.util.Arrays;
import java.util.Scanner;
/**
 * HillCipher takes a message and input for 2 x 2 matrix
 * used in encryption. It calculates the matrix inverse for
 * decryption. The code encrypts and decrypts messages using
 * hill cipher.
 */

public class HillCipher {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        System.out.print("Enter your message: ");
        String message = s.nextLine();
        System.out.print("\nEnter your a, b, c, and d for the matrix encryption.\na is top-left, b is top right, c is bottom-left,\nand d is bottom-right (use space as seperator): ");
        //the matrix used for encryption
        int[][] matrix = new int[][] { { s.nextInt(), s.nextInt() }, { s.nextInt(), s.nextInt() } };
        System.out.println("\nHere is your matrix:");
        outputTable(matrix);
        //mod is 127 since that includes all the letters and number in ASCII
        int mod = 127;
        //if odd length of message then adds a space
        if (message.length() % 2 != 0) {
            message += " ";
        }
        //putting message into vectors/matrix
        int[][] mMatrix = messageMatrix(message);
        System.out.println("\nhere is the message matrix/vectors prior to encryption:");
        outputTable(mMatrix);
        //encrypting message matrix (matrix multiplication)
        int[][]encrypted = matrixMult(matrix, mMatrix, mod);
        System.out.println("\nhere is the message matrix/vectors after encryption:");
        outputTable(encrypted);
        //calculating inverse matrix
        int[][] inverseMatrix =  inverseMatrix(matrix, mod);
        System.out.println("\nhere is the matrix inverse for decryption:");
        outputTable(inverseMatrix);
        //calculating decrypted matrix
        int[][]decrypted = matrixMult(inverseMatrix, encrypted, mod);
        System.out.println("\nhere is the decrypted message matrix:");
        outputTable(decrypted);

        //converting decrypted matrix to characters
        String clearString = getMessage(decrypted);
        System.out.print("\nhere is the decrypted message:\n"+clearString);
    }
    /** method used to convert a message matrix to characters */
    public static String getMessage(int [][] dMatrix)
    {
        String message = "";
        /** loops through the columns of dMatrix */
        for(int c = 0; c < dMatrix[0].length; c++)
        {
            //converts first row and second row into characters in ASCII
            message= message +  (char) dMatrix[0][c] + (char) dMatrix[1][c] ;
        }
        return message;
    }
    /** Method for finding multiplicative inverse
     * Implementation of extended euclid. algorithm
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
     * Method for finding inverse of a matrix
     * @param matrix the encryption matrix
     * @param modulo modulo (127)
     * @return inverse of the encryption matrix
     */
    public static int[][] inverseMatrix(int[][] matrix, int modulo)
    {
        //the adjoint of encryption matrix
        int[][] adjoint = new int[][]{{matrix[1][1],(matrix[0][1] * -1) + modulo },
                                        {(matrix[1][0] * -1) + modulo, matrix[0][0]}};
        //used for the inverse matrix, which is returned
        int[][] inverseM = new int[2][2];
        //multiplying adjoint and matrix M % modulo
        int[][] adjValue = matrixMult(adjoint, matrix,modulo);
        //finding multiplicative inverse of adjoint * M
        int inverse = egcf(modulo, adjValue[0][0])[2];
        //if multi. inverse is a negative
        if(inverse < 0) inverse+=modulo;
        //the inverse calculation-->mult. inverse of adjoint * Adj mod modulo
        for(int r = 0; r < 2; r++)
        {
            for(int c = 0; c < 2; c++)
            {
                inverseM[r][c] = (adjoint[r][c] * inverse) % modulo;
            }
        }
        return inverseM;
    }
    /**
     * method for multiplying two matrices % modulo
     * @param m1 first matrix
     * @param m2 second matrix
     * @param modulo modulo value
     * @return result of m1 x m2
     */
    public static int[][] matrixMult(int[][] m1, int [][]m2, int modulo)
    {
        //returned later
        int[][] result = new int[m1.length][m2[0].length];
        //loops through every row and column
        for(int r = 0; r < result.length; r++)
        {
            for(int c = 0; c < result[0].length;c++)
            {
                //sends the row and column of two matrix to different method for matrix cell multiplication
                result[r][c] = multiplyMatrixCell(m1,m2,r,c,modulo);
            }
        }
        return result;
    }
    /**
     * method for calculating the value of a cell during matrix       * multiplication
     */
    public static int multiplyMatrixCell(int[][] m1, int[][]m2, int row, int col, int modulo) {
        //keeps track of the value
        int cell = 0;
        //loops through the row of second matrix
        for (int i = 0; i < m2.length; i++) {
            //actual calculation
            cell += m1[row][i] * m2[i][col];
        }
        return cell % modulo;
    }
    /** 
     * method for inputting message into a matrix
     */
    public static int[][] messageMatrix(String message)
    {
        //2 row and length of row /2 column
        int[][] mMatrix = new int[2][message.length()/2];
        //tracks the message index
        int mCounter = 0;
        //loops through the column of mMatrix
        for(int c = 0; c < mMatrix[0].length;c++)
            {
                //adds the first value at mCounter to first row and c column. Also performs char to int conversion using ASCII.
                mMatrix[0][c] = (int) message.charAt(mCounter);
                //adds the second value at mCounter to second row and c column.
                mMatrix[1][c] = (int) message.charAt(mCounter+1);
                mCounter+=2;
            }
        return mMatrix;
    }

     /** function to output the table */
     public static void outputTable(int[][] table) {
        for (int[] row : table) {
            System.out.println(Arrays.toString(row));
        }
    }
    
}
package Utilities;

public class Output
{
    public static void printArray(double[] array)
    {
        System.out.print("[" + array[0]);
        for (int i = 1; i < array.length - 1; i++) System.out.print(", " + array[i]);
        System.out.println(", " + array[array.length - 1] + "]");
    }

    public static <T> void printMatrix(T[][] matrix)
    {
        for (int i = 0; i < matrix.length; i++)
        {
            System.out.print(matrix[i][0]);
            for (int j = 1; j < matrix[i].length; j++) System.out.print("\t" + matrix[i][j]);
            System.out.println();
        }
        System.out.println();
    }
}

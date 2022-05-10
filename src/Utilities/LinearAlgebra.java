package Utilities;

public class LinearAlgebra
{
    /**
     * Este método rotaciona um ponto no plano R² em
     * relação a uma origem e num determinado ângulo.
     *
     * @param point : Ponto que será rotacionado.
     * @param pointReference : Referência a partir da qual o ponto será rotacionado.
     * @param degreesAngle : Valor do ângulo em graus que o ponto será rotacionado.
     * @return
     */
    public static double[] rotatePointClockwise(double[] point, double[] pointReference, double degreesAngle)
    {
        if (point.length != 2 || pointReference.length != 2) return null;
        double[] rotatedVector = rotateVectorClockwise(new double[] {point[0] - pointReference[0], point[1] - pointReference[1]}, degreesAngle);

        return new double[] { rotatedVector[0] + pointReference[0], rotatedVector[1] + pointReference[1] };
    }

    /**
     * Este método rotaciona um vetor no
     * plano R² num determinado ângulo.
     *
     * @param vector : Vetor que será rotacionado.
     * @param degreesAngle : Valor do ângulo em graus que o vetor será rotacionado.
     * @return
     */
    public static double[] rotateVectorClockwise(double[] vector, double degreesAngle)
    {
        if (vectorDimension(vector) != 2) return null;

        return new double[]
        {
            vector[0] * Math.cos(Math.PI * degreesAngle / 180) + vector[1] * Math.sin(Math.PI * degreesAngle / 180),
            vector[1] * Math.cos(Math.PI * degreesAngle / 180) - vector[0] * Math.sin(Math.PI * degreesAngle / 180)
        };
    }

    public static double angleBetweenVectors(double[] vectorA, double[] vectorB) throws Exception
    {
        if (vectorDimension(vectorA) != vectorDimension(vectorB)) throw new Exception();
        double cosineValue = scalarProductVectors(vectorA, vectorB) / (vectorModule(vectorA) * vectorModule(vectorB));
        return Math.acos(cosineValue);
    }

    public static double scalarProductVectors(double[] vectorA, double[] vectorB) throws Exception
    {
        if (vectorDimension(vectorA) != vectorDimension(vectorB)) throw new Exception();
        double sum = 0;
        for (int i = 0; i < vectorA.length; i++) sum += vectorA[i] * vectorB[i];
        return sum;
    }

    public static void multipleVectorByScalar(double[] vector, double scalar)
    {
        for (int i = 0; i < vector.length; i++) vector[i] *= scalar;
    }

    /**
     *  Este método retorna um ponto R^n que esteja
     *  a 'distance' unidades à frente do vetor 'vector'
     */
    public static double[] forwardPoint(double[] vector, double distance)
    {
        double referenceVectorModule = vectorModule(vector);
        double[] point = new double[vectorDimension(vector)];

        for (int i = 0; i < point.length; i++) point[i] = (1 + distance/referenceVectorModule) * vector[i];

        return point;
    }

    /**
     *  Este método retorna o módulo (norma)
     *  de um vetor de N dimensões.
     */
    public static double vectorModule(double[] vector)
    {
        double auxSum = 0;

        for (int i = 0; i < vector.length; i++) {
            auxSum += Math.pow(vector[i], 2);
        }

        return Math.sqrt(auxSum);
    }

    public static int vectorDimension(double[] vector)
    {
        return vector.length;
    }

    /**
     *  Este método retorna um vetor resultante de uma combinação
     *  linear convexa, ou seja, retorna um vetor de dimensão N
     *  delimitado pelos vetores de N dimensões in 'vectors'. A
     *  localização do vetor resultante na região delimitada depende
     *  dos valores dos coeficientes, cujos valores devem estar entre
     *  0 e 1 e a soma de todos eles deve ser 1.
     *
     *  Complexidade Tempo: O(dim(vectors) * vectors.length)
     */
    public static double[] convexLinearCombination(double[][] vectors, double[] coefficients)
    {
        final int NUMBER_OF_VECTORS = vectors.length;
        final int VECTORS_DIMENSION = vectors[0].length;
        double coefficientsSum = 0;

        for (int i = 0; i < coefficients.length; i++) coefficientsSum += coefficients[i];
        for (double[] vector : vectors) { if (vectorDimension(vector) != VECTORS_DIMENSION) return null; }

        if (coefficientsSum != 1) return null;
        if (NUMBER_OF_VECTORS != coefficients.length) return null;

        double[] result = new double[VECTORS_DIMENSION];

        for (int i = 0; i < result.length; i++)
        {
            double componentSum = 0;

            for (int j = 0; j < NUMBER_OF_VECTORS; j++)
            {
                componentSum += vectors[j][i] * coefficients[j];
            }

            result[i] = componentSum;
        }

        return result;
    }
}

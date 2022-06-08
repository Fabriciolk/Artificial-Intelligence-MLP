package Exception;

public class InvalidLearningRateException extends Exception
{
    public InvalidLearningRateException(double invalidLearningRate)
    {
        super(invalidLearningRate + " is not a valid learning rate. Expected rate R such that 0.0 < R <= 1.0");
    }
}

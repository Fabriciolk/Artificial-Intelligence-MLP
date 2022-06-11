package Exception;

public class FailedToSetWeightsAndBiasException extends Exception
{
    public FailedToSetWeightsAndBiasException()
    {
        super("Failed to set weights and bias. They are already initialized.");
    }
}

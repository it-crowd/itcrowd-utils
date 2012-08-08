package pl.com.it_crowd.utils.config;

public class InvalidConfigurationException extends RuntimeException {
// --------------------------- CONSTRUCTORS ---------------------------

    public InvalidConfigurationException(String message)
    {
        super(message);
    }

    public InvalidConfigurationException(String message, Throwable cause)
    {
        super(message, cause);
    }
}

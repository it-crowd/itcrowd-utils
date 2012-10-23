package pl.itcrowd.utils.config;

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

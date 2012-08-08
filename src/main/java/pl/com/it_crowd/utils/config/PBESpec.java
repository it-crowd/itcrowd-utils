package pl.com.it_crowd.utils.config;

public interface PBESpec {
// -------------------------- OTHER METHODS --------------------------

    String getAlgorithm();

    int getIterationCount();

    String getPassword();

    String getSalt();
}

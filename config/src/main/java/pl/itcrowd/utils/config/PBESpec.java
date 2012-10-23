package pl.itcrowd.utils.config;

public interface PBESpec {
// -------------------------- OTHER METHODS --------------------------

    String getAlgorithm();

    int getIterationCount();

    String getPassword();

    String getSalt();
}

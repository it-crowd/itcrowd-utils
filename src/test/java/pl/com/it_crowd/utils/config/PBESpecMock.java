package pl.com.it_crowd.utils.config;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Alternative;
import java.io.Serializable;

@Alternative
@SessionScoped
public class PBESpecMock implements PBESpec, Serializable {
// ------------------------------ FIELDS ------------------------------

    private String algorithm;

    private int iterationCount;

    private String password;

    private String salt;

// --------------------- GETTER / SETTER METHODS ---------------------

    @Override
    public String getAlgorithm()
    {
        return algorithm;
    }

    public void setAlgorithm(String algorithm)
    {
        this.algorithm = algorithm;
    }

    @Override
    public int getIterationCount()
    {
        return iterationCount;
    }

    public void setIterationCount(int iterationCount)
    {
        this.iterationCount = iterationCount;
    }

    @Override
    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    @Override
    public String getSalt()
    {
        return salt;
    }

    public void setSalt(String salt)
    {
        this.salt = salt;
    }
}

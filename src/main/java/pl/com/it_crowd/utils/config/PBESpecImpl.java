package pl.com.it_crowd.utils.config;

import org.apache.commons.lang.StringUtils;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Alternative;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@Alternative
public class PBESpecImpl implements PBESpec {
// ------------------------------ FIELDS ------------------------------

    private String algorithm;

    private String algorithmJNDI;

    private int iterationCount;

    private String iterationCountJNDI;

    private String password;

    private String passwordJNDI;

    private String salt;

    private String saltJNDI;

// --------------------- GETTER / SETTER METHODS ---------------------

    @Override
    public String getAlgorithm()
    {
        return algorithm;
    }

    @Override
    public int getIterationCount()
    {
        return iterationCount;
    }

    @Override
    public String getPassword()
    {
        return password;
    }

    @Override
    public String getSalt()
    {
        return salt;
    }

    @PostConstruct
    private void init() throws NamingException
    {
        if (StringUtils.isBlank(algorithmJNDI) || StringUtils.isBlank(iterationCountJNDI) || StringUtils.isBlank(passwordJNDI) || StringUtils.isBlank(
            saltJNDI)) {
            throw new InvalidConfigurationException("One of attributes algorithmJNDI,iterationCountJNDI,passwordJNDI,saltJNDI is missing");
        }
        InitialContext context = new InitialContext();
        try {
            algorithm = (String) context.lookup(algorithmJNDI);
            iterationCount = Integer.parseInt((String) context.lookup(iterationCountJNDI));
            password = (String) context.lookup(passwordJNDI);
            salt = (String) context.lookup(saltJNDI);
        } catch (NamingException e) {
            throw new InvalidConfigurationException("One of attributes algorithmJNDI,iterationCountJNDI,passwordJNDI,saltJNDI is invalid", e);
        } catch (NumberFormatException e) {
            throw new InvalidConfigurationException("IterationCount is not a number", e);
        }
    }
}

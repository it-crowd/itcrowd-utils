package pl.itcrowd.utils.config;

import org.apache.commons.lang.StringUtils;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@Alternative
@ApplicationScoped
public class PBESpecImpl implements PBESpec {

    private String algorithm;

    private String algorithmJNDI;

    private Integer iterationCount;

    private String iterationCountJNDI;

    private String password;

    private String passwordJNDI;

    private String salt;

    private String saltJNDI;

    @Override
    public String getAlgorithm()
    {
        if (null == algorithm) {
            init();
        }
        return algorithm;
    }

    public String getAlgorithmJNDI()
    {
        return algorithmJNDI;
    }

    public void setAlgorithmJNDI(String algorithmJNDI)
    {
        this.algorithmJNDI = algorithmJNDI;
    }

    @Override
    public int getIterationCount()
    {
        if (null == iterationCount) {
            init();
        }
        return iterationCount;
    }

    public String getIterationCountJNDI()
    {
        return iterationCountJNDI;
    }

    public void setIterationCountJNDI(String iterationCountJNDI)
    {
        this.iterationCountJNDI = iterationCountJNDI;
    }

    @Override
    public String getPassword()
    {
        if (null == password) {
            init();
        }
        return password;
    }

    public String getPasswordJNDI()
    {
        return passwordJNDI;
    }

    public void setPasswordJNDI(String passwordJNDI)
    {
        this.passwordJNDI = passwordJNDI;
    }

    @Override
    public String getSalt()
    {
        if (null == salt) {
            init();
        }
        return salt;
    }

    public String getSaltJNDI()
    {
        return saltJNDI;
    }

    public void setSaltJNDI(String saltJNDI)
    {
        this.saltJNDI = saltJNDI;
    }

    private void init()
    {
        if (StringUtils.isBlank(algorithmJNDI) || StringUtils.isBlank(iterationCountJNDI) || StringUtils.isBlank(passwordJNDI) || StringUtils.isBlank(
            saltJNDI)) {
            throw new InvalidConfigurationException("One of attributes algorithmJNDI,iterationCountJNDI,passwordJNDI,saltJNDI is missing");
        }
        InitialContext context;
        try {
            context = new InitialContext();
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
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

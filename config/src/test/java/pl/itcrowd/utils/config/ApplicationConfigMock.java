package pl.itcrowd.utils.config;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

@SessionScoped
public class ApplicationConfigMock extends ApplicationConfig implements Serializable {
// ------------------------------ FIELDS ------------------------------

    private String mailPassword;

// --------------------- GETTER / SETTER METHODS ---------------------

    public String getMailPassword()
    {
        if (mailPassword == null) {
            mailPassword = loadAndDecrypt(KEY.MAIL_PASSWORD);
        }
        return mailPassword;
    }

    public void setMailPassword(String mailPassword)
    {
        saveEncrypted(KEY.MAIL_PASSWORD, mailPassword);
        this.mailPassword = mailPassword;
    }

// -------------------------- OTHER METHODS --------------------------

    public void reload()
    {
        mailPassword = null;
        mailPassword = loadAndDecrypt(KEY.MAIL_PASSWORD);
    }

// -------------------------- ENUMERATIONS --------------------------

    public static enum KEY {
        MAIL_PASSWORD
    }
}

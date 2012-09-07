package pl.com.it_crowd.utils.config;

import pl.com.it_crowd.utils.config.converter.SettingConverter;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.Serializable;

//TODO we need to make this class really serializable
public abstract class ApplicationConfig implements Serializable {
// ------------------------------ FIELDS ------------------------------

    private PBEHelper pbeHelper;

    @Inject
    protected Instance<PBESpec> pbeSpecInstance;

    @Inject
    protected SettingDAO settingDAO;

    @PostConstruct
    protected void init()
    {
        if (!pbeSpecInstance.isUnsatisfied()) {
            final PBESpec spec = pbeSpecInstance.get();
            pbeHelper = new PBEHelper(spec.getAlgorithm(), spec.getPassword(), spec.getSalt(), spec.getIterationCount());
        }
    }

    protected String load(Enum id)
    {
        return load(id.name());
    }

    protected String load(String id)
    {
        Setting setting = settingDAO.load(id);
        if (setting == null) {
            throw new InvalidConfigurationException(String.format("Missing %s setting", id));
        }
        return setting.getValue();
    }

    protected <T> T load(String id, SettingConverter<T> converter)
    {
        final String stringValue = load(id);
        return converter.getObject(stringValue);
    }

    protected <T> T load(Enum id, SettingConverter<T> converter)
    {
        return load(id.name(), converter);
    }

    protected String loadAndDecrypt(Enum id)
    {
        return loadAndDecrypt(id.name());
    }

    protected String loadAndDecrypt(String id)
    {
        final String value = load(id);
        return getPbeHelper().decrypt(value);
    }

    protected boolean save(Setting setting)
    {
        settingDAO.save(setting);
        return true;
    }

    protected boolean save(String id, String value)
    {
        return save(new Setting(id, value));
    }

    protected boolean save(Enum id, String value)
    {
        return save(id.name(), value);
    }

    protected boolean saveEncrypted(Setting setting)
    {
        setting.setValue(getPbeHelper().encrypt(setting.getValue()));
        return save(setting);
    }

    protected boolean saveEncrypted(String id, String value)
    {
        return saveEncrypted(new Setting(id, value));
    }

    protected boolean saveEncrypted(Enum id, String value)
    {
        return saveEncrypted(id.name(), value);
    }

    protected PBEHelper getPbeHelper()
    {
        if (pbeHelper == null) {
            throw new IllegalStateException("Encryption not supported due to lack of PBESpec");
        }
        return pbeHelper;
    }
}

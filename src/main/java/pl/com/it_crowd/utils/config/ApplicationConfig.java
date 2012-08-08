package pl.com.it_crowd.utils.config;

import pl.com.it_crowd.utils.config.converter.SettingConverter;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

public abstract class ApplicationConfig {
// ------------------------------ FIELDS ------------------------------

    protected PBEHelper pbeHelper;

    @Inject
    protected Instance<PBESpec> pbeSpecInstance;

    @Inject
    protected SettingDAO settingDAO;

    @PostConstruct
    protected void init()
    {
        final PBESpec spec = pbeSpecInstance.get();
        pbeHelper = new PBEHelper(spec.getAlgorithm(), spec.getPassword(), spec.getSalt(), spec.getIterationCount());
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

    protected boolean save(Setting setting)
    {
        return save(setting, null);
    }

    protected boolean save(Setting setting, String encryptionKey)
    {
        if (encryptionKey != null) {
            setting.setValue(pbeHelper.encrypt(setting.getValue()));
        }
        settingDAO.save(setting);
        return true;
    }

    protected boolean save(String id, String value)
    {
        return save(new Setting(id, value), null);
    }

    protected boolean save(Enum id, String value)
    {
        return save(id.name(), value);
    }

    protected boolean save(String id, String value, String encryptionKey)
    {
        return save(new Setting(id, value), encryptionKey);
    }

    protected boolean save(Enum id, String value, String encryptionKey)
    {
        return save(id.name(), value, encryptionKey);
    }
}

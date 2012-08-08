package pl.com.it_crowd.utils.config.converter;

public class LongConverter implements SettingConverter<Long> {
// ------------------------------ FIELDS ------------------------------

    public static final LongConverter INSTANCE = new LongConverter();

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface SettingConverter ---------------------

    @Override
    public Long getObject(String value)
    {
        return value == null ? null : new Long(value);
    }

    @Override
    public String getString(Long value)
    {
        return value == null ? null : value.toString();
    }
}

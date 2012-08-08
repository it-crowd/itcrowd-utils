package pl.com.it_crowd.utils.config.converter;

public class IntegerConverter implements SettingConverter<Integer> {
// ------------------------------ FIELDS ------------------------------

    public static final IntegerConverter INSTANCE = new IntegerConverter();

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface SettingConverter ---------------------

    @Override
    public Integer getObject(String value)
    {
        return value == null ? null : new Integer(value);
    }

    @Override
    public String getString(Integer value)
    {
        return value == null ? null : value.toString();
    }
}

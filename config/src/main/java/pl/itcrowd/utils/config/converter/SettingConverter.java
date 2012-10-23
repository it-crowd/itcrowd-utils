package pl.itcrowd.utils.config.converter;

public interface SettingConverter<T> {
// -------------------------- OTHER METHODS --------------------------

    T getObject(String value);

    String getString(T value);
}

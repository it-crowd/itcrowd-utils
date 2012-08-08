package pl.com.it_crowd.utils.config;

public interface SettingDAO {
// -------------------------- OTHER METHODS --------------------------

    Setting load(String id);

    Setting save(Setting setting);
}

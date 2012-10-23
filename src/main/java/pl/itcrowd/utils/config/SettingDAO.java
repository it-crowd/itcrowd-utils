package pl.itcrowd.utils.config;

public interface SettingDAO {
// -------------------------- OTHER METHODS --------------------------

    Setting load(String id);

    Setting save(Setting setting);
}

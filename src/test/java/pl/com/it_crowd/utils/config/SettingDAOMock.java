package pl.com.it_crowd.utils.config;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@SessionScoped
public class SettingDAOMock implements SettingDAO, Serializable {
// ------------------------------ FIELDS ------------------------------

    private Map<String, Setting> db = new HashMap<String, Setting>();

// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface SettingDAO ---------------------

    @Override
    public Setting load(String id)
    {
        return db.get(id);
    }

    @Override
    public Setting save(Setting setting)
    {
        db.put(setting.getId(), setting);
        return setting;
    }
}

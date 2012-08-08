package pl.com.it_crowd.utils.config;

import pl.com.it_crowd.seam.framework.Identifiable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "SETTING")
public class Setting implements Serializable, Identifiable<String> {
// ------------------------------ FIELDS ------------------------------

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "VALUE")
    private String value;

// --------------------------- CONSTRUCTORS ---------------------------

    public Setting()
    {
    }

    public Setting(String id, String value)
    {
        this.id = id;
        this.value = value;
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    public String getId()
    {
        return id;
    }

    public void setId(String key)
    {
        this.id = key;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

// ------------------------ CANONICAL METHODS ------------------------

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Setting)) {
            return false;
        }

        Setting setting = (Setting) o;

        return !(id != null ? !id.equals(setting.id) : setting.id != null);
    }

    @Override
    public int hashCode()
    {
        return id != null ? id.hashCode() : 0;
    }
}

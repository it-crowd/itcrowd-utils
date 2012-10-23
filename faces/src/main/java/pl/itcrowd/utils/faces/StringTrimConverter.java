package pl.itcrowd.utils.faces;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

/**
 * Class for removieng white spaces
 */
public class StringTrimConverter implements Converter {
// ------------------------ INTERFACE METHODS ------------------------


// --------------------- Interface Converter ---------------------

    public Object getAsObject(FacesContext context, UIComponent component, String value)
    {
        if (value == null) {
            return null;
        } else {
            final String trimmedValue = value.trim();
            return "".equals(trimmedValue) ? null : trimmedValue;
        }
    }

    public String getAsString(FacesContext context, UIComponent component, Object value)
    {
        return null == value ? null : value.toString();
    }
}

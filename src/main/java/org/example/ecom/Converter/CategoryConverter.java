package org.example.ecom.Converter;

import java.util.Optional;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import org.example.ecom.Service.CategoryDaoImp;
import org.example.ecom.model.Category;

@FacesConverter(value = "categoryConverter")
public class CategoryConverter implements Converter {
  @Override
  public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String value) {
    if (value == null || value.isEmpty()) {
      return null;
    }
    Optional<Category> category = new CategoryDaoImp().getById(Long.valueOf(value));
    return category.orElse(null);
  }

  @Override
  public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object object) {
    if (!(object instanceof Category)) {
      return null;
    }
    return String.valueOf(((Category) object).getId());
  }
}

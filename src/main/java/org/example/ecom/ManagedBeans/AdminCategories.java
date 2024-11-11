package org.example.ecom.ManagedBeans;

import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.example.ecom.Service.CategoryDaoImp;
import org.example.ecom.model.Category;

@ManagedBean(name = "adminCategories")
@ViewScoped
public class AdminCategories {
  private final CategoryDaoImp categoryDaoImp = new CategoryDaoImp();

  private List<Category> lisCategories;
  private List<Category> filteredCategories;
  private Category category = new Category();

  public AdminCategories() {}


  public void setLisCategories(List<Category> lisCategories) {
    this.lisCategories = lisCategories;
  }

  public void addCategory() {
    categoryDaoImp.create(category);
  }

  public void updateCategory() {
    categoryDaoImp.update(category);
  }

  public void saveCategory() {
    if (category.getId() != null) {
      updateCategory();
    } else {
      addCategory();
    }
    this.lisCategories = categoryDaoImp.getAll();
    this.category = new Category();
  }

  public void deleteCategory(Long id) {
    categoryDaoImp.deleteById(id);
    getListCategories();
  }

  public List<Category> getListCategories() {
    this.lisCategories = categoryDaoImp.getAll();
    return lisCategories;
  }

  public List<Category> getFilteredCategories() {
    return filteredCategories;
  }

  public void setFilteredCategories(List<Category> filteredCategories) {
    this.filteredCategories = filteredCategories;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public void prepareNewCategory() {
    this.category = new Category();
  }

}
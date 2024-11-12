package org.example.ecom.ManagedBeans;

import java.io.*;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import org.example.ecom.Service.CategoryDaoImp;
import org.example.ecom.Service.ProductDaoImpl;
import org.example.ecom.model.Category;
import org.example.ecom.model.Product;
import org.primefaces.model.file.UploadedFile;

@ManagedBean(name = "adminProducts")
@ViewScoped
public class AdminProducts implements Serializable {
  private final ProductDaoImpl productDaoImp = new ProductDaoImpl();
  private final CategoryDaoImp categoryDaoImp = new CategoryDaoImp();

  private List<Product> listProducts;
  private List<Product> filteredProducts;
  private List<Category> categoryList;
  private Product product = new Product();
  private Long selectedCategoryId;
  private UploadedFile file;

  public AdminProducts() {
    loadCategories();
  }

  public UploadedFile getFile() {
    return file;
  }

  public void setFile(UploadedFile file) {
    this.file = file;
    ServletContext servletContext =
        (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
    String path =
        servletContext.getRealPath("") + "resources" + File.separator + "img" + File.separator;
    System.out.println(path);
    try {
      @SuppressWarnings("resource")
      OutputStream outputStream = new FileOutputStream(path + file.getFileName());
      InputStream inputStream = file.getInputStream();
      byte[] buffer = new byte[1024];
      int bytesRead;

      while ((bytesRead = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, bytesRead);
      }
    } catch (Exception e) {
      System.err.println("Error during file upload: " + e.getMessage());
    }
  }

  private void loadCategories() {
    this.categoryList = categoryDaoImp.getAll();
  }

  public void prepareNewProduct() {
    this.product = new Product();
    this.selectedCategoryId = null;
  }

  public void saveProduct() {
    try {
      if (selectedCategoryId != null) {
        Category selectedCategory = categoryDaoImp.getById(selectedCategoryId).orElse(null);
        if (selectedCategory != null) {
          product.setCategory(selectedCategory);
        }
      }

      product.setImage("/img/"+file.getFileName());
      if (product.getId() != null) {
        productDaoImp.update(product);
      } else {
        productDaoImp.create(product);
      }

      this.listProducts = productDaoImp.getAll();
      this.product = new Product();
      this.selectedCategoryId = null;
      this.file = null;
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public void deleteProduct(Long id) {
    System.out.println("aune");
    productDaoImp.deleteById(id);
    this.categoryList = categoryDaoImp.getAll();
    this.listProducts = productDaoImp.getAll();
  }

  // Getters and Setters
  public List<Product> getListProducts() {
    if (this.listProducts == null) {
      this.listProducts = productDaoImp.getAll();
    }
    return listProducts;
  }

  public void setListProducts(List<Product> listProducts) {
    this.listProducts = listProducts;
  }

  public List<Product> getFilteredProducts() {
    return filteredProducts;
  }

  public void setFilteredProducts(List<Product> filteredProducts) {
    this.filteredProducts = filteredProducts;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public List<Category> getCategoryList() {
    if (this.categoryList == null) {
      loadCategories();
    }
    return categoryList;
  }

  public void setCategoryList(List<Category> categoryList) {
    this.categoryList = categoryList;
  }

  public Long getSelectedCategoryId() {
    return selectedCategoryId;
  }

  public void setSelectedCategoryId(Long selectedCategoryId) {
    this.selectedCategoryId = selectedCategoryId;
  }
}

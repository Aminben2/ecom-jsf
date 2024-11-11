package org.example.ecom.ManagedBeans;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
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
  }

  private void loadCategories() {
    this.categoryList = categoryDaoImp.getAll();
  }

  public void prepareNewProduct() {
    this.product = new Product();
    this.selectedCategoryId = null;
  }

  public void saveProduct() {
    if (file != null && file.getFileName() != null) {
      String fileType = file.getContentType();
      if (!fileType.equals("image/jpeg") && !fileType.equals("image/png") && !fileType.equals("image/gif")) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid file type. Please upload a JPEG, PNG, or GIF image.", null));
        return;
      }
      this.product.setImage(uploadImage());
    }

    try {
      if (selectedCategoryId != null) {
        Category selectedCategory = categoryDaoImp.getById(selectedCategoryId).orElse(null);
        if (selectedCategory != null) {
          product.setCategory(selectedCategory);
        }
      }

      if (product.getId() != null) {
        productDaoImp.update(product);
      } else {
        productDaoImp.create(product);
      }

      this.listProducts = productDaoImp.getAll();
      this.product = new Product();
      this.selectedCategoryId = null;
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public String uploadImage() {
    try {
      String uploadDir = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/images");
      File dir = new File(uploadDir);
      if (!dir.exists()) {
        dir.mkdirs();
      }

      String fileName = System.currentTimeMillis() + "_" + file.getFileName();
      Path filePath = Paths.get(uploadDir, fileName);

      try (InputStream input = file.getInputStream()) {
        Files.copy(input, filePath);
      }

      return fileName;
    } catch (Exception e) {
      FacesContext.getCurrentInstance().addMessage(null,
              new FacesMessage(FacesMessage.SEVERITY_ERROR, "File upload failed!", null));
    }
    return null;
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
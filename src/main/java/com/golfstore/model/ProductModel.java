package com.golfstore.model;

/**
 * ProductModel.java
 * represents a single row in the 'products' table.
 * Follows strict MVC rules: this class holds data ONLY.
 * No business logic, no database calls, no servlet imports.
 *
 * Field names map directly to the 'products' table columns:
 *   id            -> products.id
 *   name          -> products.name
 *   description   -> products.description
 *   price         -> products.price
 *   stockQuantity -> products.stock_quantity
 *   categoryId    -> products.category_id
 *   imagePath     -> products.image_path
 */

public class ProductModel {
	// Private fields — encapsulation enforced (MVC Model rule)
    private int id;
    private String name;
    private String description;
    private double price;
    private int stockQuantity;
    private int categoryId;
    private String imagePath;
    // No-argument constructor (required by JavaBean standard)
    public ProductModel() {}
    
    // Full constructor — used when building objects from
    // a JDBC ResultSet inside a Servlet
    public ProductModel(int id, String name, String description,
            double price, int stockQuantity,
            int categoryId, String imagePath) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.stockQuantity = stockQuantity;
		this.categoryId = categoryId;
		this.imagePath = imagePath;
		}

    // Getters — public accessors for each private field
    // Used by JSPs
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getImagePath() {
        return imagePath;
    }


    // Setters — public mutators for each private field
    // Used by Servlets to populate the object from form data
    // before passing to PreparedStatement
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}

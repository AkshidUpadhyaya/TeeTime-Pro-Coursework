package com.golfstore.model;

/**
 * CategoryModel.java
 * represents a single row in the 'categories' table.
 * Follows strict MVC rules: data container only.
 *
 * Field names map directly to the 'categories' table columns:
 *   id   -> categories.id
 *   name -> categories.name
 */
public class CategoryModel {

    // Private fields — encapsulation enforced
    private int id;
    private String name;

    // No-argument constructor (required by JavaBean standard)
    public CategoryModel() {}

    // Full constructor — used when building objects from
    // a JDBC ResultSet inside a Servlet
    public CategoryModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
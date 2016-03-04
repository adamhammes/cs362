package models;

import interfaces.AuthorInterface;

public class Author implements AuthorInterface {
    private String id;
    private String name;

    public Author(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    
    public void setName(String name) {
        this.name = name;
    }
}

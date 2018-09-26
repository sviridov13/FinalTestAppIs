package com.sviridov.testapp.finaltestappis;

public class Item {
    private String title;
    private String id;
    private String image;

    public Item (String title, String id, String image)
    {
        this.title = title;
        this.id = id;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

package com.bestandroidaboudemaybas.masebha;

public class CardData {
    private Integer id;
    private String name;
    private String description;
    private Integer dawra;
    public CardData() {
    }

    public CardData(Integer id, String title, String description, Integer dawra) {
        this.id = id;
        this.name = title;
        this.description = description;
        this.dawra = dawra;
    }

    public CardData(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.name = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDawra() {
        return dawra;
    }

    public void setDawra(Integer dawra) {
        this.dawra = dawra;
    }

    public String getTitle() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}


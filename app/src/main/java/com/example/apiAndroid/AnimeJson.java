package com.example.apiAndroid;

public class AnimeJson {
    public String id;
    public String name;
    public String descripcion;
    public String year;
    public String type;
    public String imagenUrl;
    public String favs;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFavs() {
        return favs;
    }

    public void setFavs(String favs) {
        this.favs = favs;
    }

    public AnimeJson() {
    }

    public AnimeJson(String imagenUrl, String titulo, String descripcion, String year, String platform) {
        this.imagenUrl = imagenUrl;
        this.name = titulo;
        this.descripcion = descripcion;
        this.year = year;
        this.type = platform;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

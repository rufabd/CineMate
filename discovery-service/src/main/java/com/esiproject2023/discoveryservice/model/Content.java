package com.esiproject2023.discoveryservice.model;

import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
public class Content {
    private String id;
    private String title;
    private double rating;
    private String cast;
    private String description;
    private String genre;
    private String poster;
    private String release_date;
    private String director;

    public Content(Map<String,String> obj) {
        this.id = obj.get("id");
        this.title = obj.get("title");
        this.rating = Double.parseDouble(obj.get("rating"));
        this.cast = obj.get("cast");
        this.description = obj.get("description");
        this.genre = obj.get("genre");
        this.poster = obj.get("poster");
        this.release_date = obj.get("release_date");
        this.director = obj.get("director");
    }

    public double getRating() {
        return rating;
    }
}

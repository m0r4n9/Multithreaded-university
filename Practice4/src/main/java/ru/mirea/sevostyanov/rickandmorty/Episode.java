package ru.mirea.sevostyanov.rickandmorty;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Episode {
    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("air_date")
    private String airDate;

    @JsonProperty("episode")
    private String episode;

    @JsonProperty("characters")
    private List<String> characters;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAirDate() {
        return airDate;
    }

    public String getEpisode() {
        return episode;
    }

    public List<String> getCharacters() {
        return characters;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAirDate(String airDate) {
        this.airDate = airDate;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public void setCharacters(List<String> characters) {
        this.characters = characters;
    }

    @Override
    public String toString() {
        return "Episode{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", airDate='" + airDate + '\'' +
                ", episode='" + episode + '\'' +
                ", characters count=" + characters.size() +
                '}';
    }
}
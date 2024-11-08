package ru.mirea.sevostyanov.rickandmorty;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import java.util.Comparator;

public class Main {
    public static void main(String[] args) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://rickandmortyapi.com")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);

        try {
            Result result = service.getEpisodes()
                    .execute()
                    .body();

            if (result != null && result.getEpisodes() != null) {
                Episode episodeWithMostCharacters = result.getEpisodes().stream()
                        .max(Comparator.comparingInt(episode -> episode.getCharacters().size()))
                        .orElseThrow(() -> new RuntimeException("No episodes found"));

                System.out.println("Эпизод с наибольшим количеством персонажей:");
                System.out.println("Название: " + episodeWithMostCharacters.getName());
                System.out.println("Эпизод: " + episodeWithMostCharacters.getEpisode());
                System.out.println("Дата выхода: " + episodeWithMostCharacters.getAirDate());
                System.out.println("Кол-во персонажей: " + episodeWithMostCharacters.getCharacters().size());
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
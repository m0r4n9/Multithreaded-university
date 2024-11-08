package ru.mirea.sevostyanov.rickandmorty;


import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("/api/episode")
    Call<Result> getEpisodes();
}
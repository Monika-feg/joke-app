package com.example.jokeap;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("random_joke")  // Endpointen för att hämta ett slumpmässigt skämt
    Call<Jokes> getJoke();  // Returnera ett enstaka skämt
}

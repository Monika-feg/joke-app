package com.example.jokeap;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView jokeTextView;
    private TextView answerTextView;
    private Button getJokeButton;
    private Button showAnswerButton;

    private ApiService service;
    private String setupText;
    private String punchlineText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jokeTextView = findViewById(R.id.joke);
        answerTextView = findViewById(R.id.answer);
        getJokeButton = findViewById(R.id.buttonGetJoke);
        showAnswerButton = findViewById(R.id.buttonShowAnswer);

        // Skapa Retrofit-instans för att anropa API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://official-joke-api.appspot.com/")  // Base URL
                .addConverterFactory(GsonConverterFactory.create())  // Gson för att konvertera JSON
                .build();

        service = retrofit.create(ApiService.class);  // Skapa ApiService-instans

        // När du trycker på "Hämta Skämt"-knappen
        getJokeButton.setOnClickListener(v -> getJoke());

        // När du trycker på "Visa Svar"-knappen
        showAnswerButton.setOnClickListener(v -> showAnswer());
    }

    public void getJoke() {
        // Gör "Visa Svar"-knappen synlig
        showAnswerButton.setVisibility(View.VISIBLE);

        // Dölja TextView för punchline initialt
        answerTextView.setVisibility(View.GONE);

        // Anropa API för att hämta ett skämt
        Call<Jokes> call = service.getJoke();
        call.enqueue(new Callback<Jokes>() {
            @Override
            public void onResponse(Call<Jokes> call, Response<Jokes> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Hämta skämtet från svaret
                    Jokes joke = response.body();
                    setupText = joke.getSetup();  // Sätt setup till variabeln
                    punchlineText = joke.getPunchline();  // Sätt punchline till variabeln

                    // Visa setup i TextView
                    jokeTextView.setText(setupText);


                } else {
                    Log.e(TAG, "Inget skämt hittades i svaret");
                }
            }

            @Override
            public void onFailure(Call<Jokes> call, Throwable t) {
                Log.e(TAG, "API-anrop misslyckades", t);
            }
        });
    }

    public void showAnswer() {
        // Visa punchline i TextView
        answerTextView.setText(punchlineText);

        // Gör TextView för punchline synlig
        answerTextView.setVisibility(View.VISIBLE);
    }
}

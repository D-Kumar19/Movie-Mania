package com.example.movies;

import java.util.Map;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.content.Intent;
import org.json.JSONException;
import android.widget.ImageView;
import android.widget.RadioGroup;
import com.android.volley.Request;
import android.widget.RadioButton;
import com.squareup.picasso.Picasso;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.atomic.AtomicBoolean;
import com.android.volley.toolbox.JsonObjectRequest;

public class GameActivity extends AppCompatActivity {

    private String correctYear = "", selectedYear = "", playerName = "", selectedGenre = "";
    private static final String API_KEY = "f8779901";

    private RadioGroup radioGroup;
    private ImageView posterOfMovie;
    private Button returnButton, submitAnswerButton;
    private RadioButton firstYearOptionButton, secondYearOptionButton, thirdYearOptionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        radioGroup = findViewById(R.id.radioGroup);
        returnButton = findViewById(R.id.returnButton);
        posterOfMovie = findViewById(R.id.posterImageView);
        submitAnswerButton = findViewById(R.id.submitButton);
        firstYearOptionButton = findViewById(R.id.yearOption1);
        thirdYearOptionButton = findViewById(R.id.yearOption3);
        secondYearOptionButton = findViewById(R.id.yearOption2);

        Intent intent = getIntent();
        if(intent != null) {
            selectedGenre = intent.getStringExtra("genre");
            playerName = intent.getStringExtra("playerName");
            displayMoviePoster();
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            RadioButton radioButton;
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                submitAnswerButton.setEnabled(true);
                radioButton = findViewById(checkedId);
                selectedYear = radioButton.getText().toString();
            }
        });
    }

    public void displayMoviePoster() {
        String[] moviesArray = getResources().getStringArray(R.array.movies_list);
        int maximum = moviesArray.length - 1;
        int minimum = 0;

        int movieIndex = (int) Math.floor(Math.random() * (maximum - minimum + 1) + minimum);

        String movieName = moviesArray[movieIndex];
        String url = "https://www.omdbapi.com/?t=" + movieName + "&apikey=" + API_KEY;

        AtomicBoolean movieFound = new AtomicBoolean(false);
        do{
            RequestQueue queue = Volley.newRequestQueue(GameActivity.this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
                try {
                    String genresOfMovie = response.getString("Genre");
                    String[] genres = genresOfMovie.split(", ");

                    for (String genre : genres) {
                        System.out.println(genre + " " + selectedGenre);
                        String year1, year2, year3;

                        if (selectedGenre == null || genre.equals(selectedGenre)) {
                            movieFound.set(true);

                            year1 = generateRandomYear();
                            do {
                                year2 = generateRandomYear();
                            } while (year1.equals(year2));

                            year3 = response.getString("Year");
                            setYearsToButtons(year1, year2, year3);

                            String posterUrl = response.getString("Poster");
                            Picasso.get().load(posterUrl).into(posterOfMovie);
                        }
                    }
                    if(!movieFound.get()){
                        displayMoviePoster();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> Log.e("MoviesAppError", "Failed to get data."));
            queue.add(jsonObjectRequest);
        }while(movieFound.get());
    }

    private String generateRandomYear() {
        int minimum = Integer.parseInt(getResources().getString(R.string.minimum_year));
        int maximum = Integer.parseInt(getResources().getString(R.string.maximum_year));

        return Integer.toString((int) Math.floor(Math.random() * (maximum - minimum + 1) + minimum));
    }

    private void setYearsToButtons(String year1, String year2, String year3){
        int a, b, c, minimum, maximum;
        minimum = Integer.parseInt(getResources().getString(R.string.minimum_choice));
        maximum = Integer.parseInt(getResources().getString(R.string.maximum_choice));

        Random random = new Random();
        a = random.nextInt(maximum) + minimum;
        do {
            b = random.nextInt(maximum) + minimum;
        } while(a == b);

        do{
            c = random.nextInt(maximum) + minimum;
        } while(a == c || b == c);

        correctYear = year3;

        String[] years = {year1, year2, year3};
        firstYearOptionButton.setText(years[a - 1]);
        secondYearOptionButton.setText(years[b - 1]);
        thirdYearOptionButton.setText(years[c - 1]);
    }

    public void checkAnswerMethod(View view) throws NullPointerException{
        radioGroup.setEnabled(false);
        returnButton.setEnabled(true);
        submitAnswerButton.setEnabled(false);
        firstYearOptionButton.setEnabled(false);
        thirdYearOptionButton.setEnabled(false);
        secondYearOptionButton.setEnabled(false);

        if(correctYear.equals(selectedYear)) {
            Database database = new Database(this);
            database.add(playerName);

            Toast.makeText(this, R.string.correct_result, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, R.string.incorrect_result, Toast.LENGTH_SHORT).show();
        }
    }

    public void returnMethod(View view) {
        Intent intent = new Intent(GameActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
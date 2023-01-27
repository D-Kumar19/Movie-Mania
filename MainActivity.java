package com.example.movies;

import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.text.Editable;
import android.content.Intent;
import android.widget.TextView;
import android.text.TextWatcher;
import android.annotation.SuppressLint;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private String selectedGenre;

    private TextView enterNameTextView;

    private Button startButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startButton = findViewById(R.id.startButton);
        enterNameTextView = findViewById(R.id.enterNameTextView);

        enterNameTextView.addTextChangedListener (new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                startButton.setEnabled(!enterNameTextView.getText().toString().trim().isEmpty());
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        Intent intent = getIntent();
        if(intent != null) {
            selectedGenre = intent.getStringExtra("genre");
        }
    }

    public void startGameButtonMethod(View view) {
        String playerName = enterNameTextView.getText().toString();

        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        intent.putExtra("playerName", playerName);
        intent.putExtra("genre", selectedGenre);
        startActivity(intent);
    }

    public void settingsButtonMethod(View view){
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    public void highScoreButtonMethod(View view){
        Intent intent = new Intent(MainActivity.this, HighScoreActivity.class);
        startActivity(intent);
    }
}
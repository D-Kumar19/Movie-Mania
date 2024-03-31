package com.example.movies;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private String selectedGenre = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        RadioGroup radioGroup = findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            RadioButton radioButton;
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = findViewById(checkedId);
                selectedGenre = radioButton.getText().toString();
            }
        });
    }

    public void returnMethod(View view) {
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        intent.putExtra("genre", selectedGenre);
        startActivity(intent);
    }
}
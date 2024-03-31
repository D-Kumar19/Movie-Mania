package com.example.movies;

import java.util.Map;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import java.util.HashMap;
import java.util.ArrayList;
import android.content.Intent;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;

public class HighScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        ListView listView = findViewById(R.id.listView);

        Database database = new Database(this);
        Map<String, Integer> highScores = database.get();

        ArrayList<String> highScoresList = new ArrayList<>();

        for (Map.Entry<String, Integer> result: highScores.entrySet()){
            highScoresList.add(result.getKey() + "     " + result.getValue());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, highScoresList);
        listView.setAdapter(arrayAdapter);
    }

    public void returnMethod(View view) {
        Intent intent = new Intent(HighScoreActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
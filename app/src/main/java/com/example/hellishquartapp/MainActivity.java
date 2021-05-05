package com.example.hellishquartapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView characterText = findViewById(R.id.character_text);
        TextView settingsText = findViewById(R.id.settings_text);
        characterText.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CharacterActivity.class)));
        settingsText.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SettingsActivity.class)));
    }
}
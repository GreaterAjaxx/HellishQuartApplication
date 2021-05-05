package com.example.hellishquartapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddActivity extends AppCompatActivity {
    private EditText editTextTitle;
    private EditText editTextInputs;
    private String who;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextInputs = findViewById(R.id.edit_text_inputs);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        who = bundle.getString("CHAR");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_add, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_combo:
                saveCombo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void saveCombo() {
        String title = editTextTitle.getText().toString();
        String inputs = editTextInputs.getText().toString();

        if(title.trim().isEmpty() || inputs.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and inputs", Toast.LENGTH_SHORT).show();
            return;
        }
        CollectionReference ref = FirebaseFirestore.getInstance()
            .collection("ComboCollection/" + who + "/Combos");
        ref.add(new Combo(title, inputs, false));
        Toast.makeText(this, "Combo Saved", Toast.LENGTH_SHORT).show();
        // closes activity
        finish();
    }
}
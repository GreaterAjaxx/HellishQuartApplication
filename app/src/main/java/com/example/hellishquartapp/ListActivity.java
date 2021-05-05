package com.example.hellishquartapp;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ListActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String[] CHAR_LIST = {"Isabella", "Gedeon", "Jacek", "Barabasz", "Marie"};
    private CollectionReference comboRef;
    private ComboAdapter adapter;
    private boolean filtered = false;
    private boolean flag = false;
    private int whichOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        TextView who = findViewById(R.id.whos);

        whichOne = getIntent().getIntExtra("CHAR_INDEX", 0);
        who.setText(CHAR_LIST[whichOne]);

        comboRef = db.collection("ComboCollection/"
                + CHAR_LIST[whichOne] + "/Combos");

        FloatingActionButton btn = findViewById(R.id.button_add_combo);
        Intent intent = new Intent(this, AddActivity.class);
        intent.putExtra("CHAR", CHAR_LIST[whichOne]);

        btn.setOnClickListener(v -> startActivity(intent));

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        Query query = comboRef.orderBy("title", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Combo> options = new FirestoreRecyclerOptions.Builder<Combo>()
                .setQuery(query, Combo.class)
                .build();
        adapter = new ComboAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                deleteTrigger(viewHolder);
            }
        }).attachToRecyclerView(recyclerView);
        adapter.setOnItemClickListener((documentSnapshot, position) -> {
            //Combo combo = documentSnapshot.toObject(Combo.class);
            String id = documentSnapshot.getId();
            //String path = documentSnapshot.getReference().getPath();

            DocumentReference docRef = comboRef.document(id);
            docRef.update("liked", !documentSnapshot.getBoolean("liked"));
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_list, menu);
        if(!flag) {
            menu.getItem(1).setIcon(R.drawable.ic_thumbs_up_down);
        }
        else if (filtered) {
            menu.getItem(1).setIcon(R.drawable.ic_thumb_up);
        } else {
            menu.getItem(1).setIcon(R.drawable.ic_thumb_down);
        }
        return true;
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                Intent intent = new Intent(this, AddActivity.class);
                intent.putExtra("CHAR", CHAR_LIST[whichOne]);
                startActivity(intent);
                return true;
            case R.id.menu_filter:
                this.filtered = !filtered;
                flag = true;
                if (filtered) {
                    item.setIcon(R.drawable.ic_thumb_up);
                } else {
                    item.setIcon(R.drawable.ic_thumb_down);
                }
                Query newQuery = comboRef.whereEqualTo("liked", filtered);
                FirestoreRecyclerOptions<Combo> newOptions = new FirestoreRecyclerOptions.Builder<Combo>()
                        .setQuery(newQuery, Combo.class)
                        .build();
                adapter.updateOptions(newOptions);
                return true;
            case R.id.menu_settings:
                startActivity(new Intent(ListActivity.this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    public void deleteTrigger(RecyclerView.ViewHolder viewHolder) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Delete Dialog");
        builder.setMessage("Would you like to delete this combo?");
        builder.setPositiveButton("Yes",
                (dialog, which) -> adapter.deleteItem(viewHolder.getAdapterPosition()));
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Query newQuery;
                if(!flag) {
                    newQuery = comboRef.orderBy("title", Query.Direction.DESCENDING);
                }
                else {
                    newQuery = comboRef.whereEqualTo("liked", filtered);
                }
                FirestoreRecyclerOptions<Combo> newOptions = new FirestoreRecyclerOptions.Builder<Combo>()
                        .setQuery(newQuery, Combo.class)
                        .build();
                adapter.updateOptions(newOptions);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
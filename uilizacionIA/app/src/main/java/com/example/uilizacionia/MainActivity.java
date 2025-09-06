package com.example.uilizacionia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements HobbyAdapter.OnHobbyActionListener {

    private RecyclerView recyclerViewHobbies;
    private TextView textViewEmptyState;
    private FloatingActionButton fabAddHobby;

    private HobbyAdapter hobbyAdapter;
    private HobbyDatabaseHelper dbHelper;
    private List<Hobby> hobbiesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        initViews();
        initDatabase();
        setupRecyclerView();
        setupListeners();
        loadHobbies();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        recyclerViewHobbies = findViewById(R.id.recyclerViewHobbies);
        textViewEmptyState = findViewById(R.id.textViewEmptyState);
        fabAddHobby = findViewById(R.id.fabAddHobby);

        setSupportActionBar(toolbar);
    }

    private void initDatabase() {
        dbHelper = new HobbyDatabaseHelper(this);
        hobbiesList = new ArrayList<>();
    }

    private void setupRecyclerView() {
        hobbyAdapter = new HobbyAdapter(this, hobbiesList, this);
        recyclerViewHobbies.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewHobbies.setAdapter(hobbyAdapter);
    }

    private void setupListeners() {
        fabAddHobby.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditHobbyActivity.class);
            startActivity(intent);
        });
    }

    private void loadHobbies() {
        hobbiesList.clear();
        hobbiesList.addAll(dbHelper.getAllHobbies());
        hobbyAdapter.updateHobbies(hobbiesList);

        updateEmptyState();
    }

    private void updateEmptyState() {
        if (hobbiesList.isEmpty()) {
            recyclerViewHobbies.setVisibility(View.GONE);
            textViewEmptyState.setVisibility(View.VISIBLE);
        } else {
            recyclerViewHobbies.setVisibility(View.VISIBLE);
            textViewEmptyState.setVisibility(View.GONE);
        }
    }

    @Override
    public void onEditHobby(Hobby hobby) {
        Intent intent = new Intent(MainActivity.this, AddEditHobbyActivity.class);
        intent.putExtra("hobby_id", hobby.getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteHobby(Hobby hobby) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete);
        builder.setMessage(R.string.confirm_delete);
        builder.setPositiveButton(R.string.yes, (dialog, which) -> {
            dbHelper.deleteHobby(hobby.getId());
            Toast.makeText(MainActivity.this, R.string.hobby_deleted, Toast.LENGTH_SHORT).show();
            loadHobbies(); // Recargar la lista
        });
        builder.setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadHobbies(); // Recargar hobbies cuando se vuelve a la actividad
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}
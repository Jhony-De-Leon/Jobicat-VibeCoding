package com.example.uilizacionia;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

public class AddEditHobbyActivity extends AppCompatActivity {

    private TextInputEditText editTextHobbyName;
    private RadioGroup radioGroupDifficulty;
    private RadioButton radioButtonEasy, radioButtonMedium, radioButtonHard;
    private Button buttonSave, buttonCancel;
    private Toolbar toolbar;

    private HobbyDatabaseHelper dbHelper;
    private boolean isEditMode = false;
    private int hobbyId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_hobby);

        initViews();
        initDatabase();
        checkEditMode();
        setupListeners();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        editTextHobbyName = findViewById(R.id.editTextHobbyName);
        radioGroupDifficulty = findViewById(R.id.radioGroupDifficulty);
        radioButtonEasy = findViewById(R.id.radioButtonEasy);
        radioButtonMedium = findViewById(R.id.radioButtonMedium);
        radioButtonHard = findViewById(R.id.radioButtonHard);
        buttonSave = findViewById(R.id.buttonSave);
        buttonCancel = findViewById(R.id.buttonCancel);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initDatabase() {
        dbHelper = new HobbyDatabaseHelper(this);
    }

    private void checkEditMode() {
        Intent intent = getIntent();
        if (intent.hasExtra("hobby_id")) {
            isEditMode = true;
            hobbyId = intent.getIntExtra("hobby_id", -1);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(R.string.edit_hobby);
            }

            loadHobbyData();
        } else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(R.string.add_hobby);
            }
        }
    }

    private void loadHobbyData() {
        Hobby hobby = dbHelper.getHobby(hobbyId);
        if (hobby != null) {
            editTextHobbyName.setText(hobby.getNombre());

            switch (hobby.getDificultad()) {
                case "Fácil":
                    radioButtonEasy.setChecked(true);
                    break;
                case "Medio":
                    radioButtonMedium.setChecked(true);
                    break;
                case "Difícil":
                    radioButtonHard.setChecked(true);
                    break;
            }
        }
    }

    private void setupListeners() {
        buttonSave.setOnClickListener(v -> saveHobby());
        buttonCancel.setOnClickListener(v -> finish());

        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void saveHobby() {
        String hobbyName = editTextHobbyName.getText() != null ?
            editTextHobbyName.getText().toString().trim() : "";

        // Validar que el nombre no esté vacío
        if (TextUtils.isEmpty(hobbyName)) {
            Toast.makeText(this, R.string.error_empty_name, Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar que se haya seleccionado una dificultad
        int selectedRadioButtonId = radioGroupDifficulty.getCheckedRadioButtonId();
        if (selectedRadioButtonId == -1) {
            Toast.makeText(this, R.string.error_no_difficulty, Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener la dificultad seleccionada
        String difficulty = "";
        RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
        if (selectedRadioButton == radioButtonEasy) {
            difficulty = getString(R.string.difficulty_easy);
        } else if (selectedRadioButton == radioButtonMedium) {
            difficulty = getString(R.string.difficulty_medium);
        } else if (selectedRadioButton == radioButtonHard) {
            difficulty = getString(R.string.difficulty_hard);
        }

        Hobby hobby = new Hobby(hobbyName, difficulty);

        if (isEditMode) {
            // Actualizar hobby existente
            hobby.setId(hobbyId);
            int result = dbHelper.updateHobby(hobby);
            if (result > 0) {
                Toast.makeText(this, R.string.hobby_updated, Toast.LENGTH_SHORT).show();
            }
        } else {
            // Crear nuevo hobby
            long result = dbHelper.addHobby(hobby);
            if (result != -1) {
                Toast.makeText(this, R.string.hobby_saved, Toast.LENGTH_SHORT).show();
            }
        }

        // Finalizar la actividad y volver a MainActivity
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}

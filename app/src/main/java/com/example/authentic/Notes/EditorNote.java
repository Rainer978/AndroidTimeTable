package com.example.authentic.Notes;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.authentic.R;

public class EditorNote extends AppCompatActivity {
    private TaskNote n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor_note);

        EditText title_note = findViewById(R.id.title_note);
        EditText description_note = findViewById(R.id.description_note);

        Button button_note = findViewById(R.id.button_note);
        TextView delete_note = findViewById(R.id.delete_note);

        DataBaseNote databasenote = new DataBaseNote(this);
        n = new TaskNote();
        String date = getIntent().getStringExtra("Date");
        n.setID(databasenote.getNextID(date));

        if (getIntent().hasExtra("Task")) {
            n.setID(getIntent().getIntExtra("ID", 0));
            n.setName(getIntent().getStringExtra("Name"));
            n.setDiscription(getIntent().getStringExtra("Description"));

            title_note.setText(n.getName());
            description_note.setText(n.getDescription());

            button_note.setOnClickListener(v -> {
                if (title_note.getText().toString().equals("")) {
                    title_note.setError("Поле не может быть пустым!");
                    return;
                }
                n.setName(title_note.getText().toString());
                n.setDiscription(description_note.getText().toString());
                databasenote.updateTaskNote(n, date); // обновление задачи, если она уже существует
                Toast.makeText(this, "Успешно обновлено", Toast.LENGTH_SHORT).show();
                finish();
            });
            delete_note.setOnClickListener(v -> {
                databasenote.deleteTaskNote(n.getID(), date);
                Toast.makeText(this, "Успешно удалено", Toast.LENGTH_SHORT).show();
                finish();
            });

        } else {
            button_note.setOnClickListener(v -> {
                if (title_note.getText().toString().equals("")) {
                    title_note.setError("Поле не может быть пустым!");
                    return;
                }
                n.setName(title_note.getText().toString());
                n.setDiscription(description_note.getText().toString());
                databasenote.addTaskNote(n, date); // добавление новой задачи
                Toast.makeText(this, "Успешно добавлено", Toast.LENGTH_SHORT).show();
                finish();
            });
            delete_note.setVisibility(View.GONE); // скрытие кнопки удаления для новых задач
        }
    }
}

package com.example.authentic;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.authentic.Notes.DataBaseNote;
import com.example.authentic.Notes.EditorNote;
import com.example.authentic.Notes.TaskNote;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;

public class NoteActivity extends AppCompatActivity {

    private LocalDate showedDateNote;
    private ArrayList<TaskNote> taskNote;
    private final DateTimeFormatter mainDateNote = DateTimeFormatter.ofPattern("EEEE dd/MM");
    private DataBaseNote databaseNote;
    private TextView date_note_form;
    private NoteActivity.ListAdapter listAdapterNote;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        date_note_form = findViewById(R.id.date_note_form);
        LinearLayout add_note = findViewById(R.id.add_note);
        ListView listview_note = findViewById(R.id.listview_note);

        taskNote = new ArrayList<>();
        listAdapterNote = new NoteActivity.ListAdapter();
        listview_note.setAdapter(listAdapterNote);
        databaseNote = new DataBaseNote(this);

        showedDateNote = LocalDate.now();
        RefreshData();

        date_note_form.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year, month, day) -> {
                showedDateNote = LocalDate.of(year, month + 1, day);
                RefreshData();
            }, showedDateNote.getYear(), showedDateNote.getMonthValue() - 1, showedDateNote.getDayOfMonth());
            datePickerDialog.show();
        });

        add_note.setOnClickListener(v -> {
            Intent intent = new Intent(NoteActivity.this, EditorNote.class);
            intent.putExtra("Date", showedDateNote.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        RefreshData();
    }

    private void RefreshData() {
        date_note_form.setText(showedDateNote.format(mainDateNote));
        ArrayList<TaskNote> ts = databaseNote.getAllTasksNote(showedDateNote.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        Collections.sort(ts);
        taskNote = ts;
        listAdapterNote.notifyDataSetChanged();
    }

    public class ListAdapter extends BaseAdapter {

        public ListAdapter() {
        }

        @Override
        public int getCount() {

            return taskNote.size();
        }

        @Override
        public TaskNote getItem(int i) {

            return taskNote.get(i);
        }

        @Override
        public long getItemId(int i) {

            return i;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = getLayoutInflater();
            @SuppressLint({"InflateParams", "ViewHolder"})
            View v = inflater.inflate(R.layout.note1, null);

            TextView titleNoteForm = v.findViewById(R.id.titleNoteForm);
            TextView descr_note = v.findViewById(R.id.descr_note);

            TaskNote n = taskNote.get(i);

            titleNoteForm.setText(n.getName());
            descr_note.setText(n.getDescription());

            titleNoteForm.setOnLongClickListener(v2 -> {
                Intent intent = new Intent(NoteActivity.this, EditorNote.class);
                intent.putExtra("ID", n.getID());
                intent.putExtra("Name", n.getName());
                intent.putExtra("Description", n.getDescription());
                startActivity(intent);
                return true;
            });

            return v;
        }
    }
}

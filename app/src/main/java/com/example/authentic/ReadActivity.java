package com.example.authentic;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReadActivity extends AppCompatActivity {
    private LocalDate showedDate;
    private ArrayList<Task> tasks;
    private final DateTimeFormatter mainDate = DateTimeFormatter.ofPattern("EEEE dd/MM");
    private Database database;
    private TextView date;
    private ReadActivity.ListAdapter listAdapter;
    private DatabaseReference mDataBase;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private String USER_KEY = "User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_layout);

        date = findViewById(R.id.date1);
        ImageView left = findViewById(R.id.left1);
        ImageView right = findViewById(R.id.right1);
        ListView listview = findViewById(R.id.listview1);
        mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);

        tasks = new ArrayList<>();
        listAdapter = new ReadActivity.ListAdapter();
        listview.setAdapter(listAdapter);
        database = new Database(this);

        showedDate = java.time.LocalDate.now();
        RefreshData();

        date.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, year, month, day) -> {
                showedDate = java.time.LocalDate.of(year, month + 1, day);
                RefreshData();
            }, showedDate.getYear(), showedDate.getMonthValue() - 1, showedDate.getDayOfMonth());
            datePickerDialog.show();
        });

        left.setOnClickListener(v -> {
            showedDate = showedDate.minusDays(1);
            RefreshData();
        });

        right.setOnClickListener(v -> {
            showedDate = showedDate.plusDays(1);
            RefreshData();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        RefreshData();
    }

    private void RefreshData() {
        date.setText(showedDate.format(mainDate));
        ArrayList<Task> ts = database.getAllTasks(showedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        Collections.sort(ts);
        tasks = ts;
        listAdapter.notifyDataSetChanged();
    }

    public class ListAdapter extends BaseAdapter {

        public ListAdapter() {
        }

        @Override
        public int getCount() {
            return tasks.size();
        }

        @Override
        public Task getItem(int i) {
            return tasks.get(i);
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
            View v = inflater.inflate(R.layout.task, null);

            TextView from = v.findViewById(R.id.from);
            TextView to = v.findViewById(R.id.to);
            TextView task = v.findViewById(R.id.task);
            TextView teacher = v.findViewById(R.id.teacher); // Новое поле для преподавателя
            TextView room = v.findViewById(R.id.room); // Новый элемент для номера кабинета

            Task t = tasks.get(i);

            from.setText(t.getFromToString());
            to.setText(t.getToToString());
            task.setText(t.getTask());
            teacher.setText(t.getTeacher()); // Устанавливаем преподавателя
            room.setText(t.getRoomNumber()); // Устанавливаем номер кабинета

            GradientDrawable backDrawable = (GradientDrawable) task.getBackground();
            backDrawable.setColor(t.getColorID(ReadActivity.this));

            task.setOnLongClickListener(v2 -> {
                Intent intent = new Intent(ReadActivity.this, TaskEditor.class);
                intent.putExtra("ID", t.getID());
                intent.putExtra("Task", t.getTask());
                intent.putExtra("From", t.getFromToString());
                intent.putExtra("To", t.getToToString());
                intent.putExtra("Color", t.getColor());
                intent.putExtra("RoomNumber", t.getRoomNumber()); // Передаем номер кабинета
                intent.putExtra("Teacher", t.getTeacher()); // Передаем преподавателя
                intent.putExtra("Date", showedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                startActivity(intent);
                return true;
            });

            return v;
        }
    }
}

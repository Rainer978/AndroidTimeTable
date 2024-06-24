package com.example.authentic;

import android.app.NotificationManager;
import android.app.NotificationChannel;
import static androidx.core.app.NotificationCompat.PRIORITY_HIGH;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import java.text.DecimalFormat;

public class TaskEditor extends AppCompatActivity {

    /*private NotificationManager notificationManager;
    private static final int NOTIFY_ID = 1;
    private static final String CHANELL_ID = "CHABBEL_ID";
    Button submit;*/
    private Task t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_editor);

        EditText task = findViewById(R.id.task);
        EditText teacher = findViewById(R.id.teacher); // Новое поле для преподавателя
        EditText roomNumber = findViewById(R.id.room_number); // Новое поле для номера кабинета

        TextView from = findViewById(R.id.from);
        TextView to = findViewById(R.id.to);
        Spinner color = findViewById(R.id.color);
        Button submit = findViewById(R.id.submit);
        TextView delete = findViewById(R.id.delete);

        /*notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);*/

        Database database = new Database(this);
        t = new Task();
        String date = getIntent().getStringExtra("Date");
        t.setID(database.getNextID(date));

        String[] colors = {"Rose", "Blue", "Green", "Red", "Yellow", "Orange", "Purple", "Grey"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, colors);
        color.setAdapter(adapter);

        if (getIntent().hasExtra("Task")) {
            t.setID(getIntent().getIntExtra("ID", 0));
            t.setTask(getIntent().getStringExtra("Task"));
            t.setFrom(getIntent().getStringExtra("From"));
            t.setTo(getIntent().getStringExtra("To"));
            t.setColor(getIntent().getStringExtra("Color"));
            t.setRoomNumber(getIntent().getStringExtra("RoomNumber")); // Получаем номер кабинета
            t.setTeacher(getIntent().getStringExtra("Teacher")); // Получаем преподавателя
            color.setSelection(adapter.getPosition(t.getColor()));
            GradientDrawable background = (GradientDrawable) color.getBackground();
            background.setColor(t.getColorID(TaskEditor.this));
            task.setText(t.getTask());
            from.setText(t.getFromToString());
            to.setText(t.getToToString());
            roomNumber.setText(t.getRoomNumber()); // Устанавливаем номер кабинета
            teacher.setText(t.getTeacher()); // Устанавливаем преподавателя

            submit.setOnClickListener(v-> {
                if (task.getText().toString().equals("")) {
                    task.setError("Поле не может быть пустым!");
                    return;
                }
                if (from.getText().equals("Время")) {
                    Toast.makeText(this, "Выберите время: Начало", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (to.getText().equals("Время")) {
                    Toast.makeText(this, "Выберите время: Конец", Toast.LENGTH_SHORT).show();
                    return;
                }
                t.setTask(task.getText().toString());
                t.setRoomNumber(roomNumber.getText().toString()); // Устанавливаем номер кабинета
                t.setTeacher(teacher.getText().toString()); // Устанавливаем преподавателя
                database.updateTask(t, date);
                Toast.makeText(this, "Успешно обновлено", Toast.LENGTH_SHORT).show();
                finish();
            });

            delete.setOnClickListener(v-> {
                database.deleteTask(t.getID(), date);
                Toast.makeText(this, "Успешно удалено", Toast.LENGTH_SHORT).show();
                finish();
            });

        } else {
            submit.setOnClickListener(v-> {
                if (task.getText().toString().equals("")) {
                    task.setError("Поле не может быть пустым!");
                    return;
                }
                if (from.getText().equals("Время")) {
                    Toast.makeText(this, "Выберите время: Начало", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (to.getText().equals("Время")) {
                    Toast.makeText(this, "Выберите время: Конец", Toast.LENGTH_SHORT).show();
                    return;
                }
                t.setTask(task.getText().toString());
                t.setRoomNumber(roomNumber.getText().toString()); // Устанавливаем номер кабинета
                t.setTeacher(teacher.getText().toString()); // Устанавливаем преподавателя
                database.addTask(t, date);
                Toast.makeText(this, "Успешно добавлено", Toast.LENGTH_SHORT).show();
                finish();
            });

            delete.setVisibility(View.GONE);

        }

        from.setOnClickListener(v-> {
            @SuppressLint("SetTextI18n")
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, hh, mm) -> {
                String ho = new DecimalFormat("00").format(hh);
                String min = new DecimalFormat("00").format(mm);
                from.setText(ho+":"+min);
                t.setFrom(ho+":"+min);
            }, t.getFrom().getHour(), t.getFrom().getMinute(), true);
            timePickerDialog.show();
        });

        to.setOnClickListener(v-> {
            @SuppressLint("SetTextI18n")
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, (timePicker, hh, mm) -> {
                String ho = new DecimalFormat("00").format(hh);
                String min = new DecimalFormat("00").format(mm);
                to.setText(ho+":"+min);
                t.setTo(ho+":"+min);
            }, t.getTo().getHour(), t.getTo().getMinute(), true);
            timePickerDialog.show();
        });

        color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                t.setColor(color.getSelectedItem().toString());
                GradientDrawable background = (GradientDrawable) color.getBackground();
                background.setColor(t.getColorID(TaskEditor.this));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        /*notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ReadActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationCompat.Builder notificationBuilder =
                        new NotificationCompat.Builder(getApplicationContext(), CHANELL_ID)
                                .setAutoCancel(false)
                                .setSmallIcon(R.drawable.ic_launcher_foreground)
                                .setWhen(System.currentTimeMillis())
                                .setContentIntent(pendingIntent)
                                .setContentTitle("Объявление")
                                .setContentText("Изменения в расписании")
                                .setPriority(NotificationCompat.PRIORITY_HIGH);

                createChannelIfNeeded(); // Вызов метода для создания канала уведомлений, если нужно

                notificationManager.notify(NOTIFY_ID, notificationBuilder.build()); // Отправка уведомления
            }
        });

    }
    private void createChannelIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANELL_ID,
                    "Channel Name",
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }*/
    }
}
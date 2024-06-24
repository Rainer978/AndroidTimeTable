package com.example.authentic;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity  extends AppCompatActivity {
    private EditText editLogin, editPassword;
    FirebaseAuth mAuth;
    private Button bStart, bSignUp, bSignIn, bSignOut, bell, note;
    private TextView tvUserName;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        init();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        FirebaseUser cUser = mAuth.getCurrentUser();
        if (cUser != null)
        {
            showSigned();
            String userName = "Добро пожаловать: " + cUser.getEmail();
            tvUserName.setText(userName);


            Toast.makeText(this, "Пользователь уже зарегистрирован", Toast.LENGTH_SHORT).show();
        }
        else
        {
            notSigned();
            Toast.makeText(this, "Пользователь не зарегистрирован", Toast.LENGTH_SHORT).show();
        }
    }

    private void init()
    {
        tvUserName = findViewById(R.id.tvUserName);
        note = findViewById(R.id.note);
        bell = findViewById(R.id.bell);
        bStart = findViewById(R.id.bStart);
        bSignIn = findViewById(R.id.bSignIn);
        bSignUp = findViewById(R.id.bSignUp);
        bSignOut = findViewById(R.id.bSignOut);
        editLogin = findViewById(R.id.editLogin);
        editPassword = findViewById(R.id.editPassword);
        mAuth = FirebaseAuth.getInstance();
    }
    public void onClickSignUp (View view) {
        if (!TextUtils.isEmpty(editLogin.getText().toString()) && !TextUtils.isEmpty(editPassword.getText().toString())) {
            mAuth.createUserWithEmailAndPassword(editLogin.getText().toString(), editPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {
                        showSigned();
                        Toast.makeText(getApplicationContext(), "Пользователь успешно зарегистрирован", Toast.LENGTH_SHORT).show();
                    } else
                    {
                        notSigned();
                        Toast.makeText(getApplicationContext(), "Пользователь уже зарегистрирован", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        }
        else
        {
            Toast.makeText(getApplicationContext(), "Введите данные", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickSighIn (View view)
    {

        if (!TextUtils.isEmpty(editLogin.getText().toString()) && !TextUtils.isEmpty(editPassword.getText().toString()))
        {
            mAuth.signInWithEmailAndPassword(editLogin.getText().toString(),editPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful())
                    {
                        showSigned();
                        Toast.makeText(getApplicationContext(), "Пользователь вошел", Toast.LENGTH_SHORT).show();
                    } else {
                        notSigned();
                        Toast.makeText(getApplicationContext(), "Пользователь не может войти", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }
    }
    public void onClickSignOut(View view)
    {
        FirebaseAuth.getInstance().signOut();
        notSigned();
    }
    private void showSigned()
    {
        note.setVisibility(View.VISIBLE);
        bell.setVisibility(View.VISIBLE);
        tvUserName.setVisibility(View.VISIBLE);
        bStart.setVisibility(View.VISIBLE);
        bSignOut.setVisibility(View.VISIBLE);
        editLogin.setVisibility(View.GONE);
        editPassword.setVisibility(View.GONE);
        bSignIn.setVisibility(View.GONE);
        bSignUp.setVisibility(View.GONE);
    }
    private void notSigned()
    {
        note.setVisibility(View.GONE);
        bell.setVisibility(View.GONE);
        tvUserName.setVisibility(View.GONE);
        bStart.setVisibility(View.GONE);
        bSignOut.setVisibility(View.GONE);
        editLogin.setVisibility(View.VISIBLE);
        editPassword.setVisibility(View.VISIBLE);
        bSignIn.setVisibility(View.VISIBLE);
        bSignUp.setVisibility(View.VISIBLE);
    }
    public void onClickStart(View view)
    {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String userName = currentUser.getEmail();
        String admin = "rainer01@list.ru";
        if (userName.equals(admin)) {
            /** Переход для администратора **/
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
        }
        else{
            /** Переход для пользователей **/
            Intent i = new Intent(LoginActivity.this, ReadActivity.class);
            startActivity(i);
        }
    }

    public void onClickBell(View view)
    {
        Intent i = new Intent(LoginActivity.this, BellActivity.class);
        startActivity(i);
    }

    public void onClickNote(View view)
    {
        Intent i = new Intent(LoginActivity.this, NoteActivity.class);
        startActivity(i);
    }
}

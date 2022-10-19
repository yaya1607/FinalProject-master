package com.example.finalproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class DifficultyActivity extends AppCompatActivity {
    int result = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);
    }

    public void easyMode(View view) {
        Intent i = new Intent(this,GameplayActivity.class);
        i.putExtra("Mode","Easy");
        startActivityForResult(i,result);
    }

    public void mediumMode(View view) {
        Intent i = new Intent(this,GameplayActivity.class);
        i.putExtra("Mode","Medium");
        startActivityForResult(i,result);
    }

    public void hardMode(View view) {
        Intent i = new Intent(this,GameplayActivity.class);
        i.putExtra("Mode","Hard");
        startActivityForResult(i,result);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }
}
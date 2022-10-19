package com.example.finalproject;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Database database;

    int lanchUserActivity = 1;
    int lanchGame = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createDatabase();
        Cursor leaderboard = database.GetData("SELECT * FROM Leaderboard");
        while (leaderboard.moveToNext()){
            String ten = leaderboard.getInt(0)+") "+ leaderboard.getString(1) + ": " + leaderboard.getString(2)+ ", " + leaderboard.getString(3)+ ", " + leaderboard.getString(4);
            Toast.makeText(this,ten,Toast.LENGTH_LONG).show();
        }
        database.QueryData("DELETE FROM leaderboard");

        ImageView btnStart = (ImageView) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,DifficultyActivity.class);
                startActivityForResult(intent,lanchGame);

            }
        });


        ImageView btnLeaderboard =  (ImageView) findViewById(R.id.btnLeaderBoard);
        btnLeaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Leaderboards will be available in the next game updates", Toast.LENGTH_LONG).show();

            }
        });


        ImageView btnAccount =  (ImageView) findViewById(R.id.btnConfirmUser);
        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,UserActivity.class);
                startActivityForResult(intent,lanchUserActivity);
            }
        });

    }

    private void createDatabase(){
        database = new Database(this, "MatchingTheTwo.sqlite",null,1) ;

        database.QueryData("CREATE TABLE IF NOT EXISTS Leaderboard(Id INTEGER PRIMARY KEY AUTOINCREMENT, Name VARCHAR(200), EasyScore INTEGER, MediumScore INTEGER, HardScore INTEGER)");

        database.QueryData("INSERT INTO Leaderboard VALUES (null, 'Triet', 200, 350, 500) ");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){ // Tránh trường hợp chưa có data đã bấm về.
            if(requestCode == lanchUserActivity){
                String strName = data.getStringExtra("NameUser");
                String strAge = data.getStringExtra("AgeUser");
                Toast.makeText(getApplicationContext(),"Welcome " + strName  ,Toast.LENGTH_LONG).show();
            }
        }

    }
}
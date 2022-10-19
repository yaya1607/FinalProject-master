package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        setSpinnerItem();
    }
    public void onConfirmClick(View view){
        Intent intent = new Intent(this,MainActivity.class);
        EditText etNameUser = (EditText) findViewById(R.id.etNameUser);
        Spinner spAgeUser = (Spinner) findViewById(R.id.spinnerAgeUser);

        intent.putExtra("AgeUser",spAgeUser.getSelectedItem().toString());

        String strNameUser = new String(etNameUser.getText().toString());
        strNameUser = cleanText(strNameUser);
        intent.putExtra("NameUser", strNameUser);
        if (strNameUser.equals("")){
            Toast.makeText(UserActivity.this,"Please enter your name!",Toast.LENGTH_LONG).show();
        }else{
            int result = 1;
            setResult(MainActivity.RESULT_OK,intent);
            finish();
        }
    }


    public String cleanText(String str){
        if(!str.isEmpty()){
            //Xóa khoảng cách đầu chuỗi.
            while(str.charAt(0) == ' ' ){
                str = str.substring(1);
                if (str.isEmpty()) break;
            }

            //Xóa khoảng giữa các chữ .
            for (int i=0; i<str.length(); i++){
                if (str.charAt(i)==' '&& str.charAt(i+1)==' '){
                    str = str.substring(0,i)+str.substring(i+1);
                    i--;
                }
            }

            //Xóa Enter giữa chuỗi.
            str = str.replace("\n"," ");
        }
        return str;
    }

    private void setSpinnerItem(){
        Spinner spinner = (Spinner) findViewById(R.id.spinnerAgeUser);
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1);
        for(int i=1;i<100;i++){
            arrayAdapter.add(i);
        }
        spinner.setAdapter(arrayAdapter);
    }
}
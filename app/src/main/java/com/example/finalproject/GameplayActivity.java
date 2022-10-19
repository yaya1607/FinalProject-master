package com.example.finalproject;

import static com.example.finalproject.R.drawable.absol;
import static com.example.finalproject.R.drawable.alakazam;
import static com.example.finalproject.R.drawable.ampharos;
import static com.example.finalproject.R.drawable.backside;
import static com.example.finalproject.R.drawable.blaziken;
import static com.example.finalproject.R.drawable.chim_lua;
import static com.example.finalproject.R.drawable.chim_set;
import static com.example.finalproject.R.drawable.gyarados;
import static com.example.finalproject.R.drawable.lucario;
import static com.example.finalproject.R.drawable.mewtwo;
import static com.example.finalproject.R.drawable.pidgeot;
import static com.example.finalproject.R.drawable.chim_tuyet;
import static com.example.finalproject.R.drawable.gengar;
import static com.example.finalproject.R.drawable.rayquaza;
import static com.example.finalproject.R.drawable.salamence;
import static com.example.finalproject.R.drawable.sceptile;
import static com.example.finalproject.R.drawable.steelix;
import static com.example.finalproject.R.drawable.swampert;
import static com.example.finalproject.R.drawable.venusaur;
import static com.example.finalproject.R.drawable.tyranitar;
import static com.example.finalproject.R.drawable.charizard;
import static com.example.finalproject.R.drawable.blastoise;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class GameplayActivity extends AppCompatActivity {
    private int countCard = 0;
    int card;
    int x = 0, y = 0;
    int preCardId;//biến giữ tạm id của lá vừa chọn.
    private int[] countTag ;
    int result =0;
    public static int height;
    public static int width;
    private TextView scoreLabel, startLabel;
    private int score;
    private SoundPlayer soundPlayer;
    private CountDownTimer countDownTimer;
    private int multiScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameplay);

        int time = 0;
        Intent i = getIntent();

        //Nhận độ khó và set độ rộng màn chơi.
        switch (i.getStringExtra("Mode")){
            case "Easy":{
                x=4;y=4;
                time = 60000;
                break;
            }
            case "Medium":{
                x=6;y=4;
                time = 90000;
                break;
            }
            case "Hard":{
                x=8;y=5;
                time = 120000;
                break;
            }
        }
        card = (4*4)/2;

        countTag = new int[x*y];
        setCountTag();

        getHeightAndWidth();
        Toast.makeText(this,height +" "+width,Toast.LENGTH_LONG).show();

        createButtonList();

        createClock(time);
    }

    public void createButtonList() {//Tạo tự động bộ bài theo số lượng đã cho

        LinearLayout oLL = (LinearLayout) findViewById(R.id.outsideLinearLayout);
        for (int i = 0; i < x; i++) {
            // Ý tưởng: -Gắn constraintLayout vào linearLayout tượng trưng cho mỗi dòng của bộ bài.
            //          -Sau đó gắn từng lá bài vào từng dòng tạo thành ma trận 2 chiều.
            //          -Set hình gốc là lá backside cho từng lá bài, setTag cho mỗi lá sao cho chỉ 2 lá có thể chung 1 tag.
            //          -onClick sẽ gọi hàm flipUp đồng thời thay đổi hình background của lá bài thành 1 hình pokemon đc gán theo tag.
            //          -nếu lật trùng thì 2 lá biến mất, không trùng thì gọi hàm flipDown.

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.bottomMargin = 5;//khoảng cách mỗi dòng.

            LinearLayout linearLayout = new LinearLayout(GameplayActivity.this);
            linearLayout.setLayoutParams(params);//Apply setting cho constrainLayout.
            oLL.addView(linearLayout);//Gắn thêm layout vào layout gốc để tạo thành từng dòng.

            for (int j = 1; j <= y; j++) {
                ImageButton imageButton = new ImageButton(GameplayActivity.this);
                LinearLayout.LayoutParams dotparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                dotparams.setMargins(0, 0, 0, 0);//Chỉnh khoảng cách giữa các lá bài
                dotparams.height =  ((height - 300 -(x-1)*10 )/x);//Chỉnh độ dài mỗi lá bài.
                dotparams.width =  ((width)/y);//Chỉnh rộng.
                imageButton.setLayoutParams(dotparams);//Apply setting cho mỗi lá bài
                imageButton.setBackgroundResource(backside);//Set hình cho lá bài
                int finalI = i;
                int finalJ = j;

                imageButton.setId(View.generateViewId());//Tạo id cho mỗi lá bài, id là số nguyên 1, 2, 3,.... Tìm lá bài bằng findViewById(R.id.1),.....

                //Tạo tag random tương ứng với biến đếm loại tag, nếu loại tag nào đã có 2 lá thì không tạo loại tag đó nữa
                int random = new Random().nextInt((x * y) / 2);
                while (imageButton.getTag() == null) {
                    if (countTag[random] < 2) {
                        imageButton.setTag(random);
                        countTag[random]++;
                    } else {
                        random = new Random().nextInt(x * y / 2);
                    }
                }

                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TextView tv = (TextView) findViewById(R.id.tv1);
                        tv.setText(String.valueOf(imageButton.getTag()));
                        flipUp(imageButton);

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {//Tạo delay để thẻ bài lật lên còn thấy đc :v
                            @Override
                            public void run() {
                                switch (countCard) {//Hàm xét xem đã đủ 2 thẻ bài để xét chưa
                                    case 0: {
                                        countCard++;
                                        preCardId = imageButton.getId();//Nếu chỉ có 1 lá được lật thì lấy id của lá đó để match với lá sau.
                                        break;
                                    }
                                    case 1: {
                                        countCard = 0;
                                        ImageButton preImageButton = (ImageButton) findViewById(preCardId);
                                        if (imageButton.getTag() == preImageButton.getTag()) {
                                            disappear(imageButton);
                                            disappear(preImageButton);
                                            score+= (multiScore/1000);
                                            card--;
                                            scoreLabel.setText(getString(R.string.score, score));
                                            soundPlayer.playHitSound();
                                            if(card == 0){
                                                winGame();
                                            }
                                        }
                                        else {
                                            flipDown(imageButton);
                                            flipDown(preImageButton);
                                            preCardId = -1;
                                        }

                                        break;
                                    }
                                }
                            }
                        }, 1000);

                    }
                });
                linearLayout.addView(imageButton);//Gắn lá bài vào bộ bài
            }
        }

    }

    private void disappear(ImageButton imageButton) {
        final ObjectAnimator oa = ObjectAnimator.ofFloat(imageButton, "scaleX", 1f, 0f);
        oa.setInterpolator(new DecelerateInterpolator());
        oa.start();

    }

    private void flipUp(ImageButton imageButton) {//Hàm lập lá bài lên.

        final ObjectAnimator oa1 = ObjectAnimator.ofFloat(imageButton, "scaleX", 1f, 0f);
        final ObjectAnimator oa2 = ObjectAnimator.ofFloat(imageButton, "scaleX", 0f, 1f);

        oa1.setInterpolator(new DecelerateInterpolator());
        oa2.setInterpolator(new AccelerateDecelerateInterpolator());

        oa1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                imageButton.setBackgroundResource(getDrawableInt(imageButton));
                oa2.start();
            }
        });
        oa1.start();

        imageButton.setEnabled(false);//Tắt khả năng bấm vào thẻ đã lật.
    }

    private void flipDown(ImageButton imageButton) {//Hàm úp lá bài xuống.

        final ObjectAnimator oa1 = ObjectAnimator.ofFloat(imageButton, "scaleX", 1f, 0f);
        final ObjectAnimator oa2 = ObjectAnimator.ofFloat(imageButton, "scaleX", 0f, 1f);

        oa2.setInterpolator(new AccelerateDecelerateInterpolator());
        oa1.setInterpolator(new DecelerateInterpolator());

        oa1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                imageButton.setBackgroundResource(backside);
                oa2.start();
            }
        });
        oa1.start();

        imageButton.setEnabled(true);//Bật khả năng bấm.
    }

    private void setCountTag() {
        // Tạo 1 chuỗi số nguyên với từng phần tử giữ số đếm của 1 loại tag.
        // Tác dụng là để đếm xem 1 tag đã đủ 2 lá bài chưa, nếu đủ thì tag đó không được tạo ra thêm nữa.

        for (int i = 0; i < countTag.length; i++) {
            countTag[i] = 0;
        }
    }

    private int getDrawableInt(ImageButton imageButton) {
        int result = 0;
        switch (Integer.parseInt(String.valueOf(imageButton.getTag()))) {
            case 0:
                result = charizard;
                break;
            case 1:
                result = blastoise;
                break;
            case 2:
                result = tyranitar;
                break;
            case 3:
                result = venusaur;
                break;
            case 4:
                result = gengar;
                break;
            case 5:
                result = chim_tuyet;
                break;
            case 6:
                result = lucario;
                break;
            case 7:
                result = chim_set;
                break;
            case 8:
                result = chim_lua;
                break;
            case 9:
                result = alakazam;
                break;
            case 10:
                result = gyarados;
                break;
            case 11:
                result = mewtwo;
                break;
            case 12:
                result = ampharos;
                break;
            case 13:
                result = steelix;
                break;
            case 14:
                result = sceptile;
                break;
            case 15:
                result = blaziken;
                break;
            case 16:
                result = swampert;
                break;
            case 17:
                result = absol;
                break;
            case 18:
                result = salamence;
                break;
            case 19:
                result = rayquaza;
                break;

        }
        return result;

    }

    private void createClock(int time){
        soundPlayer = new SoundPlayer(this);
        scoreLabel = findViewById(R.id.scoreLabel);

        TextView mTextField = (TextView) findViewById(R.id.time_bar_text);

        countDownTimer = new CountDownTimer(time, 1000) {

            public void onTick(long millisUntilFinished) {
                multiScore = (int) millisUntilFinished;
                mTextField.setText("" + multiScore/ 1000);
            }

            public void onFinish() {
                mTextField.setText("done!");
                soundPlayer.playOverSound();
                finish();
                loseGame();
            }

        }.start();
    }

    //để lấy khung máy
    private void getHeightAndWidth(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
    }

    @Override
    public void onBackPressed() { //Hàm xử lý những đứa trẻ đang chơi tự nhiên bấm thoát :v
        super.onBackPressed();
        Intent returnIntent = new Intent();
        setResult(this.RESULT_CANCELED, returnIntent);
        countDownTimer.cancel();//disable đồng hồ đếm ngược
        finish();
    }
    private void winGame(){
        Intent i = new Intent(this,ResultActivity.class);
        i.putExtra("Result",1);
        i.putExtra("Score",score);
        startActivityForResult(i,result);
        countDownTimer.cancel();
    }
    private void loseGame(){
        Intent i = new Intent(this,ResultActivity.class);
        i.putExtra("Result",0);
        i.putExtra("Score",score);
        startActivityForResult(i,result);
    }
}
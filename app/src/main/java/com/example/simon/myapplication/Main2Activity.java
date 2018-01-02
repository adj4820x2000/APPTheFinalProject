package com.example.simon.myapplication;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigation_view;
    private Toolbar toolbar;


    private Button enter,reset,turn,answer,button;
    private EditText editText;
    private TextView history,coin;
    int[] ch_tmp =new int[4];
    int[] ch_answer=new int[4];
    int i,coin1=0,card_back=0,pic_back=0,doll_back=0,count=0;
    String str="",str_tmp="",str_answer="";
    SQLiteDatabase dbrw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigation_view = (NavigationView) findViewById(R.id.navigation_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        // 用toolbar做為APP的ActionBar
        setSupportActionBar(toolbar);

        // 將drawerLayout和toolbar整合，會出現「三」按鈕
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // 為navigatin_view設置點擊事件
        navigation_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            Intent intent2 = new Intent();
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // 點選時收起選單
                drawerLayout.closeDrawer(GravityCompat.START);
                // 取得選項id
                int id = item.getItemId();
                // 依照id判斷點了哪個項目並做相應事件
                if (id == R.id.action_home) {
                    // 按下「首頁」要做的事
                    Toast.makeText(Main2Activity.this, "點餐囉", Toast.LENGTH_SHORT).show();
                    intent2.setClass(Main2Activity.this, MainActivity.class);
                    startActivity(intent2);
                    return true;
                } else if (id == R.id.action_help) {
                    // 按下「使用說明」要做的事
                    Toast.makeText(Main2Activity.this, "想要拿折扣嗎", Toast.LENGTH_SHORT).show();
                    return true;
                }
                else if (id == R.id.action_settings) {
                    // 按下「使用說明」要做的事
                    Toast.makeText(Main2Activity.this, "可以贊助我們喔", Toast.LENGTH_SHORT).show();
                    intent2.setClass(Main2Activity.this, Main3Activity.class);
                    startActivity(intent2);
                    return true;
                }
                return false;
            }
        });

        DisCountDB dbhelper = new DisCountDB(this);
        dbrw = dbhelper.getWritableDatabase();
        String[] colum = {"username","coin","ninety","eighty","fifty"};
        Cursor c;
        c = dbrw.query("DiscountTable", colum, null, null, null, null, null);

        if(c.getCount() == 0){
            ContentValues cv = new ContentValues();
            cv.put("username", "HI");
            cv.put("coin", "0");
            cv.put("ninety", "0");
            cv.put("eighty", "0");
            cv.put("fifty", "0");
            dbrw.insert("DiscountTable", null, cv);
        }
        c = dbrw.query("DiscountTable", colum, null, null, null, null, null);
        c.moveToFirst();
        coin1 = Integer.parseInt(c.getString(1));


        setTitle("玩個小遊戲吧");

        enter = (Button)findViewById(R.id.enter);
        reset = (Button)findViewById(R.id.reset);
        turn  = (Button)findViewById(R.id.turn);
        answer  = (Button)findViewById(R.id.answer);
        editText =(EditText)findViewById(R.id.editText);
        history =(TextView)findViewById(R.id.history);
        coin = (TextView)findViewById(R.id.coin);
        coin.setText("金幣:" + coin1);
        for(int k=0;k<4;k++) {
            ch_answer[k] = (int) (Math.random() * 10);
        }
        for(int a=0;a<4;a++){
            for(int s=0;s<4;s++){
                if(a!=s){
                    if(ch_answer[a]==ch_answer[s]){
                        ch_answer[a]=(int)(Math.random()*10);
                        s=-1;
                    }
                }
            }
        }

        enter.setOnClickListener(enterltn);
        reset.setOnClickListener(resetltn);
        turn.setOnClickListener(turnltn);
        answer.setOnClickListener(answerltn);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
                builder.setMessage("位置對、數字對得A\n(例如:答案為:1234、輸入為1567，1這個數字答案有且位置對所以得A)\n\n位置錯、數字對得B" +
                        "\n(例如:答案為1234、輸入為5178，1這個數字答案裡有，但位置不對，所以得B)");
                builder.setCancelable(true);
                builder.setPositiveButton("OK",null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        {
            if(requestCode==888&&resultCode==RESULT_OK){
                Bundle bundle3 = data.getExtras();
                coin1 = bundle3.getInt("BACK");
                card_back=bundle3.getInt("BACK_CARD");
                pic_back=bundle3.getInt("BACK_PIC");
                doll_back=bundle3.getInt("BACK_DOLL");

                coin.setText("金幣:"+coin1);

            }
        }
    }

    Button.OnClickListener answerltn = new Button.OnClickListener(){

        @Override
        public void onClick(View view) {
            str_answer ="";
            for(int i =0;i<4;i++) {
                str_answer = str_answer + ch_answer[i];
            }
            Toast.makeText(view.getContext(),"答案是:"+str_answer,Toast.LENGTH_SHORT).show();
        }
    };

    Button.OnClickListener turnltn = new Button.OnClickListener(){

        @Override
        public void onClick(View view) {
            Intent i =new Intent();
            i.setClass(Main2Activity.this,Main2_2Activity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("COIN",coin1);
            bundle.putInt("CARD",card_back);
            bundle.putInt("PIC",pic_back);
            bundle.putInt("DOLL",doll_back);
            i.putExtras(bundle);
            startActivityForResult(i,888);
        }
    };
    Button.OnClickListener enterltn =new Button.OnClickListener(){
        int  A=0;
        int  B=0;
        int check=0;
        @Override
        public void onClick(View view) {
            i = 0;
                  if (editText.getText().toString().equals("")) {
                    Toast.makeText(view.getContext(), "請輸入東西好嗎?,呆子", Toast.LENGTH_SHORT).show();
                    i = 1;
                } else if (editText.getText().toString().length() != 4) {
                    Toast.makeText(view.getContext(), "請輸入4個數字,呆呆", Toast.LENGTH_SHORT).show();
                    i = 1;
                } else {
                    try {
                        i = Integer.parseInt(editText.getText().toString());
                    } catch (NumberFormatException e) {
                        Toast.makeText(view.getContext(), "請輸入數字,呆子", Toast.LENGTH_SHORT).show();
                        i = 1;
                    }
                }
                if (i != 1) {
                    str_tmp = "";
                    str_answer = "";
                    A = 0;
                    B = 0;
                    check = 0;
                    if (editText.getText().toString().length() == 4) {
                        for (int x = 0; x < 4; x++) {
                            ch_tmp[x] = Character.getNumericValue(editText.getText().toString().charAt(x));
                        }
                    }

                    for (int a = 0; a < 4; a++) {
                        for (int s = 0; s < 4; s++) {
                            if (a != s) {
                                if (ch_tmp[a] == ch_tmp[s]) {
                                    check += 1;
                                }
                            }
                        }
                    }
                    if (check == 0) {
                        for (int z = 0; z < 4; z++) {
                            for (int b = 0; b < 4; b++) {
                                if (z == b) {
                                    if (ch_tmp[z] == ch_answer[b]) {
                                        A += 1;
                                    }
                                } else {
                                    if (ch_tmp[z] == ch_answer[b]) {
                                        B += 1;
                                    }
                                }
                            }
                        }

                        for (int i = 0; i < 4; i++) {
                            str_tmp = str_tmp + ch_tmp[i];

                        }
                        str = str + str_tmp + ":" + A + "A," + B + "B  ";
                        history.setText(str);
                        count+=1;
                        if (A == 4) {
                            for (int k = 0; k < 4; k++) {
                                ch_answer[k] = (int) (Math.random() * 10);
                            }
                            for (int a = 0; a < 4; a++) {
                                for (int s = 0; s < 4; s++) {
                                    if (a != s) {
                                        if (ch_answer[a] == ch_answer[s]) {
                                            ch_answer[a] = (int) (Math.random() * 10);
                                            s = -1;
                                        }
                                    }
                                }
                            }
                            str = "";
                            str_answer = "";
                            history.setText("");
                            Toast.makeText(view.getContext(), "恭喜答對!\n總共猜了"+count+"次", Toast.LENGTH_SHORT).show();
                            editText.setText("");
                            coin1 += 1;


                            ContentValues cv = new ContentValues();
                            cv.put("coin", "" + coin1);
                            dbrw.update("DiscountTable", cv, "username='HI'", null);
                            count=0;
                            coin.setText("金幣:" + coin1);

                        }
                    } else {
                        Toast.makeText(view.getContext(), "不要輸入重複得數字", Toast.LENGTH_SHORT).show();
                    }
                }
            editText.setText("");
        }
    };

    Button.OnClickListener resetltn = new Button.OnClickListener(){

        @Override
        public void onClick(View view) {
            str ="";
            str_answer="";
            count=0;
            for(int k=0;k<4;k++) {
                ch_answer[k] = (int) (Math.random() * 10);
            }
            for(int a=0;a<4;a++){
                for(int s=0;s<4;s++){
                    if(a!=s){
                        if(ch_answer[a]==ch_answer[s]){
                            ch_answer[a]=(int)(Math.random()*10);
                            s=-1;
                        }
                    }
                }
            }
            editText.setText("");
            history.setText("");
            Toast.makeText(view.getContext(),"已重新設定",Toast.LENGTH_SHORT).show();
        }
    };
}

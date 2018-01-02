package com.example.simon.myapplication;

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
import android.widget.TextView;
import android.widget.Toast;
public class Main2_2Activity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigation_view;
    private Toolbar toolbar;


    private Button card_btn,pic_btn,doll_btn,turn_back;
    private TextView card,pic,doll,coin_2;
    int coin3=0,card_c=0,pic_c=0,doll_c=0;
    SQLiteDatabase dbrw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_2);
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
                    Toast.makeText(Main2_2Activity.this, "點餐囉", Toast.LENGTH_SHORT).show();
                    intent2.setClass(Main2_2Activity.this, MainActivity.class);
                    startActivity(intent2);
                    return true;
                } else if (id == R.id.action_help) {
                    // 按下「使用說明」要做的事
                    Toast.makeText(Main2_2Activity.this, "想要拿折扣嗎", Toast.LENGTH_SHORT).show();
                    return true;
                }
                else if (id == R.id.action_settings) {
                    // 按下「使用說明」要做的事
                    Toast.makeText(Main2_2Activity.this, "可以贊助我們喔", Toast.LENGTH_SHORT).show();
                    intent2.setClass(Main2_2Activity.this, Main3Activity.class);
                    startActivity(intent2);
                    return true;
                }
                return false;
            }
        });

        setTitle("玩個小遊戲吧");

        card_btn=(Button)findViewById(R.id.card_btn);
        pic_btn=(Button)findViewById(R.id.pic_btn);
        doll_btn=(Button)findViewById(R.id.doll_btn);
        turn_back=(Button)findViewById(R.id.turn_back);
        card=(TextView)findViewById(R.id.card);
        pic=(TextView)findViewById(R.id.pic);
        doll=(TextView)findViewById(R.id.doll);
        coin_2=(TextView)findViewById(R.id.coin_2);

        card_btn.setOnClickListener(ltn);
        pic_btn.setOnClickListener(ltn);
        doll_btn.setOnClickListener(ltn);
        turn_back.setOnClickListener(backltn);

        Intent intent =this.getIntent();
        Bundle bundle= intent.getExtras();
        coin3 = bundle.getInt("COIN");
        card_c=bundle.getInt("CARD");
        pic_c=bundle.getInt("PIC");
        doll_c=bundle.getInt("DOLL");

        DisCountDB dbhelper = new DisCountDB(this);
        dbrw = dbhelper.getWritableDatabase();

        coin_2.setText("金幣:"+coin3);


        if(card_c==1){
            card.setText("九折優惠中");
            card_btn.setVisibility(View.GONE);
        }
        if(pic_c==1){
            pic.setText("八折優惠拉");
            pic_btn.setVisibility(View.GONE);
        }
        if(doll_c==1){
            doll.setText("半價送你啦");
            doll_btn.setVisibility(View.GONE);
        }
    }

    Button.OnClickListener ltn =new Button.OnClickListener(){

        @Override
        public void onClick(View view) {
            ContentValues cv = new ContentValues();
            switch (view.getId()){
                case R.id.card_btn:
                    if(coin3>=1)
                    {  coin3=coin3-1;
                        Toast.makeText(view.getContext(),"可以打九折囉",Toast.LENGTH_SHORT).show();
                        coin_2.setText("金幣:"+coin3);
                        card_c+=1;
                    }
                    else {
                        Toast.makeText(view.getContext(),"金幣不夠",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.pic_btn:
                    if(coin3>=2)
                    { coin3=coin3-2;

                        Toast.makeText(view.getContext(),"可以打八折囉",Toast.LENGTH_SHORT).show();
                        coin_2.setText("金幣:"+coin3);
                        pic_c+=1;}
                    else {
                        Toast.makeText(view.getContext(),"金幣不夠",Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.doll_btn:
                    if(coin3>=3)
                    {coin3 = coin3-3;
                        Toast.makeText(view.getContext(),"半價優惠",Toast.LENGTH_SHORT).show();
                        coin_2.setText("金幣:"+coin3);
                        doll_c+=1;}
                    else {
                        Toast.makeText(view.getContext(),"金幣不夠",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            cv.put("coin", "" + coin3);
            cv.put("ninety", "" + card_c);
            cv.put("eighty", "" + pic_c);
            cv.put("fifty", "" + doll_c);
            dbrw.update("DiscountTable", cv, "username='HI'", null);
        }
    };

    Button.OnClickListener backltn = new Button.OnClickListener(){

        @Override
        public void onClick(View view) {
            Intent intent2= new Intent();
            Bundle bundle2 = new Bundle();
            bundle2.putInt("BACK",coin3);
            bundle2.putInt("BACK_CARD",card_c);
            bundle2.putInt("BACK_PIC",pic_c);
            bundle2.putInt("BACK_DOLL",doll_c);
            intent2.putExtras(bundle2);
            setResult(RESULT_OK,intent2);
            finish();
        }
    };
}

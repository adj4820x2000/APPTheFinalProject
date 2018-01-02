package com.example.simon.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

public class Main3Activity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigation_view;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
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
            Intent intent3 = new Intent();
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // 點選時收起選單
                drawerLayout.closeDrawer(GravityCompat.START);
                // 取得選項id
                int id = item.getItemId();
                // 依照id判斷點了哪個項目並做相應事件
                if (id == R.id.action_home) {
                    // 按下「首頁」要做的事
                    Toast.makeText(Main3Activity.this, "點餐囉", Toast.LENGTH_SHORT).show();
                    intent3.setClass(Main3Activity.this, MainActivity.class);
                    startActivity(intent3);
                    return true;
                } else if (id == R.id.action_help) {
                    // 按下「使用說明」要做的事
                    Toast.makeText(Main3Activity.this, "想要拿折扣嗎", Toast.LENGTH_SHORT).show();
                    intent3.setClass(Main3Activity.this, Main2Activity.class);
                    startActivity(intent3);
                    return true;
                }
                else if (id == R.id.action_settings) {
                    // 按下「使用說明」要做的事
                    Toast.makeText(Main3Activity.this, "可以贊助我們喔", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

        setTitle("感謝你的斗內");
    }
}

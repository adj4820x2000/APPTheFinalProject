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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigation_view;
    private Toolbar toolbar;

    Spinner spinner;
    Button add,enter;
    EditText foodname,foodamount;
    ListView listView;
    ArrayAdapter<String> listviewAdapter,adapter2;
    TextView textView10;

    SQLiteDatabase dbrw,dbrw2;
    ListView lv,lv2;
    AlertDialog dialog,dialog2;
    String foodname_list,foodamount_list;
    String[] FOODNAME = {"米血","百頁","蒸煮麵","鴨珍","大腸頭","甜不辣","海帶","豆皮","金針菇","豬皮","貢丸","鳥蛋"};
    String[] items2 ={"九折折價券","八折折價券","五折折價券"};
    Integer[] FOODMONEY = {10,15,20,10,5,20,15,30,9,32,11,20};
    Integer[] MONEY = {0,0,0,0,0,0,0,0,0,0,0,0};

    Integer HAVE_USE = 0;

    int foodcount = 0;
    class Data{
        int photo;
        String name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            Intent intent = new Intent();
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // 點選時收起選單
                drawerLayout.closeDrawer(GravityCompat.START);
                // 取得選項id
                int id = item.getItemId();
                // 依照id判斷點了哪個項目並做相應事件
                if (id == R.id.action_home) {
                    // 按下「首頁」要做的事
                    Toast.makeText(MainActivity.this, "點餐囉", Toast.LENGTH_SHORT).show();
                    return true;
                } else if (id == R.id.action_help) {
                    // 按下「使用說明」要做的事
                    Toast.makeText(MainActivity.this, "想要拿折扣嗎", Toast.LENGTH_SHORT).show();
                    intent.setClass(MainActivity.this, Main2Activity.class);
                    startActivity(intent);
                    return true;
                }
                else if (id == R.id.action_settings) {
                    // 按下「使用說明」要做的事
                    Toast.makeText(MainActivity.this, "可以贊助我們喔", Toast.LENGTH_SHORT).show();
                    intent.setClass(MainActivity.this, Main3Activity.class);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        setTitle("點餐");

        DisCountDB dbhelper2 = new DisCountDB(this);
        dbrw2 = dbhelper2.getWritableDatabase();
        String[] colum2 = {"username","coin","ninety","eighty","fifty"};
        Cursor c;
        c = dbrw2.query("DiscountTable", colum2, null, null, null, null, null);

        if(c.getCount() == 0){
            ContentValues cv = new ContentValues();
            cv.put("username", "HI");
            cv.put("coin", "0");
            cv.put("ninety", "0");
            cv.put("eighty", "0");
            cv.put("fifty", "0");
            dbrw2.insert("DiscountTable", null, cv);
        }
        c = dbrw2.query("DiscountTable", colum2, null, null, null, null, null);
        c.moveToFirst();

        foodname = (EditText) findViewById(R.id.foodname);
        foodamount = (EditText) findViewById(R.id.foodamount);
        add = (Button) findViewById(R.id.add);
        enter = (Button) findViewById(R.id.enter);
        listView = (ListView) findViewById(R.id.listview);
        lv = new ListView(this);
        lv2 = new ListView(this);
        spinner = (Spinner) findViewById(R.id.spinner);
        textView10 = (TextView) findViewById(R.id.textView10);



        Data[] food = new Data[12];
        int[] FOODPHOTO = {R.drawable.food1,R.drawable.food2,R.drawable.food3,R.drawable.food4,R.drawable.food5,R.drawable.food6,R.drawable.food7,
                R.drawable.food8,R.drawable.food9,R.drawable.food10,R.drawable.food11,R.drawable.food12};
        for(int i=0;i<food.length;i++){
            food[i] = new Data();
            food[i].name = FOODNAME[i];
            food[i].photo = FOODPHOTO[i];
        }
        MyAdapter spinnerAdapter = new MyAdapter(food,R.layout.spinnerlist);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(spinnerltn);

        listviewAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(listviewAdapter);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addfood();
            }
        });

        String[] items ={"修改","不要了"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.list,R.id.item,items);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(lvltn);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(true);
        builder.setPositiveButton("返回",null);
        builder.setView(lv);
        dialog = builder.create();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String temp = adapterView.getItemAtPosition(i).toString();
                int x = temp.indexOf("　");
                int z = temp.indexOf("份");
                foodname_list = temp.substring(3,x);
                foodamount_list = temp.substring(x+1,z);
                dialog.show();
            }
        });

        foodDB dbhelper = new foodDB(this);
        dbrw = dbhelper.getWritableDatabase();

        food_show();

        items2[0] = "九折折價券 "+ c.getString(2)+"張";
        items2[1] = "八折折價券 "+ c.getString(3)+"張";
        items2[2] = "五折折價券 "+ c.getString(4)+"張";
        adapter2 = new ArrayAdapter<String>(MainActivity.this,R.layout.list,R.id.item,items2);
        lv2.setAdapter(adapter2);
        lv2.setOnItemClickListener(lv2ltn);
        AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
        builder2.setCancelable(true);
        builder2.setPositiveButton("不用折價券",null);
        builder2.setView(lv2);
        dialog2 = builder2.create();
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sum=0;
                String[] colum2 = {"username","coin","ninety","eighty","fifty"};
                Cursor c;
                c = dbrw2.query("DiscountTable", colum2, null, null, null, null, null);
                c.moveToFirst();
                if(HAVE_USE == 0) {
                    for (int h = 0; h < MONEY.length; h++) {
                        sum += MONEY[h];
                        textView10.setText("總共:" + sum + "元");
                    }
                }
                items2[0] = "九折折價券 "+ c.getString(2)+"張";
                items2[1] = "八折折價券 "+ c.getString(3)+"張";
                items2[2] = "五折折價券 "+ c.getString(4)+"張";
                //adapter2.clear();
                //adapter2.addAll(items2);
                adapter2.notifyDataSetChanged();
                dialog2.show();
            }
        });
    }

    private Spinner.OnItemSelectedListener spinnerltn = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            foodname.setText(FOODNAME[i]);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private ListView.OnItemClickListener lvltn = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (parent.getItemAtPosition(position).toString()){
                case "修改":
                    foodname.setText(foodname_list);
                    foodamount.setText(foodamount_list);
                    dialog.dismiss();
                    break;
                case "不要了":
                    Toast.makeText(MainActivity.this, foodname_list, Toast.LENGTH_SHORT).show();
                    MONEY[position] = 0;
                    dbrw.delete("foodTable", "food_name='" + foodname_list +  "'", null);
                    food_show();
                    dialog.dismiss();
                    break;
            }
        }
    };

    private ListView.OnItemClickListener lv2ltn = new ListView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String[] colum = {"username","coin","ninety","eighty","fifty"};
            Cursor c;
            c = dbrw2.query("DiscountTable", colum, null, null, null, null, null);
            c.moveToFirst();
            ContentValues cv = new ContentValues();

            double sum=0;
            for(int h=0;h<MONEY.length;h++){
                sum += MONEY[h];
            }
            switch (i){
                case 0:
                    if(Integer.parseInt(c.getString(2)) > 0 && HAVE_USE == 0){
                        cv.put("ninety", ""+ (Integer.parseInt(c.getString(2))-1) );
                        dbrw2.update("DiscountTable", cv, "username='HI'", null);
                        sum = sum*0.9;
                        dialog2.dismiss();
                        HAVE_USE = 1;
                    }
                    else if(HAVE_USE == 1){
                        Toast.makeText(MainActivity.this, "已經用過折價券囉", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MainActivity.this, "沒折價券囉", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 1:
                    if(Integer.parseInt(c.getString(3)) > 0 && HAVE_USE == 0) {
                        cv.put("eighty", "" + (Integer.parseInt(c.getString(3)) - 1));
                        dbrw2.update("DiscountTable", cv, "username='HI'", null);
                        sum = sum * 0.8;
                        dialog2.dismiss();
                        HAVE_USE = 1;
                    }
                    else if(HAVE_USE == 1){
                        Toast.makeText(MainActivity.this, "已經用過折價券囉", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MainActivity.this, "沒折價券囉", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    if(Integer.parseInt(c.getString(4)) > 0 && HAVE_USE == 0) {
                        cv.put("fifty", "" + (Integer.parseInt(c.getString(4)) - 1));
                        dbrw2.update("DiscountTable", cv, "username='HI'", null);
                        sum = sum * 0.5;
                        dialog2.dismiss();
                        HAVE_USE = 1;
                    }
                    else if(HAVE_USE == 1){
                        Toast.makeText(MainActivity.this, "已經用過折價券囉", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MainActivity.this, "沒折價券囉", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
            int temp = (int) sum;
            textView10.setText("總共:"+temp+"元");
        }
    };

    public void addfood(){
        String[] colum = {"food_name","food_amount"};
        Cursor c;
        ContentValues cv = new ContentValues();
        if(foodname.getText().toString().equals("") || foodamount.getText().toString().equals("")){
            Toast.makeText(this, "輸入資料不完全", Toast.LENGTH_SHORT).show();
        }
        else{
            c = dbrw.query("foodTable",colum,"food_name='" + foodname.getText().toString() +  "'",null,null,null,null);
            if(c.getCount() > 0){
                cv.put("food_amount", foodamount.getText().toString());
                dbrw.update("foodTable", cv, "food_name='" + foodname.getText().toString() +  "'", null);
                Toast.makeText(this,"已修改",Toast.LENGTH_SHORT).show();
            }
            else{
                cv.put("food_name", foodname.getText().toString());
                cv.put("food_amount", foodamount.getText().toString());
                dbrw.insert("foodTable", null, cv);

                Toast.makeText(this,"已加點",Toast.LENGTH_SHORT).show();
            }
            food_show();
            foodamount.setText("");
            /*String temp = (foodcount+1)+ ". " + foodname.getText().toString() + "　" + foodamount.getText().toString() + "份";
            listviewAdapter.add(temp);
            listviewAdapter.notifyDataSetChanged();
            foodcount ++;*/
        }
    }

    public void food_show(){
        Cursor c;
        String[] colum = {"food_name","food_amount"};
        String temp="";
        c = dbrw.query("foodTable",colum,null,null,null,null,null);
        if(c.getCount() > 0){
            c.moveToFirst();
            listviewAdapter.clear();
            for(int i=0;i<c.getCount();i++){
                MONEY[i] = Integer.parseInt(c.getString(1))*FOODMONEY[i];
                temp = (i+1)+ ". " + c.getString(0) + "　" + c.getString(1) + "份" + "　" + Integer.parseInt(c.getString(1))*FOODMONEY[i];
                c.moveToNext();
                listviewAdapter.add(temp);
                listviewAdapter.notifyDataSetChanged();
            }
        }
        else if(c.getCount()==0) {
            listviewAdapter.clear();
            listviewAdapter.notifyDataSetChanged();
        }
    }

    public class MyAdapter extends BaseAdapter {
        private Data[] data;
        private int view;

        public MyAdapter(Data[] data, int view){
            this.data = data;
            this.view = view;
        }

        @Override
        public int getCount() {
            return data.length;
        }

        @Override
        public Object getItem(int i) {
            return data[i];
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View rowView, ViewGroup parent) {
            rowView = getLayoutInflater().inflate(view, parent, false);
            TextView name = (TextView) rowView.findViewById(R.id.name);
            ImageView imageView =  (ImageView) rowView.findViewById(R.id.imageView);
            name.setText(data[i].name);
            imageView.setImageResource(data[i].photo);
            return rowView;
        }
    }
}

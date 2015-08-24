package com.voyager.wifi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.voyager.wifi.com.voyager.wifi.utils.Wifi;
import com.voyager.wifi.com.voyager.wifi.utils.WifiUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView lv_main;
    private List<Wifi> wifiList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv_main = (ListView) findViewById(R.id.lv_main);

        wifiList = new WifiUtils().getAllWifi();
        if (wifiList.isEmpty()) {
            Toast.makeText(MainActivity.this, "读取失败，请授权root权限", Toast.LENGTH_SHORT).show();
        }
        System.out.println(wifiList);

        lv_main.setAdapter(new MyAdapter());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return wifiList.size();
        }

        @Override
        public Object getItem(int position) {
            return wifiList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(MainActivity.this);
            textView.setText("网络名称：" + wifiList.get(position).getSsid() + "\n密码：" + wifiList.get(position).getPassword());
            return textView;
        }
    }

}

package com.voyager.wifi;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.voyager.wifi.com.voyager.wifi.utils.SystemBarTintManager;
import com.voyager.wifi.com.voyager.wifi.utils.Wifi;
import com.voyager.wifi.com.voyager.wifi.utils.WifiUtils;

import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {

    private ListView lv_main;
    private List<Wifi> wifiList;
    private MyAdapter adapter;
    private ImageView iv_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();


        refreshView();


    }

    private void refreshView() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.show();
        pd.setMessage("请稍候...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                wifiList = new WifiUtils().getAllWifi();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (wifiList.isEmpty()) {
                            Toast.makeText(MainActivity.this, "读取失败，请授权root权限", Toast.LENGTH_SHORT).show();
                        }
                        System.out.println(wifiList);
                        if (adapter == null) {
                            adapter = new MyAdapter();
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                        lv_main.setAdapter(adapter);
                        lv_main.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                copyPwd(position);
                                Toast.makeText(MainActivity.this, "密码已复制到剪贴板", Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        });
                        pd.dismiss();
                    }
                });
            }
        }).start();
    }

    private void copyPwd(int position) {
        ClipboardManager cm = (ClipboardManager) getSystemService(Service.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText(null, wifiList.get(position).getPassword()));
    }

    private void initView() {
        lv_main = (ListView) findViewById(R.id.lv_main);
        iv_refresh = (ImageView) findViewById(R.id.iv_refresh);

        iv_refresh.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.main_statusbar_bg);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_refresh:
                refreshView();
                break;
        }
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
//            TextView textView = new TextView(MainActivity.this);
//            textView.setText("网络名称：" + wifiList.get(position).getSsid() + "\n密码：" + wifiList.get(position).getPassword());
//            return textView;
            View view = View.inflate(MainActivity.this, R.layout.lv_item, null);
            TextView tv_ssid = (TextView) view.findViewById(R.id.tv_ssid);
            TextView tv_pwd = (TextView) view.findViewById(R.id.tv_pwd);
            tv_ssid.setText(wifiList.get(position).getSsid());
            tv_pwd.setText(wifiList.get(position).getPassword());
            return view;
        }
    }

    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


}

package com.voyager.wifi.com.voyager.wifi.utils;

import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuhaojie on 2015/8/24.
 */
public class WifiUtils {

    /**
     * 读取WiFi配置文件
     */
    private static final String CAT_WIFI_CONF = "cat /data/misc/wifi/wpa_supplicant.conf\n";
    /**
     * 退出
     */
    private static final String EXIT = "exit\n";
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;
    private Process process;

    public List<Wifi> getAllWifi() {
        List<Wifi> list = new ArrayList<Wifi>();
        String wifiConf = getWifiConf();
        System.out.println(wifiConf);
        return list;
    }

    @NonNull
    private String getWifiConf() {
        StringBuilder conf = new StringBuilder();
        try {
            process = Runtime.getRuntime().exec("su");
            dataOutputStream = new DataOutputStream(process.getOutputStream());
            dataInputStream = new DataInputStream(process.getInputStream());
            dataOutputStream.writeBytes(CAT_WIFI_CONF);
            dataOutputStream.writeBytes(EXIT);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(dataInputStream, "utf-8"));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                conf.append(line);
            }
            bufferedReader.close();
            process.waitFor();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (dataOutputStream != null)
                    dataOutputStream.close();
                if (dataInputStream != null)
                    dataInputStream.close();
                process.destroy();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return conf.toString();
    }
}

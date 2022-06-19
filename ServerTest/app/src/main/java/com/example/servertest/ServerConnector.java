package com.example.servertest;



import android.icu.util.Output;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

public class ServerConnector {
    String ip = "192.168.1.104:10001";

    public String getPostResponseHttp(JSONObject jsonObject) throws IOException {

        StringBuilder sb = new StringBuilder();

        URL url = new URL("http://" + ip);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        try {
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setReadTimeout(10000);
            con.setConnectTimeout(15000);

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Content-Length", "" + jsonObject.toString().length());

            OutputStream out = con.getOutputStream();
            BufferedWriter bout = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            bout.write(jsonObject.toString());
            bout.flush();
            bout.close();
            out.close();

            con.connect();

            InputStream in = new BufferedInputStream(con.getInputStream());
            BufferedReader bin = new BufferedReader(new InputStreamReader(in));

            String inputLine;

            while ((inputLine = bin.readLine()) != null ) {
                sb.append(inputLine);
            }

        } finally {
            //always disconnect
            con.disconnect();
        }

        return sb.toString();
    }

    public String getPostResponseHttps(String text) {

        String tempString = "";
        try {
            URL url = new URL("https://" + ip);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-length", String.valueOf(text.length()));
            con.setRequestProperty("Content-Type", "text/plain");
            con.setRequestProperty("USER-AGENT", "Mozilla/5.0");
            con.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
            con.setDoOutput(true);
            con.setDoInput(true);
            DataOutputStream output = new DataOutputStream(con.getOutputStream());
            output.writeBytes(text);
            output.close();
            DataInputStream input = new DataInputStream(con.getInputStream());
            tempString = tempString + input.toString();
            input.close();


            return tempString;
        } catch(Exception e) {
            return e.toString();
        }
    }

}

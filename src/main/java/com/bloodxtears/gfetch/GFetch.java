package com.bloodxtears.gfetch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class GFetch {
    private static final String RESET_COLOR = "\u001B[0m";
    private static final String TAG_COLOR = "\033[0;95m";
    private static final String FRAME_COLOR = "\033[0;96m";

    public static void main(String[] args) {
        new GFetch().run(args);
    }

    private void run(String[] args) {
        URL url = validateUserInput(args);
        if (url == null)
            return;
        String json = fetchUserInfo(url);
        if (json == null)
            return;
        Map<String, Object> userinfo = parseUserInfo(json);
        if (userinfo == null)
            return;
        displayInfo(userinfo);
    }

    private URL validateUserInput(String[] args) {
        if (args.length > 0) {
            try {
                return new URL("https://api.github.com/users/" + args[0]);
            } catch (MalformedURLException e) {
                return null;
            }
        }
        System.err.println("To use the program, you must specify a username!");
        return null;
    }

    private String fetchUserInfo(URL url) {
        HttpURLConnection connection = null;
        StringBuilder userInfo = null;
        try {
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            connection.connect();

            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                userInfo = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    userInfo.append(line).append("\n");
                }

                br.close();
            } else {
                System.err.println("User does not exist!\n" +
                        "Remember: the username can only contain numbers, letters and dashes!");
            }
        } catch (Throwable cause) {
            cause.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        if (userInfo == null)
            return null;
        else
            return userInfo.toString();
    }

    private Map<String, Object> parseUserInfo(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap;
        Map<String, Object> userinfo  = new LinkedHashMap<String, Object>();
        try {
            jsonMap = objectMapper.readValue(json, new TypeReference<Map<String, Object>>(){});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
        userinfo.put("Username",jsonMap.get("login"));
        userinfo.put("Repos",jsonMap.get("public_repos"));
        userinfo.put("Gists",jsonMap.get("public_gists"));
        userinfo.put("Followers",jsonMap.get("followers"));
        userinfo.put("Url",jsonMap.get("url"));
        return userinfo;
    }

    private void displayInfo(Map<String, Object> userinfo){
        for (Map.Entry<String, Object> entry : userinfo.entrySet()) {
            System.out.println(TAG_COLOR + entry.getKey() + RESET_COLOR + ": " + entry.getValue());
        }
    }
}

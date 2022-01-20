package com.bloodxtears.gfetch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GFetch {
    public static void main(String[] args) {
        new GFetch().run(args);
    }

    private void run(String[] args) {
        URL url = userInsertValidation(args);
        if (java.util.Objects.isNull(url))
            return;
        String json = fetchUserInfo(url);
        if (java.util.Objects.isNull(json))
            return;
    }

    private URL userInsertValidation(String[] args) {
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
            if (!java.util.Objects.isNull(connection)) {
                connection.disconnect();
            }
        }
        if (java.util.Objects.isNull(userInfo))
            return null;
        else
            return  userInfo.toString();
    }
}

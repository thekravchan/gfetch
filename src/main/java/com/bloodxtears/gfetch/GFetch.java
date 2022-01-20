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

/**
 * <p><b>gfetch</b></p>
 * <p><i>The program shows briefly information about the user's github account.</i></p>
 * <br/>
 * <p>To get information about the account, the built-in api in github is used.</p>
 * @author ElaSparks
 * @see <a href="https://github.com/elasparks">Author's github</a>
 * @see <a href="https://bloodxtears.com">Author's site</a>
 * @version 0.9v
 */
public class GFetch {
    /**
     * This constant is used for <b>reset</b> the text color in terminal.
     */
    private static final String RESET_COLOR = "\u001B[0m";
    /**
     * This constant is used for colorize <b>keys</b> of user information.
     * <p>So what comes <b>before the colon</b> will be highlighted in a different color.</p>
     */
    private static final String TAG_COLOR = "\033[0;95m";
    /**
     * User information is in an impromptu <b>frame</b>, the frame is <b>painted in this color</b>.
     */
    private static final String FRAME_COLOR = "\033[0;96m"; //color of frame around user info

    /**
     * <p><b>Program entry point.</b></p>
     *
     * @param args <b>parameters</b> that are passed to the program through the command line
     * @since 0.1v
     */
    public static void main(String[] args) {
        new GFetch().run(args);
    }

    /**
     * <p>This function <b><i>coordinates</i></b> the execution of all other modules.</p>
     *
     * @param args <b>parameters</b> received from the command line
     * @since 0.9v
     */
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

    /**
     * <p>The function <b>checks</b> that at least one parameter is entered and <b>creates a link</b> for a subsequent request.</p>
     *
     * @param args <b>parameters</b> received from the command line
     * @return <b>URL</b> to user on <a href="https://api.github.com/users">github api</a>
     * @since 0.5v
     */
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

    /**
     * <p>Makes a <b>get-request</b> to <a href="https://api.github.com/users">api</a> and returns <b>json</b> with information about the user as a string for further processing</p>
     *
     * @param url <b>URL</b> to user on <a href="https://api.github.com/users">github api</a>
     * @return <b>json</b> with information about the user as a string
     * @since 0.5v
     */
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

    /**
     * <p><b>Parses json</b> and rewrites the necessary information into a separate map.</p>
     * @param json <b>json</b> with information about the user as a string
     * @return <b>map of information</b> about user
     * @since 0.7v
     */
    private Map<String, Object> parseUserInfo(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap;
        Map<String, Object> userinfo = new LinkedHashMap<>();
        try {
            jsonMap = objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
        userinfo.put("Username", jsonMap.get("login"));
        userinfo.put("Repos", jsonMap.get("public_repos"));
        userinfo.put("Gists", jsonMap.get("public_gists"));
        userinfo.put("Followers", jsonMap.get("followers"));
        userinfo.put("Url", jsonMap.get("url"));
        return userinfo;
    }

    /**
     * <p><b>Display information</b> of user account on github</p>
     * @param userinfo <b>map of information</b> about user
     * @since 0.8v
     */
    private void displayInfo(Map<String, Object> userinfo) {
        for (Map.Entry<String, Object> entry : userinfo.entrySet()) {
            System.out.println(TAG_COLOR + entry.getKey() + RESET_COLOR + ": " + entry.getValue());
        }
    }
}

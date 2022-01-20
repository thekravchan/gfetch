package com.bloodxtears.gfetch;

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
    }

    private URL userInsertValidation(String[] args){
        if (args.length>0){
            try {
                return new URL("https://api.github.com/users/"+args[0]);
            } catch (MalformedURLException e){
                return null;
            }
        }
        System.err.println("To use the program, you must specify a username!");
        return null;
    }

    private String fetchUserInfo(URL url) {
        return null;
    }
}

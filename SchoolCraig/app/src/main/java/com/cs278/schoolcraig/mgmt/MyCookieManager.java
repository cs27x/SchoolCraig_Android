package com.cs278.schoolcraig.mgmt;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * Created by violettavylegzhanina on 11/17/14.
 */
public class MyCookieManager extends CookieManager {

    private final String SESSION_KEY = "rack.session";

    public MyCookieManager(){
        super.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
    }

    @Override
    public void put(URI uri, Map<String, List<String>> responseHeaders) throws IOException{

        super.put(uri, responseHeaders);
        if(responseHeaders != null && responseHeaders.get("Set-Cookie") != null){

            for(String possibleSessionCookieValues : responseHeaders.get("Set-Cookie")){

                if(possibleSessionCookieValues != null){
                    for(String possibleSessionCookie : possibleSessionCookieValues.split(";")){

                        if(possibleSessionCookie.startsWith(SESSION_KEY) && possibleSessionCookie.contains("=")){

                            String session = possibleSessionCookie.split("=")[1];

                            // store
                            Preferences.getInstance().writePreference(SESSION_KEY, session);

                            return;
                        }
                    }
                }
            }
        }
    }
}

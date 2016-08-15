package com.duffin22.mdmessage;

import android.graphics.Color;

/**
 * Created by matthewtduffin on 14/08/16.
 */
public class User {
    private String userId;
    private String alias;
    private int color;

    public static final String USER_ID = "userId";
    public static final String ALIAS = "alias";
    public static final String COLOR = "color";


    public User(String userId) {
        this.userId = userId;
        this.alias = userId;
        this.color = Color.rgb(240,240,240);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}

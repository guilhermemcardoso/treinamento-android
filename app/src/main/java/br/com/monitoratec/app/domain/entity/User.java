package br.com.monitoratec.app.domain.entity;

import com.google.gson.annotations.SerializedName;

public class User {
    public String login;
    @SerializedName("avatar_url")
    public String avatarUrl;
    public String url;
}

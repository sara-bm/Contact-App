package com.example.tp1;

public class User {
    private int id;
    private String name;
    private String tel;
    private String nickname;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public User(int id, String name, String phone,String nickname) {
        this.id = id;
        this.name = name;
        this.tel = phone;
        this.nickname=nickname;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTel() {
        return tel;
    }
}

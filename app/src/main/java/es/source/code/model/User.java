package es.source.code.model;

import java.io.Serializable;

public class User implements Serializable{
    private String username;
    private String password;
    private Boolean OldUser;



    public Boolean getOldUser() {
        return OldUser;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setOlduser(boolean oldUser)
    {
        if (oldUser) OldUser = true;
        else OldUser = false;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

}


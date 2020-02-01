package pers.fish.sso.common.model;


import java.io.Serializable;

/**
 * 写点什么吧
 *
 * @author fish
 * @date 2020/1/29 22:41
 */
public class User implements Serializable{


    private static final long serialVersionUID = 1733853548106392547L;

    private String id;

    private String nickname;

    private String username;

    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

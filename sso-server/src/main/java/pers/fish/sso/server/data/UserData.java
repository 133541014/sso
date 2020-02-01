package pers.fish.sso.server.data;

import pers.fish.sso.common.model.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 写点什么吧
 *
 * @author fish
 * @date 2020/1/30 16:21
 */
public class UserData {

    public static final Map<String,User> ticket2User = new ConcurrentHashMap<>();

}

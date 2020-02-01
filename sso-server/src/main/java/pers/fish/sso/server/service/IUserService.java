package pers.fish.sso.server.service;

import pers.fish.sso.common.model.User;

/**
 * IUserService
 *
 * @author fish
 * @date 2020/1/30 20:56
 */
public interface IUserService {

    /**
     * 增加用户信息
     *
     * @param ticket 票据
     * @param user 用户信息
     * @return
     */
    boolean addUser(String ticket,User user);

    /**
     * 获取用户信息
     *
     * @param ticket 票据
     * @return
     */
    User getUser(String ticket);

    /**
     * 移除用户信息
     *
     * @param ticket 票据
     * @return
     */
    boolean removeUser(String ticket);
}

package pers.fish.sso.server.service.impl;

import org.springframework.stereotype.Service;
import pers.fish.sso.common.model.User;
import pers.fish.sso.server.data.UserData;
import pers.fish.sso.server.service.IUserService;

/**
 * 写点什么吧
 *
 * @author fish
 * @date 2020/1/30 20:57
 */
@Service
public class UserServiceImpl implements IUserService{


    @Override
    public boolean addUser(String ticket, User user) {
        UserData.ticket2User.put(ticket,user);
        return true;
    }

    @Override
    public User getUser(String ticket) {
        return UserData.ticket2User.get(ticket);
    }

    @Override
    public boolean removeUser(String ticket) {
        if(UserData.ticket2User.containsKey(ticket)){
            UserData.ticket2User.remove(ticket);
        }
        return true;
    }
}

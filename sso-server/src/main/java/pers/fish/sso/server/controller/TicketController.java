package pers.fish.sso.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pers.fish.sso.common.model.User;
import pers.fish.sso.server.data.UserData;
import pers.fish.sso.server.service.IUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * TokenController
 *
 * @author fish
 * @date 2020/1/29 20:08
 */
@RestController
@RequestMapping("/ticket")
public class TicketController {

    @Autowired
    private IUserService userService;

    @GetMapping("/check")
    public User checkTicket(HttpServletRequest request, String ticket) {

        return userService.getUser(ticket);
    }
}

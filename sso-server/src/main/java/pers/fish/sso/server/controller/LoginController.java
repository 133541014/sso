package pers.fish.sso.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pers.fish.sso.common.model.User;
import pers.fish.sso.common.util.TicketUtil;
import pers.fish.sso.server.data.UserData;
import pers.fish.sso.server.service.IUserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录认证controller
 *
 * @author fish
 * @date 2020/1/29 11:50
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private IUserService userService;

    private final String COOKIE_NAME = "server_ticket";

    @GetMapping("/toLogin")
    public String toLogin(HttpServletRequest request, Model model, String service) {

        Cookie[] cookies = request.getCookies();
        String ticket = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (!COOKIE_NAME.equals(cookie.getName())) {
                    continue;
                }
                ticket = cookie.getValue();
            }
        }
        if (!StringUtils.isEmpty(ticket)) {
            //检查ticket是否有效
            User user = userService.getUser(ticket);

            if (user != null && !StringUtils.isEmpty(service)) {
                StringBuilder redirectUrl = new StringBuilder(service);
                redirectUrl.append("?ticket=").append(ticket);
                return "redirect:" + redirectUrl.toString();
            }

        }
        model.addAttribute("service", service);
        return "login";
    }

    @GetMapping("/success")
    public String success() {
        return "success";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response, String service, String username, String password) {

        //校验用户名密码正确性 生成ticket
        User user = new User();
        user.setId("123");
        user.setNickname("张三");
        String ticket = TicketUtil.generateTicket();
        userService.addUser(ticket, user);
        Cookie cookie = new Cookie(COOKIE_NAME, ticket);
        response.addCookie(cookie);
        if (StringUtils.isEmpty(service)) {
            return "redirect:/success";
        }
        StringBuilder sb = new StringBuilder(service);
        sb.append("?ticket=").append(ticket);

        return "redirect:" + sb.toString();
    }

    /**
     * 单点登出
     *
     * @param ticket
     * @return
     */
    @GetMapping("/logout")
    @ResponseBody
    public String logout(String ticket) {
        if (StringUtils.isEmpty(ticket)) {
            return "failed";
        }
        userService.removeUser(ticket);

        return "success";
    }

}

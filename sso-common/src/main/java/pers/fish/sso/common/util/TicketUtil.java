package pers.fish.sso.common.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * 写点什么吧
 *
 * @author fish
 * @date 2020/1/30 16:32
 */
public class TicketUtil {

    public static String generateTicket(){
        return UUID.randomUUID().toString().replace("-","");
    }

    public static String getCookieTicket(HttpServletRequest request,String name){
        String ticket = null;

        Cookie[] cookies = request.getCookies();

        if(cookies == null){
            return null;
        }

        for (Cookie cookie : cookies) {
            if(!name.equals(cookie.getName())){
                continue;
            }

            return cookie.getValue();
        }

        return null;
    }
}

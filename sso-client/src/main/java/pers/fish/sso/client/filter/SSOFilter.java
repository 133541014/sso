package pers.fish.sso.client.filter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;
import pers.fish.sso.common.model.User;
import pers.fish.sso.common.util.HTTPUtil;
import pers.fish.sso.common.util.TicketUtil;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 单点登录过滤器
 *
 * @author fish
 * @date 2020/1/19 22:43
 */
public class SSOFilter implements Filter {

    /**
     * 单点登录url
     */
    public static String loginUrl;

    public static String serverUrl;

    /**
     * sso登录地址
     */
    private final String LOGIN_URL = "loginUrl";

    /**
     * sso服务端地址
     */
    private final String SERVER_URL = "serverUrl";

    public static final String COOKIE_NAME = "client_ticket";

    public static final String SESSION_NAME = "sso_user";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        String loginUrl = filterConfig.getInitParameter(LOGIN_URL);

        if (StringUtils.isEmpty(loginUrl)) {

            throw new ServletException("init parameter {loginUrl} could not be empty");
        }
        if (loginUrl.endsWith("/")) {
            loginUrl = loginUrl.substring(0, loginUrl.length() - 1);
        }
        SSOFilter.loginUrl = loginUrl;

        String serverUrl = filterConfig.getInitParameter(SERVER_URL);

        if (StringUtils.isEmpty(serverUrl)) {

            throw new ServletException("init parameter {serverUrl} could not be empty");
        }

        if (serverUrl.endsWith("/")) {
            serverUrl = serverUrl.substring(0, serverUrl.length() - 1);
        }
        SSOFilter.serverUrl = serverUrl;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        //检查请求参数中是否包含ticket
        String paramTicket = req.getParameter("ticket");

        if (StringUtils.isEmpty(paramTicket)) {
            //如果请求参数中没有 从cookie中获取ticket

            String cookieTicket = TicketUtil.getCookieTicket(req,COOKIE_NAME);

            if (StringUtils.isEmpty(cookieTicket)) {
                redirectLoginPage(req, resp);
                return;
            }
            checkTicketValid(req, resp, paramTicket,chain);


        } else {
            //有ticket信息 校验ticket信息
            checkTicketValid(req, resp, paramTicket,chain);
        }


    }

    /**
     * 验证ticket有效性
     *
     * @param req
     * @param resp
     * @param ticket
     * @throws IOException
     */
    private void checkTicketValid(HttpServletRequest req, HttpServletResponse resp, String ticket,FilterChain chain) throws IOException, ServletException {
        //验证ticket有效性 并写入cookie和session
        StringBuilder checkTicketUrl = new StringBuilder(serverUrl);
        checkTicketUrl.append("/ticket/check?ticket=").append(ticket);
        String checkResult = HTTPUtil.get(checkTicketUrl.toString());

        if (StringUtils.isEmpty(checkResult)) {
            redirectLoginPage(req, resp);
            return;
        }
        //将返回信息反序列化
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(checkResult, User.class);
        Cookie cookie = new Cookie(COOKIE_NAME, ticket);
        resp.addCookie(cookie);
        HttpSession session = req.getSession();
        session.setAttribute(SESSION_NAME, user);
        //过期时间为一分钟
        session.setMaxInactiveInterval(60);
        chain.doFilter(req, resp);
    }

    /**
     * 重定向到登录页
     *
     * @param req
     * @param resp
     * @throws IOException
     */
    private void redirectLoginPage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuffer requestURL = req.getRequestURL();

        String url = requestURL.toString();
        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }
        StringBuilder redirectUrl = new StringBuilder(loginUrl);
        redirectUrl.append("?service=").append(url);
        resp.sendRedirect(redirectUrl.toString());
    }

    @Override
    public void destroy() {

    }
}

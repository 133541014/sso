package pers.fish.sso.client.filter;

import org.springframework.util.StringUtils;
import pers.fish.sso.common.util.HTTPUtil;
import pers.fish.sso.common.util.TicketUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 单点登出过滤器
 *
 * @author fish
 * @date 2020/1/30 20:44
 */
public class LogoutFilter implements Filter{

    private String logoutUrl;
    /**
     * sso服务端地址
     */
    private final String LOGOUT_URL = "logoutUrl";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String logoutUrl = filterConfig.getInitParameter(LOGOUT_URL);

        if (StringUtils.isEmpty(logoutUrl)) {

            throw new ServletException("init parameter {logoutUrl} could not be empty");
        }

        if (logoutUrl.endsWith("/")) {
            logoutUrl = logoutUrl.substring(0, logoutUrl.length() - 1);
        }
        this.logoutUrl = logoutUrl;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String serviceUrl = request.getParameter("service");
        if(StringUtils.isEmpty(serviceUrl)){
            throw new ServletException("logout url should have parameter {service}");
        }
        HttpServletRequest req = (HttpServletRequest) request;
        String cookieTicket = TicketUtil.getCookieTicket(req,SSOFilter.COOKIE_NAME);
        if(cookieTicket == null){
            chain.doFilter(request,response);
            return;
        }
        StringBuilder url = new StringBuilder(logoutUrl);
        url.append("?ticket=").append(cookieTicket);
        HTTPUtil.get(url.toString());
        HttpServletResponse resp = (HttpServletResponse) response;
        StringBuilder redirectUrl = new StringBuilder(SSOFilter.loginUrl);

        redirectUrl.append("?service=").append(serviceUrl);
        resp.sendRedirect(redirectUrl.toString());
    }


    @Override
    public void destroy() {

    }
}

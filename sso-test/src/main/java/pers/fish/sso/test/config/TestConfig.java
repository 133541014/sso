package pers.fish.sso.test.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pers.fish.sso.client.filter.LogoutFilter;
import pers.fish.sso.client.filter.SSOFilter;

import java.util.HashMap;
import java.util.Map;

/**
 * 写点什么吧
 *
 * @author fish
 * @date 2020/1/30 11:28
 */
@Configuration
public class TestConfig {

    @Bean
    public FilterRegistrationBean registerLogoutFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();

        registration.setFilter(new LogoutFilter());
        registration.addUrlPatterns("/logout");
        registration.setName("logoutFilter");
        Map<String,Object> map2 = new HashMap<>();
        map2.put("logoutUrl","http://localhost:9090/sso/login/logout");
        registration.setInitParameters(map2);
        //值越小，Filter越靠前。
        registration.setOrder(1);

        return registration;
    }

    @Bean
    public FilterRegistrationBean registerSSOFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();

        registration.setFilter(new SSOFilter());
        registration.addUrlPatterns("/*");
        registration.setName("ssoFilter");
        Map<String,Object> map1 = new HashMap<>();
        map1.put("loginUrl","http://localhost:9090/sso/login/toLogin");
        map1.put("serverUrl","http://localhost:9090/sso");
        registration.setInitParameters(map1);
        //值越小，Filter越靠前。
        registration.setOrder(2);
        return registration;
    }
}

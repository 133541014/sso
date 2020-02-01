package pers.fish.sso.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 写点什么吧
 *
 * @author fish
 * @date 2020/1/30 11:27
 */
@Controller
public class PageController {

    @GetMapping("/test")
    public String test() {
        return "test";
    }

}

package nuc.ss.swagger.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import nuc.ss.swagger.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    //   /error默认错误请求
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    //只要我们的接口中，返回值中存在实体类，他就会被扫描到Swagger中
    @PostMapping("/user")
    public User user() {
        return new User();
    }

    @ApiOperation("Hello控制接口")
    @GetMapping("/hello2")
    public String hello2(@ApiParam("用户名") String username) {
        return "hello" + username;
    }

    @ApiOperation("get测试")
    @GetMapping("/get")
    public User hello2(@ApiParam("用户") User user) {
        return user;
    }
}

package nuc.ss.swagger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.function.Predicate;

@Configuration
@EnableSwagger2 //开启Swagger2
public class SwaggerConfig {


    @Bean
    public Docket docket1(){
        return new Docket(DocumentationType.SWAGGER_2).groupName("group1");
    }
    @Bean
    public Docket docket2(){
        return new Docket(DocumentationType.SWAGGER_2).groupName("group2");
    }
    @Bean
    public Docket docket3(){
        return new Docket(DocumentationType.SWAGGER_2).groupName("group3");
    }


    //配置docket以配置Swagger具体参数
    //enable配置是否启用Swagger，如果是false，在浏览器将无法访问
    @Bean
    public Docket docket(Environment environment) {

        //设置要显示的Swagger环境
        Profiles profiles = Profiles.of("dev");

        //通过environment.acceptsProfiles判断是否处在自己设定的环境当中
        boolean flag = environment.acceptsProfiles(profiles);


        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(flag)
                .groupName("狂神")
                .select()
                //RequestHandlerSelectors 配置要扫描接口的方式
                    // basePackage: 指定要扫描的包
                    // any():扫描全部
                    // none():都不扫描
                    // withClassAnnotation(Controller.class)：扫描类的注解，参数是一个注解的反射对象
                    //  withMethodAnnotation：扫描方法上的注解
                .apis(RequestHandlerSelectors.basePackage("nuc.ss.swagger.controller"))
                //.paths(PathSelectors.ant("/ss/**"))
                .build();
    }

    //配置文档信息
    private ApiInfo apiInfo() {
        Contact contact = new Contact("联系人名字", "http://xxx.xxx.com/联系人访问链接", "联系人邮箱");
        return new ApiInfo(
                "Swagger学习", // 标题
                "学习演示如何配置Swagger", // 描述
                "v1.0", // 版本
                "http://terms.service.url/组织链接", // 组织链接
                contact, // 联系人信息
                "Apach 2.0 许可", // 许可
                "许可链接", // 许可连接
                new ArrayList<>()// 扩展
        );
    }
}

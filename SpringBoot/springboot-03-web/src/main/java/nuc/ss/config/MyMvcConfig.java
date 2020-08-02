package nuc.ss.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Locale;

//扩展 springmvc      DispatchServlet
// 如果我们要扩展springmvc，官方建议我们这样去做@Configuration
@Configuration
//@EnableWebMvc //这玩意就是导入了一个类，DelegatingWebMvcConfiguration，从容器中获取所有的webMvcConfig
public class MyMvcConfig implements WebMvcConfigurer {
    // public interface ViewResolver 实现了视图解析器接口的类，我们就可以吧它看做视图解析器

    @Bean
    public ViewResolver myViewResolver() {
        return new MyViewResolver();
    }
    // 自定义了一个自己的视图解析器
    public static class MyViewResolver implements ViewResolver {

        @Override
        public View resolveViewName(String s, Locale locale) throws Exception {

            return null;
        }
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // 浏览器发送/test ， 就会跳转到test页面；
        registry.addViewController("/test2").setViewName("test");
    }
}

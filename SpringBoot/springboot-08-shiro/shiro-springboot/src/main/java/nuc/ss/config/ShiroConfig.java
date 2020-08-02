package nuc.ss.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    //shiroFilterFactoryBean

    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("getDefaultWebSecurityManager") DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        // 设置安全管理器
        bean.setSecurityManager(defaultWebSecurityManager);

        // 添加shiro的内置过滤器
        /*
            anon： 无需认证就可以访问
            authc： 必须认证了才能访问
            user： 必须拥有记住我功能才能用
            perms： 拥有对某个资源的权限才能访问
            role： 拥有某个角色权限
         */

        //拦截
        Map<String, String> filterMap = new LinkedHashMap<>();
        //filterMap.put("/user/add","authc");
        //filterMap.put("/user/update","authc");


        //授权，正常情况下，没有授权会跳转到为授权页面
        filterMap.put("/user/add","perms[user:add]");
        filterMap.put("/user/update","perms[user:update]");

        filterMap.put("/user/*","authc");

        bean.setFilterChainDefinitionMap(filterMap);

        //设置登录的请求
        bean.setLoginUrl("/toLogin");

        //为授权页面
        bean.setUnauthorizedUrl("/noauto");

      return bean;
    }

    //DefaultWebSecurityManager

    @Bean
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        // 关联userRealm
        securityManager.setRealm(userRealm);
        return securityManager;
    }
    //创建realm对象，需要自定义类

    @Bean
    public UserRealm userRealm() {
        return new UserRealm();
    }

    // 整合ShiroDialect： 用来整合 Shiro thymeleaf
    @Bean
    public ShiroDialect getShiroDialect() {
        return new ShiroDialect();
    }
}

# 1、Shiro简介

## 1.1、Shiro 是什么？

- Apache Shiro 是 Java 的一个安全（权限）框架。

- Shiro 可以非常容易的开发出足够好的应用，其不仅可以用在 JavaSE 环境，也可以用在 JavaEE 环境。

- Shiro 可以完成：认证、授权、加密、会话管理、与Web 集成、缓存等。
- 下载地址
  - 官网：[http://shiro.apache.org/](http://shiro.apache.org/)
  - github：[https://github.com/apache/shiro](https://github.com/apache/shiro)



## 1.2、有哪些功能？

![image-20200729114647110](https://gitee.com/lzh_gitee/springboot_image/raw/master/img/image-20200729114647110.png)

- Authentication:身份认证/登录，验证用户是不是拥有相应的身份

- Authorization:授权，即权限验证，验证某个已认证的用户是否拥有某个权限；即判断用户是否能进行什么操作，如：验证某个用户是否拥有某个角色。或者细粒度的验证某个用户对某个资源是否具有某个权限

- Session Management:会话管理，即用户登录后就是一次会话，在没有退出之前，它的所有信息都在会话中；会话可以是普通JavaSE环境，也可以是Web 环境的

- Cryptography:加密，保护数据的安全性，如密码加密存储到数据库，而不是明文存储

- Web Support:Web 支持，可以非常容易的集成到Web 环境

- Caching:缓存，比如用户登录后，其用户信息、拥有的角色/权限不必每次去查，这样可以提高效率

- Concurrency:Shiro支持多线程应用的并发验证，即如在一个线程中开启另一个线程，能把权限自动传播过去

- Testing:提供测试支持

- "Run As":允许一个用户假装为另一个用户（如果他们允许）的身份进行访问

- Remember Me:记住我，这个是非常常见的功能，即一次登录后，下次再来的话不用登录了

## 1.3、Shiro架构(外部)

从外部来看Shiro，即从应用程序角度的来观察如何使用Shiro完成工作

![image-20200729114702566](https://gitee.com/lzh_gitee/springboot_image/raw/master/img/image-20200729114702566.png)

- Subject：应用代码直接交互的对象是Subject，也就是说Shiro的对外API 核心就是Subject。Subject 代表了当前“用户”，这个用户不一定是一个具体的人，与当前应用交互的任何东西都是Subject，如网络爬虫，机器人等；与Subject 的所有交互都会委托给SecurityManager；Subject 其实是一个门面，SecurityManager才是实际的执行者

- SecurityManager：安全管理器；即所有与安全有关的操作都会与SecurityManager交互；且其管理着所有Subject；可以看出它是Shiro的核心，它负责与Shiro的其他组件进行交互，它相当于SpringMVC中DispatcherServlet的角色

- Realm：Shiro从Realm 获取安全数据（如用户、角色、权限），就是说SecurityManager要验证用户身份，那么它需要从Realm 获取相应的用户进行比较以确定用户身份是否合法；也需要从Realm 得到用户相应的角色/权限进行验证用户是否能进行操作；可以把Realm 看成DataSource

## 1.4、Shiro架构(内部)

![image-20200729114720578](https://gitee.com/lzh_gitee/springboot_image/raw/master/img/image-20200729114720578.png)

- Subject：任何可以与应用交互的“用户”；
- SecurityManager：相当于SpringMVC中的DispatcherServlet；是Shiro的心脏；所有具体的交互都通过SecurityManager进行控制；它管理着所有Subject、且负责进行认证、授权、会话及缓存的管理。
- Authenticator：负责Subject 认证，是一个扩展点，可以自定义实现；可以使用认证策略（Authentication Strategy），即什么情况下算用户认证通过了；
- Authorizer：授权器、即访问控制器，用来决定主体是否有权限进行相应的操作；即控制着用户能访问应用中的哪些功能；
- Realm：可以有1 个或多个Realm，可以认为是安全实体数据源，即用于获取安全实体的；可以是JDBC 实现，也可以是内存实现等等；由用户提供；所以一般在应用中都需要实现自己的Realm；
- SessionManager：管理Session 生命周期的组件；而Shiro并不仅仅可以用在Web 环境，也可以用在如普通的JavaSE环境
  CacheManager：缓存控制器，来管理如用户、角色、权限等的缓存的；因为这些数据基本上很少改变，放到缓存中后可以提高访问的性能
- Cryptography：密码模块，Shiro提高了一些常见的加密组件用于如密码加密/解密。

# 2、Hello World

## 2.1、快速实践

- 查看官方文档：http://shiro.apache.org/tutorial.html

- 官方的quickstart : https://github.com/apache/shiro/tree/master/samples/quickstart/

  ![image-20200729115148574](https://gitee.com/lzh_gitee/springboot_image/raw/master/img/image-20200729115148574.png)

1. 创建一个maven父工程，用来学习Shiro,删掉不必要的部分

2. 创建一个普通的Maven子工程：hell-shiro

   ![image-20200729120114648](https://gitee.com/lzh_gitee/springboot_image/raw/master/img/image-20200729120114648.png)

3. 根据[官方文档](https://github.com/apache/shiro/blob/master/samples/quickstart/pom.xml)，我们导入Shiro的依赖

   ![image-20200729120207730](https://gitee.com/lzh_gitee/springboot_image/raw/master/img/image-20200729120207730.png)

   

   [版本号点击这里](https://mvnrepository.com/artifact/org.apache.shiro/shiro-core)

   ```xml
   <dependencies>
       <dependency>
           <groupId>org.apache.shiro</groupId>
           <artifactId>shiro-core</artifactId>
           <version>1.5.3</version>
       </dependency>
   
       <!-- configure logging -->
       <dependency>
           <groupId>org.slf4j</groupId>
           <artifactId>jcl-over-slf4j</artifactId>
           <version>1.7.26</version>
       </dependency>
       <dependency>
           <groupId>org.slf4j</groupId>
           <artifactId>slf4j-log4j12</artifactId>
           <version>1.7.26</version>
       </dependency>
       <dependency>
           <groupId>log4j</groupId>
           <artifactId>log4j</artifactId>
           <version>1.2.17</version>
       </dependency>
   </dependencies>
   ```

4. 相关配置文件

   - log4j.properties——[官网](https://github.com/apache/shiro/blob/master/samples/quickstart/src/main/resources/log4j.properties)

     ```properties
     log4j.rootLogger=INFO, stdout
     
     log4j.appender.stdout=org.apache.log4j.ConsoleAppender
     log4j.appender.stdout.layout=org.apache.log4j.PatternLayout
     log4j.appender.stdout.layout.ConversionPattern=%d %p [%c] - %m %n
     
     # General Apache libraries
     log4j.logger.org.apache=WARN
     
     # Spring
     log4j.logger.org.springframework=WARN
     
     # Default Shiro logging
     log4j.logger.org.apache.shiro=INFO
     
     # Disable verbose logging
     log4j.logger.org.apache.shiro.util.ThreadContext=WARN
     log4j.logger.org.apache.shiro.cache.ehcache.EhCache=WARN
     ```

   - shiro.ini——[官网](https://github.com/apache/shiro/blob/master/samples/quickstart/src/main/resources/shiro.ini)

     ```ini
     [users]
     # user 'root' with password 'secret' and the 'admin' role
     root = secret, admin
     # user 'guest' with the password 'guest' and the 'guest' role
     guest = guest, guest
     # user 'presidentskroob' with password '12345' ("That's the same combination on
     # my luggage!!!" ;)), and role 'president'
     presidentskroob = 12345, president
     # user 'darkhelmet' with password 'ludicrousspeed' and roles 'darklord' and 'schwartz'
     darkhelmet = ludicrousspeed, darklord, schwartz
     # user 'lonestarr' with password 'vespa' and roles 'goodguy' and 'schwartz'
     lonestarr = vespa, goodguy, schwartz
     
     # -----------------------------------------------------------------------------
     # Roles with assigned permissions
     # 
     # Each line conforms to the format defined in the
     # org.apache.shiro.realm.text.TextConfigurationRealm#setRoleDefinitions JavaDoc
     # -----------------------------------------------------------------------------
     [roles]
     # 'admin' role has all permissions, indicated by the wildcard '*'
     admin = *
     # The 'schwartz' role can do anything (*) with any lightsaber:
     schwartz = lightsaber:*
     # The 'goodguy' role is allowed to 'drive' (action) the winnebago (type) with
     # license plate 'eagle5' (instance specific id)
     goodguy = winnebago:drive:eagle5
     ```

   - 启动类 Quickstart——[官网](https://github.com/apache/shiro/blob/master/samples/quickstart/src/main/java/Quickstart.java)

     ```java
     /*
      * Licensed to the Apache Software Foundation (ASF) under one
      * or more contributor license agreements.  See the NOTICE file
      * distributed with this work for additional information
      * regarding copyright ownership.  The ASF licenses this file
      * to you under the Apache License, Version 2.0 (the
      * "License"); you may not use this file except in compliance
      * with the License.  You may obtain a copy of the License at
      *
      *     http://www.apache.org/licenses/LICENSE-2.0
      *
      * Unless required by applicable law or agreed to in writing,
      * software distributed under the License is distributed on an
      * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
      * KIND, either express or implied.  See the License for the
      * specific language governing permissions and limitations
      * under the License.
      */
     
     import org.apache.shiro.SecurityUtils;
     import org.apache.shiro.authc.*;
     import org.apache.shiro.config.IniSecurityManagerFactory;
     import org.apache.shiro.mgt.SecurityManager;
     import org.apache.shiro.session.Session;
     import org.apache.shiro.subject.Subject;
     import org.apache.shiro.util.Factory;
     import org.slf4j.Logger;
     import org.slf4j.LoggerFactory;
     
     
     /**
      * Simple Quickstart application showing how to use Shiro's API.
      * 简单入门Shiro使用API
      *
      * @since 0.9 RC2
      */
     public class Quickstart {
     
         private static final transient Logger log = LoggerFactory.getLogger(Quickstart.class);
     
     
         public static void main(String[] args) {
     
             // The easiest way to create a Shiro SecurityManager with configured
             // realms, users, roles and permissions is to use the simple INI config.
             // We'll do that by using a factory that can ingest a .ini file and
             // return a SecurityManager instance:
     
             // Use the shiro.ini file at the root of the classpath
             // (file: and url: prefixes load from files and urls respectively):
             // 读取配置文件:
             Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
             SecurityManager securityManager = factory.getInstance();
     
             // for this simple example quickstart, make the SecurityManager
             // accessible as a JVM singleton.  Most applications wouldn't do this
             // and instead rely on their container configuration or web.xml for
             // webapps.  That is outside the scope of this simple quickstart, so
             // we'll just do the bare minimum so you can continue to get a feel
             // for things.
             SecurityUtils.setSecurityManager(securityManager);
     
             // Now that a simple Shiro environment is set up, let's see what you can do:
     
             // get the currently executing user:
             // 获取当前的用户对象 Subject
             Subject currentUser = SecurityUtils.getSubject();
     
             // Do some stuff with a Session (no need for a web or EJB container!!!)
             //通过当前用户拿到Shiro的Session 可以脱离web存值取值
             Session session = currentUser.getSession();
             session.setAttribute("someKey", "aValue");
             String value = (String) session.getAttribute("someKey");
             if (value.equals("aValue")) {
                 log.info("Retrieved the correct value! [" + value + "]");
             }
     
             // let's login the current user so we can check against roles and permissions:
             //判断当前的用户是否被认证
             if (!currentUser.isAuthenticated()) {
                 //Token 令牌
                 UsernamePasswordToken token = new UsernamePasswordToken("lonestarr", "vespa");
                 //设置记住我
                 token.setRememberMe(true);
                 try {
                     //执行登录操作
                     currentUser.login(token);
                 } catch (UnknownAccountException uae) {
                     log.info("There is no user with username of " + token.getPrincipal());
                 } catch (IncorrectCredentialsException ice) {
                     log.info("Password for account " + token.getPrincipal() + " was incorrect!");
                 } catch (LockedAccountException lae) {
                     log.info("The account for username " + token.getPrincipal() + " is locked.  " +
                             "Please contact your administrator to unlock it.");
                 }
                 // ... catch more exceptions here (maybe custom ones specific to your application?
                 catch (AuthenticationException ae) {
                     //unexpected condition?  error?
                 }
             }
     
             //say who they are:
             //print their identifying principal (in this case, a username):
             log.info("User [" + currentUser.getPrincipal() + "] logged in successfully.");
     
             //test a role:
             // 检查角色
             if (currentUser.hasRole("schwartz")) {
                 log.info("May the Schwartz be with you!");
             } else {
                 log.info("Hello, mere mortal.");
             }
     
             //test a typed permission (not instance-level)
             //粗粒度
             if (currentUser.isPermitted("lightsaber:wield")) {
                 log.info("You may use a lightsaber ring.  Use it wisely.");
             } else {
                 log.info("Sorry, lightsaber rings are for schwartz masters only.");
             }
     
             //a (very powerful) Instance Level permission:
             //细粒度
             if (currentUser.isPermitted("winnebago:drive:eagle5")) {
                 log.info("You are permitted to 'drive' the winnebago with license plate (id) 'eagle5'.  " +
                         "Here are the keys - have fun!");
             } else {
                 log.info("Sorry, you aren't allowed to drive the 'eagle5' winnebago!");
             }
     
             //all done - log out!
             //注销
             currentUser.logout();
     
             //结束
             System.exit(0);
         }
     }
     ```

     ![image-20200729130649625](https://gitee.com/lzh_gitee/springboot_image/raw/master/img/image-20200729130649625.png)

   - Spring Secutrry都有~（只是换了个名字）

     ```java
     // 获取当前的用户对象 Subject
     Subject currentUser = SecurityUtils.getSubject();
     Session session = currentUser.getSession();
     currentUser.isAuthenticated()
         currentUser.getPrincipal()
         currentUser.hasRole("schwartz")
         currentUser.isPermitted("lightsaber:wield")
         currentUser.logout();
     ```



# 3、SpringBoot集成

## 3.1、SpringBoot整合Shiro环境搭建

1. 新建一个项目或模块，勾选依赖

   ![image-20200729174715011](https://gitee.com/lzh_gitee/springboot_image/raw/master/img/image-20200729174715011.png)

   pom.xml

   ```xml
   <dependencies>
       <!--thymeleaf-->
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-thymeleaf</artifactId>
       </dependency>
   
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-web</artifactId>
       </dependency>
   
       <dependency>
           <groupId>org.springframework.boot</groupId>
           <artifactId>spring-boot-starter-test</artifactId>
           <scope>test</scope>
           <exclusions>
               <exclusion>
                   <groupId>org.junit.vintage</groupId>
                   <artifactId>junit-vintage-engine</artifactId>
               </exclusion>
           </exclusions>
       </dependency>
   </dependencies>
   ```

2. 测试环境是否正常

   - 新建一个controller页面

     ```java
     @Controller
     public class MyController {
     
         @RequestMapping({"/","/index"})
         public String toIndex(Model model) {
             model.addAttribute("msg","hello,Shiro");
             return "index";
         }
         
         @RequestMapping("/user/add")
         public String add() {
             return "user/add";
         }
     
         @RequestMapping("/user/update")
         public String update() {
             return "user/update";
     	}
     }
     ```

   - 新建一个index.html页面

     ```html
     <!DOCTYPE html>
     <html lang="en" xmlns:th="http://www.thymeleaf.org">
     <head>
         <meta charset="UTF-8">
         <title>首页</title>
     </head>
     <body>
     <div>
         <h1>首页</h1>
         <p th:text="${msg}"></p>
     
         <hr>
         <a th:href="@{/user/add}">add</a>   | <a th:href="@{/user/update}">update</a>
     </div>
     </body>
     </html>
     ```

   - 新建一个add.html页面

     ```html
     <!DOCTYPE html>
     <html lang="en">
     <head>
         <meta charset="UTF-8">
         <title>Title</title>
     </head>
     <body>
         <h1>add</h1>
     </body>
     </html>
     ```

   - 新建一个update.html页面

     ```html
     <!DOCTYPE html>
     <html lang="en">
     <head>
         <meta charset="UTF-8">
         <title>Title</title>
     </head>
     <body>
         <h1>update</h1>
     </body>
     </html>
     ```

   - 项目结构

     ![image-20200729190325307](https://gitee.com/lzh_gitee/springboot_image/raw/master/img/image-20200729190325307.png)

   - 运行截图

     ![image-20200729190548307](https://gitee.com/lzh_gitee/springboot_image/raw/master/img/image-20200729190548307.png)

3. 导入shiro整合spring的包——[官网](https://mvnrepository.com/artifact/org.apache.shiro/shiro-spring),查看最新版本

   ```xml
   <!--
         Subject  用户
         SecurityManager 管理所有用户
         Realm 连接数据库
   -->
   
   <!--shiro整合spring的包-->
   <dependency>
       <groupId>org.apache.shiro</groupId>
       <artifactId>shiro-spring</artifactId>
       <version>1.5.3</version>
   </dependency>
   ```

4. 编写导入配置类

   - 编写一个自定义类UserRealm

     ```java
     //自定义的UserRealm
     public class UserRealm extends AuthorizingRealm {
         //授权
         @Override
         protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
             System.out.println("执行了=>授权doGetAuthorizationInfo");
             return null;
         }
     
         //认证
         @Override
         protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
             System.out.println("执行了=>认证doGetAuthorizationInfo");
             return null;
         }
     }
     ```

   - 编写配置ShiroConfig

     - 创建realm对象，需要自定义类
     - DefaultWebSecurityManager
     - ShiroFilterFactoryBean

     ```java
     @Configuration
     public class ShiroConfig {
     
         //3. shiroFilterFactoryBean
     
         @Bean
         public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("getDefaultWebSecurityManager") DefaultWebSecurityManager defaultWebSecurityManager) {
             ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
             // 设置安全管理器
             bean.setSecurityManager(defaultWebSecurityManager);
     
             return bean;
         }
     
         //2. DefaultWebSecurityManager
     
         @Bean
         public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm) {
             DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
     
             // 关联userRealm
             securityManager.setRealm(userRealm);
             return securityManager;
         }
         //1. 创建realm对象，需要自定义类
     
         @Bean
         public UserRealm userRealm() {
             return new UserRealm();
         }
     }
     ```

## 3.2、Shiro实现登录拦截

- 在`ShiroConfig`中的`getShiroFilterFactoryBean`方法中添加如下配置

    - anon： 无需认证就可以访问
    - authc： 必须认证了才能访问
    - user： 必须拥有记住我功能才能用
    - perms： 拥有对某个资源的权限才能访问
    - role： 拥有某个角色权限

    ```java
    Map<String, String> filterMap = new LinkedHashMap<>();
    filterMap.put("/user/add","authc");
    filterMap.put("/user/update","authc");
    bean.setFilterChainDefinitionMap(filterMap);
    ```

- 点击首页的add或者update之后

  ![image-20200729191619576](https://gitee.com/lzh_gitee/springboot_image/raw/master/img/image-20200729191619576.png)

- 添加拦截成功页面
  - 登录页面login.html

    ```html
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>登录页面</title>
    </head>
    <body>
    <h1>登录</h1>
    <hr>

    <form action="">
        <p>用户名：<input type="text" name="username"></p>
        <p>密码：<input type="text" name="password"></p>
        <p>密码：<input type="submit"></p>
    </form>
    </body>
    </html>
    ```

  - 在MyConfig中添加
  
      ```java
      @RequestMapping("/toLogin")
      public String toLogin() {
          return "login";
      }
      ```
  
  - 在`ShiroConfig`中的`getShiroFilterFactoryBean`方法中添加如下配置
  
      ```java
      //设置登录的请求
      bean.setLoginUrl("/toLogin");
      ```
  
- 拦截成功页面

  ![image-20200729192409085](https://gitee.com/lzh_gitee/springboot_image/raw/master/img/image-20200729192409085.png)

## 3.3、Shiro实现用户认证

1. 在`MyController`中编写用户提交表单之后处理

   ```java
   @RequestMapping("/login")
   public String login(String username, String password, Model model) {
       //获取一个用户
       Subject subject = SecurityUtils.getSubject();
       // 封装用户的登录数据
       UsernamePasswordToken token = new UsernamePasswordToken(username, password);
   
       try {
           subject.login(token);//执行登录的方法，如果没有异常就说明ok了
           return "index";
       } catch (UnknownAccountException e) {//用户名不存在
           model.addAttribute("msg","用户名错误");
           return "login";
       } catch (IncorrectCredentialsException e) {//密码不存在
           model.addAttribute("msg","密码错误");
           return "login";
       }
   
   }
   ```

2. login.html的修改

   ```html
   <!DOCTYPE html>
   <html lang="en" xmlns:th="http://www.thymeleaf.org">
   <head>
       <meta charset="UTF-8">
       <title>登录页面</title>
   </head>
   <body>
   <h1>登录</h1>
   <hr>
   
   <p th:text="${msg}" style="color: red;"></p>
   <form th:action="@{/login}">
       <p>用户名：<input type="text" name="username"></p>
       <p>密码：<input type="text" name="password"></p>
       <p>密码：<input type="submit"></p>
   </form>
   </body>
   </html>
   ```

3. 用户输入登录信息

   - 页面

     ![image-20200729220647520](https://gitee.com/lzh_gitee/springboot_image/raw/master/img/image-20200729220647520.png)

   - 控制台

     ![image-20200729220926500](https://gitee.com/lzh_gitee/springboot_image/raw/master/img/image-20200729220926500.png)

4. 用户认证编写`UserRealm`中的认证（doGetAuthenticationInfo）

   ```java
   //认证
   @Override
   protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
       System.out.println("执行了=>认证doGetAuthorizationInfo");
       // 用户名、密码， 数据中取
       String name = "root";
       String password = "123456";
   
       UsernamePasswordToken userToken = (UsernamePasswordToken) token;
   
       if (!userToken.getUsername().equals(name)) {
           return null;//抛出异常 UnknownAccountException
       }
   
       // 密码认证，shiro做
       return new SimpleAuthenticationInfo("",password,"");
   }
   ```

## 3.4、Shiro整合Mybatis

1. 导入依赖

   ```xml
   <dependency>
       <groupId>org.projectlombok</groupId>
       <artifactId>lombok</artifactId>
   </dependency>
   <dependency>
       <groupId>mysql</groupId>
       <artifactId>mysql-connector-java</artifactId>
   </dependency>
   
   <dependency>
       <groupId>log4j</groupId>
       <artifactId>log4j</artifactId>
       <version>1.2.17</version>
   </dependency>
   
   <dependency>
       <groupId>com.alibaba</groupId>
       <artifactId>druid</artifactId>
       <version>1.1.23</version>
   </dependency>
   
   <!--引入mybatis，这是MyBatis官方提供的适配spring Boot的，而不是spring Boot自己的-->
   <dependency>
       <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
       <version>2.1.3</version>
   </dependency>
   ```
   
2. 配置文件application.yml的编写

   ```yml
   spring:
     datasource:
       username: root
       password: admin
       #?serverTimezone=UTC解决时区的报错
       url: jdbc:mysql://localhost:3306/mybatis?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8
       driver-class-name: com.mysql.cj.jdbc.Driver
       type: com.alibaba.druid.pool.DruidDataSource
   
       #Spring Boot 默认是不注入这些属性值的，需要自己绑定
       #druid 数据源专有配置
       initialSize: 5
       minIdle: 5
       maxActive: 20
       maxWait: 60000
       timeBetweenEvictionRunsMillis: 60000
       minEvictableIdleTimeMillis: 300000
       validationQuery: SELECT 1 FROM DUAL
       testWhileIdle: true
       testOnBorrow: false
       testOnReturn: false
       poolPreparedStatements: true
   
       #配置监控统计拦截的filters，stat:监控统计、log4j：日志记录、wall：防御sql注入
       #如果允许时报错  java.lang.ClassNotFoundException: org.apache.log4j.Priority
       #则导入 log4j 依赖即可，Maven 地址：https://mvnrepository.com/artifact/log4j/log4j
       filters: stat,wall,log4j
       maxPoolPreparedStatementPerConnectionSize: 20
       useGlobalDataSourceStat: true
       connectionProperties: druid.stat.mergeSql=true;druid.stat.slowSqlMillis=500
       
   mybatis:
     type-aliases-package: nuc.ss.pojo
     mapper-locations: classpath:mapper/*.xml
   ```

3. User类的编写

   ```java
   @Data
   @AllArgsConstructor
   @NoArgsConstructor
   public class User {
       private int id;
       private String name;
       private String pwd;
   }
   ```

4. UserMapper.xml映射

   ```xml
   <?xml version="1.0" encoding="UTF-8" ?>
   <!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
           "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
   <!--namespace=绑定一个对应的Dao/Mapper接口-->
   <mapper namespace="nuc.ss.mapper.UserMapper">
       
       <select id="queryUserList" resultType="User">
           select * from mybatis.user;
       </select>
   
       <select id="queryUserById" resultType="User">
           select * from mybatis.user where id = #{id};
       </select>
   
       <insert id="addUser" parameterType="User">
           insert into mybatis.user (id, name, pwd) values (#{id},#{name},#{pwd});
       </insert>
   
       <update id="updateUser" parameterType="User">
           update mybatis.user set name=#{name},pwd = #{pwd} where id = #{id};
       </update>
   
       <delete id="deleteUser" parameterType="int">
           delete from mybatis.user where id = #{id}
       </delete>
   </mapper>
   ```
   
5. UserService接口实现

   ```java
   public interface UserService {
   
       public User queryUserByName(String name);
   }
   ```

6. UserServiceImpl业务逻辑

   ```java
   @Service
   public class UserServiceImpl implements UserService {
   
       @Autowired
       UserMapper userMapper;
       @Override
       public User queryUserByName(String name) {
           return userMapper.queryUserByName(name);
       }
   }
   
   ```

7. 测试环境

   ```java
   @SpringBootTest
   class ShiroSpringbootApplicationTests {
   
       @Autowired
       UserService userService;
       @Test
       void contextLoads() {
           System.out.println(userService.queryUserByName("狂神"));
       }
   
   }
   ```

   ![image-20200730121720922](https://gitee.com/lzh_gitee/springboot_image/raw/master/img/image-20200730121720922.png)

8. `UserRealm`连接真实数据库

   ```java
   //认证
   @Override
   protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
       System.out.println("执行了=>认证doGetAuthorizationInfo");
   
       UsernamePasswordToken userToken = (UsernamePasswordToken) token;
       
       // 真实数据库 用户名、密码， 数据中取
       User user = userService.queryUserByName(userToken.getUsername());
   
       if (user == null) {//没有这个人
           return null;
       }
   
       // 密码认证，shiro做
       return new SimpleAuthenticationInfo("",user.getPwd(),"");
   }
   ```

   ![image-20200730180019861](https://gitee.com/lzh_gitee/springboot_image/raw/master/img/image-20200730180019861.png)
   
9. 断点测试密码加密类型

   - 打断点Debug

     ![image-20200730182621912](https://gitee.com/lzh_gitee/springboot_image/raw/master/img/image-20200730182621912.png)

   - 默认是`SimpleCredentialsMatcher`加密

     ![image-20200730181814293](https://gitee.com/lzh_gitee/springboot_image/raw/master/img/image-20200730181814293.png)

   - MD5加密——[测试](http://tool.chinaz.com/tools/md5.aspx)

     123456——E10ADC3949BA59ABBE56E057F20F883E

   - MD5盐值加密

   - 所有加密

     ![image-20200730181944253](https://gitee.com/lzh_gitee/springboot_image/raw/master/img/image-20200730181944253.png)

## 3.5、Shiro实现用户授权

1. `ShiroConfig`中的`getShiroFilterFactoryBean`方法添加认证代码

   ```java
   //授权，正常情况下，没有授权会跳转到为授权页面
   filterMap.put("/user/add","perms[user:add]");
   filterMap.put("/user/update","perms[user:update]");
   ```

2. 登录之后点击add按钮会弹出如下页面

   ![image-20200730195133631](https://gitee.com/lzh_gitee/springboot_image/raw/master/img/image-20200730195133631.png)

3. 添加为授权页面

   - MyController

     ```java
     @RequestMapping("/noauto")
     @ResponseBody
     public String unauthorized() {
         return "未经授权，无法访问此页面";
     }
     ```

   - `ShiroConfig`中的`getShiroFilterFactoryBean`方法中添加

     ```java
     //为授权页面
     bean.setUnauthorizedUrl("/noauto");
     ```

4. 再次测试

   ![image-20200730195807437](https://gitee.com/lzh_gitee/springboot_image/raw/master/img/image-20200730195807437.png)

   ![image-20200730195946692](https://gitee.com/lzh_gitee/springboot_image/raw/master/img/image-20200730195946692.png)

   所以需要在UserRealm中为用户进行真正授权

5. UserRealm类的修改

   ```java
   //自定义的UserRealm
   public class UserRealm extends AuthorizingRealm {
   
       @Autowired
       UserService userService;
       //授权
       @Override
       protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
           System.out.println("执行了=>授权doGetAuthorizationInfo");
   
           SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
   
           //拿到当前登录的这个对象
           Subject subject = SecurityUtils.getSubject();
           User currentUser = (User)subject.getPrincipal();//拿到user对象
   
           //设置当前用户的权限
           info.addStringPermission(currentUser.getPerms());
   
           return info;
       }
   
       //认证
       @Override
       protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
           ......
           // 密码认证，shiro做
           return new SimpleAuthenticationInfo(user,user.getPwd(),"");
       }
   }
   
   ```

6. 再次测试

   ![image-20200730202810034](https://gitee.com/lzh_gitee/springboot_image/raw/master/img/image-20200730202810034.png)

## 3.6、Shiro整合Thymeleaf

1. shiro-thymeleaf整合包导入——[官网](https://mvnrepository.com/artifact/com.github.theborakompanioni/thymeleaf-extras-shiro)

   ```xml
   <!--shiro-thymeleaf整合-->
   <dependency>
       <groupId>com.github.theborakompanioni</groupId>
       <artifactId>thymeleaf-extras-shiro</artifactId>
       <version>2.0.0</version>
   </dependency>
   ```

2. 在ShiroConfig中整合ShiroDialect

   ```java
   // 整合ShiroDialect： 用来整合 Shiro thymeleaf
   @Bean
   public ShiroDialect getShiroDialect() {
       return new ShiroDialect();
   }
   ```

3. index.html页面

   ```html
   <!DOCTYPE html>
   <html lang="en" xmlns:th="http://www.thymeleaf.org"
         xmlns:shiro="http://www.thymeleaf.org/thymeleaf-extras-shiro">
   <head>
       <meta charset="UTF-8">
       <title>首页</title>
   </head>
   <body>
   
   <div>
       <h1>首页</h1>
       <p th:text="${msg}"></p>
   
       <!--用session实现，配合UserRealm中的session实现-->
       <!--<div th:if="${session.loginUser==null}">
           <a th:href="@{/toLogin}">登录</a>
       </div>-->
   
       <div shiro:notAuthenticated>
           <a th:href="@{/toLogin}">登录</a>
       </div>
   
       <hr>
   
       <div shiro:hasPermission="user:add">
           <a th:href="@{/user/add}">add</a>
       </div>
   
       <div shiro:hasPermission="user:update">
           <a th:href="@{/user/update}">update</a>
       </div>
   
   </div>
   </body>
   </html>
   ```

4. 页面显示

   ![image-20200730205736153](https://gitee.com/lzh_gitee/springboot_image/raw/master/img/image-20200730205736153.png)

## 3.7、所有代码

- ShiroConfig

  ```java
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
  ```

- UserRealm

  ```java
  package nuc.ss.config;
  
  import nuc.ss.pojo.User;
  import nuc.ss.service.UserService;
  import org.apache.shiro.SecurityUtils;
  import org.apache.shiro.authc.*;
  import org.apache.shiro.authz.AuthorizationInfo;
  import org.apache.shiro.authz.SimpleAuthorizationInfo;
  import org.apache.shiro.realm.AuthorizingRealm;
  import org.apache.shiro.session.Session;
  import org.apache.shiro.subject.PrincipalCollection;
  import org.apache.shiro.subject.Subject;
  import org.springframework.beans.factory.annotation.Autowired;
  
  //自定义的UserRealm
  public class UserRealm extends AuthorizingRealm {
  
      @Autowired
      UserService userService;
      //授权
      @Override
      protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
          System.out.println("执行了=>授权doGetAuthorizationInfo");
  
          SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
  
          //info.addStringPermission("user:add");
  
          //拿到当前登录的这个对象
          Subject subject = SecurityUtils.getSubject();
          User currentUser = (User)subject.getPrincipal();//拿到user对象
  
          //设置当前用户的权限
          info.addStringPermission(currentUser.getPerms());
  
          return info;
      }
  
      //认证
      @Override
      protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
          System.out.println("执行了=>认证doGetAuthorizationInfo");
  
          UsernamePasswordToken userToken = (UsernamePasswordToken) token;
  
          // 虚拟用户
          //String name = "root";
          //String password = "123456";
          //if (!userToken.getUsername().equals(name)) {
          //    return null;//抛出异常 UnknownAccountException
          //}
  
          // 真实数据库 用户名、密码， 数据中取
          User user = userService.queryUserByName(userToken.getUsername());
  
          if (user == null) {//没有这个人
              return null;
          }
  
          //首页
          //Subject currentSubject = SecurityUtils.getSubject();
          //Session session = currentSubject.getSession();
          //session.setAttribute("loginUser",user);
  
  
          // 密码认证，shiro做
          return new SimpleAuthenticationInfo(user,user.getPwd(),"");
      }
  }
  ```

- MyController

  ```java
  package nuc.ss.controller;
  
  import org.apache.shiro.SecurityUtils;
  import org.apache.shiro.authc.IncorrectCredentialsException;
  import org.apache.shiro.authc.UnknownAccountException;
  import org.apache.shiro.authc.UsernamePasswordToken;
  import org.apache.shiro.subject.Subject;
  import org.springframework.stereotype.Controller;
  import org.springframework.ui.Model;
  import org.springframework.web.bind.annotation.RequestMapping;
  import org.springframework.web.bind.annotation.ResponseBody;
  
  @Controller
  public class MyController {
  
      @RequestMapping({"/","/index"})
      public String toIndex(Model model) {
          model.addAttribute("msg","hello,Shiro");
          return "index";
      }
  
      @RequestMapping("/user/add")
      public String add() {
          return "user/add";
      }
  
      @RequestMapping("/user/update")
      public String update() {
          return "user/update";
      }
  
      @RequestMapping("/toLogin")
      public String toLogin() {
          return "login";
      }
  
      @RequestMapping("/login")
      public String login(String username, String password, Model model) {
          //获取一个用户
          Subject subject = SecurityUtils.getSubject();
          // 封装用户的登录数据
          UsernamePasswordToken token = new UsernamePasswordToken(username, password);
  
          try {
              subject.login(token);//执行登录的方法，如果没有异常就说明ok了
              return "index";
          } catch (UnknownAccountException e) {//用户名不存在
              model.addAttribute("msg","用户名错误");
              return "login";
          } catch (IncorrectCredentialsException e) {//密码不存在
              model.addAttribute("msg","密码错误");
              return "login";
          }
      }
  
      @RequestMapping("/noauto")
      @ResponseBody
      public String unauthorized() {
          return "未经授权，无法访问此页面";
      }
  }
  
  ```

- pom依赖

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
      <modelVersion>4.0.0</modelVersion>
      <groupId>nuc.ss</groupId>
      <artifactId>shiro-springboot</artifactId>
      <version>0.0.1-SNAPSHOT</version>
      <name>shiro-springboot</name>
      <description>Demo project for Spring Boot</description>
  
      <properties>
          <java.version>1.8</java.version>
          <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
          <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
          <spring-boot.version>2.3.0.RELEASE</spring-boot.version>
      </properties>
  
      <dependencies>
  
          <!--
              Subject  用户
              SecurityManager 管理所有用户
              Realm 连接数据库
          -->
  
          <!--shiro-thymeleaf整合-->
          <!-- https://mvnrepository.com/artifact/com.github.theborakompanioni/thymeleaf-extras-shiro -->
          <dependency>
              <groupId>com.github.theborakompanioni</groupId>
              <artifactId>thymeleaf-extras-shiro</artifactId>
              <version>2.0.0</version>
          </dependency>
  
          <dependency>
              <groupId>org.projectlombok</groupId>
              <artifactId>lombok</artifactId>
          </dependency>
          <dependency>
              <groupId>mysql</groupId>
              <artifactId>mysql-connector-java</artifactId>
          </dependency>
  
          <dependency>
              <groupId>log4j</groupId>
              <artifactId>log4j</artifactId>
              <version>1.2.17</version>
          </dependency>
  
          <dependency>
              <groupId>com.alibaba</groupId>
              <artifactId>druid</artifactId>
              <version>1.1.23</version>
          </dependency>
  
          <!--引入mybatis，这是MyBatis官方提供的适配spring Boot的，而不是spring Boot自己的-->
          <dependency>
              <groupId>org.mybatis.spring.boot</groupId>
              <artifactId>mybatis-spring-boot-starter</artifactId>
              <version>2.1.3</version>
          </dependency>
  
          <!--shiro整合spring的包-->
          <dependency>
              <groupId>org.apache.shiro</groupId>
              <artifactId>shiro-spring</artifactId>
              <version>1.5.3</version>
          </dependency>
          <!--thymeleaf-->
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-thymeleaf</artifactId>
          </dependency>
  
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-web</artifactId>
          </dependency>
  
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-test</artifactId>
              <scope>test</scope>
              <exclusions>
                  <exclusion>
                      <groupId>org.junit.vintage</groupId>
                      <artifactId>junit-vintage-engine</artifactId>
                  </exclusion>
              </exclusions>
          </dependency>
      </dependencies>
  
      <dependencyManagement>
          <dependencies>
              <dependency>
                  <groupId>org.springframework.boot</groupId>
                  <artifactId>spring-boot-dependencies</artifactId>
                  <version>${spring-boot.version}</version>
                  <type>pom</type>
                  <scope>import</scope>
              </dependency>
          </dependencies>
      </dependencyManagement>
  
      <build>
          <plugins>
              <plugin>
                  <groupId>org.apache.maven.plugins</groupId>
                  <artifactId>maven-compiler-plugin</artifactId>
                  <configuration>
                      <source>1.8</source>
                      <target>1.8</target>
                      <encoding>UTF-8</encoding>
                  </configuration>
              </plugin>
              <plugin>
                  <groupId>org.springframework.boot</groupId>
                  <artifactId>spring-boot-maven-plugin</artifactId>
              </plugin>
          </plugins>
      </build>
  
  </project>
  
  ```

  

# 4、完美的解释

[让 Apache Shiro 保护你的应用](https://www.infoq.cn/article/apache-shiro/?itm_source=infoq_en&itm_medium=link_on_en_item&itm_campaign=item_in_other_langs)
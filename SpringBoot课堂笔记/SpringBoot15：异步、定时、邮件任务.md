# 异步任务

1. 创建一个service包

2. 创建一个类AsyncService

   异步处理还是非常常用的，比如我们在网站上发送邮件，后台会去发送邮件，此时前台会造成响应不动，直到邮件发送完毕，响应才会成功，所以我们一般会采用多线程的方式去处理这些任务。

   编写方法，假装正在处理数据，使用线程设置一些延时，模拟同步等待的情况；

    ```java
    @Service
    public class AsyncService {

       public void hello(){
           try {
               Thread.sleep(3000);
          } catch (InterruptedException e) {
               e.printStackTrace();
          }
           System.out.println("业务进行中....");
      }
    }
    ```

3. 编写controller包

4. 编写AsyncController类

   我们去写一个Controller测试一下

    ```java
    @RestController
    public class AsyncController {

       @Autowired
       AsyncService asyncService;

       @GetMapping("/hello")
       public String hello(){
           asyncService.hello();
           return "OK";
      }

    }
    ```

5. 访问http://localhost:8080/hello进行测试，3秒后出现OK，这是同步等待的情况。

   问题：我们如果想让用户直接得到消息，就在后台使用多线程的方式进行处理即可，但是每次都需要自己手动去编写多线程的实现的话，太麻烦了，我们只需要用一个简单的办法，在我们的方法上加一个简单的注解即可，如下：

6. 给hello方法添加@Async注解；

    ```java
    //告诉Spring这是一个异步方法
    @Async
    public void hello(){
       try {
           Thread.sleep(3000);
      } catch (InterruptedException e) {
           e.printStackTrace();
      }
       System.out.println("业务进行中....");
    }
    ```

    SpringBoot就会自己开一个线程池，进行调用！但是要让这个注解生效，我们还需要在主程序上添加一个注解@EnableAsync ，开启异步注解功能；

    ```java
    @EnableAsync //开启异步注解功能
    @SpringBootApplication
    public class SpringbootTaskApplication {
    
       public static void main(String[] args) {
           SpringApplication.run(SpringbootTaskApplication.class, args);
      }
    
    }
    ```

7、重启测试，网页瞬间响应，后台代码依旧执行！

# 邮件任务

邮件发送，在我们的日常开发中，也非常的多，Springboot也帮我们做了支持

- 邮件发送需要引入spring-boot-start-mail
- SpringBoot 自动配置MailSenderAutoConfiguration
- 定义MailProperties内容，配置在application.yml中
- 自动装配JavaMailSender
- 测试邮件发送

**测试：**

1. 引入pom依赖

   ```xml
   <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-mail</artifactId>
   </dependency>
   ```

   看它引入的依赖，可以看到 jakarta.mail

   ```xml
   <dependency>
      <groupId>com.sun.mail</groupId>
      <artifactId>jakarta.mail</artifactId>
      <scope>compile</scope>
   </dependency>
   ```

2. 查看自动配置类：MailSenderAutoConfiguration

   ![image-20200801104504309](https://gitee.com/lzh_gitee/springboot_image/raw/master/img/image-20200801104504309.png)

   这个类中存在bean，JavaMailSenderImpl

   ![](https://gitee.com/lzh_gitee/springboot_image/raw/master/img/image-20200801104504309.png)

   然后我们去看下配置文件

   ```java
   @ConfigurationProperties(prefix = "spring.mail")
   public class MailProperties {
   
   	private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
   	private String host;
   	private Integer port;
   	private String username;
   	private String password;
   	private String protocol = "smtp";
   	private Charset defaultEncoding = DEFAULT_CHARSET;
   	private Map<String, String> properties = new HashMap<>();
   	private String jndiName;
   	//set、get方法省略。。。
   }
   
   ```

3. 配置文件：

   ```properties
   spring.mail.username=1710841251@qq.com
   spring.mail.password=你的qq授权码
   spring.mail.host=smtp.qq.com
   # qq需要配置ssl
   spring.mail.properties.mail.smtp.ssl.enable=true
   ```

   获取授权码：在QQ邮箱中的设置->账户->开启pop3和smtp服务

   ![image-20200801105503766](https://gitee.com/lzh_gitee/springboot_image/raw/master/img/image-20200801105503766.png)

4. Spring单元测试

   ```java
   @Autowired
   JavaMailSenderImpl javaMailSender;
   @Test//邮件设置1：一个简单的邮件
   void contextLoads() {
       SimpleMailMessage mailMessage = new SimpleMailMessage();
       mailMessage.setSubject("狂神，你好");
       mailMessage.setText("谢谢你的狂神说Java系列课程");
   
       mailMessage.setTo("24736743@qq.com");
       mailMessage.setFrom("1710841251@qq.com");
       javaMailSender.send(mailMessage);
   }
   
   @Test// 一个复杂的邮件
   void contextLoads2() throws MessagingException {
       MimeMessage mimeMessage = javaMailSender.createMimeMessage();
       //组装
       MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
   
       //正文
       helper.setSubject("狂神，你好~plus");
       helper.setText("<p style='color:red'>谢谢你的狂神说Java系列课程</p>", true);
   
       //附件
       helper.addAttachment("1.jpg", new File(""));
       helper.addAttachment("2.jpg", new File(""));
   
       helper.setTo("24736743@qq.com");
       helper.setFrom("1710841251@qq.com");
   
       javaMailSender.send(mimeMessage);
   
   }
   ```

   ![image-20200801112646650](https://gitee.com/lzh_gitee/springboot_image/raw/master/img/image-20200801112646650.png)

查看邮箱，邮件接收成功！

我们只需要使用Thymeleaf进行前后端结合即可开发自己网站邮件收发功能了！

# 定时任务

项目开发中经常需要执行一些定时任务，比如需要在每天凌晨的时候，分析一次前一天的日志信息，Spring为我们提供了异步执行任务调度的方式，提供了两个接口。

- TaskExecutor接口（任务执行者）
- TaskScheduler接口（任务调度者）

两个注解：

- @EnableScheduling——开启定时功能的注解
- @Scheduled——什么时候执行

**cron表达式：**

| 字段 | 允许值                | 允许特殊字符    |
| ---- | --------------------- | --------------- |
| 秒   | 0-59                  | , - * /         |
| 分   | 0-59                  | , - * /         |
| 小时 | 0-23                  | , - * /         |
| 日期 | 1-31                  | , - * / ? L W C |
| 月份 | 1-12                  | , - * /         |
| 星期 | 0-1或SUN-SAT 0,7是SUN | , - * / ? L W C |

| 特殊字符 | 代表含义                   |
| -------- | -------------------------- |
| ,        | 枚举                       |
| -        | 区间                       |
| *        | 任意                       |
| /        | 步长                       |
| ?        | 日/星期冲突匹配            |
| L        | 最后                       |
| W        | 工作日                     |
| C        | 和calendar练习后计算过的值 |
| #        | 星期，4#2 第二个星期三     |

**测试步骤：**

1、创建一个ScheduledService

我们里面存在一个hello方法，他需要定时执行，怎么处理呢？

```java
@Service
public class ScheduledService {

    // 在一个特定的时间执行这个方法——Timer
    //cron表达式
    // 秒 分 时 日 月 周几

    /*
        0 49 11 * * ?   每天的11点49分00秒执行
        0 0/5 11,12 * * ?   每天的11点和12点每个五分钟执行一次
        0 15 10 ? * 1-6     每个月的周一到周六的10点15分执行一次
        0/2 * * * * ?     每2秒执行一次
     */
    @Scheduled(cron = "0/2 * * * * ?")
    public void hello() {
        System.out.println("hello,你被执行了");
    }
}
```

2、这里写完定时任务之后，我们需要在主程序上增加@EnableScheduling 开启定时任务功能

```java
@EnableAsync //开启异步注解功能
@EnableScheduling //开启基于注解的定时任务
@SpringBootApplication
public class SpringbootTaskApplication {

   public static void main(String[] args) {
       SpringApplication.run(SpringbootTaskApplication.class, args);
  }

}
```

3、我们来详细了解下cron表达式；

[http://www.bejson.com/othertools/cron/](http://www.bejson.com/othertools/cron/)

4、常用的表达式

```
（1）0/2 * * * * ?   表示每2秒 执行任务
（1）0 0/2 * * * ?   表示每2分钟 执行任务
（1）0 0 2 1 * ?   表示在每月的1日的凌晨2点调整任务
（2）0 15 10 ? * MON-FRI   表示周一到周五每天上午10:15执行作业
（3）0 15 10 ? 6L 2002-2006   表示2002-2006年的每个月的最后一个星期五上午10:15执行作
（4）0 0 10,14,16 * * ?   每天上午10点，下午2点，4点
（5）0 0/30 9-17 * * ?   朝九晚五工作时间内每半小时
（6）0 0 12 ? * WED   表示每个星期三中午12点
（7）0 0 12 * * ?   每天中午12点触发
（8）0 15 10 ? * *   每天上午10:15触发
（9）0 15 10 * * ?     每天上午10:15触发
（10）0 15 10 * * ?   每天上午10:15触发
（11）0 15 10 * * ? 2005   2005年的每天上午10:15触发
（12）0 * 14 * * ?     在每天下午2点到下午2:59期间的每1分钟触发
（13）0 0/5 14 * * ?   在每天下午2点到下午2:55期间的每5分钟触发
（14）0 0/5 14,18 * * ?     在每天下午2点到2:55期间和下午6点到6:55期间的每5分钟触发
（15）0 0-5 14 * * ?   在每天下午2点到下午2:05期间的每1分钟触发
（16）0 10,44 14 ? 3 WED   每年三月的星期三的下午2:10和2:44触发
（17）0 15 10 ? * MON-FRI   周一至周五的上午10:15触发
（18）0 15 10 15 * ?   每月15日上午10:15触发
（19）0 15 10 L * ?   每月最后一日的上午10:15触发
（20）0 15 10 ? * 6L   每月的最后一个星期五上午10:15触发
（21）0 15 10 ? * 6L 2002-2005   2002年至2005年的每月的最后一个星期五上午10:15触发
（22）0 15 10 ? * 6#3   每月的第三个星期五上午10:15触发
```


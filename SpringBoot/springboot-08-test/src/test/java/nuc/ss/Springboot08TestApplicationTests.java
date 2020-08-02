package nuc.ss;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@SpringBootTest
class Springboot08TestApplicationTests {

    @Autowired
    JavaMailSenderImpl javaMailSender;

    @Test
    void contextLoads() {

        // 一个简单的邮件
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject("狂神，你好");
        mailMessage.setText("谢谢你的狂神说Java系列课程");

        mailMessage.setTo("1710841251@qq.com");
        mailMessage.setFrom("1710841251@qq.com");
        javaMailSender.send(mailMessage);
    }

    @Test
    void contextLoads2() throws MessagingException {

        // 一个复杂的邮件
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        //组装
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        //正文
        helper.setSubject("狂神，你好~plus");
        helper.setText("<p style='color:red'>谢谢你的狂神说Java系列课程</p>", true);

        //附件
        helper.addAttachment("1.jpg", new File("D:\\win7我的文档-桌面-收藏夹\\Desktop\\ssm\\SpringBoot课堂笔记\\SpringBoot01：Hello,World！.assets\\1595409428560.png"));
        helper.addAttachment("2.jpg", new File("D:\\win7我的文档-桌面-收藏夹\\Desktop\\ssm\\SpringBoot课堂笔记\\SpringBoot01：Hello,World！.assets\\1595409428560.png"));

        helper.setTo("1710841251@qq.com");
        helper.setFrom("1710841251@qq.com");

        javaMailSender.send(mimeMessage);

    }

    /**
     *
     * @param html
     * @param subject
     * @param text
     * @throws MessagingException
     * @Author 123
     */
    public void sendMail(Boolean html, String subject, String text) throws MessagingException {
        // 一个复杂的邮件
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        //组装
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, html);

        //正文
        helper.setSubject(subject);
        helper.setText(text, true);

        //附件
        helper.addAttachment("1.jpg", new File("D:\\win7我的文档-桌面-收藏夹\\Desktop\\ssm\\SpringBoot课堂笔记\\SpringBoot01：Hello,World！.assets\\1595409428560.png"));
        helper.addAttachment("2.jpg", new File("D:\\win7我的文档-桌面-收藏夹\\Desktop\\ssm\\SpringBoot课堂笔记\\SpringBoot01：Hello,World！.assets\\1595409428560.png"));

        helper.setTo("1710841251@qq.com");
        helper.setFrom("1710841251@qq.com");

        javaMailSender.send(mimeMessage);

    }

}

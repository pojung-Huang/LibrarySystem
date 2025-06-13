package tw.ispan.librarysystem.controller.captcha;

import com.google.code.kaptcha.Producer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
@RequestMapping("/api/captcha")
@CrossOrigin(origins = "http://localhost:3000") // 根據你的 Nuxt 前端開發網址
public class CaptchaController {

    @Autowired
    private Producer captchaProducer;

    @GetMapping("/m2")
    public void m2() throws IOException {
        System.out.println("OK2");
    }

    @GetMapping("/m1")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");

        // 產生驗證碼文字
        String captchaText = captchaProducer.createText();
        // 存入 Session 供後續驗證使用
        request.getSession().setAttribute("captcha", captchaText);

        // 產生驗證碼的圖ㄆ
        BufferedImage captchaImage = captchaProducer.createImage(captchaText);
        ImageIO.write(captchaImage, "jpg", response.getOutputStream());
    }
}

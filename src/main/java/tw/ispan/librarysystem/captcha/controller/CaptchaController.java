package tw.ispan.librarysystem.captcha.controller;

import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
@RequestMapping("/api/captcha")
@CrossOrigin(origins = "http://localhost:3000")
public class CaptchaController {

    @Autowired
    private Producer captchaProducer;

    @GetMapping("/api/captcha")
    public void getCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String captchaText = captchaProducer.createText();
        BufferedImage captchaImage = captchaProducer.createImage(captchaText);

        // 儲存驗證碼到 session，前端提交時可以比對
        request.getSession().setAttribute("captcha", captchaText);

        // 回傳圖片
        response.setContentType("image/jpeg");
        ImageIO.write(captchaImage, "jpg", response.getOutputStream());
    }
}

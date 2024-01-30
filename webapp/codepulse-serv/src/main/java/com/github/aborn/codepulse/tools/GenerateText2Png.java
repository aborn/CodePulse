package com.github.aborn.codepulse.tools;

import com.alibaba.fastjson2.JSONObject;
import com.github.aborn.codepulse.oauth2.ThirdOauthLoginController;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 将图片转成文字，生成粥享记的图片
 * 地址： https://www.bejson.com/image/text2img/
 * @author aborn (jiangguobao)
 * @date 2024/01/30 10:26
 */
@Slf4j
public class GenerateText2Png {

    public static void main(String[] args) {
        // 请求接口参数
        String api = "https://www.bejson.com/Bejson/Api/Image/text2img";
        Map<String, Object> formData = new HashMap<>();
        String dateValue = "20240126";
        String imageFormat = "png";
        formData.put("input", "粥享记 " + dateValue);
        formData.put("width", "600"); // 图片宽度
        formData.put("height", "250"); // 图片高度
        formData.put("bgcolor", "#0f4c81"); // 背景颜色
        formData.put("fontcolor", "#ffffff"); // 字体颜色
        formData.put("fontsize", "40"); // 字体大小
        formData.put("font", "msyh"); // 字段类型
        formData.put("x", "100"); // x、y座标
        formData.put("y", "150");

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(api))
                .header("Accept", "application/json")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(ThirdOauthLoginController.ofForm(formData))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                JSONObject jsonObject = JSONObject.parseObject(response.body());
                int code = jsonObject.getIntValue("code");
                String msg = jsonObject.getString("msg");
                if (code != 200) {
                    log.info("生成图片失败，原因调用接口失败：" + msg);
                }
                JSONObject data = jsonObject.getJSONObject("data");
                String fileBase64 = data.getString("file");
                String fileName = String.format("/Users/aborn/Downloads/zxj%s.%s", dateValue, imageFormat);
                String base64Image = fileBase64.split(",")[1];
                byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
                BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
                File outputfile = new File(fileName);
                ImageIO.write(image, imageFormat, outputfile);
                log.info("生成成功，图片地址：" + fileName);
            } else {
                log.info("Get accessToken info from github error!");
                log.error("Get accessToken Status code: {}", response.statusCode());
                log.error("Res Body: {}", response.body());
            }
        } catch (InterruptedException e) {
            log.error("getAccessToken exception. msg={}", e.getMessage());
        } catch (IOException ioException) {
            log.error("getAccessToken exception. msg={}", ioException.getMessage());
        }
    }
}

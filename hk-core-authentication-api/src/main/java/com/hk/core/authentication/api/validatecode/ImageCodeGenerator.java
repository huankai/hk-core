package com.hk.core.authentication.api.validatecode;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * 图片验证码
 *
 * @author kevin
 * @date 2018-07-27 14:53
 */
public class ImageCodeGenerator extends AbstractValidateCodeGenerator implements ValidateCodeGenerator<ImageCode> {

    /**
     * 宽度
     */
    private int width;

    /**
     * 高度
     */
    private int height;

    public ImageCodeGenerator() {
        this(200, 50);
    }

    public ImageCodeGenerator(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public ImageCode generate() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics g = image.getGraphics();

        Random random = new Random();

        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        g.setFont(new Font("Times New Roman", Font.ITALIC, 20));
        g.setColor(getRandColor(160, 200));
        for (int i = 0; i < 155; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }

        StringBuilder sRand = new StringBuilder();
        for (int i = 0; i < getCodeLength(); i++) {
            String rand = String.valueOf(random.nextInt(10));
            sRand.append(rand);
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            g.drawString(rand, 13 * i + 6, 16);
        }
        g.dispose();
        return new ImageCode(image, sRand.toString(), getExpireSends());
    }

    /**
     * 生成随机背景条纹
     */
    private Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255) {
            fc = 255;
        }
        if (bc > 255) {
            bc = 255;
        }
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

}

package com.hk.core.authentication.api.validatecode;

import lombok.Getter;

import java.awt.image.BufferedImage;

/**
 * 图片验证码
 *
 * @author: kevin
 * @date 2018-07-26 15:08
 */
@SuppressWarnings("serial")
public class ImageCode extends ValidateCode {

    /**
     * 图片验证码
     */
    @Getter
    private BufferedImage image;

    public ImageCode(BufferedImage image, String code, int expireIn) {
        super(code, expireIn);
        this.image = image;
    }


}

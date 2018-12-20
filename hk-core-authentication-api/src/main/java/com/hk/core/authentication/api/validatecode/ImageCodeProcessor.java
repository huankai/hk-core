package com.hk.core.authentication.api.validatecode;

import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;

/**
 * 图片验证码生成器
 *
 * @author kevin
 * @date 2018-07-27 14:46
 */
public class ImageCodeProcessor extends AbstractValidateCodeProcessor<ImageCode> {

    public ImageCodeProcessor(ValidateCodeGenerator<ImageCode> validateCodeGenerator, String codeParameterName) {
        super(validateCodeGenerator, codeParameterName);
    }

    @Override
    protected void send(ImageCode imageCode, ServletWebRequest request) throws Exception {
        ImageIO.write(imageCode.getImage(), "JPEG", request.getResponse().getOutputStream());
    }
}

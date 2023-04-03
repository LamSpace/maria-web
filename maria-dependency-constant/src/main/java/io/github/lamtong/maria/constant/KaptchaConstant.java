/*
Copyright 2023 the original author, Lam Tong

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package io.github.lamtong.maria.constant;

/**
 * 校验码生成器配置相关常量定义.
 *
 * @author Lam Tong
 * @version 0.0.1
 * @since 0.0.1
 */
public final class KaptchaConstant {

    /**
     * 校验码图片宽度属性名
     */
    public static final String IMAGE_WIDTH = "kaptcha.image.width";

    /**
     * 校验码图片宽度值
     */
    public static final String IMAGE_WIDTH_VALUE = "120";

    /**
     * 校验码图片高度属性名
     */
    public static final String IMAGE_HEIGHT = "kaptcha.image.height";

    /**
     * 校验码图片高度值
     */
    public static final String IMAGE_HEIGHT_VALUE = "40";

    /**
     * 校验码文字属性名
     */
    public static final String TEXT_CHAR_STRING = "kaptcha.textproducer.char.string";

    /**
     * 校验码文字字符串
     */
    public static final String TEXT_CHAR_STRING_VALUE = "0123456789";

    /**
     * 校验码文字长度属性名
     */
    public static final String TEXT_CHAR_LENGTH = "kaptcha.textproducer.char.length";

    /**
     * 校验码文字长度值
     */
    public static final String TEXT_CHAR_LENGTH_VALUE = "4";

    private KaptchaConstant() {
    }

}

package com.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @auther xzl on 13:28 2018/4/12
 *
 * 发送短信内容参数
 */
@Getter
@Setter
public class SmsSendParam {
    private String number;//手机号
    private int user_id;//识别号
    private String[] text_param;//短信内容
}

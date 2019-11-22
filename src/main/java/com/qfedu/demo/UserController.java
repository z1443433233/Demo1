package com.qfedu.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import java.util.Random;

@Controller
public class UserController {

    @RequestMapping("/sendCode")
    @ResponseBody
    public String sendCode(String phone){
        if(phone==null){
            return "error";
        }
        //1、 生成验证码
        String verifyCode=genCode(4);
        //2 存储验证码
        Jedis jedis =new Jedis("192.168.200.128",6379);

        String phonekey = "phone_num:"+phone;
        jedis.setex(phonekey,20, verifyCode);
        jedis.close();
        //3 发送验证码
        System.out.println(verifyCode);
        //4 返回
        return "success";
    }


    private  String genCode(int code_length){
        String code="";
        for (int i = 0; i < code_length; i++) {
            int num=  new Random().nextInt(10);
            code+=num;
        }
        return code;
    }

    @RequestMapping("/verifiCode")
    @ResponseBody
    public String verifiCode(String phone,String verify_code){
        //判断参数
        if(verify_code==null){
            return "error";
        }

        //验证
        Jedis jedis =new Jedis("192.168.200.128",6379);
        String phonekey = jedis.get("phone_num:"+phone);

        System.out.println(phonekey);

        if(verify_code.equals(phonekey)){
            return "success";
        }
        jedis.close();
        return "error";
    }
}

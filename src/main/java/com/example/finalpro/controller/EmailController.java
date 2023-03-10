package com.example.finalpro.controller;

import com.example.finalpro.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class EmailController {
    @Autowired
    private EmailService emailService;

    @GetMapping("/sendEmailCode")
    public String sendEmailCode(String email){

        String code  = "";
        Random r = new Random();
        code += r.nextInt(10);
        code += r.nextInt(10);
        code += r.nextInt(10);
        code += r.nextInt(10);
        code += r.nextInt(10);
        code += r.nextInt(10);

        emailService.sendEmail(email, "[T-CATCH] 비밀번호 재설정 인증코드", code+"를 입력해주세요.");
        return code;
    }

}

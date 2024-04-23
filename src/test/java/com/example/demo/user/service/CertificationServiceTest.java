package com.example.demo.user.service;

import com.example.demo.mock.FakeMailSender;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CertificationServiceTest {

    @Test
    public void 이메일과_컨텐츠가_제대로_만들어지는지_확인한다(){
        //given
        FakeMailSender fakeMailSender = new FakeMailSender();
        CertificationService certificationService = new CertificationService(fakeMailSender);
        //when
        certificationService.send("whssodi@gmail.com", 1, "aaaaaa-aaa-aa");
        //then
        assertThat(fakeMailSender.email).isEqualTo("whssodi@gmail.com");
        assertThat(fakeMailSender.title).isEqualTo("Please certify your email address");
        assertThat(fakeMailSender.content).isEqualTo("Please click the following link to certify your email address: http://localhost:8080/api/users/1/verify?certificationCode=aaaaaa-aaa-aa");

    }

}
package com.example.demo.user.controller.response;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MyProfileResponseTest {

    @Test
    public void User로_응답을_생성한다() {
        //given
        User user = User.builder()
                .id(1L)
                .email("whssodi@gmail.com")
                .nickname("whssodi")
                .address("seoul")
                .certificationCode("aaaaaaaaa-aaaaaaaaa-aaaaaaaaa")
                .lastLoginAt(100L)
                .status(UserStatus.ACTIVE)
                .build();
        //when
        MyProfileResponse myProfileResponse = MyProfileResponse.from(user);

        //then
        assertThat(myProfileResponse.getId()).isEqualTo(1L);
        assertThat(myProfileResponse.getEmail()).isEqualTo("whssodi@gmail.com");
        assertThat(myProfileResponse.getNickname()).isEqualTo("whssodi");
        assertThat(myProfileResponse.getAddress()).isEqualTo("seoul");
        assertThat(myProfileResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(myProfileResponse.getLastLoginAt()).isEqualTo(100L);
    }

}

package com.example.demo.user.controller.response;

import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserResponseTest {


    @Test
    public void User로_응답을_생성한다() {
    	// given
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
        UserResponse userResponse = UserResponse.from(user);

        // then
        assertThat(userResponse.getId()).isEqualTo(1L);
        assertThat(userResponse.getEmail()).isEqualTo("whssodi@gmail.com");
        assertThat(userResponse.getNickname()).isEqualTo("whssodi");
        assertThat(userResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(userResponse.getLastLoginAt()).isEqualTo(100L);
    }





}

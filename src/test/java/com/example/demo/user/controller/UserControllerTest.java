package com.example.demo.user.controller;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestContainer;
import com.example.demo.user.controller.response.MyProfileResponse;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


public class UserControllerTest {

    @Test
    void 사용자는_특정_유저의_개인정보가_없는_정보를_찾을_수_있다() throws Exception {
        //given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("whssodi@gmail.com")
                .nickname("whssodi")
                .address("Seoul")
                .certificationCode("aaaaaaaaa-aaaaaaaaa-aaaaaaaaa")
                .lastLoginAt(100L)
                .status(UserStatus.ACTIVE)
                .build());
        //when
        ResponseEntity<UserResponse> result = testContainer.userController.getUserById(1);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat((result.getBody().getId())).isEqualTo(1L);
        assertThat((result.getBody().getEmail())).isEqualTo("whssodi@gmail.com");
        assertThat((result.getBody().getNickname())).isEqualTo("whssodi");
        assertThat((result.getBody().getStatus())).isEqualTo(UserStatus.ACTIVE);
        assertThat((result.getBody().getLastLoginAt())).isEqualTo(100L);

    }

    @Test
    void 존재하지_않는_유저ID로_api호출하면_404응답을_받는다() {
        //given
        TestContainer testContainer = TestContainer.builder()
                .build();

        //when
        //then
        assertThatThrownBy(() -> {
            testContainer.userController.getUserById(1);
        }).isInstanceOf(ResourceNotFoundException.class);


    }

    @Test
    void 사용자는_인증코드로_계정을_활성화_할_수_있다() throws Exception {
        //given
        TestContainer testContainer = TestContainer.builder()
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("whssodi@gmail.com")
                .nickname("whssodi")
                .address("Seoul")
                .certificationCode("aaaaaaaaa-aaaaaaaaa-aaaaaaaaa")
                .lastLoginAt(100L)
                .status(UserStatus.PENDING)
                .build());
        //when
        ResponseEntity<Void> result = testContainer.userController.verifyEmail(1, "aaaaaaaaa-aaaaaaaaa-aaaaaaaaa" );

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(302));
        assertThat(testContainer.userRepository.getById(1L).getStatus()).isEqualTo(UserStatus.ACTIVE);


    }

    @Test
    void 사용자는_내_정보를_불러올_때_개인정보_주소도_가져올_수_있다() throws Exception {
        //given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> 1678530673958L)
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("whssodi@gmail.com")
                .nickname("whssodi")
                .address("Seoul")
                .certificationCode("aaaaaaaaa-aaaaaaaaa-aaaaaaaaa")
                .lastLoginAt(100L)
                .status(UserStatus.ACTIVE)
                .build());

        ResponseEntity<MyProfileResponse> result = testContainer.userController.getMyInfo("whssodi@gmail.com");

        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat((result.getBody().getEmail())).isEqualTo("whssodi@gmail.com");
        assertThat((result.getBody().getNickname())).isEqualTo("whssodi");
        assertThat((result.getBody().getAddress())).isEqualTo("Seoul");
        assertThat((result.getBody().getStatus())).isEqualTo(UserStatus.ACTIVE);
        assertThat((result.getBody().getLastLoginAt())).isEqualTo(100L);

    }

    @Test
    void 사용자는_내_정보를_수정_할_수_있다() throws Exception {
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> 1678530673958L)
                .build();
        testContainer.userRepository.save(User.builder()
                .id(1L)
                .email("whssodi@gmail.com")
                .nickname("whssodi")
                .address("Seoul")
                .certificationCode("aaaaaaaaa-aaaaaaaaa-aaaaaaaaa")
                .lastLoginAt(100L)
                .status(UserStatus.ACTIVE)
                .build());
        //when
        ResponseEntity<MyProfileResponse> result = testContainer.userController.updateMyInfo("whssodi@gmail.com", UserUpdate.builder()
                .address("Busan")
                .nickname("wwwwwww").build());

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat((result.getBody().getEmail())).isEqualTo("whssodi@gmail.com");
        assertThat((result.getBody().getNickname())).isEqualTo("wwwwwww");
        assertThat((result.getBody().getAddress())).isEqualTo("Busan");
        assertThat((result.getBody().getStatus())).isEqualTo(UserStatus.ACTIVE);
        assertThat((result.getBody().getLastLoginAt())).isEqualTo(100L);
    }




    }

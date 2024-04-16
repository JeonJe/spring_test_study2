package com.example.demo.user.domain;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.infrastructure.SystemClockHolder;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class UserTest {

    @Test
    public void User는_UserCreate_객체로_생성할_수_있다() {
        //given
        UserCreate userCreate = UserCreate.builder()
                .email("whssodi@gmail.com")
                .address("Seoul")
                .nickname("whssodi")
                .build();
        //when
        User user = User.from(userCreate, new TestUuidHolder("aaaa-aaa"));
        //then
        assertThat(user.getId()).isNull();
        assertThat(user.getAddress()).isEqualTo("Seoul");
        assertThat(user.getNickname()).isEqualTo("whssodi");
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(user.getCertificationCode()).isEqualTo("aaaa-aaa");
    }
    @Test
    public void User는_UserUpdate_객체로_수정할_수_있다() {
        //given
        User user = User.builder()
                .id(1L)
                .email("whssodi@gmail.com")
                .nickname("whssodi")
                .address("Seoul")
                .certificationCode("aaaaaaaaa-aaaaaaaaa-aaaaaaaaa")
                .lastLoginAt(100L)
                .status(UserStatus.ACTIVE)
                .build();

        UserUpdate userUpdate = UserUpdate.builder()
                .address("Seoul2")
                .nickname("whssodi2")
                .build();

        //when
        user = user.update(userUpdate);

        //then
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getAddress()).isEqualTo("Seoul2");
        assertThat(user.getNickname()).isEqualTo("whssodi2");
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.getLastLoginAt()).isEqualTo(100L);
        assertThat(user.getCertificationCode()).isEqualTo("aaaaaaaaa-aaaaaaaaa-aaaaaaaaa");

    }

    @Test
    public void User는_로그인_할_수있고_로그인시_마지막_로그인_시간이_변경된다() {
        //given
        User user = User.builder()
                .id(1L)
                .email("whssodi@gmail.com")
                .nickname("whssodi")
                .address("Seoul")
                .certificationCode("aaaaaaaaa-aaaaaaaaa-aaaaaaaaa")
                .lastLoginAt(100L)
                .status(UserStatus.ACTIVE)
                .build();

        //when
        user = user.login(new TestClockHolder(1678530673958L));
        //then
        assertThat(user.getLastLoginAt()).isEqualTo(1678530673958L);

    }

    @Test
    public void User는_유효한_인증코드로_계정을_활성화_할_수_있다() {
        //given
        User user = User.builder()
                .id(1L)
                .email("whssodi@gmail.com")
                .nickname("whssodi")
                .address("Seoul")
                .certificationCode("aaaaaaaaa-aaaaaaaaa-aaaaaaaaa")
                .lastLoginAt(100L)
                .status(UserStatus.PENDING)
                .build();
        //when
        user = user.certificate("aaaaaaaaa-aaaaaaaaa-aaaaaaaaa");

        //then
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getAddress()).isEqualTo("Seoul");
        assertThat(user.getNickname()).isEqualTo("whssodi");
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.getLastLoginAt()).isEqualTo(100L);
        assertThat(user.getCertificationCode()).isEqualTo("aaaaaaaaa-aaaaaaaaa-aaaaaaaaa");


    }

    @Test
    public void User_는_잘못된_인증_코드로_계정을_활성화_시도하면_에러를_던진다() {
        //given
        User user = User.builder()
                .id(1L)
                .email("whssodi@gmail.com")
                .nickname("whssodi")
                .address("Seoul")
                .certificationCode("aaaaaaaaa-aaaaaaaaa-aaaaaaaaa")
                .lastLoginAt(100L)
                .status(UserStatus.PENDING)
                .build();
        //when
        assertThatThrownBy(() ->
            user.certificate("bb")
        ).isInstanceOf(CertificationCodeNotMatchedException.class);
    }



}

package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.infrastructure.UserEntity;
import com.example.demo.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
        @Sql(value = "/sql/user-service-test-data.sql", executionPhase =  Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

})
class UserServiceTest {
    @Autowired
    private UserService userService;
    @MockBean
    private JavaMailSender javaMailSender;
    @Test
    void getByEmail_은_Active_상태인_유저를_찾아올_수_있다(){
        //given
        String email = "whssodi@gmail.com";
        //when
        UserEntity userEntity = userService.getByEmail(email);

        //then
        assertThat(userEntity.getNickname()).isEqualTo("whssodi");

    }
    void getByEmail_은_PENDING_상태인_유저를_찾아올_수_없다(){
        //given
        String email = "whssodi2@gmail.com";
        //when
        //then
        assertThatThrownBy(() -> {
            UserEntity result = userService.getByEmail(email);
        }).isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    void getBtyId_은_Active_상태인_유저를_찾아올_수_있다(){
        //given

        //when
        UserEntity userEntity = userService.getById(1);

        //then
        assertThat(userEntity.getNickname()).isEqualTo("whssodi");

    }
    void getById_은_PENDING_상태인_유저를_찾아올_수_없다(){
        //given
        //when
        //then
        assertThatThrownBy(() -> {
            UserEntity result = userService.getById(2);
        }).isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    void userCreateDto를_이용하여_유저를_생성할_수_있다(){
        //given
        UserCreate userCreate = UserCreate.builder()
                .email("whssodi@gmail.com")
                .address("Seoul")
                .nickname("whssodi")
                .build();


        BDDMockito.doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));
        //when
        UserEntity userEntity = userService.create(userCreate);

        //then
        assertThat(userEntity.getId()).isNotNull();
        assertThat(userEntity.getStatus()).isEqualTo(UserStatus.PENDING);

    }

    @Test
    void userUpdateDto를_이용하여_유저를_수정할_수_있다(){
        //given
        UserUpdate updateCreateDto = UserUpdate.builder()
                .address("Incheon")
                .nickname("whssodi33")
                .build();

        //when
        UserEntity userEntity = userService.update(1, updateCreateDto);
        //then
        assertThat(userEntity).isNotNull();
        assertThat(userEntity.getAddress()).isEqualTo("Incheon");
        assertThat(userEntity.getNickname()).isEqualTo("whssodi33");

    }

    @Test
    void user_로그인_시_마지막_로그인_시간이_변경된다(){
        //given
        //when
        userService.login(1);
        //then
        UserEntity userEntity = userService.getById(1);
        assertThat(userEntity).isNotNull();
        assertThat(userEntity.getLastLoginAt()).isGreaterThan(0L);
    }

    @Test
    void PENDING인_유저는_인증_코드로_ACTIVE_시킨다(){
        //given
        //when
        userService.verifyEmail(2, "aaaaaa-aaa-aa");
        //then
        UserEntity userEntity = userService.getById(2);
        assertThat(userEntity).isNotNull();
        assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void PENDING인_유저는_잘못된_인증_코드로_ACTIVE_시킬_수_없다(){
        //given
        //when
        //then
        assertThatThrownBy(() -> {
            userService.verifyEmail(2, "aaaaaa-aaa-bb");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }






}
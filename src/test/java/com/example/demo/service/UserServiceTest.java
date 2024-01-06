package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.dto.UserCreateDto;
import com.example.demo.model.dto.UserUpdateDto;
import com.example.demo.repository.UserEntity;
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
    void getByEmaul_은_Active_상태인_유저를_찾아올_수_있다(){
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
        UserCreateDto userCreateDto = UserCreateDto.builder()
                .email("whssodi@gmail.com")
                .address("Seoul")
                .nickname("whssodi")
                .build();

//        BDDMockito.doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));
        //when
//        UserEntity userEntity = userService.create(userCreateDto);
//
//        //then
//        assertThat(userEntity.getId()).isNotNull();
//        assertThat(userEntity.getStatus()).isEqualTo("PENDING");

    }

    @Test
    void userCreateDto를_이용하여_유저를_수정할_수_있다(){
        //given
        UserUpdateDto updateCreateDto = UserUpdateDto.builder()
                .address("Incheon")
                .nickname("whssodi33")
                .build();

        //when
        UserEntity userEntity = userService.update(1, updateCreateDto);
        //then
        assertThat(userEntity.getAddress()).isEqualTo("Incheon");

    }


}
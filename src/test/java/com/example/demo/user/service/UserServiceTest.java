package com.example.demo.user.service;

import com.example.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.FakeMailSender;
import com.example.demo.mock.FakeUserRepository;
import com.example.demo.mock.TestClockHolder;
import com.example.demo.mock.TestUuidHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserCreate;
import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.BeforeEach;
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

class UserServiceTest {
    private UserService userService;

    //TestFixture
    @BeforeEach
    void init() {
        FakeMailSender fakeMailSender = new FakeMailSender();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();

        this.userService = UserService.builder()
                .uuidHolder(new TestUuidHolder("aaa-aa-a")) //고정된 값만 내려주는 stub으로 대체
                .clockHolder(new TestClockHolder(1678530673958L)) //고정된 값을
                .userRepository(fakeUserRepository)
                .certificationService(new CertificationService(fakeMailSender)) //의존 관게
                .build();

        fakeUserRepository.save(User.builder()
                .id(1L)
                .email("whssodi@gmail.com")
                .nickname("whssodi")
                .address("Seoul")
                .certificationCode("aaaaaa-aaa-aa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(0L)
                .build()
        );

        fakeUserRepository.save(User.builder()
                .id(2L)
                .email("'whssodi2@gmail.com'")
                .nickname("whssodi2")
                .address("Seoul")
                .certificationCode("aaaaaa-aaa-aa")
                .status(UserStatus.PENDING)
                .lastLoginAt(0L)
                .build()
        );
    }

    @Test
    void getByEmail_은_Active_상태인_유저를_찾아올_수_있다() {
        //given
        String email = "whssodi@gmail.com";
        //when
        User user = userService.getByEmail(email);

        //then
        assertThat(user.getNickname()).isEqualTo("whssodi");

    }

    void getByEmail_은_PENDING_상태인_유저를_찾아올_수_없다() {
        //given
        String email = "whssodi2@gmail.com";
        //when
        //then
        assertThatThrownBy(() -> {
            User result = userService.getByEmail(email);
        }).isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    void getBtyId_은_Active_상태인_유저를_찾아올_수_있다() {
        //given

        //when
        User user = userService.getById(1);

        //then
        assertThat(user.getNickname()).isEqualTo("whssodi");

    }

    void getById_은_PENDING_상태인_유저를_찾아올_수_없다() {
        //given
        //when
        //then
        assertThatThrownBy(() -> {
            User result = userService.getById(2);
        }).isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    void userCreateDto를_이용하여_유저를_생성할_수_있다() {
        //given
        UserCreate userCreate = UserCreate.builder()
                .email("whssodi@gmail.com")
                .address("Seoul")
                .nickname("whssodi")
                .build();

        //when
        User user = userService.create(userCreate);

        //then
        assertThat(user.getId()).isNotNull();
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(user.getCertificationCode()).isEqualTo("aaa-aa-a"); //테스트가 가능해졌다.

    }

    @Test
    void userUpdateDto를_이용하여_유저를_수정할_수_있다() {
        //given
        UserUpdate updateCreateDto = UserUpdate.builder()
                .address("Incheon")
                .nickname("whssodi33")
                .build();

        //when
        User user = userService.update(1, updateCreateDto);
        //then
        assertThat(user).isNotNull();
        assertThat(user.getAddress()).isEqualTo("Incheon");
        assertThat(user.getNickname()).isEqualTo("whssodi33");

    }

    @Test
    void user_로그인_시_마지막_로그인_시간이_변경된다() {
        //given
        //when
        userService.login(1);
        //then
        User user = userService.getById(1);
        assertThat(user).isNotNull();
        assertThat(user.getLastLoginAt()).isEqualTo(1678530673958L);

    }

    @Test
    void PENDING인_유저는_인증_코드로_ACTIVE_시킨다() {
        //given
        //when
        userService.verifyEmail(2, "aaaaaa-aaa-aa");
        //then
        User user = userService.getById(2);
        assertThat(user).isNotNull();
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    void PENDING인_유저는_잘못된_인증_코드로_ACTIVE_시킬_수_없다() {
        //given
        //when
        //then
        assertThatThrownBy(() -> {
            userService.verifyEmail(2, "aaaaaa-aaa-bb");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }


}
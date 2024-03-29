package com.example.demo.repositroy;

import com.example.demo.model.UserStatus;
import com.example.demo.repository.UserEntity;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest(showSql = true)
@TestPropertySource("classpath:test-application.properties")
@Sql("/sql/user-repository-test-data.sql")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByIdAndStatus_로_유저_데이터를_찾아올_수_있다(){
        //given

        //when

        Optional<UserEntity> result = userRepository.findByIdAndStatus(1,UserStatus.ACTIVE);
        //then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void findByIdAndStatus_는_데이터가_없으면_Optional_empty를_내려준다(){
        //given

        //when

        Optional<UserEntity> result = userRepository.findByIdAndStatus(1,UserStatus.PENDING);
        //then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    void findByEmailAndStatus_로_유저_데이터를_찾아올_수_있다(){
        //given

        //when

        Optional<UserEntity> result = userRepository.findByEmailAndStatus("whssodi@gmail.com",UserStatus.ACTIVE);
        //then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    void findByEmailAndStatus_는_데이터가_없으면_Optional_empty를_내려준다(){
        //given

        //when

        Optional<UserEntity> result = userRepository.findByEmailAndStatus("whssodi@gmail.com",UserStatus.PENDING);
        //then
        assertThat(result.isEmpty()).isTrue();
    }


}

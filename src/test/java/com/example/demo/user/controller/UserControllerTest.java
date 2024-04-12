package com.example.demo.user.controller;

import com.example.demo.user.domain.UserStatus;
import com.example.demo.user.domain.UserUpdate;
import com.example.demo.user.infrastructure.UserEntity;
import com.example.demo.user.infrastructure.UserJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
        @Sql(value = "/sql/user-controller-test-data.sql", executionPhase =  Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private ObjectMapper objectMapper;
    @Test
    void 사용자는_특정_유저의_개인정보가_없는_정보를_찾을_수_있다() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("whssodi@gmail.com"))
                .andExpect(jsonPath("$.address").doesNotExist());;
    }

    @Test
    void 존재하지_않는_유저ID로_api호출하면_404응답을_받는다() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/api/users/1231231"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Users에서 ID 1231231를 찾을 수 없습니다."));
    }

    @Test
    void 사용자는_인증코드로_계정을_활성화_할_수_있다() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/api/users/2/verify")
                        .queryParam("certificationCode", "aaaaaa-aaa-aa"))
                .andExpect(status().isFound());

        UserEntity userEntity = userJpaRepository.findById(2L).get();
        assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);

    }

    @Test
    void 사용자는_내_정보를_불러올_때_개인정보_주소도_가져올_수_있다() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/api/users/me")
                        .header("EMAIL", "whssodi@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("whssodi@gmail.com"))
                .andExpect(jsonPath("$.nickname").value("whssodi"))
                .andExpect(jsonPath("$.address").value("Seoul"));
    }

    @Test
    void 사용자는_내_정보를_수정_할_수_있다() throws Exception {
        //given
        UserUpdate userUpdate = UserUpdate.builder()
                .nickname("modified")
                .address("sssssss")
                .build();

        //when
        //then
        mockMvc.perform(put("/api/users/me")
                        .header("EMAIL", "whssodi@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("whssodi@gmail.com"))
                .andExpect(jsonPath("$.nickname").value("modified"))
                .andExpect(jsonPath("$.address").value("sssssss"));
    }




    }

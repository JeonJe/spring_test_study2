package com.example.demo.post.controller;

import com.example.demo.mock.TestContainer;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.user.controller.response.UserResponse;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class PostCreateControllerTest {


    @Test
    void 게시물_생성() throws Exception {
        //given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> 1678530673958L)
                .uuidHolder(() -> "aaaaaaaaa-aaaaaaaaa-aaaaaaaaa")
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

        PostCreate postCreate = PostCreate.builder()
                .writerId(1)
                .content("hello")
                .build();
        //when
        ResponseEntity<PostResponse> result = testContainer.postCreateController.createPost(postCreate);
        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(result.getBody()).isNotNull();
        assertThat((result.getBody().getWriter().getId())).isEqualTo(1L);
        assertThat((result.getBody().getCreatedAt())).isEqualTo(1678530673958L);
        assertThat((result.getBody().getContent())).isEqualTo("hello");



    }
}

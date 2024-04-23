package com.example.demo.post.controller;

import com.example.demo.common.domain.exception.ResourceNotFoundException;
import com.example.demo.mock.TestContainer;
import com.example.demo.post.controller.response.PostResponse;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PostControllerTest {

    @Test
    void 사용자는_게시물_단건_조회_가능() throws Exception {
        //given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> 1678530673958L)
                .uuidHolder(() -> "aaaaaaaaa-aaaaaaaaa-aaaaaaaaa")
                .build();

        User user = User.builder()
                .id(1L)
                .email("whssodi@gmail.com")
                .nickname("whssodi")
                .address("Seoul")
                .certificationCode("aaaaaaaaa-aaaaaaaaa-aaaaaaaaa")
                .lastLoginAt(100L)
                .status(UserStatus.PENDING)
                .build();
        testContainer.userRepository.save(user);
        Post post = Post.builder()
                .writer(user)
                .content("hello")
                .createdAt(100L)
                .build();

        testContainer.postRepository.save(post);
        //when
        ResponseEntity<PostResponse> result = testContainer.postController.getPostById(1L);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat((result.getBody().getContent())).isEqualTo("hello");
        assertThat((result.getBody().getWriter().getNickname())).isEqualTo("whssodi");
        assertThat((result.getBody().getCreatedAt())).isEqualTo(100L);

    }

    @Test
    void 사용자는_미존재게시글_조회불가() throws Exception {
        //given
        TestContainer testContainer = TestContainer.builder()
                .build();

        //when
        //then
        assertThatThrownBy(() -> {
            testContainer.postController.getPostById(1);
        }).isInstanceOf(ResourceNotFoundException.class);

    }



    @Test
    void 사용자는_게시물_수정할_수_있다() throws Exception {

        //given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> 200L)
                .uuidHolder(() -> "aaaaaaaaa-aaaaaaaaa-aaaaaaaaa")
                .build();

        User user = User.builder()
                .id(1L)
                .email("whssodi@gmail.com")
                .nickname("whssodi")
                .address("Seoul")
                .certificationCode("aaaaaaaaa-aaaaaaaaa-aaaaaaaaa")
                .lastLoginAt(100L)
                .status(UserStatus.PENDING)
                .build();
        testContainer.userRepository.save(user);
        Post post = Post.builder()
                .writer(user)
                .content("hello")
                .createdAt(100L)
                .build();

        testContainer.postRepository.save(post);
        //when
        ResponseEntity<PostResponse> result = testContainer.postController.updatePost(1L, PostUpdate.builder()
                .content("updated")
                .build());

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat((result.getBody().getContent())).isEqualTo("updated");
        assertThat((result.getBody().getWriter().getNickname())).isEqualTo("whssodi");
        assertThat((result.getBody().getCreatedAt())).isEqualTo(100L);
        assertThat((result.getBody().getModifiedAt())).isEqualTo(200L);

    }

}

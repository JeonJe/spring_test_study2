package com.example.demo.post.service;

import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.post.infrastructure.PostEntity;
import com.example.demo.post.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
        @Sql(value = "/sql/post-service-test-data.sql", executionPhase =  Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
class PostServiceTest {
    @Autowired
    private PostService postService;
    @Test
    void getById_존재하는_게시물을_가져온다(){
        //given
        int id = 1;
        //when
        PostEntity postEntity = postService.getPostById(1);

        //then
        assertThat(postEntity).isNotNull();
        assertThat(postEntity.getContent()).isEqualTo("helloword");
        assertThat(postEntity.getWriter().getEmail()).isEqualTo("whssodi@gmail.com");


    }

    @Test
    void postCreateDto를_이용하여_게시물를_생성할_수_있다(){
        //given
        PostCreate postCreate = PostCreate.builder()
                .writerId(1)
                .content("helloword")
                .build();

        //when
        PostEntity postEntity = postService.create(postCreate);

        //then
        assertThat(postEntity.getId()).isNotNull();
        assertThat(postEntity.getContent()).isEqualTo("helloword");
        assertThat(postEntity.getCreatedAt()).isGreaterThan(0L);

    }

    @Test
    void postUpdateDto를_이용하여_게시물를_수정할_수_있다(){
        //given
        PostUpdate postUpdate = PostUpdate.builder()
                .content("helloword22")
                .build();

        //when
        PostEntity postEntity = postService.update(1L, postUpdate);

        //then
        assertThat(postEntity.getId()).isNotNull();
        assertThat(postEntity.getContent()).isEqualTo("helloword22");
        assertThat(postEntity.getModifiedAt()).isGreaterThan(0L);

    }




}
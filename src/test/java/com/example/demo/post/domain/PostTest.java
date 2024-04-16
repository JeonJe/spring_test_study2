package com.example.demo.post.domain;

import com.example.demo.mock.TestClockHolder;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PostTest {

    @Test
    public void PostCreate로_게시물을_만들_수_있다() {
        //given
        PostCreate postCreate = PostCreate.builder()
                .writerId(1)
                .content("hello")
                .build();

        User writer = User.builder()
                .email("whssodi@gmail.com")
                .nickname("whssodi")
                .address("seoul")
                .certificationCode("aaaaaaaaa-aaaaaaaaa-aaaaaaaaa")
                .status(UserStatus.ACTIVE)
                .build();

        //when
        Post post = Post.from(writer, postCreate, new TestClockHolder(1678530673958L));

        //then
        assertThat(post.getContent()).isEqualTo("hello");
        assertThat(post.getWriter().getEmail()).isEqualTo("whssodi@gmail.com");
        assertThat(post.getWriter().getNickname()).isEqualTo("whssodi");
        assertThat(post.getWriter().getAddress()).isEqualTo("seoul");
        assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(post.getCreatedAt()).isEqualTo(1678530673958L);
        assertThat(post.getWriter().getCertificationCode()).isEqualTo("aaaaaaaaa-aaaaaaaaa-aaaaaaaaa");
    }

    @Test
    public void PostUpdate로_게시물을_수정할_수_있다() {
        //given
        PostUpdate postUpdate = PostUpdate.builder()
                .content("foobar")
                .build();

        User writer = User.builder()
                .email("whssodi@gmail.com")
                .nickname("whssodi")
                .address("seoul")
                .certificationCode("aaaaaaaaa-aaaaaaaaa-aaaaaaaaa")
                .status(UserStatus.ACTIVE)
                .build();

        Post post = Post.builder()
                .id(1L)
                .content("hellworld")
                .createdAt(1678530673958L)
                .modifiedAt(0L)
                .writer(writer)
                .build();

        //when
         post = post.update(postUpdate,  new TestClockHolder(1678530673958L));

        //then
        assertThat(post.getContent()).isEqualTo("foobar");
    }


}

package com.example.demo.post.controller.response;

import com.example.demo.post.domain.Post;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PostResponseTest {

    @Test
    public void Post_로_응답을_생성할_수_있다() {
        //given
        Post post = Post.builder()
                .content("helloword")
                .writer(User.builder()
                        .email("whssodi@gmail.com")
                        .nickname("whssodi")
                        .address("seoul")
                        .certificationCode("aaaaaaaaa-aaaaaaaaa-aaaaaaaaa")
                        .status(UserStatus.ACTIVE)
                        .build())
                .build();


        //when
        PostResponse postResponse = PostResponse.from(post);
        //then
        assertThat(postResponse.getContent()).isEqualTo("helloword");
        assertThat(postResponse.getWriter().getEmail()).isEqualTo("whssodi@gmail.com");
        assertThat(postResponse.getWriter().getNickname()).isEqualTo("whssodi");
        assertThat(postResponse.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

}

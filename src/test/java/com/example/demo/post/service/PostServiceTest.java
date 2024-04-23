package com.example.demo.post.service;

import com.example.demo.mock.*;
import com.example.demo.post.domain.Post;
import com.example.demo.post.domain.PostCreate;
import com.example.demo.post.domain.PostUpdate;
import com.example.demo.user.domain.User;
import com.example.demo.user.domain.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PostServiceTest {

    private PostServiceImpl postService;

    //TestFixture
    @BeforeEach
    void init() {
        FakePostRepository fakePostRepository = new FakePostRepository();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();

        this.postService = PostServiceImpl.builder()
                .postRepository(fakePostRepository)
                .userRepository(fakeUserRepository)
                .clockHolder(new TestClockHolder(1678530673958L))
                .build();
        User user1 = User.builder()
                .id(1L)
                .email("whssodi@gmail.com")
                .nickname("whssodi")
                .address("Seoul")
                .certificationCode("aaaaaa-aaa-aa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(0L)
                .build();

        User user2 = User.builder()
                .id(2L)
                .email("'whssodi2@gmail.com'")
                .nickname("whssodi2")
                .address("Seoul")
                .certificationCode("aaaaaa-aaa-aa")
                .status(UserStatus.PENDING)
                .lastLoginAt(0L)
                .build();

        fakeUserRepository.save(user1);

        fakeUserRepository.save(user2);

        fakePostRepository.save(Post.builder()
                .id(1L)
                .content("hellworld")
                .createdAt(1678530673958L)
                .modifiedAt(0L)
                .writer(user1)
                .build());


    }

    @Test
    void postCreateDto를_이용하여_게시물를_생성할_수_있다() {
        //given
        PostCreate postCreate = PostCreate.builder()
                .writerId(1)
                .content("helloword")
                .build();

        //when
        Post post = postService.create(postCreate);

        //then
        assertThat(post.getId()).isNotNull();
        assertThat(post.getContent()).isEqualTo("helloword");
        assertThat(post.getCreatedAt()).isEqualTo(1678530673958L);

    }

    @Test
    void postUpdateDto를_이용하여_게시물를_수정할_수_있다() {
        //given
        PostUpdate postUpdate = PostUpdate.builder()
                .content("helloword22")
                .build();

        //when
        Post post = postService.update(1L, postUpdate);

        //then
        assertThat(post.getId()).isNotNull();
        assertThat(post.getContent()).isEqualTo("helloword22");
        assertThat(post.getModifiedAt()).isEqualTo(1678530673958L);

    }


}
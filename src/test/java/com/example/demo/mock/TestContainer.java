package com.example.demo.mock;

import com.example.demo.common.service.ClockHolder;
import com.example.demo.common.service.UuidHolder;
import com.example.demo.post.controller.PostController;
import com.example.demo.post.controller.PostCreateController;
import com.example.demo.post.controller.port.PostService;
import com.example.demo.post.service.PostServiceImpl;
import com.example.demo.post.service.port.PostRepository;
import com.example.demo.user.controller.UserController;
import com.example.demo.user.controller.UserCreateController;
import com.example.demo.user.controller.port.UserService;
import com.example.demo.user.service.CertificationService;
import com.example.demo.user.service.UserServiceImpl;
import com.example.demo.user.service.port.MailSender;
import com.example.demo.user.service.port.UserRepository;
import lombok.Builder;

public class TestContainer {

    public final MailSender mailSender;
    public UserRepository userRepository;

    public final PostRepository postRepository;

    public PostService postService;
    public final CertificationService certificationService;

    public final UserController userController;
    public final UserCreateController userCreateController;
    public final PostCreateController postCreateController;
    public final PostController postController;
    @Builder
    public TestContainer(ClockHolder clockHolder, UuidHolder uuidHolder) {

        this.mailSender = new FakeMailSender();
        this.userRepository = new FakeUserRepository();
        this.postRepository = new FakePostRepository();
        this.postService = PostServiceImpl.builder()
                .postRepository(this.postRepository)
                .userRepository(this.userRepository)
                .clockHolder(clockHolder)
                .build();

        this.certificationService = new CertificationService(this.mailSender);

        UserServiceImpl userService = UserServiceImpl.builder()
                .uuidHolder(new TestUuidHolder("aaa-aa-a")) //고정된 값만 내려주는 stub으로 대체
                .clockHolder(clockHolder)
                .userRepository(this.userRepository)
                .certificationService(this.certificationService) //의존 관게
                .build();

        this.userController =  UserController.builder()
                .userService(userService)
                .build();
        this.userCreateController = UserCreateController.builder()
                .userService(userService).build();
        this.postCreateController = PostCreateController.builder()
                .postService(postService).build();
        this.postController = PostController.builder()
                .postService(postService)
                .userController(userController)
                .build();
    }


}

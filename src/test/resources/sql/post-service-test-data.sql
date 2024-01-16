insert into `users` (`id`,`email`,`nickname`,`address`,`certification_code`,`status`,`last_login_at`)
values (1, 'whssodi@gmail.com', 'whssodi', 'Seoul', 'aaaaaa-aaa-aa', 'ACTIVE', 0);

insert into `users` (`id`,`email`,`nickname`,`address`,`certification_code`,`status`,`last_login_at`)
values (2, 'whssodi2@gmail.com', 'whssodi2', 'Seoul', 'aaaaaa-aaa-aa', 'PENDING', 0);

insert into `posts` (`id`, `content`, `created_at`,  `modified_at`, `user_id`)
values (1, 'helloword', 1678530673958, 0, 1);

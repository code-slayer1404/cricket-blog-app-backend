package com.pranshu.blogapp.repository;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.pranshu.blogapp.TestConfig;
import com.pranshu.blogapp.entity.User;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Import(TestConfig.class)
public class UserRepositoryTest {


    @Autowired
    private UserRepo userRepo;

    @Test
    public void userRepo_save_User() {
        // arrange

        User user = User.builder()
                        .name("Test User")
                        .username("pranshu@test.com")
                        .password("12345678")
                        .build();

        // act
        User savedUser = userRepo.save(user);

        // Assert
        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getUsername().equals("pranshu@test.com"));

    }

    @Test
    public void userRepo_findByUsername_User() {
        User user = User.builder()
                .name("Test User")
                .username("test_username")
                .password("12345678")
                .build();

        User savedUser = userRepo.save(user);

        User searchedUser = userRepo.findByUsername("test_username").orElseThrow();

        Assertions.assertThat(savedUser.getId()).isEqualTo(searchedUser.getId());

    }

    @Test
    public void userRepo_findAll_MoreThanOneUsers() {
        User user1 = User.builder()
                .name("Test User")
                .username("pranshu@test.com")
                .password("12345678")
                .build();

        User user2 = User.builder()
                .name("Test User2")
                .username("pranshu1@test.com")
                .password("12345678")
                .build();

        userRepo.save(user1);
        userRepo.save(user2);

        List<User> users = userRepo.findAll();

        Assertions.assertThat(users.size()).isEqualTo(2);
        Assertions.assertThat(users).extracting((user)->user.getUsername())
                                    .containsExactlyInAnyOrder("pranshu@test.com","pranshu1@test.com");

    }
}
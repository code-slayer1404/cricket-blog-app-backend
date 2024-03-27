package com.pranshu.blogapp.repository;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.pranshu.blogapp.TestConfig;
import com.pranshu.blogapp.entity.User;
import com.pranshu.blogapp.payload.UserDTO;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Import(TestConfig.class)
public class UserRepositoryTest {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepo userRepo;



    @Test
    public void userRepo_save_User() {
        // arrange
        UserDTO userDTO = UserDTO.builder().id(5).name("test_name").username("test_username").password("test_password")
                .build();
        User user = modelMapper.map(userDTO, User.class);

        // act
        User savedUser = userRepo.save(user);

        // Assert
        Assertions.assertThat(savedUser).isNotNull();

    }

    @Test
    public void userRepo_findByUsername_User() {
        UserDTO userDTO = UserDTO.builder().id(5).name("test_name").username("test_username").password("test_password")
                .build();
        User user = modelMapper.map(userDTO, User.class);

        User savedUser = userRepo.save(user);

        User searchedUser = userRepo.findByUsername("test_username").orElseThrow();

        Assertions.assertThat(savedUser.getId()).isEqualTo(searchedUser.getId());

    }


    @Test
    public void userRepo_findAll_MoreThanOneUsers(){
        UserDTO userDTO1 = UserDTO.builder().id(5).name("test_name").username("test_username").password("test_password")
                .build();
        UserDTO userDTO2 = UserDTO.builder().id(6).name("test_name2").username("test_username2").password("test_password2")
                .build();

        User user1 = modelMapper.map(userDTO1, User.class);
        User user2 = modelMapper.map(userDTO2, User.class);

        userRepo.save(user1);
        userRepo.save(user2);

        List<User> users = userRepo.findAll();

        for (User user : users) {
            System.out.println(user.getId()+" "+user.getName());
        }

        Assertions.assertThat(users.size()).isEqualTo(2);

    }
}
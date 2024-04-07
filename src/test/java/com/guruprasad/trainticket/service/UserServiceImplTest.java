package com.guruprasad.trainticket.service;

import com.guruprasad.trainticket.dto.TicketReservationPayload;
import com.guruprasad.trainticket.dto.User;
import com.guruprasad.trainticket.repository.UserRepository;
import com.guruprasad.trainticket.snippets.StaticCodeSnippets;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class UserServiceImplTest {

    UserServiceImplTest() {
        StaticCodeSnippets.setLiquibaseNetUtilsLocalHost();
    }

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteByEmail(getMyUserInMemoryObject().getEmail());
    }

    private User getMyUserInMemoryObject() {
        User me = new User();
        me.setFirstName("Guruprasad");
        me.setLastName("Bhat");
        me.setEmail("bpg168g@gmail.com");

        return me;
    }

    @Test
    void test_getOrCreateUser() {
        /* Arrange */
        TicketReservationPayload resPayload = new TicketReservationPayload();
        User notSaved = getMyUserInMemoryObject();
        resPayload.setUser(notSaved);

        // notice the in memory object has id, created_at, updated_at as null
        assertThat(notSaved.getId()).isNull();
        assertThat(notSaved.getCreatedAt()).isNull();
        assertThat(notSaved.getUpdatedAt()).isNull();

        /* Act */
        User saved = userService.getOrCreateUser(resPayload);
        // calling second time will not cause db error due to unique constraints
        userService.getOrCreateUser(resPayload);

        /* Assert */
        // notice the saved object has id, created_at, updated_at already filled in from database
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();

        // notice the in memory as well as db object have same name & email
        assertThat(saved.getFirstName()).isEqualTo(notSaved.getFirstName());
        assertThat(saved.getLastName()).isEqualTo(notSaved.getLastName());
        assertThat(saved.getEmail()).isEqualTo(notSaved.getEmail());
    }

    @Test
    void test_GetByEmail() {
        // arrange
        userRepository.save(getMyUserInMemoryObject());
        String email = "bpg168g@gmail.com";

        // act
        User retrieved = userService.getByEmail(email);

        // assert
        assertThat(retrieved.getEmail()).isEqualTo(email);
    }

    @Test
    void test_GetById() {
        // arrange
        User saved = userRepository.save(getMyUserInMemoryObject());
        Long userId = saved.getId();

        // act
        User retrieved = userService.getById(userId);

        // assert
        assertThat(retrieved.getId()).isEqualTo(userId);
    }

    @Test
    void test_DeleteByEmail() {
        // arrange
        User user = getMyUserInMemoryObject();
        userRepository.save(user);

        // notice the user exists in db
        assertThat(userService.getByEmail(user.getEmail())).isNotNull();

        // act
        userService.deleteByEmail(user.getEmail());

        // assert
        assertThat(userService.getByEmail(user.getEmail())).isNull();
    }

    @Test
    void test_DeleteById() {
        // arrange
        User user = getMyUserInMemoryObject();
        User savedUser = userRepository.save(user);

        // notice the user exists in db
        assertThat(userService.getById(savedUser.getId())).isNotNull();

        // act
        userService.deleteById(savedUser.getId());

        // assert
        assertThat(userService.getById(user.getId())).isNull();
    }
}
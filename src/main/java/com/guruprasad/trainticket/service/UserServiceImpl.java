package com.guruprasad.trainticket.service;

import com.guruprasad.trainticket.dto.TicketReservationPayload;
import com.guruprasad.trainticket.dto.User;
import com.guruprasad.trainticket.repository.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getByEmail(String email) {
        return userRepository.getByEmail(email);
    }

    public User getById(String id) {
        return userRepository.getById(id);
    }

    public User getOrCreateUser(TicketReservationPayload payload) {
        User payloadUser = payload.getUser();
        User dbUser = getByEmail(payloadUser.getEmail());

        if (dbUser != null) {
            return dbUser;
        }

        return userRepository.save(payloadUser);
    }

    public void deleteByEmail(String email) {
        userRepository.deleteByEmail(email);
    }

    public void deleteById(String id) {
        userRepository.deleteById(id);
    }
}

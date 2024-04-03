package com.guruprasad.trainticket.repository;

import com.guruprasad.trainticket.dto.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Transactional
    void deleteByEmail(String email);

    @Query("select u from User u where u.email = ?1")
    User getByEmail(String email);

    @Query("select u from User u where u.id = ?1")
    User getById(String id);
}

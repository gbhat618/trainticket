package com.guruprasad.trainticket.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(name = "users_seq", sequenceName = "users_SEQ", allocationSize = 1)
    Long id;

    @Column(name = "first_name", nullable = false)
    String firstName;

    // Note here we allow last_name to be null; however it does mean we allow duplicate users, as email will have non-duplicate constraint.
    @Column(name = "last_name")
    String lastName;

    @Column(name = "email", nullable = false)
    String email;

    @JsonIgnore
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    LocalDateTime createdAt;

    @JsonIgnore
    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    LocalDateTime updatedAt;
}

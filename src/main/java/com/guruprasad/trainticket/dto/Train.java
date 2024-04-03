package com.guruprasad.trainticket.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "trains")
public class Train {

    @Id
    @Column(name = "train_number")
    Long trainNumber;

    @Column(name = "name")
    String name;

    @JsonIgnore
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    LocalDateTime createdAt;

    @JsonIgnore
    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    LocalDateTime updatedAt;
}

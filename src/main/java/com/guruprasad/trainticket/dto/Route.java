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
@Table(name = "routes")
public class Route {
    @Id
    Long id;

    @Column(name = "from_station", nullable = false)
    String fromStation;

    @Column(name = "to_station", nullable = false)
    String toStation;

    @JsonIgnore
    @Column(name = "remaining_seats_section_a", nullable = false)
    Integer remainingSeatSectionA;

    @JsonIgnore
    @Column(name = "remaining_seats_section_b", nullable = false)
    Integer remainingSeatSectionB;

    @Column(name = "price", nullable = false)
    Integer price;

    @JsonIgnore
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    LocalDateTime createdAt;

    @JsonIgnore
    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "train_number")
    Train train;
}

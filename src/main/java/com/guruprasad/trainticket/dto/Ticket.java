package com.guruprasad.trainticket.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.guruprasad.trainticket.constant.PassengerSex;
import com.guruprasad.trainticket.constant.TicketStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "tickets")
public class Ticket {
    @Id
    String pnr;

    @Column(name = "section")
    String section;

    @Column(name = "seat_number")
    Integer seatNumber;

    @Column(name = "passenger_full_name", nullable = false)
    String passengerFullName;

    @Column(name = "passenger_age")
    Integer passengerAge;

    @Column(name = "passenger_sex")
    @Enumerated(EnumType.STRING)
    PassengerSex passengerSex;

    @Column(name = "status", nullable = false)
    TicketStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    LocalDateTime createdAt;

    @JsonIgnore
    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "route_id")
    Route route;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "purchased_by")
    User purchasedBy;
}

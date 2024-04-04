package com.guruprasad.trainticket.repository;

import com.guruprasad.trainticket.dto.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, String> {

    @Query("SELECT t FROM Ticket t WHERE t.pnr=?1")
    Ticket findByPnrNumber(String pnr);

    @Query("""
        SELECT t
        FROM Ticket t, Route r, Train tn
        WHERE tn.trainNumber = ?1 AND t.section = ?2 AND t.status= com.guruprasad.trainticket.constant.TicketStatus.RESERVED
    """)
    List<Ticket> listTicketsByTrainSection(int trainNumber, String section);

    @Query("""
        SELECT t
        FROM Ticket t, Route r, Train tn
        WHERE tn.trainNumber = ?1 AND t.section = ?2 and t.seatNumber= ?3
    """)
    Ticket getTicketForSectionAndSeatNumber(long trainNumber, String section, int seat);
}

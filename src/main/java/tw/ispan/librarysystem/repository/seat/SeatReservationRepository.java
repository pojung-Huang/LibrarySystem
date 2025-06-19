package tw.ispan.librarysystem.repository.seat;

import org.springframework.data.jpa.repository.JpaRepository;
import tw.ispan.librarysystem.entity.seat.SeatReservation;
import tw.ispan.librarysystem.entity.seat.SeatStatus;

import java.time.LocalDate;
import java.util.List;

public interface SeatReservationRepository extends JpaRepository<SeatReservation, Long> {

    List<SeatReservation> findByReservationDateAndTimeSlotAndStatus(
            LocalDate reservationDate, String timeSlot, SeatReservation.Status status
    );

    boolean existsBySeatAndReservationDateAndTimeSlotAndStatus(
            SeatStatus seat, LocalDate date, String timeSlot, SeatReservation.Status status
    );

    List<SeatReservation> findBySeatAndReservationDateAndTimeSlotAndStatus(
            SeatStatus seat,
            LocalDate reservationDate,
            String timeSlot,
            SeatReservation.Status status
    );
}


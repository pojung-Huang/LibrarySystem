package tw.ispan.librarysystem.service.seat;

import java.time.LocalDate;
import java.util.List;

public interface SeatReservationService {
    List<String> getReservedSeatLabels(LocalDate date, String timeSlot);
    String reserveSeat(String seatLabel, LocalDate date, String timeSlot, Integer userId);
    String cancelReservation(String seatLabel, LocalDate date, String timeSlot);
}

package tw.ispan.librarysystem.controller.seat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tw.ispan.librarysystem.service.seat.SeatReservationService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/seats/reservations")
public class SeatReservationController {

    @Autowired
    private SeatReservationService seatReservationService;

    @GetMapping("/occupied")
    public List<String> getReservedSeats(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("timeSlot") String timeSlot
    ) {
        return seatReservationService.getReservedSeatLabels(date, timeSlot);
    }

    @PostMapping("/book")
    public ResponseEntity<String> bookSeat(
            @RequestParam String seatLabel,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam String timeSlot,
            @RequestParam Integer userId
    ) {
        String result = seatReservationService.reserveSeat(seatLabel, date, timeSlot, userId);
        if (result.contains("成功")) return ResponseEntity.ok(result);
        if (result.contains("預約")) return ResponseEntity.status(409).body(result);
        return ResponseEntity.badRequest().body(result);
    }

    @PutMapping("/cancel")
    public ResponseEntity<String> cancelReservation(
            @RequestParam String seatLabel,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam String timeSlot
    ) {
        String result = seatReservationService.cancelReservation(seatLabel, date, timeSlot);
        if (result.contains("成功")) return ResponseEntity.ok(result);
        return ResponseEntity.badRequest().body(result);
    }
}


package tw.ispan.librarysystem.controller.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tw.ispan.librarysystem.dto.ReservationDTO;
import tw.ispan.librarysystem.entity.reservation.ReservationEntity;
import tw.ispan.librarysystem.repository.reservation.ReservationRepository;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "http://localhost:3000")
public class ReservationController {

    @Autowired
    private ReservationRepository reservationRepository;

    @GetMapping
    public List<ReservationEntity> getAllReservations() {
        return reservationRepository.findAll();
    }
    @GetMapping("/with-book")
public List<ReservationDTO> getAllReservationsWithBook() {
    return reservationRepository.findReservationsWithBookTitle();
}

    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationEntity> getReservationById(@PathVariable Integer reservationId) {
        return reservationRepository.findById(reservationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public List<ReservationEntity> getReservationsByUserId(@PathVariable Integer userId) {
        return reservationRepository.findByUserId(userId);
    }

    @GetMapping("/book/{bookId}")
    public List<ReservationEntity> getReservationsByBookId(@PathVariable Integer bookId) {
        return reservationRepository.findByBookBookId(bookId);
    }

    @PostMapping
    public ReservationEntity createReservation(@RequestBody ReservationEntity reservation) {
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setUpdatedAt(LocalDateTime.now());
        reservation.setStatus("PENDING");
        return reservationRepository.save(reservation);
    }

    @PutMapping("/{reservationId}")
    public ResponseEntity<ReservationEntity> updateReservation(
            @PathVariable Integer reservationId,
            @RequestBody ReservationEntity reservationDetails) {
        return reservationRepository.findById(reservationId)
                .map(reservation -> {
                    reservation.setStatus(reservationDetails.getStatus());
                    reservation.setExpiryDate(reservationDetails.getExpiryDate());
                    reservation.setUpdatedAt(LocalDateTime.now());
                    return ResponseEntity.ok(reservationRepository.save(reservation));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<?> deleteReservation(@PathVariable Integer reservationId) {
        return reservationRepository.findById(reservationId)
                .map(reservation -> {
                    reservationRepository.delete(reservation);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
} 
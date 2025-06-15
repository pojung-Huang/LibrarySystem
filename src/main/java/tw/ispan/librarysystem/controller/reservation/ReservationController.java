// @SuppressWarnings("SpellCheckingInspection")
package tw.ispan.librarysystem.controller.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tw.ispan.librarysystem.dto.ReservationDTO;
import tw.ispan.librarysystem.entity.reservation.ReservationEntity;
import tw.ispan.librarysystem.repository.reservation.ReservationRepository;
import tw.ispan.librarysystem.service.ReservationService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "http://localhost:3000")
public class ReservationController {

    @Autowired
    private ReservationRepository reservationRepository;
    
    @Autowired
    private ReservationService reservationService;

    @GetMapping
    public List<ReservationDTO> getAllReservations() {
        return reservationService.getAllReservationsWithBookInfo();
    }

    @GetMapping("/with-book")
    public List<ReservationDTO> getAllReservationsWithBook() {
        return reservationService.getAllReservationsWithBookInfo();
    }

    @GetMapping("/{reservationId}")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Integer reservationId) {
        return reservationService.getReservationById(reservationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    public List<ReservationDTO> getReservationsByUserId(@PathVariable Integer userId) {
        return reservationService.getReservationsByUserId(userId);
    }

    @GetMapping("/book/{bookId}")
    public List<ReservationDTO> getReservationsByBookId(@PathVariable Integer bookId) {
        return reservationService.getReservationsByBookId(bookId);
    }

    @PostMapping
    public ResponseEntity<ReservationDTO> createReservation(@RequestBody ReservationEntity reservation) {
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setUpdatedAt(LocalDateTime.now());
        reservation.setStatus("PENDING");
        ReservationEntity savedReservation = reservationRepository.save(reservation);
        return ResponseEntity.ok(reservationService.convertToDTO(savedReservation));
    }

    @PutMapping("/{reservationId}")
    public ResponseEntity<ReservationDTO> updateReservation(
            @PathVariable Integer reservationId,
            @RequestBody ReservationEntity reservationDetails) {
        return reservationRepository.findById(reservationId)
                .map(reservation -> {
                    reservation.setStatus(reservationDetails.getStatus());
                    reservation.setExpiryDate(reservationDetails.getExpiryDate());
                    reservation.setUpdatedAt(LocalDateTime.now());
                    ReservationEntity updatedReservation = reservationRepository.save(reservation);
                    return ResponseEntity.ok(reservationService.convertToDTO(updatedReservation));
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
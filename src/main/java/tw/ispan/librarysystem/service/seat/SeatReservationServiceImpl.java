package tw.ispan.librarysystem.service.seat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tw.ispan.librarysystem.entity.seat.SeatReservation;
import tw.ispan.librarysystem.entity.seat.SeatStatus;
import tw.ispan.librarysystem.repository.seat.SeatReservationRepository;
import tw.ispan.librarysystem.repository.seat.SeatStatusRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class SeatReservationServiceImpl implements SeatReservationService {

    @Autowired
    private SeatReservationRepository reservationRepo;

    @Autowired
    private SeatStatusRepository seatRepo;

    @Override
    public List<String> getReservedSeatLabels(LocalDate date, String timeSlot) {
        return reservationRepo.findByReservationDateAndTimeSlotAndStatus(
                        date, timeSlot, SeatReservation.Status.RESERVED
                ).stream()
                .map(r -> r.getSeat().getSeatLabel())
                .toList();
    }

    @Override
    public String reserveSeat(String seatLabel, LocalDate date, String timeSlot, Integer userId) {
        SeatStatus seat = seatRepo.findBySeatLabel(seatLabel).orElse(null);
        if (seat == null || seat.getStatus() == SeatStatus.Status.BROKEN) {
            return "❌ 座位不存在或已損壞";
        }

        boolean exists = reservationRepo.existsBySeatAndReservationDateAndTimeSlotAndStatus(
                seat, date, timeSlot, SeatReservation.Status.RESERVED);

        if (exists) {
            return "❌ 該座位已被預約";
        }

        SeatReservation reservation = new SeatReservation();
        reservation.setSeat(seat);
        reservation.setUserId(userId);
        reservation.setReservationDate(date);
        reservation.setTimeSlot(timeSlot);
        reservation.setStatus(SeatReservation.Status.RESERVED);
        reservationRepo.save(reservation);

        return "✅ 預約成功";
    }

    @Override
    public String cancelReservation(String seatLabel, LocalDate date, String timeSlot) {
        SeatStatus seat = seatRepo.findBySeatLabel(seatLabel).orElse(null);
        if (seat == null) return "❌ 座位不存在";

        List<SeatReservation> found = reservationRepo
                .findBySeatAndReservationDateAndTimeSlotAndStatus(seat, date, timeSlot, SeatReservation.Status.RESERVED);

        if (found.isEmpty()) return "❌ 沒有找到預約記錄";

        found.forEach(res -> {
            res.setStatus(SeatReservation.Status.CANCELLED);
            reservationRepo.save(res);
        });

        return "✅ 已取消預約";
    }

}


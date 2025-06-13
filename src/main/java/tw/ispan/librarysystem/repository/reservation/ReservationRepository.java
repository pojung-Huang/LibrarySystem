package tw.ispan.librarysystem.repository.reservation;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tw.ispan.librarysystem.dto.ReservationDTO;
import tw.ispan.librarysystem.entity.reservation.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Integer> {

    @Query("SELECT new tw.ispan.librarysystem.dto.ReservationDTO(r.reservationId, r.status, b.title, r.reservationDate) " +
           "FROM ReservationEntity r JOIN r.book b")
    List<ReservationDTO> findReservationsWithBookTitle();

    List<ReservationEntity> findByUserId(Integer userId);

    List<ReservationEntity> findByBookBookId(Integer bookId);
}

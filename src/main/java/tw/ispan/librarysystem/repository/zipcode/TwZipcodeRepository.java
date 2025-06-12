package tw.ispan.librarysystem.repository.zipcode;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tw.ispan.librarysystem.entity.zipcode.TwZipcode;

import java.util.List;

@Repository
public interface TwZipcodeRepository extends JpaRepository<TwZipcode, Long> {
    List<TwZipcode> findByCounty(String county);
    TwZipcode findByCountyAndTown(String county, String town);
}

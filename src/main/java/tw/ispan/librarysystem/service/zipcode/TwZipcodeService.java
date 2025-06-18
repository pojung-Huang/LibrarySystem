package tw.ispan.librarysystem.service.zipcode;

import org.springframework.stereotype.Service;
import tw.ispan.librarysystem.entity.zipcode.TwZipcode;
import tw.ispan.librarysystem.repository.zipcode.TwZipcodeRepository;

import java.util.List;

@Service
public class TwZipcodeService {

    private final TwZipcodeRepository repository;

    public TwZipcodeService(TwZipcodeRepository repository) {
        this.repository = repository;
    }

    public List<String> getAllCounties() {
        return repository.findAll().stream()
                .map(TwZipcode::getCounty)
                .distinct()
                .toList();
    }

    public List<String> getTownsByCounty(String county) {
        return repository.findByCounty(county).stream()
                .map(TwZipcode::getTown)
                .toList();
    }

    public String getZip(String county, String town) {
        TwZipcode zipcode = repository.findByCountyAndTown(county, town);
        return zipcode != null ? zipcode.getZip3() : "";
    }
}

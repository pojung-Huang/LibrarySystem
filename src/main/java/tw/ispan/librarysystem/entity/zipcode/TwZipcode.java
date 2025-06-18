package tw.ispan.librarysystem.entity.zipcode;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tw_zipcodes")
@NoArgsConstructor
@AllArgsConstructor
public class TwZipcode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String county;
    private String town;
    private String zip3;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getZip3() {
        return zip3;
    }

    public void setZip3(String zip3) {
        this.zip3 = zip3;
    }
}

package tw.ispan.librarysystem.zipcode.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tw_zipcodes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TwZipcode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String county;
    private String town;
    private String zip3;
}

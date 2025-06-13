package tw.ispan.librarysystem.entity.member;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "members")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    private String name;
    private String gender;
    @Column(name = "id_number")
    private String idNumber;
    @Column(name = "birth_date")
    private java.sql.Date birthDate;
    private String nationality;
    private String education;
    private String occupation;
    @Column(name = "address_county")
    private String addressCounty;
    @Column(name = "address_town")
    private String addressTown;
    @Column(name = "address_zip")
    private String addressZip;
    @Column(name = "address_detail")
    private String addressDetail;
    private String email;
    private String phone;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    private String password;

    // Getter and Setter methods
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getIdNumber() { return idNumber; }
    public void setIdNumber(String idNumber) { this.idNumber = idNumber; }
    public java.sql.Date getBirthDate() { return birthDate; }
    public void setBirthDate(java.sql.Date birthDate) { this.birthDate = birthDate; }
    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }
    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }
    public String getOccupation() { return occupation; }
    public void setOccupation(String occupation) { this.occupation = occupation; }
    public String getAddressCounty() { return addressCounty; }
    public void setAddressCounty(String addressCounty) { this.addressCounty = addressCounty; }
    public String getAddressTown() { return addressTown; }
    public void setAddressTown(String addressTown) { this.addressTown = addressTown; }
    public String getAddressZip() { return addressZip; }
    public void setAddressZip(String addressZip) { this.addressZip = addressZip; }
    public String getAddressDetail() { return addressDetail; }
    public void setAddressDetail(String addressDetail) { this.addressDetail = addressDetail; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
} 
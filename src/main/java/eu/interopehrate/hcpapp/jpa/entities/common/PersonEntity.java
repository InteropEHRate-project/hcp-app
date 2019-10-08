package eu.interopehrate.hcpapp.jpa.entities.common;

import eu.interopehrate.hcpapp.jpa.entities.AddressEntity;
import eu.interopehrate.hcpapp.jpa.entities.enums.Gender;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "PERSONS")
@Inheritance(strategy = InheritanceType.JOINED)
public class PersonEntity extends HCPApplicationEntity {
    @NotNull
    @Column(name = "FIRST_NAME")
    private String firstName;
    @NotNull
    @Column(name = "LAST_NAME")
    private String lastName;
    @NotNull
    @Column(name = "GENDER")
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @NotNull
    @Column(name = "BIRTH_DATE")
    private LocalDate birthDate;
    @Lob
    private byte[] picture;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "PERSON_ADDRESS",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id"))
    private List<AddressEntity> addresses = new ArrayList<>();
}

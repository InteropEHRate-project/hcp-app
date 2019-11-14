package eu.interopehrate.hcpapp.jpa.entities.common;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public class HCPApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Long version;
    @CreationTimestamp
    @Column(name = "CREATED_DATE", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdDate;
    @UpdateTimestamp
    @Column(name = "UPDATED_DATE", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedDate;
}

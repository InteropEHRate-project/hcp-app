package eu.interopehrate.hcpapp.jpa.entities.common;

import lombok.Getter;
import lombok.Setter;

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
    @Column(name = "CREATED_DATE", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdDate;
    @Column(name = "UPDATED_DATE", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime updatedDate;

    @PrePersist
    protected void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdDate = now;
        this.updatedDate = now;
    }

    @PreUpdate
    protected void preUpdate() {
        this.updatedDate = LocalDateTime.now();
    }
}

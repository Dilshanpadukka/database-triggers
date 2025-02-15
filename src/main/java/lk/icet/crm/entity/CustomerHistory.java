package lk.icet.crm.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Immutable;

import java.time.LocalDateTime;

@Data
@Entity
@Immutable
@Table(name = "customer_h")
public class CustomerHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auditId;
    private Long id;
    private String firstName;
    private String lastName;
    private String nic;
    private Boolean inActive;
    private String operationType;
    private LocalDateTime operationTimestamp;
    private String operationBy;
}

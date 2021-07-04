package model.pg;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "code")
public class PgCode {
    @Id
    private String cis;
    
    private String orderId;
    
    private String reportId;
}

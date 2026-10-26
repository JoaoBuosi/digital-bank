package dev.buno.app;
import jakarta.persistence.*;
import java.math.BigDecimal;
@Entity
public class Account {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @Column(nullable=false,precision=19,scale=2) public BigDecimal balance = BigDecimal.ZERO;
 @Version public Long version;
 protected Account(){}
 public Account(BigDecimal balance){this.balance=balance;}
}

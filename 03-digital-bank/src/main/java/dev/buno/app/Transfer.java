package dev.buno.app;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
@Entity
public class Transfer {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @Column(nullable=false,unique=true) public String idempotencyKey;
 public Long fromAccount; public Long toAccount;
 @Column(precision=19,scale=2) public BigDecimal amount;
 public Instant createdAt=Instant.now();
 protected Transfer(){}
 public Transfer(String key,Long from,Long to,BigDecimal amount){idempotencyKey=key;fromAccount=from;toAccount=to;this.amount=amount;}
}

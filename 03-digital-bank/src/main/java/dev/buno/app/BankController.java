package dev.buno.app;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.util.*;
@RestController @RequestMapping("/api")
public class BankController {
 private final AccountRepository accounts; private final TransferRepository transfers;
 public BankController(AccountRepository a,TransferRepository t){accounts=a;transfers=t;}
 public record CreateAccount(BigDecimal initialBalance){}
 public record TransferRequest(Long fromAccount,Long toAccount,BigDecimal amount){}
 @PostMapping("/accounts") public Account create(@RequestBody CreateAccount r){if(r.initialBalance()==null||r.initialBalance().signum()<0)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Saldo inicial inválido");return accounts.save(new Account(r.initialBalance()));}
 @GetMapping("/accounts") public List<Account> accounts(){return accounts.findAll();}
 @GetMapping("/accounts/{id}/statement") public List<Transfer> statement(@PathVariable Long id){return transfers.findAll().stream().filter(t->Objects.equals(t.fromAccount,id)||Objects.equals(t.toAccount,id)).toList();}
 @PostMapping("/transfers") @Transactional public Transfer transfer(@RequestHeader("Idempotency-Key") String key,@RequestBody TransferRequest r){
  if(key==null||key.isBlank())throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Idempotency-Key obrigatório");
  var prior=transfers.findByIdempotencyKey(key); if(prior.isPresent())return prior.get();
  if(r.amount()==null||r.amount().signum()<=0||Objects.equals(r.fromAccount(),r.toAccount()))throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Transferência inválida");
  Account from=accounts.findById(r.fromAccount()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Conta de origem"));
  Account to=accounts.findById(r.toAccount()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Conta de destino"));
  if(from.balance.compareTo(r.amount())<0)throw new ResponseStatusException(HttpStatus.CONFLICT,"Saldo insuficiente");
  from.balance=from.balance.subtract(r.amount()); to.balance=to.balance.add(r.amount()); accounts.save(from);accounts.save(to);
  return transfers.save(new Transfer(key,from.id,to.id,r.amount()));
 }
}

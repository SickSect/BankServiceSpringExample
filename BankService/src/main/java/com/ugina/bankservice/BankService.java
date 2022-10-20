package com.ugina.bankservice;

import com.ugina.bankservice.model.TransferBalance;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class BankService {
    private final BalanceRepository repository;
    public BigDecimal getBalance(Long accountId) {
        BigDecimal balance =  repository.getBalanceById(accountId);
        if (balance == null)
            throw new IllegalArgumentException();
        else
            return balance;
    }

    public BigDecimal addMoney(Long to, BigDecimal amount) {
        BigDecimal tmp = repository.getBalanceById(to);
        if (tmp == null) {
            repository.save(to, amount);
            return amount;
        }else{
            BigDecimal update =  tmp.add(amount);
            repository.save(to,update);
            return update;
        }
    }

    public void makeTransfer(TransferBalance transferBalance) {
        BigDecimal fromBalance = repository.getBalanceById(transferBalance.getFrom());
        BigDecimal toBalance = repository.getBalanceById(transferBalance.getTo());
        if (fromBalance == null || toBalance == null)
            throw new IllegalArgumentException();
        if (transferBalance.getAmount().compareTo(fromBalance) > 0)
            throw new IllegalArgumentException("You dont have enough money");
        else{
            BigDecimal updateFromBalance = fromBalance.subtract(transferBalance.getAmount());
            BigDecimal updateToBalance = toBalance.add(transferBalance.getAmount());
            repository.save(transferBalance.getFrom(), updateFromBalance);
            repository.save(transferBalance.getTo(), updateToBalance);
        }
    }
}

package com.hellface.facebook.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Johno Crawford (johno@hellface.com)
 */
@Component
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public void save(String identifier, List<Friend> friends) {
        accountRepository.save(new Account(identifier, new ArrayList<Friend>(friends)));
    }

    @Transactional(readOnly = true)
    public List<Account> findByIdentifier(String identifier) {
        return accountRepository.findByIdentifier(identifier);
    }
}

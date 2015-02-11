package com.hellface.facebook.dao;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Johno Crawford (johno@hellface.com)
 */
public interface AccountRepository extends CrudRepository<Account, Integer> {

    List<Account> findByIdentifier(String identifier);
}

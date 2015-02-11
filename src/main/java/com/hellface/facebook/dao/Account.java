package com.hellface.facebook.dao;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author Johno Crawford (johno@hellface.com)
 */
@Entity
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "identifier")
    private String identifier;

    @ElementCollection
    @CollectionTable(name = "friends", joinColumns = @JoinColumn(name = "account_id"))
    @Column(name = "friend")
    private List<Friend> friends;

    public Account() {
    }

    public Account(String identifier, List<Friend> friends) {
        this.identifier = identifier;
        this.friends = friends;
    }

    public String getIdentifier() {
        return identifier;
    }

    public List<Friend> getFriends() {
        return friends;
    }
}

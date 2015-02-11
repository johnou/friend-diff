package com.hellface.facebook.dao;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

/**
* @author Johno Crawford (johno@hellface.com)
*/
@Embeddable
public class Friend {

    @Column(name = "identifier")
    private String identifier;

    @Column(name = "name")
    private String name;

    @Transient
    private String picture;

    public Friend() {
    }

    public Friend(String identifier, String name, String picture) {
        this.identifier = identifier;
        this.name = name;
        this.picture = picture;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public String getPicture() {
        return picture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Friend friend = (Friend) o;

        if (identifier != null ? !identifier.equals(friend.identifier) : friend.identifier != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return identifier != null ? identifier.hashCode() : 0;
    }
}

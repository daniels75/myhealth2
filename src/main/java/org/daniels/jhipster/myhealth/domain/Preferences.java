package org.daniels.jhipster.myhealth.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import org.daniels.jhipster.myhealth.domain.enumeration.Units;

/**
 * A Preferences.
 */
@Entity
@Table(name = "preferences")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Preferences implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "weekly_goal", nullable = false)
    private Integer weekly_goal;

    @Enumerated(EnumType.STRING)
    @Column(name = "weight_units")
    private Units weight_units;

    @OneToOne(mappedBy = "")
    @JsonIgnore
    private User user;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWeekly_goal() {
        return weekly_goal;
    }

    public void setWeekly_goal(Integer weekly_goal) {
        this.weekly_goal = weekly_goal;
    }

    public Units getWeight_units() {
        return weight_units;
    }

    public void setWeight_units(Units weight_units) {
        this.weight_units = weight_units;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Preferences preferences = (Preferences) o;
        return Objects.equals(id, preferences.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Preferences{" +
            "id=" + id +
            ", weekly_goal='" + weekly_goal + "'" +
            ", weight_units='" + weight_units + "'" +
            '}';
    }
}

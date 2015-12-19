package org.daniels.jhipster.myhealth.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DateEntity.
 */
@Entity
@Table(name = "date_entity")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DateEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "first_date", nullable = false)
    private LocalDate firstDate;

    @NotNull
    @Column(name = "second_date", nullable = false)
    private ZonedDateTime secondDate;

    @Column(name = "test")
    private Integer test;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(LocalDate firstDate) {
        this.firstDate = firstDate;
    }

    public ZonedDateTime getSecondDate() {
        return secondDate;
    }

    public void setSecondDate(ZonedDateTime secondDate) {
        this.secondDate = secondDate;
    }

    public Integer getTest() {
        return test;
    }

    public void setTest(Integer test) {
        this.test = test;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DateEntity dateEntity = (DateEntity) o;
        return Objects.equals(id, dateEntity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "DateEntity{" +
            "id=" + id +
            ", firstDate='" + firstDate + "'" +
            ", secondDate='" + secondDate + "'" +
            ", test='" + test + "'" +
            '}';
    }
}

package org.daniels.jhipster.myhealth.repository;

import java.util.List;

import org.daniels.jhipster.myhealth.domain.Points;
import org.joda.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Spring Data JPA repository for the Points entity.
 */
public interface PointsRepository extends JpaRepository<Points,Long> {

    @Query("select points from Points points where points.user.login = ?#{principal.username} order by points.date desc")
    Page<Points> findAllForCurrentUser(Pageable pageable);

    List<Points> findAllByDateBetweenAndUserLogin(java.time.LocalDate firstDate, java.time.LocalDate secondDate, String login);

    Page<Points> findAllByOrderByDateDesc(Pageable pageable);
}

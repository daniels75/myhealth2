package org.daniels.jhipster.myhealth.repository;

import org.daniels.jhipster.myhealth.domain.Points;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Points entity.
 */
public interface PointsRepository extends JpaRepository<Points,Long> {

    @Query("select points from Points points where points.user.login = ?#{principal.username}")
    List<Points> findByUserIsCurrentUser();

}

package org.daniels.jhipster.myhealth.repository;

import org.daniels.jhipster.myhealth.domain.DateEntity;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the DateEntity entity.
 */
public interface DateEntityRepository extends JpaRepository<DateEntity,Long> {

}

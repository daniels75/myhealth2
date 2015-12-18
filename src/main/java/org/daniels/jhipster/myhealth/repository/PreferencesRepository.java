package org.daniels.jhipster.myhealth.repository;

import org.daniels.jhipster.myhealth.domain.Preferences;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Preferences entity.
 */
public interface PreferencesRepository extends JpaRepository<Preferences,Long> {

}

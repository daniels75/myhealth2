package org.daniels.jhipster.myhealth.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.daniels.jhipster.myhealth.domain.Preferences;
import org.daniels.jhipster.myhealth.repository.PreferencesRepository;
import org.daniels.jhipster.myhealth.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing Preferences.
 */
@RestController
@RequestMapping("/api")
public class PreferencesResource {

    private final Logger log = LoggerFactory.getLogger(PreferencesResource.class);
        
    @Inject
    private PreferencesRepository preferencesRepository;
    
    /**
     * POST  /preferencess -> Create a new preferences.
     */
    @RequestMapping(value = "/preferencess",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Preferences> createPreferences(@Valid @RequestBody Preferences preferences) throws URISyntaxException {
        log.debug("REST request to save Preferences : {}", preferences);
        if (preferences.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("preferences", "idexists", "A new preferences cannot already have an ID")).body(null);
        }
        Preferences result = preferencesRepository.save(preferences);
        return ResponseEntity.created(new URI("/api/preferencess/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("preferences", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /preferencess -> Updates an existing preferences.
     */
    @RequestMapping(value = "/preferencess",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Preferences> updatePreferences(@Valid @RequestBody Preferences preferences) throws URISyntaxException {
        log.debug("REST request to update Preferences : {}", preferences);
        if (preferences.getId() == null) {
            return createPreferences(preferences);
        }
        Preferences result = preferencesRepository.save(preferences);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("preferences", preferences.getId().toString()))
            .body(result);
    }

    /**
     * GET  /preferencess -> get all the preferencess.
     */
    @RequestMapping(value = "/preferencess",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Preferences> getAllPreferencess(@RequestParam(required = false) String filter) {
        if ("user-is-null".equals(filter)) {
            log.debug("REST request to get all Preferencess where user is null");
            return StreamSupport
                .stream(preferencesRepository.findAll().spliterator(), false)
                .filter(preferences -> preferences.getUser() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Preferencess");
        return preferencesRepository.findAll();
            }

    /**
     * GET  /preferencess/:id -> get the "id" preferences.
     */
    @RequestMapping(value = "/preferencess/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Preferences> getPreferences(@PathVariable Long id) {
        log.debug("REST request to get Preferences : {}", id);
        Preferences preferences = preferencesRepository.findOne(id);
        return Optional.ofNullable(preferences)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /preferencess/:id -> delete the "id" preferences.
     */
    @RequestMapping(value = "/preferencess/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePreferences(@PathVariable Long id) {
        log.debug("REST request to delete Preferences : {}", id);
        preferencesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("preferences", id.toString())).build();
    }
}

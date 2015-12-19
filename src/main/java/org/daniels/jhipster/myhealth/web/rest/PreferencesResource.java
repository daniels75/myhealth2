package org.daniels.jhipster.myhealth.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;
import javax.validation.Valid;

import org.daniels.jhipster.myhealth.domain.Preferences;
import org.daniels.jhipster.myhealth.domain.User;
import org.daniels.jhipster.myhealth.repository.PreferencesRepository;
import org.daniels.jhipster.myhealth.repository.UserRepository;
import org.daniels.jhipster.myhealth.security.AuthoritiesConstants;
import org.daniels.jhipster.myhealth.security.SecurityUtils;
import org.daniels.jhipster.myhealth.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;


/**
 * REST controller for managing Preferences.
 */
@RestController
@RequestMapping("/api")
public class PreferencesResource {

    private final Logger log = LoggerFactory.getLogger(PreferencesResource.class);

    @Inject
    private PreferencesRepository preferencesRepository;

    @Inject
    private UserRepository userRepository;

    /**
     * POST  /preferences -> Create a new preferences.
     */
    @RequestMapping(value = "/preferences",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Preferences> create(@Valid @RequestBody Preferences preferences) throws URISyntaxException {
        log.debug("REST request to save Preferences : {}", preferences);
        if (preferences.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new preferences cannot already have an ID").body(null);
        }

        Preferences result = preferencesRepository.save(preferences);

        log.debug("Settings preferences for current user: {}", SecurityUtils.getCurrentLogin());
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin().getUsername()).get();
        user.setPreferences(result);
        userRepository.save(user);

        return ResponseEntity.created(new URI("/api/preferences/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("preferences", result.getId().toString()))
                .body(result);
    }

    /**
     * PUT  /preferences -> Updates an existing preferences.
     */
    @RequestMapping(value = "/preferences",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Preferences> update(@Valid @RequestBody Preferences preferences) throws URISyntaxException {
        log.debug("REST request to update Preferences : {}", preferences);
        if (preferences.getId() == null) {
            return create(preferences);
        }
        Preferences result = preferencesRepository.save(preferences);
        return ResponseEntity.ok()
                .headers(HeaderUtil.createEntityUpdateAlert("preferences", preferences.getId().toString()))
                .body(result);
    }

    /**
     * GET  /preferences -> get all the preferences.
     */
    @RequestMapping(value = "/preferences",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Preferences> getAll() {
        log.debug("REST request to get all Preferences");
        List<Preferences> preferences = new ArrayList<>();
        if (SecurityUtils.isUserInRole(AuthoritiesConstants.ADMIN)) {
            preferences = preferencesRepository.findAll();
        } else {
            Preferences userPreferences = getUserPreferences().getBody();
            // don't return default value of 10 points in this method
            if (userPreferences.getId() != null) {
                preferences.add(userPreferences);
            }
        }
        return preferences;
    }

    /**
     * GET  /my-preferences -> get the current user's preferences.
     */
    @RequestMapping(value = "/my-preferences",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Preferences> getUserPreferences() {
        String username = SecurityUtils.getCurrentLogin().getUsername();
        log.debug("REST request to get Preferences : {}", username);
        User user = userRepository.findOneByLogin(username).get();

        if (user.getPreferences() != null) {
            return new ResponseEntity<>(user.getPreferences(), HttpStatus.OK);
        } else {
            Preferences defaultPreferences = new Preferences();
            defaultPreferences.setWeeklyGoal(10); // default
            return new ResponseEntity<>(defaultPreferences, HttpStatus.OK);
        }
    }

    /**
     * GET  /preferences/:id -> get the "id" preferences.
     */
    @RequestMapping(value = "/preferences/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Preferences> get(@PathVariable Long id) {
        log.debug("REST request to get Preferences : {}", id);
        return Optional.ofNullable(preferencesRepository.findOne(id))
            .map(preferences -> new ResponseEntity<>(
                preferences,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /preferences/:id -> delete the "id" preferences.
     */
    @RequestMapping(value = "/preferences/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Preferences : {}", id);

        if (SecurityUtils.getCurrentLogin() != null) {
            User user = userRepository.findOneByLogin(SecurityUtils.getCurrentLogin().getUsername()).get();
            user.setPreferences(null);
            userRepository.save(user);
        }

        preferencesRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("preferences", id.toString())).build();
    }

    /**
     * SEARCH  /_search/preferences/:query -> search for the preferences corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/preferences/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Preferences> search(@PathVariable String query) {
    	return Lists.newArrayList();
//        return StreamSupport
//            .stream(preferencesSearchRepository.search(queryString(query)).spliterator(), false)
//            .collect(Collectors.toList());
    }
}

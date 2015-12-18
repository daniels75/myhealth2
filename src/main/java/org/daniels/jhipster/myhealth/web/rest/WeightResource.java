package org.daniels.jhipster.myhealth.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.daniels.jhipster.myhealth.domain.Weight;
import org.daniels.jhipster.myhealth.repository.WeightRepository;
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

/**
 * REST controller for managing Weight.
 */
@RestController
@RequestMapping("/api")
public class WeightResource {

    private final Logger log = LoggerFactory.getLogger(WeightResource.class);
        
    @Inject
    private WeightRepository weightRepository;
    
    /**
     * POST  /weights -> Create a new weight.
     */
    @RequestMapping(value = "/weights",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Weight> createWeight(@Valid @RequestBody Weight weight) throws URISyntaxException {
        log.debug("REST request to save Weight : {}", weight);
        if (weight.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("weight", "idexists", "A new weight cannot already have an ID")).body(null);
        }
        Weight result = weightRepository.save(weight);
        return ResponseEntity.created(new URI("/api/weights/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("weight", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /weights -> Updates an existing weight.
     */
    @RequestMapping(value = "/weights",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Weight> updateWeight(@Valid @RequestBody Weight weight) throws URISyntaxException {
        log.debug("REST request to update Weight : {}", weight);
        if (weight.getId() == null) {
            return createWeight(weight);
        }
        Weight result = weightRepository.save(weight);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("weight", weight.getId().toString()))
            .body(result);
    }

    /**
     * GET  /weights -> get all the weights.
     */
    @RequestMapping(value = "/weights",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Weight> getAllWeights() {
        log.debug("REST request to get all Weights");
        return weightRepository.findAll();
            }

    /**
     * GET  /weights/:id -> get the "id" weight.
     */
    @RequestMapping(value = "/weights/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Weight> getWeight(@PathVariable Long id) {
        log.debug("REST request to get Weight : {}", id);
        Weight weight = weightRepository.findOne(id);
        return Optional.ofNullable(weight)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /weights/:id -> delete the "id" weight.
     */
    @RequestMapping(value = "/weights/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteWeight(@PathVariable Long id) {
        log.debug("REST request to delete Weight : {}", id);
        weightRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("weight", id.toString())).build();
    }
}

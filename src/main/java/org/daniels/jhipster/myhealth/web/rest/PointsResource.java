package org.daniels.jhipster.myhealth.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.daniels.jhipster.myhealth.domain.Points;
import org.daniels.jhipster.myhealth.repository.PointsRepository;
import org.daniels.jhipster.myhealth.web.rest.util.HeaderUtil;
import org.daniels.jhipster.myhealth.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
 * REST controller for managing Points.
 */
@RestController
@RequestMapping("/api")
public class PointsResource {

    private final Logger log = LoggerFactory.getLogger(PointsResource.class);
        
    @Inject
    private PointsRepository pointsRepository;
    
    /**
     * POST  /pointss -> Create a new points.
     */
    @RequestMapping(value = "/pointss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Points> createPoints(@Valid @RequestBody Points points) throws URISyntaxException {
        log.debug("REST request to save Points : {}", points);
        if (points.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("points", "idexists", "A new points cannot already have an ID")).body(null);
        }
        Points result = pointsRepository.save(points);
        return ResponseEntity.created(new URI("/api/pointss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("points", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pointss -> Updates an existing points.
     */
    @RequestMapping(value = "/pointss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Points> updatePoints(@Valid @RequestBody Points points) throws URISyntaxException {
        log.debug("REST request to update Points : {}", points);
        if (points.getId() == null) {
            return createPoints(points);
        }
        Points result = pointsRepository.save(points);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("points", points.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pointss -> get all the pointss.
     */
    @RequestMapping(value = "/pointss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Points>> getAllPointss(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Pointss");
        Page<Points> page = pointsRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/pointss");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /pointss/:id -> get the "id" points.
     */
    @RequestMapping(value = "/pointss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Points> getPoints(@PathVariable Long id) {
        log.debug("REST request to get Points : {}", id);
        Points points = pointsRepository.findOne(id);
        return Optional.ofNullable(points)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /pointss/:id -> delete the "id" points.
     */
    @RequestMapping(value = "/pointss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePoints(@PathVariable Long id) {
        log.debug("REST request to delete Points : {}", id);
        pointsRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("points", id.toString())).build();
    }
}

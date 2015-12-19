package org.daniels.jhipster.myhealth.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.daniels.jhipster.myhealth.domain.DateEntity;
import org.daniels.jhipster.myhealth.repository.DateEntityRepository;
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
 * REST controller for managing DateEntity.
 */
@RestController
@RequestMapping("/api")
public class DateEntityResource {

    private final Logger log = LoggerFactory.getLogger(DateEntityResource.class);
        
    @Inject
    private DateEntityRepository dateEntityRepository;
    
    /**
     * POST  /dateEntitys -> Create a new dateEntity.
     */
    @RequestMapping(value = "/dateEntitys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DateEntity> createDateEntity(@Valid @RequestBody DateEntity dateEntity) throws URISyntaxException {
        log.debug("REST request to save DateEntity : {}", dateEntity);
        if (dateEntity.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("dateEntity", "idexists", "A new dateEntity cannot already have an ID")).body(null);
        }
        DateEntity result = dateEntityRepository.save(dateEntity);
        return ResponseEntity.created(new URI("/api/dateEntitys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("dateEntity", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /dateEntitys -> Updates an existing dateEntity.
     */
    @RequestMapping(value = "/dateEntitys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DateEntity> updateDateEntity(@Valid @RequestBody DateEntity dateEntity) throws URISyntaxException {
        log.debug("REST request to update DateEntity : {}", dateEntity);
        if (dateEntity.getId() == null) {
            return createDateEntity(dateEntity);
        }
        DateEntity result = dateEntityRepository.save(dateEntity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("dateEntity", dateEntity.getId().toString()))
            .body(result);
    }

    /**
     * GET  /dateEntitys -> get all the dateEntitys.
     */
    @RequestMapping(value = "/dateEntitys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<DateEntity>> getAllDateEntitys(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of DateEntitys");
        Page<DateEntity> page = dateEntityRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/dateEntitys");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /dateEntitys/:id -> get the "id" dateEntity.
     */
    @RequestMapping(value = "/dateEntitys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<DateEntity> getDateEntity(@PathVariable Long id) {
        log.debug("REST request to get DateEntity : {}", id);
        DateEntity dateEntity = dateEntityRepository.findOne(id);
        return Optional.ofNullable(dateEntity)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /dateEntitys/:id -> delete the "id" dateEntity.
     */
    @RequestMapping(value = "/dateEntitys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDateEntity(@PathVariable Long id) {
        log.debug("REST request to delete DateEntity : {}", id);
        dateEntityRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("dateEntity", id.toString())).build();
    }
}

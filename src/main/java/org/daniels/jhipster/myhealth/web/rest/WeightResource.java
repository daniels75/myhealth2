package org.daniels.jhipster.myhealth.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.validation.Valid;

import org.daniels.jhipster.myhealth.domain.Weight;
import org.daniels.jhipster.myhealth.repository.UserRepository;
import org.daniels.jhipster.myhealth.repository.WeightRepository;
import org.daniels.jhipster.myhealth.security.AuthoritiesConstants;
import org.daniels.jhipster.myhealth.security.SecurityUtils;
import org.daniels.jhipster.myhealth.web.rest.dto.WeightByPeriod;
import org.daniels.jhipster.myhealth.web.rest.util.HeaderUtil;
import org.daniels.jhipster.myhealth.web.rest.util.PaginationUtil;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.google.common.collect.Lists;

/**
 * REST controller for managing Weight.
 */
@RestController
@RequestMapping("/api")
public class WeightResource {

    private final Logger log = LoggerFactory.getLogger(WeightResource.class);

    @Inject
    private WeightRepository weightRepository;

    @Inject
    private UserRepository userRepository;

    /**
     * POST  /weights -> Create a new weight.
     */
    @RequestMapping(value = "/weights",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Weight> create(@Valid @RequestBody Weight weight) throws URISyntaxException {
        log.debug("REST request to save Weight : {}", weight);
        if (weight.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new weight cannot already have an ID").body(null);
        }
        if (!SecurityUtils.isUserInRole(AuthoritiesConstants.ADMIN)) {
            log.debug("No user passed in, using current user: {}", SecurityUtils.getCurrentLogin());
            weight.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentLogin().getUsername()).get());
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
    public ResponseEntity<Weight> update(@Valid @RequestBody Weight weight) throws URISyntaxException {
        log.debug("REST request to update Weight : {}", weight);
        if (weight.getId() == null) {
            return create(weight);
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
    public ResponseEntity<List<Weight>> getAll(@RequestParam(value = "page", required = false) Integer offset,
                                               @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<Weight> page;
        if (SecurityUtils.isUserInRole(AuthoritiesConstants.ADMIN)) {
            page = weightRepository.findAllByOrderByTimestampDesc(PaginationUtil.generatePageRequest(offset, limit));
        } else {
            page = weightRepository.findAllForCurrentUser(PaginationUtil.generatePageRequest(offset, limit));
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/weights", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bp-by-days -> get all the weigh-ins by last x days.
     */
    @RequestMapping(value = "/weight-by-days/{days}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WeightByPeriod> getByDays(@PathVariable int days) {
        LocalDate today = new LocalDate();
        LocalDate previousDate = today.minusDays(days);
        DateTime daysAgo = previousDate.toDateTimeAtCurrentTime();
        DateTime rightNow = today.toDateTimeAtCurrentTime();

        java.time.LocalDate today1 = java.time.LocalDate.now();
        java.time.LocalDate previousDate1 = today1.minusDays(days);
        ZonedDateTime daysAgo1 = previousDate1.atStartOfDay(ZoneId.systemDefault());
        ZonedDateTime rightNow1 = today1.atStartOfDay(ZoneId.systemDefault());
        
        List<Weight> weighIns = weightRepository.findAllByTimestampBetweenOrderByTimestampDesc(daysAgo1, rightNow1);
        WeightByPeriod response = new WeightByPeriod("Last " + days + " Days", filterByUser(weighIns));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * GET  /bp-by-days -> get all the blood pressure readings for a particular month.
     */
    @RequestMapping(value = "/weight-by-month/{date}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<WeightByPeriod> getByMonth(@PathVariable @DateTimeFormat(pattern = "yyyy-MM") LocalDate date) {
        LocalDate firstDay = date.dayOfMonth().withMinimumValue();
        LocalDate lastDay = date.dayOfMonth().withMaximumValue();

        java.time.LocalDate today1 = java.time.LocalDate.now();
        java.time.LocalDate previousDate1 = java.time.LocalDate.now();
        ZonedDateTime daysAgo1 = previousDate1.atStartOfDay(ZoneId.systemDefault());
        ZonedDateTime rightNow1 = today1.atStartOfDay(ZoneId.systemDefault());
        
        List<Weight> weighIns = weightRepository.
            findAllByTimestampBetweenOrderByTimestampDesc(daysAgo1,
            		rightNow1);

        DateTimeFormatter fmt = org.joda.time.format.DateTimeFormat.forPattern("yyyy-MM");
        String yearAndMonth = fmt.print(firstDay);

        WeightByPeriod response = new WeightByPeriod(yearAndMonth, filterByUser(weighIns));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private List<Weight> filterByUser(List<Weight> readings) {
        Stream<Weight> userReadings = readings.stream()
            .filter(bp -> bp.getUser().getLogin().equals(SecurityUtils.getCurrentLogin()));
        return userReadings.collect(Collectors.toList());
    }

    /**
     * GET  /weights/:id -> get the "id" weight.
     */
    @RequestMapping(value = "/weights/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Weight> get(@PathVariable Long id) {
        log.debug("REST request to get Weight : {}", id);
        return Optional.ofNullable(weightRepository.findOne(id))
            .map(weight -> new ResponseEntity<>(
                weight,
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
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete Weight : {}", id);
        weightRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("weight", id.toString())).build();
    }

    /**
     * SEARCH  /_search/weights/:query -> search for the weight corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/weights/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Weight> search(@PathVariable String query) {
    	return Lists.newArrayList();
//        return StreamSupport
//            .stream(weightSearchRepository.search(queryString(query)).spliterator(), false)
//            .collect(Collectors.toList());
    }
}

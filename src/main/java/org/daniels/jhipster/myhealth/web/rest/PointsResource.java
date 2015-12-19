package org.daniels.jhipster.myhealth.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.DayOfWeek;
import java.time.Year;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

import javax.inject.Inject;
import javax.validation.Valid;

import org.daniels.jhipster.myhealth.domain.Points;
import org.daniels.jhipster.myhealth.repository.PointsRepository;
import org.daniels.jhipster.myhealth.repository.UserRepository;
import org.daniels.jhipster.myhealth.security.AuthoritiesConstants;
import org.daniels.jhipster.myhealth.security.SecurityUtils;
import org.daniels.jhipster.myhealth.web.rest.dto.PointsPerMonth;
import org.daniels.jhipster.myhealth.web.rest.dto.PointsPerWeek;
import org.daniels.jhipster.myhealth.web.rest.util.HeaderUtil;
import org.daniels.jhipster.myhealth.web.rest.util.PaginationUtil;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
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
 * REST controller for managing Points.
 */
@RestController
@RequestMapping("/api")
public class PointsResource {

	private final Logger log = LoggerFactory.getLogger(PointsResource.class);

	@Inject
	private PointsRepository pointsRepository;

	@Inject
	private UserRepository userRepository;

	/**
	 * POST /points -> Create a new points.
	 */
	@RequestMapping(value = "/points", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Points> create(@Valid @RequestBody Points points) throws URISyntaxException {
		log.debug("REST request to save Points : {}", points);
		if (points.getId() != null) {
			return ResponseEntity.badRequest().header("Failure", "A new points cannot already have an ID").body(null);
		}
		if (!SecurityUtils.isUserInRole(AuthoritiesConstants.ADMIN)) {
			log.debug("No user passed in, using current user: {}", SecurityUtils.getCurrentLogin().getUsername());
			points.setUser(userRepository.findOneByLogin(SecurityUtils.getCurrentLogin().getUsername()).get());
		}
		Points result = pointsRepository.save(points);
		return ResponseEntity.created(new URI("/api/points/" + result.getId()))
				.headers(HeaderUtil.createEntityCreationAlert("points", result.getId().toString())).body(result);
	}

	/**
	 * PUT /points -> Updates an existing points.
	 */
	@RequestMapping(value = "/points", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Points> update(@Valid @RequestBody Points points) throws URISyntaxException {
		log.debug("REST request to update Points : {}", points);
		if (points.getId() == null) {
			return create(points);
		}
		Points result = pointsRepository.save(points);
		pointsRepository.save(points);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert("points", points.getId().toString()))
				.body(result);
	}

	/**
	 * GET /points -> get all the points.
	 */
	@RequestMapping(value = "/points", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<Points>> getAll(@RequestParam(value = "page", required = false) Integer offset,
			@RequestParam(value = "per_page", required = false) Integer limit) throws URISyntaxException {
		Page<Points> page;
		if (SecurityUtils.isUserInRole(AuthoritiesConstants.ADMIN)) {
			page = pointsRepository.findAllByOrderByDateDesc(PaginationUtil.generatePageRequest(offset, limit));
		} else {
			page = pointsRepository.findAllForCurrentUser(PaginationUtil.generatePageRequest(offset, limit));
		}
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/points", offset, limit);
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	/**
	 * GET /points -> get all the points for the current week.
	 */
	@RequestMapping(value = "/points-this-week")
	@Timed
	public ResponseEntity<PointsPerWeek> getPointsThisWeek(TimeZone timeZone) {
		// TODO: allow user to set timezone in preferences.
		// On Heroku, timeZone.getID() is "Etc/UTC"
		DateTimeZone usersTimeZone = DateTimeZone.forID("America/Denver");
		log.debug("Getting points for week with timezone: {}", usersTimeZone);

		// Get current date
		LocalDate now = new LocalDate(usersTimeZone);
		// Get first day of week
		LocalDate startOfWeek = now.withDayOfWeek(DateTimeConstants.MONDAY);
		// Get last day of week
		LocalDate endOfWeek = now.withDayOfWeek(DateTimeConstants.SUNDAY);
		log.debug("Looking for points between: {} and {}", startOfWeek, endOfWeek);

		
		java.time.LocalDate today = java.time.LocalDate.now();
		java.time.LocalDate monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		java.time.LocalDate sunday = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
		
		System.out.println("------------------------------------------------------");
		java.time.LocalDate startOfWeek1 = java.time.LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		
		// Get last day of week
		java.time.LocalDate endOfWeek1 = java.time.LocalDate.now().with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

		List<Points> points = pointsRepository.findAllByDateBetweenAndUserLogin(monday, sunday,
				SecurityUtils.getCurrentLogin().getUsername());
		System.out.println("------------------------------------------------------");
		return calculatePoints(startOfWeek, points);
	}

	private ResponseEntity<PointsPerWeek> calculatePoints(LocalDate startOfWeek, List<Points> points) {
		Integer numPoints = points.stream().mapToInt(p -> p.getExercise() + p.getMeals() + p.getAlcohol()).sum();

		PointsPerWeek count = new PointsPerWeek(startOfWeek, numPoints);
		return new ResponseEntity<>(count, HttpStatus.OK);
	}

	/**
	 * GET /points -> get all the points for a particular week.
	 */
	@RequestMapping(value = "/points-by-week/{startDate}")
	@Timed
	public ResponseEntity<PointsPerWeek> getPointsByWeek(
			@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate) {
		// Get last day of week
		LocalDate endOfWeek = startDate.withDayOfWeek(DateTimeConstants.SUNDAY);
		
		java.time.LocalDate startOfWeek1 = java.time.LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		// Get last day of week
		java.time.LocalDate endOfWeek1 = java.time.LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
		
		List<Points> points = pointsRepository.findAllByDateBetweenAndUserLogin(startOfWeek1, endOfWeek1,
				SecurityUtils.getCurrentLogin().getUsername());
		return calculatePoints(startDate, points);
	}

	/**
	 * GET /points -> get all the points for a particular current month.
	 */
	@RequestMapping(value = "/points-by-month/{yearWithMonth}")
	@Timed
	public ResponseEntity<PointsPerMonth> getPointsByMonth(
			@PathVariable @DateTimeFormat(pattern = "yyyy-MM") LocalDate yearWithMonth) {
		// Get ast day of the month
		LocalDate endOfMonth = yearWithMonth.dayOfMonth().withMaximumValue();
		
		
		java.time.LocalDate yearWithMonth1 = java.time.LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		// Get last day of week
		java.time.LocalDate endOfMonth1 = java.time.LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
		
		List<Points> points = pointsRepository.findAllByDateBetweenAndUserLogin(yearWithMonth1, endOfMonth1,
				SecurityUtils.getCurrentLogin().getUsername());
		PointsPerMonth pointsPerMonth = new PointsPerMonth(yearWithMonth, points);
		return new ResponseEntity<>(pointsPerMonth, HttpStatus.OK);
	}

	/**
	 * GET /points/:id -> get the "id" points.
	 */
	@RequestMapping(value = "/points/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Points> get(@PathVariable Long id) {
		log.debug("REST request to get Points : {}", id);
		return Optional.ofNullable(pointsRepository.findOne(id))
				.map(points -> new ResponseEntity<>(points, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * DELETE /points/:id -> delete the "id" points.
	 */
	@RequestMapping(value = "/points/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		log.debug("REST request to delete Points : {}", id);
		pointsRepository.delete(id);
		return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("points", id.toString())).build();
	}

	/**
	 * SEARCH /_search/points/:query -> search for the points corresponding to
	 * the query.
	 */
	@RequestMapping(value = "/_search/points/{query}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public List<Points> search(@PathVariable String query) {
		return Lists.newArrayList();
//		return StreamSupport.stream(pointsSearchRepository.search(queryString(query)).spliterator(), false)
//				.collect(Collectors.toList());
	}
}

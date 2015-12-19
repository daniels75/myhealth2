package org.daniels.jhipster.myhealth.web.rest;

import org.daniels.jhipster.myhealth.Application;
import org.daniels.jhipster.myhealth.domain.DateEntity;
import org.daniels.jhipster.myhealth.repository.DateEntityRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the DateEntityResource REST controller.
 *
 * @see DateEntityResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class DateEntityResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));


    private static final LocalDate DEFAULT_FIRST_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FIRST_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final ZonedDateTime DEFAULT_SECOND_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_SECOND_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_SECOND_DATE_STR = dateTimeFormatter.format(DEFAULT_SECOND_DATE);

    private static final Integer DEFAULT_TEST = 1;
    private static final Integer UPDATED_TEST = 2;

    @Inject
    private DateEntityRepository dateEntityRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDateEntityMockMvc;

    private DateEntity dateEntity;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DateEntityResource dateEntityResource = new DateEntityResource();
        ReflectionTestUtils.setField(dateEntityResource, "dateEntityRepository", dateEntityRepository);
        this.restDateEntityMockMvc = MockMvcBuilders.standaloneSetup(dateEntityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        dateEntity = new DateEntity();
        dateEntity.setFirstDate(DEFAULT_FIRST_DATE);
        dateEntity.setSecondDate(DEFAULT_SECOND_DATE);
        dateEntity.setTest(DEFAULT_TEST);
    }

    @Test
    @Transactional
    public void createDateEntity() throws Exception {
        int databaseSizeBeforeCreate = dateEntityRepository.findAll().size();

        // Create the DateEntity

        restDateEntityMockMvc.perform(post("/api/dateEntitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dateEntity)))
                .andExpect(status().isCreated());

        // Validate the DateEntity in the database
        List<DateEntity> dateEntitys = dateEntityRepository.findAll();
        assertThat(dateEntitys).hasSize(databaseSizeBeforeCreate + 1);
        DateEntity testDateEntity = dateEntitys.get(dateEntitys.size() - 1);
        assertThat(testDateEntity.getFirstDate()).isEqualTo(DEFAULT_FIRST_DATE);
        assertThat(testDateEntity.getSecondDate()).isEqualTo(DEFAULT_SECOND_DATE);
        assertThat(testDateEntity.getTest()).isEqualTo(DEFAULT_TEST);
    }

    @Test
    @Transactional
    public void checkFirstDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = dateEntityRepository.findAll().size();
        // set the field null
        dateEntity.setFirstDate(null);

        // Create the DateEntity, which fails.

        restDateEntityMockMvc.perform(post("/api/dateEntitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dateEntity)))
                .andExpect(status().isBadRequest());

        List<DateEntity> dateEntitys = dateEntityRepository.findAll();
        assertThat(dateEntitys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSecondDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = dateEntityRepository.findAll().size();
        // set the field null
        dateEntity.setSecondDate(null);

        // Create the DateEntity, which fails.

        restDateEntityMockMvc.perform(post("/api/dateEntitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dateEntity)))
                .andExpect(status().isBadRequest());

        List<DateEntity> dateEntitys = dateEntityRepository.findAll();
        assertThat(dateEntitys).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDateEntitys() throws Exception {
        // Initialize the database
        dateEntityRepository.saveAndFlush(dateEntity);

        // Get all the dateEntitys
        restDateEntityMockMvc.perform(get("/api/dateEntitys?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(dateEntity.getId().intValue())))
                .andExpect(jsonPath("$.[*].firstDate").value(hasItem(DEFAULT_FIRST_DATE.toString())))
                .andExpect(jsonPath("$.[*].secondDate").value(hasItem(DEFAULT_SECOND_DATE_STR)))
                .andExpect(jsonPath("$.[*].test").value(hasItem(DEFAULT_TEST)));
    }

    @Test
    @Transactional
    public void getDateEntity() throws Exception {
        // Initialize the database
        dateEntityRepository.saveAndFlush(dateEntity);

        // Get the dateEntity
        restDateEntityMockMvc.perform(get("/api/dateEntitys/{id}", dateEntity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(dateEntity.getId().intValue()))
            .andExpect(jsonPath("$.firstDate").value(DEFAULT_FIRST_DATE.toString()))
            .andExpect(jsonPath("$.secondDate").value(DEFAULT_SECOND_DATE_STR))
            .andExpect(jsonPath("$.test").value(DEFAULT_TEST));
    }

    @Test
    @Transactional
    public void getNonExistingDateEntity() throws Exception {
        // Get the dateEntity
        restDateEntityMockMvc.perform(get("/api/dateEntitys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDateEntity() throws Exception {
        // Initialize the database
        dateEntityRepository.saveAndFlush(dateEntity);

		int databaseSizeBeforeUpdate = dateEntityRepository.findAll().size();

        // Update the dateEntity
        dateEntity.setFirstDate(UPDATED_FIRST_DATE);
        dateEntity.setSecondDate(UPDATED_SECOND_DATE);
        dateEntity.setTest(UPDATED_TEST);

        restDateEntityMockMvc.perform(put("/api/dateEntitys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(dateEntity)))
                .andExpect(status().isOk());

        // Validate the DateEntity in the database
        List<DateEntity> dateEntitys = dateEntityRepository.findAll();
        assertThat(dateEntitys).hasSize(databaseSizeBeforeUpdate);
        DateEntity testDateEntity = dateEntitys.get(dateEntitys.size() - 1);
        assertThat(testDateEntity.getFirstDate()).isEqualTo(UPDATED_FIRST_DATE);
        assertThat(testDateEntity.getSecondDate()).isEqualTo(UPDATED_SECOND_DATE);
        assertThat(testDateEntity.getTest()).isEqualTo(UPDATED_TEST);
    }

    @Test
    @Transactional
    public void deleteDateEntity() throws Exception {
        // Initialize the database
        dateEntityRepository.saveAndFlush(dateEntity);

		int databaseSizeBeforeDelete = dateEntityRepository.findAll().size();

        // Get the dateEntity
        restDateEntityMockMvc.perform(delete("/api/dateEntitys/{id}", dateEntity.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<DateEntity> dateEntitys = dateEntityRepository.findAll();
        assertThat(dateEntitys).hasSize(databaseSizeBeforeDelete - 1);
    }
}

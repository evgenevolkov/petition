package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.Application;
import com.mycompany.myapp.domain.Petition;
import com.mycompany.myapp.repository.PetitionRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PetitionResource REST controller.
 *
 * @see PetitionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PetitionResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";
    private static final String DEFAULT_BODY = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_BODY = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATION_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATION_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATION_DATE_TIME_STR = dateTimeFormatter.format(DEFAULT_CREATION_DATE_TIME);

    @Inject
    private PetitionRepository petitionRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPetitionMockMvc;

    private Petition petition;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PetitionResource petitionResource = new PetitionResource();
        ReflectionTestUtils.setField(petitionResource, "petitionRepository", petitionRepository);
        this.restPetitionMockMvc = MockMvcBuilders.standaloneSetup(petitionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        petition = new Petition();
        petition.setTitle(DEFAULT_TITLE);
        petition.setBody(DEFAULT_BODY);
        petition.setCreationDateTime(DEFAULT_CREATION_DATE_TIME);
    }

    @Test
    @Transactional
    public void createPetition() throws Exception {
        int databaseSizeBeforeCreate = petitionRepository.findAll().size();

        // Create the Petition

        restPetitionMockMvc.perform(post("/api/petitions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(petition)))
                .andExpect(status().isCreated());

        // Validate the Petition in the database
        List<Petition> petitions = petitionRepository.findAll();
        assertThat(petitions).hasSize(databaseSizeBeforeCreate + 1);
        Petition testPetition = petitions.get(petitions.size() - 1);
        assertThat(testPetition.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testPetition.getBody()).isEqualTo(DEFAULT_BODY);
        assertThat(testPetition.getCreationDateTime()).isEqualTo(DEFAULT_CREATION_DATE_TIME);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = petitionRepository.findAll().size();
        // set the field null
        petition.setTitle(null);

        // Create the Petition, which fails.

        restPetitionMockMvc.perform(post("/api/petitions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(petition)))
                .andExpect(status().isBadRequest());

        List<Petition> petitions = petitionRepository.findAll();
        assertThat(petitions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBodyIsRequired() throws Exception {
        int databaseSizeBeforeTest = petitionRepository.findAll().size();
        // set the field null
        petition.setBody(null);

        // Create the Petition, which fails.

        restPetitionMockMvc.perform(post("/api/petitions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(petition)))
                .andExpect(status().isBadRequest());

        List<Petition> petitions = petitionRepository.findAll();
        assertThat(petitions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreationDateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = petitionRepository.findAll().size();
        // set the field null
        petition.setCreationDateTime(null);

        // Create the Petition, which fails.

        restPetitionMockMvc.perform(post("/api/petitions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(petition)))
                .andExpect(status().isBadRequest());

        List<Petition> petitions = petitionRepository.findAll();
        assertThat(petitions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPetitions() throws Exception {
        // Initialize the database
        petitionRepository.saveAndFlush(petition);

        // Get all the petitions
        restPetitionMockMvc.perform(get("/api/petitions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(petition.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY.toString())))
                .andExpect(jsonPath("$.[*].creationDateTime").value(hasItem(DEFAULT_CREATION_DATE_TIME_STR)));
    }

    @Test
    @Transactional
    public void getPetition() throws Exception {
        // Initialize the database
        petitionRepository.saveAndFlush(petition);

        // Get the petition
        restPetitionMockMvc.perform(get("/api/petitions/{id}", petition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(petition.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.body").value(DEFAULT_BODY.toString()))
            .andExpect(jsonPath("$.creationDateTime").value(DEFAULT_CREATION_DATE_TIME_STR));
    }

    @Test
    @Transactional
    public void getNonExistingPetition() throws Exception {
        // Get the petition
        restPetitionMockMvc.perform(get("/api/petitions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePetition() throws Exception {
        // Initialize the database
        petitionRepository.saveAndFlush(petition);

		int databaseSizeBeforeUpdate = petitionRepository.findAll().size();

        // Update the petition
        petition.setTitle(UPDATED_TITLE);
        petition.setBody(UPDATED_BODY);
        petition.setCreationDateTime(UPDATED_CREATION_DATE_TIME);

        restPetitionMockMvc.perform(put("/api/petitions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(petition)))
                .andExpect(status().isOk());

        // Validate the Petition in the database
        List<Petition> petitions = petitionRepository.findAll();
        assertThat(petitions).hasSize(databaseSizeBeforeUpdate);
        Petition testPetition = petitions.get(petitions.size() - 1);
        assertThat(testPetition.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testPetition.getBody()).isEqualTo(UPDATED_BODY);
        assertThat(testPetition.getCreationDateTime()).isEqualTo(UPDATED_CREATION_DATE_TIME);
    }

    @Test
    @Transactional
    public void deletePetition() throws Exception {
        // Initialize the database
        petitionRepository.saveAndFlush(petition);

		int databaseSizeBeforeDelete = petitionRepository.findAll().size();

        // Get the petition
        restPetitionMockMvc.perform(delete("/api/petitions/{id}", petition.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Petition> petitions = petitionRepository.findAll();
        assertThat(petitions).hasSize(databaseSizeBeforeDelete - 1);
    }
}

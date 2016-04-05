package com.zenika.csd.tweeter.web.rest;

import com.zenika.csd.tweeter.TweeterApp;
import com.zenika.csd.tweeter.domain.Tweet;
import com.zenika.csd.tweeter.repository.TweetRepository;
import com.zenika.csd.tweeter.service.TweetService;

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
 * Test class for the TweetResource REST controller.
 *
 * @see TweetResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TweeterApp.class)
@WebAppConfiguration
@IntegrationTest
public class TweetResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final Integer DEFAULT_USERID = 1;
    private static final Integer UPDATED_USERID = 2;

    private static final ZonedDateTime DEFAULT_PUBLICATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_PUBLICATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_PUBLICATION_DATE_STR = dateTimeFormatter.format(DEFAULT_PUBLICATION_DATE);
    private static final String DEFAULT_TWEET = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_TWEET = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private TweetRepository tweetRepository;

    @Inject
    private TweetService tweetService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTweetMockMvc;

    private Tweet tweet;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TweetResource tweetResource = new TweetResource();
        ReflectionTestUtils.setField(tweetResource, "tweetService", tweetService);
        this.restTweetMockMvc = MockMvcBuilders.standaloneSetup(tweetResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        tweet = new Tweet();
        tweet.setUserid(DEFAULT_USERID);
        tweet.setPublication_date(DEFAULT_PUBLICATION_DATE);
        tweet.setTweet(DEFAULT_TWEET);
    }

    @Test
    @Transactional
    public void createTweet() throws Exception {
        int databaseSizeBeforeCreate = tweetRepository.findAll().size();

        // Create the Tweet

        restTweetMockMvc.perform(post("/api/tweets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tweet)))
                .andExpect(status().isCreated());

        // Validate the Tweet in the database
        List<Tweet> tweets = tweetRepository.findAll();
        assertThat(tweets).hasSize(databaseSizeBeforeCreate + 1);
        Tweet testTweet = tweets.get(tweets.size() - 1);
        assertThat(testTweet.getUserid()).isEqualTo(DEFAULT_USERID);
        assertThat(testTweet.getPublication_date()).isEqualTo(DEFAULT_PUBLICATION_DATE);
        assertThat(testTweet.getTweet()).isEqualTo(DEFAULT_TWEET);
    }

    @Test
    @Transactional
    public void checkUseridIsRequired() throws Exception {
        int databaseSizeBeforeTest = tweetRepository.findAll().size();
        // set the field null
        tweet.setUserid(null);

        // Create the Tweet, which fails.

        restTweetMockMvc.perform(post("/api/tweets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tweet)))
                .andExpect(status().isBadRequest());

        List<Tweet> tweets = tweetRepository.findAll();
        assertThat(tweets).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPublication_dateIsRequired() throws Exception {
        int databaseSizeBeforeTest = tweetRepository.findAll().size();
        // set the field null
        tweet.setPublication_date(null);

        // Create the Tweet, which fails.

        restTweetMockMvc.perform(post("/api/tweets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tweet)))
                .andExpect(status().isBadRequest());

        List<Tweet> tweets = tweetRepository.findAll();
        assertThat(tweets).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTweetIsRequired() throws Exception {
        int databaseSizeBeforeTest = tweetRepository.findAll().size();
        // set the field null
        tweet.setTweet(null);

        // Create the Tweet, which fails.

        restTweetMockMvc.perform(post("/api/tweets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tweet)))
                .andExpect(status().isBadRequest());

        List<Tweet> tweets = tweetRepository.findAll();
        assertThat(tweets).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTweets() throws Exception {
        // Initialize the database
        tweetRepository.saveAndFlush(tweet);

        // Get all the tweets
        restTweetMockMvc.perform(get("/api/tweets?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(tweet.getId().intValue())))
                .andExpect(jsonPath("$.[*].userid").value(hasItem(DEFAULT_USERID)))
                .andExpect(jsonPath("$.[*].publication_date").value(hasItem(DEFAULT_PUBLICATION_DATE_STR)))
                .andExpect(jsonPath("$.[*].tweet").value(hasItem(DEFAULT_TWEET.toString())));
    }

    @Test
    @Transactional
    public void getTweet() throws Exception {
        // Initialize the database
        tweetRepository.saveAndFlush(tweet);

        // Get the tweet
        restTweetMockMvc.perform(get("/api/tweets/{id}", tweet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(tweet.getId().intValue()))
            .andExpect(jsonPath("$.userid").value(DEFAULT_USERID))
            .andExpect(jsonPath("$.publication_date").value(DEFAULT_PUBLICATION_DATE_STR))
            .andExpect(jsonPath("$.tweet").value(DEFAULT_TWEET.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTweet() throws Exception {
        // Get the tweet
        restTweetMockMvc.perform(get("/api/tweets/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTweet() throws Exception {
        // Initialize the database
        tweetService.save(tweet);

        int databaseSizeBeforeUpdate = tweetRepository.findAll().size();

        // Update the tweet
        Tweet updatedTweet = new Tweet();
        updatedTweet.setId(tweet.getId());
        updatedTweet.setUserid(UPDATED_USERID);
        updatedTweet.setPublication_date(UPDATED_PUBLICATION_DATE);
        updatedTweet.setTweet(UPDATED_TWEET);

        restTweetMockMvc.perform(put("/api/tweets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTweet)))
                .andExpect(status().isOk());

        // Validate the Tweet in the database
        List<Tweet> tweets = tweetRepository.findAll();
        assertThat(tweets).hasSize(databaseSizeBeforeUpdate);
        Tweet testTweet = tweets.get(tweets.size() - 1);
        assertThat(testTweet.getUserid()).isEqualTo(UPDATED_USERID);
        assertThat(testTweet.getPublication_date()).isEqualTo(UPDATED_PUBLICATION_DATE);
        assertThat(testTweet.getTweet()).isEqualTo(UPDATED_TWEET);
    }

    @Test
    @Transactional
    public void deleteTweet() throws Exception {
        // Initialize the database
        tweetService.save(tweet);

        int databaseSizeBeforeDelete = tweetRepository.findAll().size();

        // Get the tweet
        restTweetMockMvc.perform(delete("/api/tweets/{id}", tweet.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Tweet> tweets = tweetRepository.findAll();
        assertThat(tweets).hasSize(databaseSizeBeforeDelete - 1);
    }
}

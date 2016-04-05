package com.zenika.csd.tweeter.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.zenika.csd.tweeter.domain.Tweet;
import com.zenika.csd.tweeter.service.TweetService;
import com.zenika.csd.tweeter.web.rest.util.HeaderUtil;
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
 * REST controller for managing Tweet.
 */
@RestController
@RequestMapping("/api")
public class TweetResource {

    private final Logger log = LoggerFactory.getLogger(TweetResource.class);
        
    @Inject
    private TweetService tweetService;
    
    /**
     * POST  /tweets : Create a new tweet.
     *
     * @param tweet the tweet to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tweet, or with status 400 (Bad Request) if the tweet has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/tweets",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Tweet> createTweet(@Valid @RequestBody Tweet tweet) throws URISyntaxException {
        log.debug("REST request to save Tweet : {}", tweet);
        if (tweet.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("tweet", "idexists", "A new tweet cannot already have an ID")).body(null);
        }
        Tweet result = tweetService.save(tweet);
        return ResponseEntity.created(new URI("/api/tweets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("tweet", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tweets : Updates an existing tweet.
     *
     * @param tweet the tweet to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tweet,
     * or with status 400 (Bad Request) if the tweet is not valid,
     * or with status 500 (Internal Server Error) if the tweet couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/tweets",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Tweet> updateTweet(@Valid @RequestBody Tweet tweet) throws URISyntaxException {
        log.debug("REST request to update Tweet : {}", tweet);
        if (tweet.getId() == null) {
            return createTweet(tweet);
        }
        Tweet result = tweetService.save(tweet);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("tweet", tweet.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tweets : get all the tweets.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tweets in body
     */
    @RequestMapping(value = "/tweets",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Tweet> getAllTweets() {
        log.debug("REST request to get all Tweets");
        return tweetService.findAll();
    }

    /**
     * GET  /tweets/:id : get the "id" tweet.
     *
     * @param id the id of the tweet to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tweet, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/tweets/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Tweet> getTweet(@PathVariable Long id) {
        log.debug("REST request to get Tweet : {}", id);
        Tweet tweet = tweetService.findOne(id);
        return Optional.ofNullable(tweet)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tweets/:id : delete the "id" tweet.
     *
     * @param id the id of the tweet to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/tweets/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTweet(@PathVariable Long id) {
        log.debug("REST request to delete Tweet : {}", id);
        tweetService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("tweet", id.toString())).build();
    }

}

package com.zenika.csd.tweeter.service.impl;

import com.zenika.csd.tweeter.service.TweetService;
import com.zenika.csd.tweeter.domain.Tweet;
import com.zenika.csd.tweeter.repository.TweetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Tweet.
 */
@Service
@Transactional
public class TweetServiceImpl implements TweetService{

    private final Logger log = LoggerFactory.getLogger(TweetServiceImpl.class);
    
    @Inject
    private TweetRepository tweetRepository;
    
    /**
     * Save a tweet.
     * 
     * @param tweet the entity to save
     * @return the persisted entity
     */
    public Tweet save(Tweet tweet) {
        log.debug("Request to save Tweet : {}", tweet);
        Tweet result = tweetRepository.save(tweet);
        return result;
    }

    /**
     *  Get all the tweets.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Tweet> findAll() {
        log.debug("Request to get all Tweets");
        List<Tweet> result = tweetRepository.findAll();
        return result;
    }

    /**
     *  Get one tweet by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Tweet findOne(Long id) {
        log.debug("Request to get Tweet : {}", id);
        Tweet tweet = tweetRepository.findOne(id);
        return tweet;
    }

    /**
     *  Delete the  tweet by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Tweet : {}", id);
        tweetRepository.delete(id);
    }
}

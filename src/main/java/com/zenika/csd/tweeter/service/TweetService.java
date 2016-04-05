package com.zenika.csd.tweeter.service;

import com.zenika.csd.tweeter.domain.Tweet;

import java.util.List;

/**
 * Service Interface for managing Tweet.
 */
public interface TweetService {

    /**
     * Save a tweet.
     * 
     * @param tweet the entity to save
     * @return the persisted entity
     */
    Tweet save(Tweet tweet);

    /**
     *  Get all the tweets.
     *  
     *  @return the list of entities
     */
    List<Tweet> findAll();

    /**
     *  Get the "id" tweet.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Tweet findOne(Long id);

    /**
     *  Delete the "id" tweet.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);
}

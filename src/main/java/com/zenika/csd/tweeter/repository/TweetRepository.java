package com.zenika.csd.tweeter.repository;

import com.zenika.csd.tweeter.domain.Tweet;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Tweet entity.
 */
public interface TweetRepository extends JpaRepository<Tweet,Long> {

    @Query("select tweet from Tweet tweet where tweet.tweetuserid.login = ?#{principal.username}")
    List<Tweet> findByTweetuseridIsCurrentUser();

}

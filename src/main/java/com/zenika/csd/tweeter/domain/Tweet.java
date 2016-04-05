package com.zenika.csd.tweeter.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Tweet.
 */
@Entity
@Table(name = "tweet")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Tweet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "userid", nullable = false)
    private Integer userid;

    @NotNull
    @Column(name = "publication_date", nullable = false)
    private ZonedDateTime publication_date;

    @NotNull
    @Size(max = 140)
    @Column(name = "tweet", length = 140, nullable = false)
    private String tweet;

    @ManyToOne
    private User tweetuserid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public ZonedDateTime getPublication_date() {
        return publication_date;
    }

    public void setPublication_date(ZonedDateTime publication_date) {
        this.publication_date = publication_date;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(String tweet) {
        this.tweet = tweet;
    }

    public User getTweetuserid() {
        return tweetuserid;
    }

    public void setTweetuserid(User user) {
        this.tweetuserid = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tweet tweet = (Tweet) o;
        if(tweet.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, tweet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Tweet{" +
            "id=" + id +
            ", userid='" + userid + "'" +
            ", publication_date='" + publication_date + "'" +
            ", tweet='" + tweet + "'" +
            '}';
    }
}

package com.bot.cloudy.repository;

import com.bot.cloudy.model.Subscription;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends MongoRepository<Subscription, Long> {

    Subscription findByUserId(long userId);

    void deleteByUserId(long userId);
}

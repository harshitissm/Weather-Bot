package com.bot.cloudy.service;

import com.bot.cloudy.model.Subscription;
import com.bot.cloudy.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public void addUserSubscription(Long userId, String state) {
        Subscription subscription = new Subscription(userId, state);
        subscriptionRepository.save(subscription);
    }

    public List<Subscription> getAllUsers() {
        return subscriptionRepository.findAll();
    }

    public String getUserSubscription(long userId) {
        Subscription subscription = subscriptionRepository.findByUserId(userId);
        return (subscription != null) ? subscription.getState() : null;
    }

    public void deleteUserSubscription(Long userId) {
        subscriptionRepository.deleteByUserId(userId);
    }
}

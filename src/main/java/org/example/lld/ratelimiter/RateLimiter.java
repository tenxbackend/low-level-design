package org.example.lld.ratelimiter;


import java.time.Clock;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimiter {

    private RateLimitingStrategy rateLimitingStrategy;

    public RateLimiter(RateLimitingStrategy strategy) {
        this.rateLimitingStrategy = strategy;
    }

    public boolean isAllowed(String key) {

        // logic for rate limiting
        return this.rateLimitingStrategy.isAllowed(key);

    }
}


interface RateLimitingStrategy {
    boolean isAllowed(String key);
}


class RateLimiterDemo {
    public static void main(String[] args) {

        ConcurrentHashMap<String, TokenBucketState> store = new ConcurrentHashMap<>();
        TokenBucketConfig config = new TokenBucketConfig(5, 1, Duration.ofSeconds(15));
        RateLimitingStrategy strategy = new TokenBucketStrategy(store, config, Clock.systemUTC());

        RateLimiter rateLimiter = new RateLimiter(strategy);

        for(int i=1;i<=50;i++){
            boolean isAllowed = rateLimiter.isAllowed("user1_otp_api");
            System.out.println("Request " + i + " is allowed: " + isAllowed);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO: handle exception
            }
        }


    }
}
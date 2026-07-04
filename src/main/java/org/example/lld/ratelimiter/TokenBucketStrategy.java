package org.example.lld.ratelimiter;


// capacity
// refillRate -> refill, refillTime

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

record TokenBucketConfig(int capacity, int tokensToAdd, Duration refillTime) {
}

// refilling will be done lazily
record TokenBucketState(int availableTokens, Instant lastRefilledAt) {
}

public class TokenBucketStrategy implements RateLimitingStrategy {

    private final ConcurrentHashMap<String, TokenBucketState> bucketStore;
    private final TokenBucketConfig config;
    private final Clock clock;

    public TokenBucketStrategy(ConcurrentHashMap<String, TokenBucketState> bucketStore, TokenBucketConfig config, Clock clock) {
        this.bucketStore = bucketStore;
        this.config = config;
        this.clock = clock;
    }

    @Override
    public boolean isAllowed(String key) {

        Instant now = clock.instant();
        // compute is done as atomic - no thread safety concerns
        AtomicBoolean isAllowed = new AtomicBoolean(false);
        bucketStore.compute(key, (k, bucketState) -> {
            if (Objects.isNull(bucketState)) {
                bucketState = new TokenBucketState(config.capacity(), now);
            } else {
                // bucket does exist
                bucketState = refill(bucketState, now);
            }
            return bucketState = consumeToken(key, bucketState, isAllowed);
        });

        return isAllowed.get();

    }

    private TokenBucketState consumeToken(String key, TokenBucketState bucketState, AtomicBoolean isAllowed) {
        if (bucketState.availableTokens() <= 0) {
            isAllowed.set(false);
        } else {
            bucketState = new TokenBucketState(bucketState.availableTokens() - 1, bucketState.lastRefilledAt());
            isAllowed.set(true);
        }
        return bucketState;
    }

    private TokenBucketState refill(TokenBucketState bucketState, Instant now) {
        Duration timeElapsed = Duration.between(bucketState.lastRefilledAt(), now);

        int intervalsPassed = (int) (timeElapsed.toMillis() / config.refillTime().toMillis());
        int tokensToAdd = intervalsPassed * config.tokensToAdd();
        int availableTokens = Math.min(config.capacity(), bucketState.availableTokens() + tokensToAdd);

        // using now here is BUG

        long timeTakenToAddTokens = intervalsPassed * config.refillTime().toMillis();
        bucketState = new TokenBucketState(availableTokens, bucketState.lastRefilledAt().plusMillis(timeTakenToAddTokens));
        // bug explanation --
        // 10 seconds 1 token is added
        // 10:00:00  -> 1
        // 10:00:15 - request comes -> // time diff = 15 seconds, -> 1 , lastRefilled as now -> 10:00:10 . 1
        // 10:00:20 - request 2 comes -> / / time diff - 5 seconds, -> 0 tokens to add.-XXX wrong
        return bucketState;
    }
}

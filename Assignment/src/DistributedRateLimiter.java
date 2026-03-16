import java.util.*;
import java.util.concurrent.*;
class TokenBucket
{
    int tokens;
    int maxTokens;
    long lastRefillTime;
    long refillInterval;
    TokenBucket(int maxTokens, long refillInterval)
    {
        this.maxTokens = maxTokens;
        this.tokens = maxTokens;
        this.refillInterval = refillInterval;
        this.lastRefillTime = System.currentTimeMillis();
    }
    synchronized boolean allowRequest()
    {
        refill();
        if (tokens > 0)
        {
            tokens--;
            return true;
        }
        return false;
    }
    synchronized int getRemainingTokens()
    {
        refill();
        return tokens;
    }
    synchronized long getRetryAfter()
    {
        long now = System.currentTimeMillis();
        long nextRefill = lastRefillTime + refillInterval;
        return Math.max(0, (nextRefill - now) / 1000);
    }
    private void refill()
    {
        long now = System.currentTimeMillis();
        if (now - lastRefillTime >= refillInterval)
        {
            tokens = maxTokens;
            lastRefillTime = now;
        }
    }
}
public class DistributedRateLimiter
{
    private Map<String, TokenBucket> clients = new ConcurrentHashMap<>();
    private int LIMIT = 1000;
    private long WINDOW = 3600000;
    public String checkRateLimit(String clientId)
    {
        clients.putIfAbsent(clientId, new TokenBucket(LIMIT, WINDOW));
        TokenBucket bucket = clients.get(clientId);
        if (bucket.allowRequest())
        {
            return "Allowed (" + bucket.getRemainingTokens() + " requests remaining)";
        }
        else
        {
            return "Denied (0 requests remaining, retry after " + bucket.getRetryAfter() + "s)";
        }
    }
    public Map<String, Object> getRateLimitStatus(String clientId)
    {
        Map<String, Object> status = new HashMap<>();
        TokenBucket bucket = clients.get(clientId);
        if (bucket == null)
        {
            status.put("used", 0);
            status.put("limit", LIMIT);
            status.put("reset", System.currentTimeMillis() + WINDOW);
            return status;
        }
        int remaining = bucket.getRemainingTokens();
        status.put("used", LIMIT - remaining);
        status.put("limit", LIMIT);
        status.put("reset", bucket.lastRefillTime + WINDOW);
        return status;
    }
    public static void main(String args[])
    {
        DistributedRateLimiter limiter = new DistributedRateLimiter();
        System.out.println(limiter.checkRateLimit("abc123"));
        System.out.println(limiter.checkRateLimit("abc123"));
        Map<String, Object> status = limiter.getRateLimitStatus("abc123");
        System.out.println(status);
    }
}
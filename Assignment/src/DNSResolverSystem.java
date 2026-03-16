import java.util.*;
import java.util.concurrent.*;

// 1. Entry class to store domain metadata
class DNSEntry {
    String domain;
    String ipAddress;
    long expiryTime;

    public DNSEntry(String domain, String ipAddress, int ttlSeconds) {
        this.domain = domain;
        this.ipAddress = ipAddress;
        // Current time + TTL in milliseconds
        this.expiryTime = System.currentTimeMillis() + (ttlSeconds * 1000L);
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTime;
    }
}

public class DNSResolverSystem {
    private final int MAX_CAPACITY;
    private final Map<String, DNSEntry> cache;

    // Performance Metrics
    private int hits = 0;
    private int misses = 0;

    public DNSResolverSystem(int capacity) {
        this.MAX_CAPACITY = capacity;
        // LinkedHashMap with accessOrder=true enables LRU (Least Recently Used)
        this.cache = Collections.synchronizedMap(new LinkedHashMap<String, DNSEntry>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, DNSEntry> eldest) {
                return size() > MAX_CAPACITY;
            }
        });

        // 2. Background thread to clean expired entries every 30 seconds
        ScheduledExecutorService cleaner = Executors.newSingleThreadScheduledExecutor();
        cleaner.scheduleAtFixedRate(this::cleanupExpired, 30, 30, TimeUnit.SECONDS);
    }

    // 3. Resolve method: Check cache -> Handle Expired -> Query Upstream
    public String resolve(String domain) {
        DNSEntry entry = cache.get(domain);

        // Case: Cache Hit and Not Expired
        if (entry != null && !entry.isExpired()) {
            hits++;
            return "Cache HIT -> " + entry.ipAddress;
        }

        // Case: Cache Miss or Expired
        misses++;
        if (entry != null) {
            cache.remove(domain); // Remove if it was just expired
        }

        // Simulate Upstream Query (100ms)
        String ip = "192.168.1." + (new Random().nextInt(254) + 1);
        cache.put(domain, new DNSEntry(domain, ip, 5)); // 5 second TTL for testing

        return "Cache MISS -> Query upstream -> " + ip;
    }

    private void cleanupExpired() {
        synchronized (cache) {
            cache.values().removeIf(DNSEntry::isExpired);
        }
    }

    public void printStats() {
        double ratio = (hits + misses == 0) ? 0 : ((double) hits / (hits + misses)) * 100;
        System.out.printf("--- Stats --- Hits: %d, Misses: %d, Hit Rate: %.1f%%%n", hits, misses, ratio);
    }

    // --- MAIN METHOD ---
    public static void main(String[] args) throws InterruptedException {
        DNSResolverSystem dns = new DNSResolverSystem(3); // Small capacity to test LRU

        System.out.println(dns.resolve("google.com")); // Miss
        System.out.println(dns.resolve("google.com")); // Hit

        dns.resolve("apple.com");
        dns.resolve("netflix.com");
        dns.resolve("amazon.com"); // This should trigger LRU and kick out google.com

        dns.printStats();

        System.out.println("\nWaiting for TTL to expire (6 seconds)...");
        Thread.sleep(6000);

        System.out.println(dns.resolve("amazon.com")); // Should be MISS due to Expiry
        dns.printStats();
    }
}
import java.util.*;
class Event
{
    String url;
    String userId;
    String source;
    Event(String url, String userId, String source)
    {
        this.url = url;
        this.userId = userId;
        this.source = source;
    }
}
public class RealTimeAnalyticsDashboard
{
    private Map<String, Integer> pageViews = new HashMap<>();
    private Map<String, Set<String>> uniqueVisitors = new HashMap<>();
    private Map<String, Integer> trafficSources = new HashMap<>();
    public synchronized void processEvent(Event e)
    {
        pageViews.put(e.url, pageViews.getOrDefault(e.url, 0) + 1);
        uniqueVisitors.computeIfAbsent(e.url, k -> new HashSet<>()).add(e.userId);
        trafficSources.put(e.source, trafficSources.getOrDefault(e.source, 0) + 1);
    }
    public synchronized void getDashboard()
    {
        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());
        pq.addAll(pageViews.entrySet());
        System.out.println("Top Pages:");
        int rank = 1;
        while (!pq.isEmpty() && rank <= 10)
        {
            Map.Entry<String, Integer> entry = pq.poll();
            String url = entry.getKey();
            int views = entry.getValue();
            int unique = uniqueVisitors.getOrDefault(url, Collections.emptySet()).size();
            System.out.println(rank + ". " + url + " - " + views + " views (" + unique + " unique)");
            rank++;
        }
        int total = trafficSources.values().stream().mapToInt(i -> i).sum();
        System.out.println("\nTraffic Sources:");
        for (Map.Entry<String, Integer> e : trafficSources.entrySet())
        {
            double percent = total == 0 ? 0 : (e.getValue() * 100.0) / total;
            System.out.println(e.getKey() + ": " + String.format("%.0f", percent) + "%");
        }
    }
    public static void main(String args[])
    {
        RealTimeAnalyticsDashboard dashboard = new RealTimeAnalyticsDashboard();
        dashboard.processEvent(new Event("/article/breaking-news", "user_123", "google"));
        dashboard.processEvent(new Event("/article/breaking-news", "user_456", "facebook"));
        dashboard.processEvent(new Event("/sports/championship", "user_789", "direct"));
        dashboard.processEvent(new Event("/article/breaking-news", "user_123", "google"));
        dashboard.getDashboard();
    }
}
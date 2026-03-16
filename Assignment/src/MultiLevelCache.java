import java.util.*;
class VideoData
{
    String id;
    String content;
    VideoData(String id, String content)
    {
        this.id = id;
        this.content = content;
    }
}
public class MultiLevelCache
{
    private LinkedHashMap<String, VideoData> l1;
    private LinkedHashMap<String, VideoData> l2;
    private Map<String, VideoData> database = new HashMap<>();
    private Map<String, Integer> accessCount = new HashMap<>();
    private int L1_CAP = 10000;
    private int L2_CAP = 100000;
    private int l1Hits = 0, l2Hits = 0, l3Hits = 0, requests = 0;
    public MultiLevelCache()
    {
        l1 = new LinkedHashMap<String, VideoData>(L1_CAP, 0.75f, true)
        {
            protected boolean removeEldestEntry(Map.Entry<String, VideoData> e)
            {
                return size() > L1_CAP;
            }
        };
        l2 = new LinkedHashMap<String, VideoData>(L2_CAP, 0.75f, true)
        {
            protected boolean removeEldestEntry(Map.Entry<String, VideoData> e)
            {
                return size() > L2_CAP;
            }
        };
    }
    public String getVideo(String id)
    {
        requests++;
        if (l1.containsKey(id))
        {
            l1Hits++;
            accessCount.put(id, accessCount.getOrDefault(id, 0) + 1);
            return "L1 Cache HIT (0.5ms)";
        }
        if (l2.containsKey(id))
        {
            l2Hits++;
            VideoData v = l2.get(id);
            accessCount.put(id, accessCount.getOrDefault(id, 0) + 1);
            if (accessCount.get(id) > 2)
            {
                l1.put(id, v);
            }
            return "L1 MISS → L2 HIT (5ms) → Promoted to L1";
        }
        VideoData v = database.get(id);
        if (v != null)
        {
            l3Hits++;
            l2.put(id, v);
            accessCount.put(id, 1);
            return "L1 MISS → L2 MISS → L3 Database HIT (150ms) → Added to L2";
        }
        return "Video Not Found";
    }
    public void addVideoToDatabase(String id, String content)
    {
        database.put(id, new VideoData(id, content));
    }
    public void invalidate(String id)
    {
        l1.remove(id);
        l2.remove(id);
        accessCount.remove(id);
    }
    public void getStatistics()
    {
        double l1Rate = requests == 0 ? 0 : (l1Hits * 100.0 / requests);
        double l2Rate = requests == 0 ? 0 : (l2Hits * 100.0 / requests);
        double l3Rate = requests == 0 ? 0 : (l3Hits * 100.0 / requests);
        System.out.println("L1: Hit Rate " + String.format("%.1f", l1Rate) + "%, Avg Time: 0.5ms");
        System.out.println("L2: Hit Rate " + String.format("%.1f", l2Rate) + "%, Avg Time: 5ms");
        System.out.println("L3: Hit Rate " + String.format("%.1f", l3Rate) + "%, Avg Time: 150ms");
        double overall = ((l1Hits + l2Hits + l3Hits) * 100.0) / requests;
        System.out.println("Overall: Hit Rate " + String.format("%.1f", overall) + "%");
    }
    public static void main(String args[])
    {
        MultiLevelCache cache = new MultiLevelCache();
        cache.addVideoToDatabase("video_123", "Movie Data");
        cache.addVideoToDatabase("video_999", "Documentary");
        System.out.println(cache.getVideo("video_123"));
        System.out.println(cache.getVideo("video_123"));
        System.out.println(cache.getVideo("video_999"));
        cache.getStatistics();
    }
}
import java.util.*;
class TrieNode
{
    Map<Character, TrieNode> children = new HashMap<>();
    boolean isEnd;
}
public class AutoCompleteSystem
{
    private TrieNode root = new TrieNode();
    private Map<String, Integer> frequency = new HashMap<>();
    public void addQuery(String query)
    {
        frequency.put(query, frequency.getOrDefault(query, 0) + 1);
        TrieNode node = root;
        for (char c : query.toCharArray())
        {
            node.children.putIfAbsent(c, new TrieNode());
            node = node.children.get(c);
        }
        node.isEnd = true;
    }
    public List<String> search(String prefix)
    {
        TrieNode node = root;
        for (char c : prefix.toCharArray())
        {
            if (!node.children.containsKey(c)) return new ArrayList<>();
            node = node.children.get(c);
        }
        List<String> results = new ArrayList<>();
        dfs(node, prefix, results);
        PriorityQueue<String> pq = new PriorityQueue<>(
                (a, b) -> frequency.get(a) - frequency.get(b)
        );
        for (String r : results)
        {
            pq.offer(r);
            if (pq.size() > 10) pq.poll();
        }
        List<String> top = new ArrayList<>();
        while (!pq.isEmpty()) top.add(pq.poll());
        Collections.reverse(top);
        return top;
    }
    private void dfs(TrieNode node, String path, List<String> results)
    {
        if (node.isEnd) results.add(path);
        for (Map.Entry<Character, TrieNode> e : node.children.entrySet())
        {
            dfs(e.getValue(), path + e.getKey(), results);
        }
    }
    public void updateFrequency(String query)
    {
        addQuery(query);
    }
    public static void main(String args[])
    {
        AutoCompleteSystem system = new AutoCompleteSystem();
        system.addQuery("java tutorial");
        system.addQuery("javascript");
        system.addQuery("java download");
        system.addQuery("java tutorial");
        system.addQuery("java 21 features");
        List<String> suggestions = system.search("jav");
        int rank = 1;
        for (String s : suggestions)
        {
            System.out.println(rank + ". " + s + " (" + system.frequency.get(s) + " searches)");
            rank++;
        }
        system.updateFrequency("java 21 features");
        system.updateFrequency("java 21 features");
    }
}
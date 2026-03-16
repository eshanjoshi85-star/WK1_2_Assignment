import java.util.*;
public class PlagiarismDetector
{
    private Map<String, Set<String>> index = new HashMap<>();
    private Map<String, List<String>> documentNgrams = new HashMap<>();
    private int n = 5;
    public void addDocument(String docId, String text)
    {
        List<String> ngrams = generateNgrams(text);
        documentNgrams.put(docId, ngrams);
        for (String gram : ngrams)
        {
            index.computeIfAbsent(gram, k -> new HashSet<>()).add(docId);
        }
    }
    public void analyzeDocument(String docId)
    {
        List<String> ngrams = documentNgrams.get(docId);
        Map<String, Integer> matchCount = new HashMap<>();
        for (String gram : ngrams)
        {
            Set<String> docs = index.getOrDefault(gram, Collections.emptySet());
            for (String d : docs)
            {
                if (!d.equals(docId))
                {
                    matchCount.put(d, matchCount.getOrDefault(d, 0) + 1);
                }
            }
        }
        System.out.println("Extracted " + ngrams.size() + " n-grams");
        for (Map.Entry<String, Integer> entry : matchCount.entrySet())
        {
            String otherDoc = entry.getKey();
            int matches = entry.getValue();
            double similarity = (matches * 100.0) / ngrams.size();
            System.out.println("Found " + matches + " matching n-grams with \"" + otherDoc + "\"");
            System.out.println("Similarity: " + String.format("%.1f", similarity) + "%" +
                    (similarity > 60 ? " (PLAGIARISM DETECTED)" : similarity > 10 ? " (suspicious)" : ""));
        }
    }
    private List<String> generateNgrams(String text)
    {
        String[] words = text.toLowerCase().split("\\s+");
        List<String> grams = new ArrayList<>();
        for (int i = 0; i <= words.length - n; i++)
        {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < n; j++)
            {
                if (j > 0) sb.append(" ");
                sb.append(words[i + j]);
            }
            grams.add(sb.toString());
        }
        return grams;
    }
    public static void main(String args[])
    {
        PlagiarismDetector detector = new PlagiarismDetector();
        detector.addDocument("essay_089.txt", "machine learning is a field of artificial intelligence that focuses on data driven models");
        detector.addDocument("essay_092.txt", "machine learning is a field of artificial intelligence that focuses on data driven models and prediction systems");
        detector.addDocument("essay_123.txt", "machine learning is a field of artificial intelligence that focuses on data driven models");
        detector.analyzeDocument("essay_123.txt");
    }
}
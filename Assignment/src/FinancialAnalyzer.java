import java.util.*;
class Transaction
{
    int id;
    int amount;
    String merchant;
    String account;
    long time;
    Transaction(int id, int amount, String merchant, String account, long time)
    {
        this.id = id;
        this.amount = amount;
        this.merchant = merchant;
        this.account = account;
        this.time = time;
    }
}
public class FinancialAnalyzer
{
    public List<List<Integer>> findTwoSum(List<Transaction> txs, int target)
    {
        Map<Integer, Transaction> map = new HashMap<>();
        List<List<Integer>> res = new ArrayList<>();
        for (Transaction t : txs)
        {
            int complement = target - t.amount;
            if (map.containsKey(complement))
            {
                res.add(Arrays.asList(map.get(complement).id, t.id));
            }
            map.put(t.amount, t);
        }
        return res;
    }
    public List<List<Integer>> findTwoSumWithTimeWindow(List<Transaction> txs, int target, long windowMillis)
    {
        Map<Integer, List<Transaction>> map = new HashMap<>();
        List<List<Integer>> res = new ArrayList<>();
        for (Transaction t : txs)
        {
            int complement = target - t.amount;
            if (map.containsKey(complement))
            {
                for (Transaction prev : map.get(complement))
                {
                    if (Math.abs(t.time - prev.time) <= windowMillis)
                    {
                        res.add(Arrays.asList(prev.id, t.id));
                    }
                }
            }
            map.computeIfAbsent(t.amount, k -> new ArrayList<>()).add(t);
        }
        return res;
    }
    public List<List<Integer>> findKSum(List<Transaction> txs, int k, int target)
    {
        List<List<Integer>> res = new ArrayList<>();
        backtrack(txs, k, target, 0, new ArrayList<>(), res);
        return res;
    }
    private void backtrack(List<Transaction> txs, int k, int target, int start, List<Integer> path, List<List<Integer>> res)
    {
        if (k == 0 && target == 0)
        {
            res.add(new ArrayList<>(path));
            return;
        }
        if (k == 0) return;
        for (int i = start; i < txs.size(); i++)
        {
            path.add(txs.get(i).id);
            backtrack(txs, k - 1, target - txs.get(i).amount, i + 1, path, res);
            path.remove(path.size() - 1);
        }
    }
    public List<String> detectDuplicates(List<Transaction> txs)
    {
        Map<String, Set<String>> map = new HashMap<>();
        List<String> res = new ArrayList<>();
        for (Transaction t : txs)
        {
            String key = t.amount + "_" + t.merchant;
            map.computeIfAbsent(key, k -> new HashSet<>()).add(t.account);
        }
        for (Map.Entry<String, Set<String>> e : map.entrySet())
        {
            if (e.getValue().size() > 1)
            {
                res.add(e.getKey() + " accounts:" + e.getValue());
            }
        }
        return res;
    }
    public static void main(String args[])
    {
        List<Transaction> txs = new ArrayList<>();
        txs.add(new Transaction(1, 500, "StoreA", "acc1", 1000));
        txs.add(new Transaction(2, 300, "StoreB", "acc2", 1100));
        txs.add(new Transaction(3, 200, "StoreC", "acc3", 1200));
        txs.add(new Transaction(4, 500, "StoreA", "acc4", 1300));

        FinancialAnalyzer fa = new FinancialAnalyzer();
        System.out.println(fa.findTwoSum(txs, 500));
        System.out.println(fa.findTwoSumWithTimeWindow(txs, 500, 3600000));
        System.out.println(fa.findKSum(txs, 3, 1000));
        System.out.println(fa.detectDuplicates(txs));
    }
}
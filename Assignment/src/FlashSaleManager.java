import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class FlashSaleManager {
    // productId -> Current Stock (AtomicInteger for thread-safe O(1) updates)
    private final ConcurrentHashMap<String, AtomicInteger> inventory = new ConcurrentHashMap<>();

    // productId -> Queue of User IDs (LinkedBlockingQueue for FIFO waiting list)
    private final ConcurrentHashMap<String, BlockingQueue<Integer>> waitingLists = new ConcurrentHashMap<>();

    public void addProduct(String productId, int initialStock) {
        inventory.put(productId, new AtomicInteger(initialStock));
        waitingLists.put(productId, new LinkedBlockingQueue<>());
    }

    public int checkStock(String productId) {
        AtomicInteger stock = inventory.get(productId);
        return (stock != null) ? stock.get() : 0;
    }

    public String purchaseItem(String productId, int userId) {
        AtomicInteger stock = inventory.get(productId);
        if (stock == null) return "Product not found";

        // Atomic decrement: ensures no two threads grab the last item simultaneously
        int remainingBefore = stock.getAndUpdate(current -> current > 0 ? current - 1 : 0);

        if (remainingBefore > 0) {
            return "User " + userId + ": Success, " + (remainingBefore - 1) + " units remaining";
        } else {
            BlockingQueue<Integer> queue = waitingLists.get(productId);
            queue.offer(userId);
            return "User " + userId + ": Added to waiting list, position #" + queue.size();
        }
    }

    public static void main(String[] args) {
        FlashSaleManager sale = new FlashSaleManager();
        String item = "IPHONE15_256GB";

        // Setup: 100 units in stock
        sale.addProduct(item, 100);

        System.out.println("Initial Stock: " + sale.checkStock(item));

        // Simulate first few purchases
        System.out.println(sale.purchaseItem(item, 12345)); // Success
        System.out.println(sale.purchaseItem(item, 67890)); // Success

        // Simulate clearing out the remaining 98 units
        for (int i = 1; i <= 98; i++) {
            sale.purchaseItem(item, i);
        }

        // This user should hit the waiting list
        System.out.println("Stock Empty");
        System.out.println(sale.purchaseItem(item, 99999)); // Waiting list #1
        System.out.println(sale.purchaseItem(item, 88888)); // Waiting list #2
    }
}
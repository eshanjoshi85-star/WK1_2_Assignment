import java.util.*;

public class UsernameChecker {
    // Core data: Stores taken usernames for O(1) lookup
    private Map<String, Integer> users = new HashMap<>();
    // Stats: Stores how many times a username was checked (case-insensitive)
    private Map<String, Integer> attempts = new HashMap<>();
    
    private int nextUserId = 1;

    // 1. Check if username exists in O(1)
    public boolean checkAvailability(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        String uname = username.toLowerCase();
        // Track the attempt regardless of availability (case-insensitive)
        attempts.put(uname, attempts.getOrDefault(uname, 0) + 1);
        // HashMap lookup is O(1) on average
        return !users.containsKey(uname);
    }

    // 2. Register user (to make names "taken")
    public void registerUser(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        String uname = username.toLowerCase();
        if (checkAvailability(uname)) {
            users.put(uname, nextUserId++);
        }
    }

    // 3. Suggest similar available usernames
    public List<String> suggestAlternatives(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        List<String> suggestions = new ArrayList<>();
        int suffix = 1;
        String uname = username.toLowerCase();
        while (suggestions.size() < 3) {
            String candidate = uname + suffix;
            if (!users.containsKey(candidate)) {
                suggestions.add(candidate);
            }
            suffix++;
        }
        return suggestions;
    }

    // 4. Track popularity: Get the most attempted username
    public String getMostAttempted() {
        return attempts.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No attempts yet");
    }

    // Main method for demonstration
    public static void main(String[] args) {
        UsernameChecker checker = new UsernameChecker();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter a username to check:");
        String username = scanner.nextLine();
        if (checker.checkAvailability(username)) {
            System.out.println("Username is available. Registering...");
            checker.registerUser(username);
        } else {
            System.out.println("Username is taken. Suggestions:");
            List<String> suggestions = checker.suggestAlternatives(username);
            for (String suggestion : suggestions) {
                System.out.println(suggestion);
            }
        }
        System.out.println("Most attempted username: " + checker.getMostAttempted());
        scanner.close();
    }
}
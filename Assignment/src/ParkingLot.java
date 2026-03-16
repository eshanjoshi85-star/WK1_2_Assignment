import java.util.*;
class ParkingSpot
{
    String plate;
    long entryTime;
    String status;
    ParkingSpot()
    {
        status = "EMPTY";
    }
}
public class ParkingLot
{
    private ParkingSpot[] table;
    private int capacity;
    private int occupied = 0;
    private int totalProbes = 0;
    private int operations = 0;
    private Map<Integer, Integer> hourlyCount = new HashMap<>();
    public ParkingLot(int capacity)
    {
        this.capacity = capacity;
        table = new ParkingSpot[capacity];
        for (int i = 0; i < capacity; i++) table[i] = new ParkingSpot();
    }
    private int hash(String plate)
    {
        return Math.abs(plate.hashCode()) % capacity;
    }
    public String parkVehicle(String plate)
    {
        int index = hash(plate);
        int probes = 0;
        while (probes < capacity)
        {
            int i = (index + probes) % capacity;
            if (table[i].status.equals("EMPTY") || table[i].status.equals("DELETED"))
            {
                table[i].plate = plate;
                table[i].entryTime = System.currentTimeMillis();
                table[i].status = "OCCUPIED";
                occupied++;
                totalProbes += probes;
                operations++;
                int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                hourlyCount.put(hour, hourlyCount.getOrDefault(hour, 0) + 1);
                return "Assigned spot #" + i + " (" + probes + " probes)";
            }
            probes++;
        }
        return "Parking Full";
    }
    public String exitVehicle(String plate)
    {
        int index = hash(plate);
        int probes = 0;
        while (probes < capacity)
        {
            int i = (index + probes) % capacity;
            if (table[i].status.equals("OCCUPIED") && plate.equals(table[i].plate))
            {
                long durationMs = System.currentTimeMillis() - table[i].entryTime;
                double hours = durationMs / 3600000.0;
                double fee = hours * 5.5;
                table[i].status = "DELETED";
                occupied--;
                return "Spot #" + i + " freed, Duration: " +
                        String.format("%.2f", hours) + "h, Fee: $" +
                        String.format("%.2f", fee);
            }
            probes++;
        }
        return "Vehicle not found";
    }
    public void getStatistics()
    {
        double occupancy = (occupied * 100.0) / capacity;
        double avgProbes = operations == 0 ? 0 : (double) totalProbes / operations;
        int peakHour = -1;
        int max = 0;
        for (Map.Entry<Integer, Integer> e : hourlyCount.entrySet())
        {
            if (e.getValue() > max)
            {
                max = e.getValue();
                peakHour = e.getKey();
            }
        }
        System.out.println("Occupancy: " + String.format("%.0f", occupancy) + "%");
        System.out.println("Avg Probes: " + String.format("%.2f", avgProbes));
        if (peakHour != -1)
        {
            System.out.println("Peak Hour: " + peakHour + "-" + (peakHour + 1));
        }
    }
    public static void main(String[] args)
    {
        ParkingLot lot = new ParkingLot(500);
        System.out.println(lot.parkVehicle("ABC-1234"));
        System.out.println(lot.parkVehicle("ABC-1235"));
        System.out.println(lot.parkVehicle("XYZ-9999"));
        System.out.println(lot.exitVehicle("ABC-1234"));
        lot.getStatistics();
    }
}
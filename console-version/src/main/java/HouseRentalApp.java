import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDate;

class House {
    private String id, location, owner;
    private double price;
    private int bedrooms;

    public House(String id, String location, double price, int bedrooms, String owner) {
        this.id = id;
        this.location = location;
        this.price = price;
        this.bedrooms = bedrooms;
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public double getPrice() {
        return price;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public String getOwner() {
        return owner;
    }

    @Override
    public String toString() {
        return id + ", " + location + ", " + price + ", " + bedrooms + " beds, Owner: " + owner;
    }
}

class Tenant {
    private String id, name, contact, preferredLocation;

    public Tenant(String id, String name, String contact, String preferredLocation) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.preferredLocation = preferredLocation;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }

    public String getPreferredLocation() {
        return preferredLocation;
    }
}

class RentalAgreement {
    private House house;
    private Tenant tenant;
    private LocalDate startDate, endDate;
    private double deposit;
    private List<LocalDate> paymentDueDates;

    public RentalAgreement(House house, Tenant tenant, LocalDate startDate, LocalDate endDate, double deposit) {
        this.house = house;
        this.tenant = tenant;
        this.startDate = startDate;
        this.endDate = endDate;
        this.deposit = deposit;
        this.paymentDueDates = new ArrayList<>();
        generatePaymentDueDates();
    }

    private void generatePaymentDueDates() {
        LocalDate dueDate = startDate; // Start from the start date
        while (dueDate.isBefore(endDate) || dueDate.isEqual(endDate)) {
            paymentDueDates.add(dueDate);
            dueDate = dueDate.plusMonths(1);
        }
    }

    public House getHouse() {
        return house;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public double getDeposit() {
        return deposit;
    }

    public List<LocalDate> getPaymentDueDates() {
        return paymentDueDates;
    }

    @Override
    public String toString() {
        return "RentalAgreement{" +
                "house=" + house +
                ", tenant=" + tenant +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", deposit=" + deposit +
                ", paymentDueDates=" + paymentDueDates +
                '}';
    }
}

class HouseNotFoundException extends Exception {
    public HouseNotFoundException(String message) {
        super(message);
    }
}

public class HouseRentalApp {
    private static List<House> houses = new ArrayList<>();
    private static List<Tenant> tenants = new ArrayList<>();
    private static List<RentalAgreement> agreements = new ArrayList<>();
    private static final String HOUSES_FILE_NAME = "houses.txt";
    private static final String TENANTS_FILE_NAME = "tenants.txt";
    private static final String AGREEMENTS_FILE_NAME = "agreements.txt";

    public static void main(String[] args) {
        loadHouses();
        loadTenants();
        loadAgreements();
        cleanupAgreements(); // Add this line
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\nHouse Rental Management System");
            System.out.println("1. Add House");
            System.out.println("2. Remove House");
            System.out.println("3. View Houses");
            System.out.println("4. Search Houses");
            System.out.println("5. Book House");
            System.out.println("6. Record Payment");
            System.out.println("7. Check Due Dates");
            System.out.println("8. Save & Exit");
            System.out.print("Enter choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    try {
                        addHouse(scanner);
                    } catch (IOException e) {
                        System.out.println("Error adding house: " + e.getMessage());
                    }
                    break;
                case 2:
                    removeHouse(scanner);
                    break;
                case 3:
                    viewHouses();
                    break;
                case 4:
                    searchHouses(scanner);
                    break;
                case 5:
                    try {
                        bookHouse(scanner);
                    } catch (HouseNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 6:
                    recordPayment(scanner);
                    break;
                case 7:
                    checkDueDates();
                    break;
                case 8:
                    saveHouses();
                    saveTenants();
                    saveAgreements();
                    System.out.println("Data saved. Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 8);
    }

    private static void addHouse(Scanner scanner) throws IOException {
        System.out.print("Enter House ID: ");
        String id = scanner.nextLine().toLowerCase();

        // Validate if house ID already exists
        if (houses.stream().anyMatch(h -> h.getId().equalsIgnoreCase(id))) {
            System.out.println("House ID already exists!");
            return;
        }

        System.out.print("Enter Location: ");
        String location = scanner.nextLine().toLowerCase();
        System.out.print("Enter Price: ");
        double price = scanner.nextDouble();
        if (price < 0) {
            System.out.println("Price cannot be negative.");
            return;
        }
        System.out.print("Enter Bedrooms: ");
        int bedrooms = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Owner Name: ");
        String owner = scanner.nextLine();

        House house = new House(id, location, price, bedrooms, owner);
        houses.add(house);
        saveHouseToFile(house);
        System.out.println("House added successfully!");
    }

    private static void removeHouse(Scanner scanner) {
        System.out.print("Enter House ID to remove: ");
        String id = scanner.nextLine().toLowerCase();
        houses.removeIf(h -> h.getId().equalsIgnoreCase(id));
        System.out.println("House removed successfully!");
    }

    private static void saveHouseToFile(House house) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HOUSES_FILE_NAME, true))) {
            writer.write(house.getId() + "," + house.getLocation() + "," + house.getPrice() + ","
                    + house.getBedrooms() + "," + house.getOwner());
            writer.newLine();
        }
    }

    private static void viewHouses() {
        if (houses.isEmpty()) {
            System.out.println("No houses available.");
        } else {
            for (House house : houses) {
                System.out.println(house);
            }
        }
    }

    private static void searchHouses(Scanner scanner) {
        System.out.print("Enter Location: ");
        String location = scanner.nextLine().toLowerCase();
        System.out.print("Enter Max Price: ");
        double maxPrice = scanner.nextDouble();
        scanner.nextLine();

        List<House> results = houses.stream()
                .filter(h -> h.getLocation().equalsIgnoreCase(location) && h.getPrice() <= maxPrice)
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            System.out.println("No houses found.");
        } else {
            System.out.println("Found " + results.size() + " house(s):");
            for (House house : results) {
                System.out.println(house);
            }
        }
    }

    private static synchronized void bookHouse(Scanner scanner) throws HouseNotFoundException {
        System.out.print("Enter House ID to book: ");
        String houseId = scanner.nextLine().toLowerCase();
        System.out.print("Enter Tenant ID: ");
        String tenantId = scanner.nextLine().toLowerCase();
        System.out.print("Enter Tenant Name: ");
        String tenantName = scanner.nextLine();
        System.out.print("Enter Tenant Contact: ");
        String tenantContact = scanner.nextLine();
        System.out.print("Enter Tenant Preferred Location: ");
        String tenantPreferredLocation = scanner.nextLine().toLowerCase();
        System.out.print("Enter Start Date (YYYY-MM-DD): ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter End Date (YYYY-MM-DD): ");
        LocalDate endDate = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter Deposit: ");
        double deposit = scanner.nextDouble();
        scanner.nextLine();

        House house = houses.stream().filter(h -> h.getId().equalsIgnoreCase(houseId)).findFirst().orElse(null);
        if (house == null) {
            throw new HouseNotFoundException("House not found.");
        }

        Tenant tenant = new Tenant(tenantId, tenantName, tenantContact, tenantPreferredLocation);
        tenants.add(tenant);
        try {
            saveTenantToFile(tenant);
        } catch (IOException e) {
            System.out.println("Error saving tenant: " + e.getMessage());
        }

        RentalAgreement agreement = new RentalAgreement(house, tenant, startDate, endDate, deposit);
        agreements.add(agreement);
        try {
            saveAgreementToFile(agreement);
        } catch (IOException e) {
            System.out.println("Error saving agreement: " + e.getMessage());
        }

        System.out.println("House booked successfully!");
        System.out.println("Agreement Details: " + agreement);
    }

    private static void saveTenantToFile(Tenant tenant) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TENANTS_FILE_NAME, true))) {
            writer.write(tenant.getId() + "," + tenant.getName() + "," + tenant.getContact() + ","
                    + tenant.getPreferredLocation());
            writer.newLine();
        }
    }

    private static void saveAgreementToFile(RentalAgreement agreement) throws IOException {
        // Only append the agreement data, not the details
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(AGREEMENTS_FILE_NAME, true))) {
            writer.write(agreement.getHouse().getId() + "," + agreement.getTenant().getId() + ","
                    + agreement.getTenant().getName() + ","
                    + agreement.getStartDate() + "," + agreement.getEndDate() + "," + agreement.getDeposit() + ","
                    + agreement.getPaymentDueDates().stream().map(LocalDate::toString)
                            .collect(Collectors.joining(";")));
            writer.newLine();
        }
    }

    private static void saveHouses() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(HOUSES_FILE_NAME))) {
            for (House house : houses) {
                writer.println(house.getId() + "," + house.getLocation() + "," + house.getPrice() + ","
                        + house.getBedrooms() + "," + house.getOwner());
            }
        } catch (IOException e) {
            System.out.println("Error saving houses: " + e.getMessage());
        }
    }

    private static void saveTenants() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TENANTS_FILE_NAME))) {
            for (Tenant tenant : tenants) {
                writer.println(tenant.getId() + "," + tenant.getName() + "," + tenant.getContact() + ","
                        + tenant.getPreferredLocation());
            }
        } catch (IOException e) {
            System.out.println("Error saving tenants: " + e.getMessage());
        }
    }

    private static void saveAgreements() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(AGREEMENTS_FILE_NAME))) {
            for (RentalAgreement agreement : agreements) {
                writer.println(agreement.getHouse().getId() + "," + agreement.getTenant().getId() + ","
                        + agreement.getTenant().getName() + ","
                        + agreement.getStartDate() + "," + agreement.getEndDate() + "," + agreement.getDeposit() + ","
                        + agreement.getPaymentDueDates().stream().map(LocalDate::toString)
                                .collect(Collectors.joining(";")));
            }
        } catch (IOException e) {
            System.out.println("Error saving agreements: " + e.getMessage());
        }
    }

    private static void loadHouses() {
        File file = new File(HOUSES_FILE_NAME);
        if (!file.exists())
            return;

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String[] data = fileScanner.nextLine().split(",");
                houses.add(new House(
                        data[0],
                        data[1],
                        Double.parseDouble(data[2]),
                        Integer.parseInt(data[3]),
                        data[4]));
            }
        } catch (Exception e) {
            System.out.println("Error loading houses: " + e.getMessage());
        }
    }

    private static void loadTenants() {
        File file = new File(TENANTS_FILE_NAME);
        if (!file.exists())
            return;

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String[] data = fileScanner.nextLine().split(",");
                tenants.add(new Tenant(
                        data[0],
                        data[1],
                        data[2],
                        data[3]));
            }
        } catch (Exception e) {
            System.out.println("Error loading tenants: " + e.getMessage());
        }
    }

    private static void loadAgreements() {
        File file = new File(AGREEMENTS_FILE_NAME);
        if (!file.exists()) {
            System.out.println("No agreements file found.");
            return;
        }

        System.out.println("\nLoading agreements...");
        System.out.println("Houses loaded: " + houses.size());
        System.out.println("Tenants loaded: " + tenants.size());

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                System.out.println("Reading line: " + line); // Debug line
                if (!line.isEmpty()) {
                    String[] data = line.split(",");
                    if (data.length >= 7) { // Changed from 6 to 7
                        House house = houses.stream()
                                .filter(h -> h.getId().equalsIgnoreCase(data[0]))
                                .findFirst().orElse(null);
                        Tenant tenant = tenants.stream()
                                .filter(t -> t.getId().equalsIgnoreCase(data[1]))
                                .findFirst().orElse(null);

                        if (house != null && tenant != null) {
                            try {
                                RentalAgreement agreement = new RentalAgreement(
                                        house,
                                        tenant,
                                        LocalDate.parse(data[3]), // Changed from data[2]
                                        LocalDate.parse(data[4]), // Changed from data[3]
                                        Double.parseDouble(data[5])); // Changed from data[4]

                                // Parse payment dates from the last element
                                String[] dueDates = data[6].split(";"); // Changed from data[5]
                                Set<LocalDate> uniqueDates = new TreeSet<>();
                                for (String dueDate : dueDates) {
                                    uniqueDates.add(LocalDate.parse(dueDate));
                                }
                                agreement.getPaymentDueDates().clear();
                                agreement.getPaymentDueDates().addAll(uniqueDates);
                                agreements.add(agreement);
                                System.out.println("Successfully loaded agreement for house: " + house.getId()); // Debug
                                                                                                                 // line
                            } catch (Exception e) {
                                System.out.println("Error parsing agreement data: " + e.getMessage());
                                e.printStackTrace();
                            }
                        } else {
                            if (house == null)
                                System.out.println("House not found: " + data[0]);
                            if (tenant == null)
                                System.out.println("Tenant not found: " + data[1]);
                        }
                    } else {
                        System.out.println("Invalid agreement data format: " + line);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading agreements: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Loaded " + agreements.size() + " agreements.");
    }

    private static void recordPayment(Scanner scanner) {
        System.out.print("Enter House ID: ");
        String houseId = scanner.nextLine().toLowerCase(); // Convert to lowercase
        System.out.print("Enter Tenant ID: ");
        String tenantId = scanner.nextLine().toLowerCase(); // Convert to lowercase
        System.out.print("Enter Payment Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        System.out.println("Searching for agreement..."); // Debug output
        System.out.println("Total agreements loaded: " + agreements.size()); // Debug output

        RentalAgreement agreement = agreements.stream()
                .filter(a -> a.getHouse().getId().equalsIgnoreCase(houseId) &&
                        a.getTenant().getId().equalsIgnoreCase(tenantId))
                .findFirst().orElse(null);

        if (agreement == null) {
            System.out.println("Agreement not found. Available agreements:");
            for (RentalAgreement a : agreements) {
                System.out.println("House ID: " + a.getHouse().getId() +
                        ", Tenant ID: " + a.getTenant().getId());
            }
            return;
        }

        System.out.println("Payment of " + amount + " recorded for agreement: " + agreement);
    }

    private static void checkDueDates() {
        LocalDate today = LocalDate.now();
        System.out.println("\n=== Payment Due Date Check ===");
        System.out.println("Today's Date: " + today);
        System.out.println("Number of agreements: " + agreements.size());

        if (agreements.isEmpty()) {
            System.out.println("No rental agreements found.");
            return;
        }

        boolean foundDueDate = false;
        for (RentalAgreement agreement : agreements) {
            System.out.println("\nChecking agreement for:");
            System.out.println("House: " + agreement.getHouse().getId());
            System.out.println("Tenant: " + agreement.getTenant().getName());
            System.out.println("Payment dates: " + agreement.getPaymentDueDates());

            // Check for upcoming payments within next 7 days
            for (LocalDate dueDate : agreement.getPaymentDueDates()) {
                if (dueDate.equals(today) ||
                        (dueDate.isAfter(today) && dueDate.isBefore(today.plusDays(7)))) {
                    System.out.println("\n*** PAYMENT DUE ***");
                    System.out.println("Due date: " + dueDate);
                    System.out.println("House ID: " + agreement.getHouse().getId());
                    System.out.println("Location: " + agreement.getHouse().getLocation());
                    System.out.println("Tenant: " + agreement.getTenant().getName());
                    System.out.println("Amount: $" + String.format("%.2f", agreement.getHouse().getPrice()));
                    System.out.println("Days until due: " + today.until(dueDate).getDays());
                    foundDueDate = true;
                }
            }
        }

        if (!foundDueDate) {
            System.out.println("\nNo payments due today or in the next 7 days.");
        }
    }

    private static void cleanupAgreements() {
        System.out.println("Cleaning up agreements...");
        Set<RentalAgreement> uniqueAgreements = new LinkedHashSet<>(agreements);
        agreements.clear();
        agreements.addAll(uniqueAgreements);
        System.out.println("Agreements cleaned.");
    }
}

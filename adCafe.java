import java.io.*;
import java.util.*;

class CafeManagementSystem {
    private static final String MENU_FILE = "menu.txt";
    private static final String ORDER_FILE = "orders.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Menu cafeMenu = new Menu();

        // Load menu items from file if available
        loadMenuFromFile(cafeMenu);

        while (true) {
            System.out.println("                                                ");
            System.out.println("************************************************");
            System.out.println("*              Deni's Cafe                     *");
            System.out.println("************************************************");
            System.out.println("1. Add Menu Item");
            System.out.println("2. View Menu");
            System.out.println("3. Create Order");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice;
            while(true){
                try{
                    choice=Integer.parseInt(scanner.nextLine());
                    break;
                }catch(NumberFormatException e){
                    System.out.println("INVALID CHOICE!!!");
                    System.out.println("PLEASE ENTER VALID NUMBER");
                }
            }
             // Consume the newline character

            switch (choice) {
                case 1:
                    System.out.print("Enter item name: ");
                    String itemName = scanner.nextLine();
                    System.out.print("Enter item price: ");
                    double itemPrice = scanner.nextDouble();
                    cafeMenu.addMenuItem(itemName, itemPrice);
                    saveMenuToFile(cafeMenu);
                    break;
                case 2:
                    cafeMenu.viewMenu();
                    break;
                case 3:
                    createOrder(scanner, cafeMenu);
                    break;
                case 4:
                    System.out.println("********************************************");
                    System.out.println("Exiting Cafe Management System. Thank you!!!");
                    System.out.println("********************************************");
                    System.exit(0);
                default:
                      System.out.println("---------------------------------");
                    System.out.println("Invalid choice. Please try again.");
                     System.out.println("--------------------------------");
            }
        }
    }

    private static void createOrder(Scanner scanner, Menu menu) {
        Order order = new Order();
        while (true) {
            menu.viewMenu();
            System.out.println("---------------------------");
            System.out.println("Your Current Order is: ");
            System.out.println("---------------------------");
            List<CafeItem> orderItems = order.getItems();
            for (int i = 0; i < orderItems.size(); i++) {
                System.out.println((i + 1)+"." + " " + orderItems.get(i));
            }

            System.out.println("1. Add Item to Order");
            System.out.println("2. Remove Item from Order");
            System.out.println("3. Checkout");
            System.out.println("4. Cancel Order");
            System.out.print("Enter your choice: ");

            int choice;
            while(true){
                try{
                    choice=Integer.parseInt(scanner.nextLine());
                    break;
                }catch(NumberFormatException e){
                    System.out.println("Invlid choice plz enter valid cholice");
                }
            }
             // Consume the newline character

            switch (choice) {
                case 1:
                    System.out.print("Enter item number to add: ");
                    int itemNumber = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character
                    if (itemNumber >= 1 && itemNumber <= menu.getMenuItems().size()) {
                        CafeItem selectedItem = menu.getMenuItems().get(itemNumber - 1);
                        System.out.print("Enter quantity: ");
                        int quantity = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character
                        if (quantity > 0) {
                             System.out.println("---------------------------");
            System.out.println("Your Current Order is: ");
            System.out.println("---------------------------");
                            // Add the item with the specified quantity to the order
                            for (int i = 0; i < quantity; i++) {
                                order.addItem(selectedItem);
                            }
                        } else {
                            System.out.println("-----------------");
                            System.out.println("Invalid quantity.");
                            System.out.println("-----------------");
                        }
                    } else {
                         System.out.println("--------------------");
                        System.out.println("Invalid item number.");
                        System.out.println("--------------------");
                    }
                    break;
                case 2:
                    if (orderItems.isEmpty()) {
                         System.out.println("----------------------------");
                    System.out.println("PLEASE ORDER SOMETHING FIRST");
                    System.out.println("----------------------------");
                    } else {
                        System.out.print("Enter item number to remove: ");
                        int removeNumber = scanner.nextInt();
                        scanner.nextLine(); // Consume the newline character
                        if (removeNumber >= 1 && removeNumber <= orderItems.size()) {
                            orderItems.remove(removeNumber - 1);
                        } else {
                             System.out.println("-------------------");
                            System.out.println("Invalid item number.");
                             System.out.println("-------------------");
                        }
                    }
                    break;
                case 3:
                  if (orderItems.isEmpty()) {
                    System.out.println("----------------------------");
                    System.out.println("PLEASE ORDER SOMETHING FIRST");
                    System.out.println("----------------------------");
                    } 
                else
                {
                    double total = order.calculateTotal();
                    System.out.println("------------------------------------");
                     System.out.println("Order placed successfully!");
                     System.out.println("------------------------------------");
                    System.out.println("Total amount to pay: Rs " + total);
                    System.out.println("------------------------------------");
                    saveOrderToFile(order);
                }
                    return;
                case 4:
                    if (orderItems.isEmpty()) {
                         System.out.println("----------------------------");
                    System.out.println("PLEASE ORDER SOMETHING FIRST");
                    System.out.println("----------------------------");
                    }
                    else
                    {
                    System.out.println("***********************");
                    System.out.println("YOUR ORDER IS CANCELLED");
                    System.out.println("***********************");
                    }
                    return;
                default:
                System.out.println("---------------------------------");
                System.out.println("Invalid choice. Please try again.");
                System.out.println("---------------------------------");
            }
        }
    }

    private static void saveMenuToFile(Menu menu) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MENU_FILE))) {
            for (CafeItem item : menu.getMenuItems()) {
                writer.write(item.getName() + "," + item.getPrice());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadMenuFromFile(Menu menu) {
        try (BufferedReader reader = new BufferedReader(new FileReader(MENU_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    double price = Double.parseDouble(parts[1].trim());
                    menu.addMenuItem(name, price);
                }
            }
        } catch (IOException e) {
            System.out.println("Menu file not found. Creating an empty menu.");
        }
    }

    private static void saveOrderToFile(Order order) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ORDER_FILE, true))) {
            writer.write("Order Date: " + new Date());
            writer.newLine();
            for (CafeItem item : order.getItems()) {
                writer.write(item.getName() + "\t\t" + item.getPrice());
                writer.newLine();
            }
            writer.write("Total: $" + order.calculateTotal());
            writer.newLine();
            writer.write("----------------------------------------");
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class CafeItem {
    private String name;
    private double price;

    public CafeItem(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "\t" + "Item: " +name+ " \t" + " Price: $" + price;
    }
}

class Menu {
    private List<CafeItem> menuItems;

    public Menu() {
        this.menuItems = new ArrayList<>();
    }

    public void addMenuItem(String name, double price) {
        CafeItem newItem = new CafeItem(name, price);
        menuItems.add(newItem);
    }

    public void viewMenu() {
        System.out.println("Cafe Menu:");
        for (int i = 0; i < menuItems.size(); i++) {
            System.out.println((i + 1) + ". " + menuItems.get(i));
        }
    }

    public List<CafeItem> getMenuItems() {
        return menuItems;
    }
}

class Order {
    private List<CafeItem> items;

    public Order() {
        this.items = new ArrayList<>();
    }

    public void addItem(CafeItem item) {
        items.add(item);
    }

    public double calculateTotal() {
        double total = 0;
        for (CafeItem item : items) {
            total += item.getPrice();
        }
        return total;
    }

    public List<CafeItem> getItems() {
        return items;
    }
}
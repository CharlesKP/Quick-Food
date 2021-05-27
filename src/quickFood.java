import javax.swing.*;
import java.io.*;
import java.util.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.IOException;



public class quickFood {
    public static void main(String[] args) throws FileNotFoundException {

        //creating a random order number for each transaction up to 10000
        double orderNumberRandom = Math.random() * 1000000000;
        int orderNumber = (int) orderNumberRandom % 10000;

        //Initiate customer object, getting customer info from Custom()
        Customer customer = Custom(orderNumber);
        System.out.println(customer);

        //get the Restaurant information/object from the user.
        //customer location is passed through to make sure customer and restaurant in same city
        String customerLocation = customer.city;
        //restaurant object equal to values returned from takeaways method
        Restaurant restaurant = takeaways(customerLocation);
        System.out.println(restaurant);

        //array list of arrays for drivers initialized and file, passed through to the method
        File fileDrivers = new File("drivers.txt");
        ArrayList<String> driverInfo = drivers(fileDrivers);

        //Initiate driver object and the values will be found in findDriver(driver.txt, customer location) method
        Driver yourDriver = findDriver(driverInfo, customerLocation);

        //form to order. gets price and item name for each order
        //passing restaurant information into the orderForm method to dictate menu
        ArrayList<String> orderList = orderForm(restaurant);
        String specialInstructions = instructions();

        String order = duplicates(orderList);

        //calculates the total added from all the orders
        double total = 0.00;
        for (int i = 0; i < orderList.size(); i += 2) {
            double cost = Double.parseDouble(orderList.get(i + 1));
            total += cost;
        }
        //prints order to console
        System.out.println(order);

        //print for console
        System.out.println("Total: " + total);
        System.out.println("Order number: " + orderNumber);
        System.out.println(yourDriver);

        //relevant values passed into Invoice method, which goes to write method (.txt file)
        Invoice(customer, restaurant, specialInstructions, order, yourDriver, orderNumber, total);

        //creating a string of customer name+order# and sent to be ordered and appended

        String customerOrder = customer.name + " - " + orderNumber;
        File file1 = new File("customers.txt");



        //if there is not yet a file in existence create it
        if (!file1.exists()) {
            try {
                file1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        alphabetWrite(customerOrder, "customers.txt");



        //creating a string of customer+location and sent to be ordered and appended
        String customerAndLocation = customer.city + " - " + customer.name;
        File file2 = new File("customerLocation.txt");

        //if there is not yet a file in existence create it
        if (!file2.exists()) {
            try {
                file2.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        alphabetWrite(customerAndLocation, "customerLocation.txt");


        updateDriverList(driverInfo, yourDriver);
    }





    //method to obtain customer information and GUI
    public static Customer Custom(int orderNUmber) {
        //text fields to be added to the form below
        JTextField nameText = new JTextField(15);
        JTextField emailText = new JTextField(15);
        JTextField phNumberText = new JTextField(15);
        JTextField streetText = new JTextField(15);
        JTextField suburbText = new JTextField(15);
        JTextField cityText = new JTextField(15);


        //labels for the form to capture user information
        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("Name:"));
        myPanel.add(nameText);

        myPanel.add(new JLabel("Email"));
        myPanel.add(emailText);

        myPanel.add(new JLabel("Cell:"));
        myPanel.add(phNumberText);

        myPanel.add(new JLabel("Street Address"));
        myPanel.add(streetText);

        myPanel.add(new JLabel("Suburb"));
        myPanel.add(suburbText);

        myPanel.add(new JLabel("City"));
        myPanel.add(cityText);


        //show panel with text fields. Send to cancel, if cancel option clicked the program exits
        int result = JOptionPane.showConfirmDialog(null, myPanel,
                "Please enter your details", JOptionPane.OK_CANCEL_OPTION);
        cancel(result);


        //storing the text field entries into better named variables
        String name = nameText.getText();
        String email = emailText.getText();
        String number = phNumberText.getText();
        String street = streetText.getText();
        String suburb = suburbText.getText();
        String city = cityText.getText();

        //The customer class object is populated with the values from above "Customer.java" class file
        //return the value of customer to "main"
        Customer customer = new Customer(orderNUmber, name, email, number, street, suburb, city);

        //checking to see if information is empty, if it is then send to error function+exit(try again)
        //msg to be sent to the method - used in other areas also
        String msg = " Please make sure full information is given and try again ";

        if (name.equals("")) {
            notComplete(msg);
        }
        if (email.equals("")) {
            notComplete(msg);
        }
        if (number.equals("")) {
            notComplete(msg);
        }
        if (street.equals("")) {
            notComplete(msg);
        }
        if (suburb.equals("")) {
            notComplete(msg);
        }
        if (city.equals("")) {
            notComplete(msg);
        }

        return customer;
    }





    //method to make cancel click on panel exit program
    public static void cancel(int result) {
        //if the result value from clicking cancel is true, then exit program
        if (result == JOptionPane.CANCEL_OPTION) {
            System.exit(0);
        }
    }





    //method to create ArrayList from textFile of drivers
    public static ArrayList drivers(File fileDrivers) throws FileNotFoundException {
        //create scanner and initialize strings/Arrays
        Scanner scan = new Scanner(fileDrivers);
        String line;
        String[] lineArray;
        ArrayList<String> driverInfo = new ArrayList<>();


        //scan each line of the text file
        while (scan.hasNextLine()) {
            line = scan.nextLine();
            lineArray = line.split(", ");

            //add each element of the array to the ArrayList
            driverInfo.add(lineArray[0]);
            driverInfo.add(lineArray[1]);
            driverInfo.add(lineArray[2]);
        }
        //close scanner and return Arraylist
        scan.close();
        return driverInfo;
    }





    //method to obtain restaurant information and GUI
    public static Restaurant takeaways(String customerLocation) {     //customer location passed in to trigger error if not equal to restaurant location

        //initialising restaurant to empty
        Restaurant restaurant = new Restaurant("", "", "");

        Object[] options = {"Billies Burgers", "Wok This Hay", "Pete's Pizzas"};
        int whichRestaurant = JOptionPane.showOptionDialog(null, "Which restaurant would you like to order from?", "Restaurants",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                options, options[0]);


        //switch statement for getting values from the restaurant selected from above
        //values retrieved from restaurant methods
        switch (whichRestaurant) {
            case 0:
                restaurant = billies(customerLocation);
                break;
            case 1:
                restaurant = wok(customerLocation);
                break;
            case 2:
                restaurant = pete(customerLocation);
                break;
        }

        return restaurant;
    }



    //customer location is shared for location of restaurant
    //driver operation area still needs to align with customer location
    //menus are below method for restaurant
    public static Restaurant billies(String customerLocation) {
        Restaurant billies = new Restaurant("Billies Burgers", customerLocation, "666-1324");
        return billies;
    }

    public static ArrayList billiesMenu() {

        //create hashmap and populate with the menu
        ArrayList<String> menu = new ArrayList<>();
        menu.add(0, "Beef Burger @ R99");
        menu.add(1, "Hawaiian Burger @ R 109");
        menu.add(2, "Bacon Avo Feta Burger @ R129");
        menu.add(3, "Tikka Chicken Burger @ R109");
        menu.add(4, "Cheddar Cheese Melt @ R109");
        menu.add(5,"Deep Fried Chicken Burger @ R109");
        menu.add(6,"Chips @ R35");
        menu.add(7, "Coke @ R22");
        menu.add(8, "Fresh Juice @ R30");
        menu.add(9, "Beer @ R30");

        return menu;
    }





    public static Restaurant wok(String customerLocation) {
        Restaurant wok = new Restaurant("Wok This Hay", customerLocation, "555-4325");
        return wok;
    }

    public static ArrayList wokMenu() {

        //create hashmap and populate with the menu
        ArrayList<String> menu = new ArrayList<>();
        menu.add(0,"Phad Thai @ R105");
        menu.add(1,"Sweet & Sour Beef @ R115");
        menu.add(2,"Sweet & Sour Pork @ R129");
        menu.add(3,"Thai Green Curry @ R109");
        menu.add(4,"Crispy Duck @ R140");
        menu.add(5,"Egg Fried Rice @ R25");
        menu.add(6,"Noodles @ R25");
        menu.add(7,"Soda @ R20");
        menu.add(8,"Iced Tea @ R28");
        menu.add(9,"Cider @ R27");

        return menu;
    }



    public static Restaurant pete(String customerLocation) {
        Restaurant pete = new Restaurant("Pete's Pizzas", customerLocation, "777-8279");
        return pete;
    }

    public static ArrayList peteMenu() {

        //create an ArrayList and populate with the menu
        ArrayList<String> menu = new ArrayList<>();
        menu.add(0, "Margarita Pizza @ R95");
        menu.add(1, "Hawaiian Pizza @ R120");
        menu.add(2, "Mushroom & Ham Pizza @ R129");
        menu.add(3, "Smog Pizza @ R129");
        menu.add(4, "Four Meat Pizza @ R145");
        menu.add(5, "Bacon Avo Feta @ R130");
        menu.add(6, "Seafood Pizza @ R165");
        menu.add(7, "Coke @ R25");
        menu.add(8, "Wine @ R146");
        menu.add(9, "Beer @ R32");


        return menu;
    }




    //method for ordering items and returning array of items ordered + cost
    public static ArrayList orderForm(Restaurant restaurant) {
        ArrayList<String> orderList = new ArrayList<>();
        ArrayList<String> menu = new ArrayList<>();

        switch(restaurant.name) {
            case "Billies Burgers":
                menu = billiesMenu();
                break;
            case "Wok This Hay":
                menu = wokMenu();
                break;
            case "Pete's Pizzas":
                menu = peteMenu();
                break;
        }

        String menuScreen = "";

        for (int i = 0, j = 1; i < menu.size(); i++, j++) {
            menuScreen += j + ") " + menu.get(i) + "\n";
        }


        //text fields
        JTextArea info = new JTextArea (menuScreen);
        JTextField orderText = new JTextField(2);


        //labels for the form
        JPanel myPanel = new JPanel();

        myPanel.add(info);
        myPanel.add(new JLabel("Item:"));
        myPanel.add(orderText);



        int next = 0;
        //loop to continue ordering, continue to checkout or cancel
        while (next == 0) {
            //sets text fields to empty for each loop of adding another item
            orderText.setText("");

            int result = JOptionPane.showConfirmDialog(null, myPanel,
                    "Select the numbered item to order:", JOptionPane.OK_CANCEL_OPTION);

            //if the result of the option pane above is true (ie OK)
            if (result == JOptionPane.OK_OPTION) {

                //creating an index from the number input from user
                String itemNum = orderText.getText();
                int index = Integer.parseInt(itemNum);

                if (index >= 1 && index <= 10) {

                    //gets the string from the ArrayList eg: "Bacon Avo Feta @ R130"
                    String itemString = menu.get(index - 1);
                    //splits into 2 index array. [0] is the item and [1] is the cost
                    String[] itemArray = itemString.split(" @ R");

                    //adding the item and cost String to the orderList array
                    orderList.add(itemArray[0]);
                    orderList.add(itemArray[1]);


                } else {
                    //else the user did not enter a number between 1 and 10... error & exit
                    String msg = " Unfortunately that is not a valid order, please try again :) ";
                    notComplete(msg);
                }

                //creates an object that holds the values of the customized options
                Object[] options = {"Add Item", "Checkout", "Exit"};
                next = JOptionPane.showOptionDialog(null, "What would you like to do next?", "Order Progress....",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                        options, options[0]);

            } else {
                //if ok option not true (ie CANCEL) then break out of program/loop
                break;
            }
        }
        return orderList;
    }



    public static String instructions() {

        JTextField specialInstructions = new JTextField(20);

        //labels for the form
        JPanel myPanel = new JPanel();
        myPanel.add(new JLabel("Special Instructions:"));
        myPanel.add(specialInstructions);

        int result = JOptionPane.showConfirmDialog(null, myPanel,
        "Food Preparation Preferences", JOptionPane.OK_CANCEL_OPTION);

        //store the instructions in this variable to be returned
        String instructions = specialInstructions.getText();

        return instructions;
    }





    //method that's called when user doesn't fill in text fields correctly and exits
    public static void notComplete(String msg) {
        JOptionPane.showMessageDialog(null, "***" + msg + "***");
        System.exit(0);
    }





    //method to find the driver with minimum load in the customer location
    private static Driver findDriver(ArrayList driverInfo, String customerLocation) {

        //finding out which of the drivers are in the customer location
        String driverLocation;
        ArrayList<String> suitableDrivers = new ArrayList<>();

        //trying to loop through index 2 of each group of three (the location)
        for (int i = 1; i < driverInfo.size(); i += 3) {

            driverLocation = driverInfo.get(i).toString();

            //if the locations are a match for the customer/restaurant location
            //ignoring case for each value
            if (driverLocation.equalsIgnoreCase(customerLocation)) {
                String name = driverInfo.get(i - 1).toString();
                String load = driverInfo.get(i + 1).toString();

                //adding the name/location/load of the drivers in required location to a new arraylist "suitableDrivers"
                suitableDrivers.add(name);
                suitableDrivers.add(driverLocation);
                suitableDrivers.add(load);
            }
        }

        //if there is no list of suitable drivers, checked by location, then
        //the ArrayList of suitable drivers would be empty, show error and exit
        if (suitableDrivers.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Sorry! Our drivers are currently not operating in your area.");
            System.exit(0);
        }


        //creating an array based on the load of the suitable drivers
        int length = suitableDrivers.size();
        ArrayList<Integer> intLoadArray = new ArrayList<>();
        //the load is the 3rd item (2) in the array, so i starts at 2 and increments by 3 to find
        //each new driver load
        for (int i = 2; i < length; i += 3) {
            int driverLoad = Integer.parseInt(suitableDrivers.get(i));
            intLoadArray.add(driverLoad);
        }


        int i = smallestIndex(intLoadArray);
        int lenArray = 3;
        String name = suitableDrivers.get(i * lenArray);
        String location = suitableDrivers.get(i * lenArray + 1);
        int load = intLoadArray.get(i);

        //creates driver object and passes in the name, location and load
        Driver yourDriver = new Driver(name, location, load);
        return yourDriver;
    }





    //This method returns the index of the smallest value in the array of given size
    public static int smallestIndex(ArrayList intLoadArray) {
        //current and smallest positions
        int current = Integer.parseInt(String.valueOf(intLoadArray.get(0)));
        int smallest = 0;

        //smallest is equal to 0 so we start at 1, assuming that 0 is the smallest
        for (int j = 1; j < intLoadArray.size(); j++) {
            int place = Integer.parseInt(String.valueOf(intLoadArray.get(j)));
            //if it isn't then switch
            if (place < current) {
                current = place;
                smallest = j;
            }
        }
        //return the index of the smallest value in load array
        return smallest;
    }





    //method to remove duplicates for the Invoice and store the count/map
    //this method is purely for the invoice to show itemized counts of items
    public static String duplicates(ArrayList orderList) {
        //creating a new ArrayList turning the order item and price into one string item eg ("item  (price)")
        List<String> form = new ArrayList<>();
        for (int i = 0; i < orderList.size(); i += 2) {
            String string = orderList.get(i) + "  (R" + orderList.get(i + 1) + ")";
            form.add(string);
        }

        //creating a Map to hold pairs - keys+ values
        Map<String, Integer> duplicates = new HashMap<>();

        //enhanced for loop, for the String form
        for (String str : form) {
            //if the map already contains the item then add 1
            if (duplicates.containsKey(str)) {
                duplicates.put(str, duplicates.get(str) + 1);
                //else just the item with values 1
            } else {
                duplicates.put(str, 1);
            }
        }
        //initializing a string variable
        String order = "";
        //for each map element add to the string
        for (Map.Entry<String, Integer> entry : duplicates.entrySet()) {
            order += entry.getValue() + " X " + entry.getKey() + "\n";
        }
        return order;
    }





    //creating invoice as a string to be printed to specific invoice file
    public static void Invoice(Customer customer, Restaurant restaurant, String specialInstructions, String order, Driver yourDriver, int orderNumber, double total) throws FileNotFoundException {
        //making a variable for line spacing n = newline
        String n = "\n";
        //populating all the strings
        String customerDetails = customer.toString() + n + n;
        String from = "Your order is from: " + restaurant.name.toUpperCase() + " (" + restaurant.city.toUpperCase() + ")" + n + n;
        String totalString = Double.toString(total);
        String instructions = "Special instructions: " + specialInstructions + n + n;
        String stringTotal = n + "Total: R";
        String deliver = n + "Your driver " + yourDriver.name + " is on their way to deliver your food to your address:" + n;
        String customerAddress = n + customer.street + n + customer.suburb + n + customer.city + n;
        String contact = n + "If you would need to contact " + restaurant.name.toUpperCase() + ", their telephone number is: " + restaurant.number;

        //creating 1 string from all the variables
        //send to the write function to create and write the text
        String invoice = customerDetails + from + order + n + instructions + stringTotal + totalString + n + n + deliver + customerAddress + contact;
        writeToFile(invoice, orderNumber, customer.name);
    }





    //write invoice to a txt file. takes in Invoice string and (order number + customer name) to create an invoice file that can be relevantly named
    public static void writeToFile(String invoice, int orderNumber, String name) throws FileNotFoundException {
        //create a print writer and gives a file path from the variables passed in to the method
        PrintWriter writer = null;
        try {
            //unique invoice created for each successful order
            writer = new PrintWriter(name + " " + orderNumber + " Invoice.txt", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //print input(text) to file and close
        writer.println(invoice);
        writer.close();

        //order successful notice
        JOptionPane.showMessageDialog(null, "Order Successful!!\nYour invoice is ready, have a nice day :)");
    }






    //method to order customer+order number alphabetically by name
    public static void alphabetWrite(String string, String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        ArrayList<String> allCustomers = new ArrayList();

        //getting each line of the customers.txt and adding to allCustomers
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            //    if (line.contains("Cape Town")) {
            List<String> customerList = Arrays.asList(line);
            allCustomers.addAll(customerList);

        }
        scanner.close();
        //adding the current customer to allCustomers
        allCustomers.add(string);
        //sorting all Customers alphabetically
        Collections.sort(allCustomers);

        //clears the file so can append the ordered list
        PrintWriter writer = new PrintWriter(file);
        writer.print("");
        writer.close();

        //append the file for each
        for (String allCustomer : allCustomers) {
            appendFile(allCustomer, fileName);
        }
    }






    //method to append customer order to file
    public static void appendFile(String string, String fileName) {

        try {
            FileWriter fw = new FileWriter(fileName, true);    //the true will append the new data
            fw.write(string + "\n");    //appends the string to the file
            fw.close();   //close
        }
        //catches error which prints error message
        catch (IOException ioe) {
            System.err.println("IOException: " + ioe.getMessage());
        }
    }



    // to add 1 to the load of the driver found to be most suitable to deliver
    public static void updateDriverList(ArrayList drivers, Driver driver) throws FileNotFoundException {

        //turning the driver object into a string to match with the ArrayList
        String theDriver = driver.name + ", " + driver.location + ", " + driver.load;
        //adding 1 to the load
        int loadPlus1 = driver.load + 1;
        String updateLoad = driver.name + ", " + driver.location + ", " + loadPlus1;


        //creating an arrayList to get each line to write to txt file
        ArrayList<String> perLine = new ArrayList<>();
        for (int i = 0; i < drivers.size(); i+=3) {
            String line = drivers.get(i) + ", " + drivers.get(i + 1) + ", " + drivers.get(i+2);
            perLine.add(line);
        }


        //searching array list to find what index theDriver is the same, to replace/update that index
        for (int i = 0; i < perLine.size(); i++) {
            if (perLine.get(i).equals(theDriver)) {
                //set that index with updated load
                perLine.set(i, updateLoad);
            }
        }

        File file = new File("drivers.txt");
        //clears the file so can append the ordered list
        PrintWriter writer = new PrintWriter(file);
        writer.print("");
        writer.close();

        //append the file for each
        for (int i = 0; i < perLine.size(); i++) {
            String line = String.valueOf(perLine.get(i));
            appendFile(line, "drivers.txt");
        }
    }
}
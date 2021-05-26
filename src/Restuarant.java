class Restaurant {
    //attributes for class object Restaurant
    String name;
    String city;
    String number;
    String specialInstructions;

    //constructor
    Restaurant(String name, String city, String number, String specialInstructions) {
        this.name = name;
        this.city = city;
        this.number = number;
        this.specialInstructions = specialInstructions;
    }

    //method to print the object
    public String toString() {
        String output = "\nRestaurant: " + this.name;
        output += "\nCity: " + this.city;
        output += "\nNumber: " + this.number;
        output += "\nInstructions: " + this.specialInstructions;
        return output;
    }
}
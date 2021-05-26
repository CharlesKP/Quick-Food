class Restaurant {
    //attributes for class object Restaurant
    String name;
    String city;
    String number;

    //constructor
    Restaurant(String name, String city, String number) {
        this.name = name;
        this.city = city;
        this.number = number;
    }

    //method to print the object
    public String toString() {
        String output = "\nRestaurant: " + this.name;
        output += "\nCity: " + this.city;
        output += "\nNumber: " + this.number;
        return output;
    }
}
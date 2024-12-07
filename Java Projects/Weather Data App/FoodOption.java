public class FoodOption {
    private String name;
    private double price;
    
    public FoodOption(String name, double price){
        this.name = name;
        this.price = price;
    }
    // Getters and setters 
    public String getFoodName(){
        return name;
    }
    public double getPrice(){
        return price;
    }
    
    // Additional food options
    public static FoodOption createSoup() {
        return new FoodOption("Goat Water (Soup)", 10);
    }
    public static FoodOption createIcePop() {
        return new FoodOption("Haribo Push Up Lollie", 5);
    }
}

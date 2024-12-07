public class PopularSpot {
    private String name;
    private String category;
    private String description;

    public PopularSpot(String name, String category, String description) {
        this.name = name;
        this.category = category;
        this.description = description;
    }
  // Getters and setters
    public String getName(){
       return name; 
    }
    public String getCategory(){
        return category;
    }
    public String getDescription(){
        return description;
    }
    @Override
    public String toString() {
        return  "Name:'" + name + '\'' +
                ", Category:'" + category + '\'' +
                ", Description:'" + description + '\'' +
                '}';
    } 
        // Additional popular spot options
    public static PopularSpot showBridge() {
        return new PopularSpot("Tower Bridge","Museums",
                "Tower Bridge is a Grade I listed combined bascule and suspension bridge in London, built "
                 + "between 1886 and 1894, \nengineered by John Wolfe Barry and architected by Horace Jones.");
    }
    public static PopularSpot showBigBen() {
        return new PopularSpot("Big Ben", "Architecture",
                "Big Ben is the nickname for the Great Bell of the striking clock at the north end"
                + " of the Palace of Westminster; \nthe name is frequently extended to also refer to "
                + "the clock and the clock tower.");
    }
    public static PopularSpot showConservatory() {
        return new PopularSpot("Barbican Conservatory", "Convservatory",
                "Is verdant oasis in the heart of the city that will make you forget all about the pouring rain. Instead, you’ll be "
                + "transported to a faraway jungle as you surround yourself with the greenery.\n" + "Elsewhere, London’s "
                + "Sky Garden is another verdant wonderland for a sky-high drink and some awesome city views, even on a rainy day");
    }
    public static PopularSpot showCoffeeShop() {
        return new PopularSpot("Coffee Shop", "Food",
                "What better way to while away a rainy day in London than with a warm cup "
                 + "of coffee and some people-watching? \nGet comfy in one of our city’s many "
                 + "amazing coffee shops with a book and some music. \nYou could even switch up "
                 + "your work environment for the day and romanticise your life as you stare "
                 + "out the window while you “work”.");
    }
}
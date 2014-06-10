import java.util.ArrayList;
import java.util.List;

abstract class Movie
{
    private String title;

    public Movie(String title) {
        this.title            = title;
    }

    public String getTitle () {
        return title;
    }

    public abstract double determineAmount(int daysRented);

    public abstract int determineFrequentRenterPoints(int daysRented);
}


class ChildrensMovie extends Movie {
    public ChildrensMovie(String title) {
        super(title);
    }

    public double determineAmount(int daysRented) {
        double thisAmount = 1.5;
        if (daysRented > 3)
            thisAmount += (daysRented - 3) * 1.5;

        return thisAmount;
    }

    public int determineFrequentRenterPoints(int daysRented) {
        return 1;
    }
}


class RegularMovie extends Movie {
    public RegularMovie(String title) {
        super(title);
    }

    public double determineAmount(int daysRented) {
        double thisAmount = 2;
        if (daysRented > 2)
            thisAmount += (daysRented - 2) * 1.5;

        return thisAmount;
    }

    public int determineFrequentRenterPoints(int daysRented) {
        return 1;
    }
}

class NewReleaseMovie extends Movie {
    public NewReleaseMovie(String title) {
        super(title);
    }

    public double determineAmount(int daysRented) {
        return daysRented * 3.0;
    }

    public int determineFrequentRenterPoints(int daysRented) {
        return (daysRented > 1) ? 2 : 1;
    }
}


class Rental
{
    private Movie movie;
    private int daysRented;

    public Rental (Movie movie, int daysRented) {
        this.movie        = movie;
        this.daysRented = daysRented;
    }

    public String getTitle() {
        return movie.getTitle();
    }

    public double determineAmount() {
        return movie.determineAmount(daysRented);
    }

    public int determineFrequentRenterPoints() {
        return movie.determineFrequentRenterPoints(daysRented);
    }
}


class RentalStatement {
    private String name;
    private List<Rental> rentals = new ArrayList<Rental>();
    private double totalAmount;
    private int frequentRenterPoints;

    public RentalStatement(String customerName) {
        this.name = customerName;
    }

    public void addRental(Rental rental) {
        rentals.add(rental);
    }

    public String makeRentalStatement() {
        clearTotals();
        return makeHeader() + makeRentalLines() + makeSummary();
    }

    private void clearTotals() {
        totalAmount = 0;
        frequentRenterPoints = 0;
    }

    private String makeHeader() {
        return "Rental Record for " + getName() + "\n";
    }

    private String makeRentalLines() {
        String rentalLines = "";

        for (Rental rental : rentals)
            rentalLines += makeRentalLine(rental);

        return rentalLines;
    }

    private String makeRentalLine(Rental rental) {
        double thisAmount = rental.determineAmount();
        frequentRenterPoints += rental.determineFrequentRenterPoints();
        totalAmount += thisAmount;

        return formatRentalLine(rental, thisAmount);
    }

    private String formatRentalLine(Rental rental, double thisAmount) {
        return "\t" + rental.getTitle() + "\t" + thisAmount + "\n";
    }

    private String makeSummary() {
        return "You owed " + totalAmount + "\n" +
            "You earned " + frequentRenterPoints +
            " frequent renter points\n";
    }

    public String getName() {
        return name;
    }

    public double getAmountOwed() {
        return totalAmount;
    }

    public int getFrequentRenterPoints() {
        return frequentRenterPoints;
    }
}

public class videostore {
    public static void main(String[] args){
        RentalStatement rs = new RentalStatement("Tom");
        rs.addRental(new Rental(new RegularMovie("Reg1"), 1));
        rs.addRental(new Rental(new NewReleaseMovie("NR2"), 2));
        rs.addRental(new Rental(new ChildrensMovie("Childrens3"), 3));
        System.out.println(rs.makeRentalStatement());
    }
}

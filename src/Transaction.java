import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Transaction is a POJO class to set and get private fields of a transaction
 * record.
 * 
 * @author Harshit Shah
 */
public class Transaction {

    public static final String DELIMITER = ",";

    // Date format given in the CSV file.
    public static final SimpleDateFormat format = new SimpleDateFormat(
            "yyyy-mm-dd");

    // Date, decscription and amount fields from CSV file.
    private Date date;
    private String description;
    private double amount;

    /**
     * @return date
     */
    public Date getDate() {
        return date;
    }

    /**
     * Set date.
     * 
     * @param date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set description.
     * 
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return amount for this transaction.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Set amount.
     * 
     * @param amount
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /*
     * Display CSV file record in proper format.
     * 
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "[" + format.format(date) + DELIMITER + description + DELIMITER
                + amount + "]";
    }
}

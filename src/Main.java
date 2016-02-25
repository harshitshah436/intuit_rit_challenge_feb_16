import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * Main class reads the csv file, stores data creating Transaction object for
 * each row of CSV file and then applies StringMatching and Reservoir Sampling
 * algorithm to produce recommendation of saving for a road trip.
 * 
 * @author Harshit Shah
 */
public class Main {

    // Dictionary of words for purchase details and accessible stores in the
    // dataset of given area.
    public static final String DICTIONARY_NAME = "dictionary.txt";

    // Size of pool for transactions with maximum spending.
    public static final int POOL_SIZE = 12;

    // Randomized algorithm reservoir sampling for selecting transactions from
    // available pool.
    public static final int RESERVOIR_SIZE = 5;

    public static String filename;

    // Lists to store dictionary and input csv file.
    public List<String> dictionary = new ArrayList<String>();
    public List<Transaction> input = new ArrayList<Transaction>();

    // Map to store transaction and amount details.
    public Map<String, Double> transactionAmount = new HashMap<String, Double>();

    /**
     * Main method.
     * 
     * @param args
     *            command-line arguments.
     */
    public static void main(String[] args) {

        // Command-line arguments verification.
        if (args.length < 1) {
            System.err.println("Usage: java Main <file_name>");
            System.err.println("Note: file must be in CSV format");
            return;
        }

        try {

            // Create an object of a class.
            Main obj = new Main();

            filename = args[0];

            // Read dictionary file and input datafile.
            obj.readDictionaryFile();
            obj.readInputCSVFile();

            // Create an object of StringMatching class to apply string
            // algorithms on data.
            StringMatching sm = new StringMatching();
            obj.transactionAmount = sm.getTransactionsAmountMap(obj.input,
                    obj.dictionary);

            // Sort the Map with Highest amount transaction at lower indexes.
            obj.transactionAmount = sm.sortMapByValue(obj.transactionAmount);

            // Get a list of transactions with maximum spending amount of pool
            // size.
            List<String> trans_details = sm.getHighCreditTransactionsIntoPool(
                    obj.transactionAmount, POOL_SIZE);

            // Create an object to apply reservoir sampling algorithm to
            // randomly selecting transactions with higher amount.
            ReservoirSampling reservoirSampling = new ReservoirSampling(
                    RESERVOIR_SIZE);
            List<String> reservoir = reservoirSampling
                    .createReservoir(trans_details);

            // Get a duration of transactions for a given dataset. (in months)
            int month_diff = sm.getDateDiff();

            obj.printRecommendations(reservoir, month_diff);

        } catch (IOException | ParseException e) {
            System.err
                    .println("Please check that both the files - input CSV and dictionary.txt files are present in current directory.");
        }
    }

    /**
     * Read dictionary file and store into list which contains nearby stores.
     * You can add new store details in a dictionary.txt file.
     * 
     * @throws IOException
     */
    public void readDictionaryFile() throws IOException {

        FileReader fr = new FileReader(DICTIONARY_NAME);
        BufferedReader br = new BufferedReader(fr);

        String line = "";
        while ((line = br.readLine()) != null) {
            dictionary.add(line);
        }

        br.close();
    }

    /**
     * Read input CSV file and conver each record into Transaction object and
     * then store them into a list.
     * 
     * @throws IOException
     * @throws ParseException
     */
    public void readInputCSVFile() throws IOException, ParseException {
        FileReader fr = new FileReader(filename);

        // Parce CSV file.
        CSVFormat format = CSVFormat.DEFAULT;
        CSVParser parser = new CSVParser(fr, format);

        int line_count = 0;

        // Store each record into a list.
        for (CSVRecord record : parser) {
            Transaction trans = new Transaction();
            trans.setDate(Transaction.format.parse(record.get(0)));
            trans.setDescription(record.get(1));
            trans.setAmount(Double.parseDouble((record.get(2))));
            input.add(trans);
            line_count++;
        }

        System.out.println("File: " + filename + " - total records read: "
                + line_count);

        parser.close();
        fr.close();
    }

    /**
     * This method prints recommendations and calculate monthly average of the
     * transactions. And shows a customer how much he can save in 3 months if he
     * stop using the recommended services for that period.
     * 
     * @param reservoir
     *            list to print and calculate monthly average spending.
     * @param month_diff
     *            month difference of smallest and largest date from the
     *            transactions.
     */
    public void printRecommendations(List<String> reservoir, int month_diff) {

        System.out
                .println("\nYou should stop spending following average amount per month.\n");
        System.out.println(String.format("%40s", "Description") + " \t Amount");
        System.out.println(String.format("%40s", "-----------") + " \t "
                + "------");

        // Calculate total amount from reservoir transactions and print each
        // recommended transactions.
        double total_amount = 0;
        for (String string : reservoir) {
            double trans_amount_per_month = transactionAmount.get(string)
                    / month_diff;
            System.out.println(String.format("%40s", string) + " \t "
                    + String.format("%.2f", trans_amount_per_month));
            total_amount += trans_amount_per_month;
        }

        System.out.println("\nSo, you can save upto total "
                + Math.round(total_amount * 3) + " bucks in next 3 months.");

        System.out
                .println("\n\nNote: 'Rent' and 'Bank withdraw' transactions are ignored considering they are spent behind your basic needs.");
        if (transactionAmount.get("RESIDENT") != null)
            System.out.println("\t Resident payment for Rent (per month): $"
                    + String.format("%.2f", transactionAmount.get("RESIDENT")
                            / month_diff));
        if (transactionAmount.get("BKOFAMERICA ATM") != null)
            System.out
                    .println("\t Bank of America ATM Withdrawal (per month): $"
                            + String.format("%.2f",
                                    transactionAmount.get("BKOFAMERICA ATM")
                                            / month_diff));

    }

}

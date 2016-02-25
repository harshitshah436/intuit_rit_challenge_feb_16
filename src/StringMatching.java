import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * StringMatching class applies pattern matching algorithms and create a Map of
 * transactions to amount spend. Later on used to sort this map by values,
 * creating a list of POOL size with maximum spending. Also includes some
 * utility methods.
 * 
 * @author Harshit Shah
 */
public class StringMatching {

    // First and last dates from transactions data.
    public Date first_date;
    public Date last_date;

    // Total amount spent in all the transactions.
    public double total_spendings;

    // Constructs an object by assigning default values to instance variables.
    public StringMatching() {
        first_date = new Date();
        last_date = new Date(10000000000L);
        total_spendings = 0;
    }

    /**
     * Creates a map of transaction details to total amount spend over the
     * period of time.
     * 
     * @param input
     *            list of Transaction objects which contains a record from a CSV
     *            file.
     * @param dictionary
     *            list of stores/ possible spending for.
     * @return a transaction map.
     */
    public Map<String, Double> getTransactionsAmountMap(
            List<Transaction> input, List<String> dictionary) {

        Map<String, Double> transactionAmount = new HashMap<String, Double>();

        // The stores/transaction details are not available in dictionary file.
        // Later could be added in the dictionary file.
        Map<String, Double> notMappedTransactions = new HashMap<String, Double>();

        for (Transaction trans : input) {
            Date date = trans.getDate();
            String desc = trans.getDescription();
            double amount = trans.getAmount();
            total_spendings += amount;

            // Find earliest and last date of transactions.
            if (date.before(first_date))
                first_date = date;
            if (date.after(last_date))
                last_date = date;

            // Insert records into a map.
            String trans_detail = getRelatedTransactionFromDictionary(desc,
                    dictionary);
            if (trans_detail != null) {
                insertIntoTransactionMap(trans_detail, amount,
                        transactionAmount);
            } else {
                insertIntoTransactionMap(desc, amount, notMappedTransactions);
            }

        }
        return transactionAmount;
    }

    /**
     * This method returns a related dictionary word to a given transaction.
     * 
     * @param desc
     *            transaction details.
     * @param dictionary
     *            list of dictionary words.
     * @return a matching word from dictionary.
     */
    public String getRelatedTransactionFromDictionary(String desc,
            List<String> dictionary) {
        for (String str : dictionary) {
            if (desc.contains(str))
                return str;
        }
        return null;
    }

    /**
     * 
     * @param trans_detail
     *            transaction related word from a Dictionary.
     * @param amount
     *            specific amount spent for a transaction.
     * @param transactionAmount
     *            a map of transaction to amount.
     */
    public void insertIntoTransactionMap(String trans_detail, double amount,
            Map<String, Double> transactionAmount) {
        // Insert a transaction to a given map. If already exists add amount to
        // current amount.
        if (transactionAmount.containsKey(trans_detail))
            transactionAmount.replace(trans_detail,
                    transactionAmount.get(trans_detail) + amount);
        else
            transactionAmount.put(trans_detail, amount);
    }

    /**
     * Sort a map by value in descending order. Higher amount transactions
     * should come first in the map.
     * 
     * @param transactionAmount
     *            unsorted map of transactions to amount.
     * @return a sorted Map.
     */
    public Map<String, Double> sortMapByValue(
            Map<String, Double> transactionAmount) {

        // Create a list of Map entry.
        List<Map.Entry<String, Double>> list = new LinkedList<>(
                transactionAmount.entrySet());

        // Sort a list in descending order by comparing value.
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> entry1,
                    Map.Entry<String, Double> entry2) {
                return (entry2.getValue()).compareTo(entry1.getValue());
            }
        });

        // Convert a list of entry map into LinkedHashMap.
        Map<String, Double> result = new LinkedHashMap<>();
        for (Map.Entry<String, Double> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    /**
     * Get a list of pool size with highest credited transactions.
     * 
     * @param transactionAmount
     *            map.
     * @param poolSize
     *            size of n to apply reservoir sampling.
     * @return a list.
     */
    public List<String> getHighCreditTransactionsIntoPool(
            Map<String, Double> transactionAmount, int poolSize) {
        List<String> trans_details = new ArrayList<String>();
        Iterator<String> it = transactionAmount.keySet().iterator();

        // If size of a map is greater then pool size.
        if (transactionAmount.size() > poolSize) {
            int i = 0;
            while (i < poolSize) {
                String trans = it.next().toString();
                // Ignore 'Resident' and 'bank withdraw' transactions.
                if (trans.contains("BKOFAMERICA ATM")
                        || trans.contains("RESIDENT")) {
                    continue;
                }
                trans_details.add(trans);
                i++;
            }
        } else {
            while (it.hasNext()) {
                String trans = it.next().toString();
                if (trans.contains("BKOFAMERICA ATM")
                        || trans.contains("RESIDENT"))
                    continue;
                trans_details.add(trans);
            }
        }
        return trans_details;
    }

    /**
     * Get a difference in a months between two dates. (earliest and last dates
     * from a transactions.)
     * 
     * @return a difference of months.
     */
    public int getDateDiff() {

        // Assign dates to calendar instance.
        Calendar first_cal = Calendar.getInstance();
        first_cal.setTime(first_date);
        Calendar last_cal = Calendar.getInstance();
        last_cal.setTime(last_date);

        // Find out year and month differences.
        int year_diff = last_cal.get(Calendar.YEAR)
                - first_cal.get(Calendar.YEAR);
        int month_diff = year_diff * 12 + last_cal.get(Calendar.MONTH)
                - first_cal.get(Calendar.MONTH);

        return month_diff;
    }
}

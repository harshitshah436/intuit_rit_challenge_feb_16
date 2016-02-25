import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class applies Reservoir Sampling, a randomized algorithm. If there is a
 * lot of data and POOL SIZE is too large then this will work effectively for
 * selecting K records from a pool of n.
 * 
 * Ref: https://en.wikipedia.org/wiki/Reservoir_sampling
 * 
 * @author Harshit Shah
 */
public class ReservoirSampling {

    private final int k;

    /**
     * Constructs an object with the reservoir size.
     * 
     * @param k
     *            total number of sample elements.
     */
    public ReservoirSampling(int k) {
        this.k = k;
    };

    /**
     * Returns a list of random 'k' samples from the input list of POOL Size or
     * n.
     * 
     * @param list
     *            of transactions from which k samples would be chosen.
     * @return the list containing k recommended transactions.
     */
    public List<String> createReservoir(List<String> list) {

        // Create a list of K elements.
        List<String> reservoir = new ArrayList<String>(k);

        int count = 0;
        Random random = new Random();
        for (String item : list) {

            // Fill the reservoir list.
            if (count < k) {
                reservoir.add(item);
            } else { // Fill the list probabilistic.
                int r = random.nextInt(count + 1);
                if (r < k) {
                    reservoir.set(r, item);
                }
            }
            count++;
        }
        return reservoir;
    }
}
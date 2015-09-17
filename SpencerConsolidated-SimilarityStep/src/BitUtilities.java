import org.apache.lucene.util.OpenBitSet;

/**
 * Created by patron on 1/15/15.
 */
public class BitUtilities {
    public static OpenBitSet convert(double value) {
        assert(value >= 0 && value <= 1);
        OpenBitSet bits = new OpenBitSet();
        int k = (int)(value*1000);
        int index = 0;
        while(k != 0) {
            if(k % 2 != 0) {
                bits.set(index);
            }
            ++ index;
            k = k >>> 1;
        }
        return bits;
    }

    public static double convert(OpenBitSet bits) {
        double value = 0;
        for(int i = 0; i < bits.length(); ++i) {
            value += bits.get(i) ? (1 << i) : 0;
        }
        return value/1000.0;
    }

}

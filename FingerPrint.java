import org.apache.lucene.util.OpenBitSet;


/**
 * Created by patron on 12/23/14.
 */
public class FingerPrint {
    String moleculeName;
    OpenBitSet fp;
    long card;
    int idx;
    int[] bitfreqs;
    int hashcode;
    int[] bytebitfreqs;

}

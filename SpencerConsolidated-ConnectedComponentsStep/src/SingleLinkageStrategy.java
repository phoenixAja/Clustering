

/**
 * Created by patron on 1/20/15.
 */
public class SingleLinkageStrategy implements LinkageStrategy {
    public double calculateDistance(double[] d) {
        double min = FileManager.INVALID_DISTANCE;
        for(int i = 0; i < d.length; i ++) {
            if(d[i] != FileManager.INVALID_DISTANCE) {
                min = Math.min(d[i], min);
            }
        }
        return min;

    }
}

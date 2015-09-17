import org.apache.lucene.util.OpenBitSet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by pradap on 5/25/15.
 */
public class Driver {
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        if(args.length != 3) {
            System.err.println("(Usage) java <class_name> fingerprint.fps candidate_set.txt similarity_output_file.txt");
        }
        String filename = args[0];
        String blockingFilename = args[1];
        String outFilename = args[2];

        // load the fingerprint data
        HashMap<Integer, FingerPrint> objHM = FileLoader.loadFingerPrintData(filename);

        writeSimilarities(blockingFilename, outFilename, objHM);

    }
    public static void writeSimilarities(String filename, String outFilename, HashMap<Integer, FingerPrint> objHM) throws FileNotFoundException, UnsupportedEncodingException {
        long start = System.currentTimeMillis();
        FileInputStream inputStream = null;
        Scanner sc = null;
        try{
            inputStream = new FileInputStream(filename);
        }catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        sc = new Scanner(inputStream, "UTF-8");
        int index = 0;
        String[] splittedLine;
        DistanceMap linkages = new DistanceMap();
        PrintWriter writer = new PrintWriter(outFilename, "UTF-8");

        while(sc.hasNextLine()) {
            String line = sc.nextLine();
            splittedLine = line.split("\\,");
            ClusterPair link = new ClusterPair();
            String lCluster;
            String rCluster;
            lCluster = splittedLine[0].trim();
            rCluster = splittedLine[1].trim();
            FingerPrint fp1 = null;
            FingerPrint fp2 = null;
            Integer lClusterInteger = new Integer(lCluster);
            if (objHM.containsKey(lClusterInteger) == true) {
                fp1 = objHM.get(lClusterInteger);
            } else {
                System.err.println("objHM donot contain key : " + lCluster);
                System.exit(0);
            }
            Integer rClusterInteger = new Integer(rCluster);
            if (objHM.containsKey(rClusterInteger) == true) {
                fp2 = objHM.get(rClusterInteger);

            } else {
                System.err.println("objHM donot contain key : " + rCluster);
                System.exit(0);
            }
            double sim = getSimilarity(fp1.bs, fp2.bs);
            writer.write(lCluster+"|"+rCluster+"|"+sim +"\n");
            if(sim < 0.5)
            {
                System.err.println("NOT GOOD !!!!");
                System.exit(0);
            }
        }
        writer.close();
    }
    public static double getSimilarity(OpenBitSet b1, OpenBitSet b2) {
        // tanimoto similarity
        // int/(c1+c2-int)
        double intersectedValue = (double)OpenBitSet.intersectionCount(b1, b2);
        double sim = intersectedValue/(b1.cardinality()+b2.cardinality() - intersectedValue);
        //System.err.println("++ sim: " + sim);
        return sim;

    }


}

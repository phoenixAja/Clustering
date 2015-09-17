/**
 * Created by pradap on 5/25/15.
 */
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

public class Driver {
    public static void main(String[] args) throws FileNotFoundException {
        if(args.length != 5) {
            System.err.println("(Usage)java <class_name> fingerprint.fps cc_dir_name num_clusters outfile_for_cz_gt_1 outfile_for_cs_eq_1");
            System.exit(1);
        }

        String filename = args[0];
        String dirName = args[1];
        int numClusters = Integer.parseInt(args[2]);

        String outputFilename = args[3];
        String outputIndividualClusters = args[4];

        Scanner sc = null;
        FileInputStream inputStream = null;

        try {
            inputStream = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        sc = new Scanner(inputStream, "UTF-8");
        String splittedLine[];
        Integer i1, i2;
        long start = System.currentTimeMillis();
        int index = 0;
        HashMap<String, String> objHM = new HashMap<String, String>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            splittedLine = line.split("\\ ");
            String molName = splittedLine[0].trim();
            String idx = new Integer(index).toString();
            if(objHM.containsKey(idx) == false) {
                objHM.put(idx, molName+"|0");
            } else {
                System.out.println("Hmm interesting");
                System.exit(0);
            }
            System.out.println(index++);
        }
        PrintWriter writer = new PrintWriter(outputFilename);
        writer.write("MoleculeName, LineNumber, ClusterId\n");
        for(int i = 0; i < numClusters; i ++) {
            String fname = dirName+"c"+i+".txt" ;
            System.out.println(fname);
            try {
                inputStream = new FileInputStream(fname);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            sc = new Scanner(inputStream, "UTF-8");
            while(sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if(objHM.containsKey(line) == true) {
                    splittedLine = objHM.get(line).split("\\|");
                    writer.write(splittedLine[0].trim() +","+ line+","+i +"\n");
                    objHM.put(line, splittedLine[0] + "|1");
                } else {
                    System.err.println("Interesting !!!");
                    System.exit(0);
                }
            }
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        writer.close();
        writer = new PrintWriter(outputIndividualClusters);
        Set<String> keys = objHM.keySet();
        writer.write("MoleculeName, LineNumber, ClusterId\n");
        for(String s:keys) {
            String v = objHM.get(s);
            splittedLine = v.split("\\|");
            if(splittedLine[1].equalsIgnoreCase("0")) {
                writer.write(splittedLine[0].trim() +","+ s+","+ "NA" +"\n");
            }

        }
        writer.close();
        System.out.println("Done !!!");

    }
}

import org.apache.lucene.util.OpenBitSet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by pradap on 1/15/15.
 */
public class FileLoader {
    public static HashMap<Integer, FingerPrint> loadFingerPrintData(String filename) {

        FileInputStream inputStream = null;
        Scanner sc = null;
        BitUtilities objBU = new BitUtilities();
        HashMap<Integer, FingerPrint> objHM = new HashMap<Integer, FingerPrint>();

        long start = System.currentTimeMillis();

        try{
            inputStream = new FileInputStream(filename);
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        sc = new Scanner(inputStream, "UTF-8");
        int index = 0;
        String[] splittedLine;
        while(sc.hasNextLine()) {
            String line = sc.nextLine();
            splittedLine = line.split("\\ ");
            FingerPrint objFP = new FingerPrint();
            objFP.index = index+"";
            objFP.bs = getBitSet(splittedLine[1].trim());
            objFP.moleculeName = splittedLine[0].trim();
            objHM.put(new Integer(index), objFP);
            index++;
            System.err.println(index);
        }


        long end = System.currentTimeMillis();

        System.out.println("Loading fingerprint data took : " + (end-start )/1000 + " seconds !!!");


        return objHM;


    }

    public static OpenBitSet getBitSet(String bitString) {
        OpenBitSet bs = new OpenBitSet(2048);
        for(int i = 0; i < 2048; i ++) {
            if(bitString.charAt(i) == '1') {
                bs.set(i);
            }
        }
        return bs;
    }

}
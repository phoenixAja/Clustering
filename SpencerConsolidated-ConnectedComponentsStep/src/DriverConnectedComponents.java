import gnu.trove.list.array.TIntArrayList;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.hash.TIntHashSet;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Scanner;

/**
 * Created by pradap on 5/25/15.
 */
public class DriverConnectedComponents {
    public static TIntObjectHashMap<TIntHashSet> objConnectedComponent = new TIntObjectHashMap<TIntHashSet>();
    public static TIntObjectHashMap<TIntArrayList> objAdjacencyList = new TIntObjectHashMap<TIntArrayList>();

    public static void main(String[] args) throws FileNotFoundException {
        if(args.length != 2) {
            System.err.println("(Usage) java <class_name> cc_filaname.txt|sim_values_filename.txt cc_outdir");
        }

        String filename = args[0];
        String outdir = args[1];

        // read the contents of the file.
        readFile(filename);


        // Get the connected components
        Queue<Integer> objQueue = new ArrayDeque<Integer>();

        int startIdx;
        int idx = 0;
        while(objAdjacencyList.isEmpty() == false) {
            startIdx =  objAdjacencyList.keys()[0];
            objQueue.add(startIdx);
            TIntHashSet elements = new TIntHashSet();
            int elt;
            TIntArrayList obj;
            int[] adjArray;
            while(objQueue.isEmpty() == false) {
                elt = objQueue.remove().intValue();
                elements.add(elt);
                if(objAdjacencyList.containsKey(elt)) {
                    obj = objAdjacencyList.get(elt);
                    adjArray = obj.toArray();
                    for(int i = 0; i < adjArray.length; i++) {
                        objQueue.add(adjArray[i]);
                        elements.add(adjArray[i]);

                    }
                    objAdjacencyList.remove(elt);
                }
            }
            objConnectedComponent.put(idx, elements);
            idx ++;

        }
        System.out.println("Writing Results !!!");

        writeToFiles(outdir);


    }
    public static void writeToFiles(String dirname) {
        TIntHashSet obj;
        int[] elts;
        String filename;
        for(int i = 0; i < objConnectedComponent.size(); i ++) {
            obj = (TIntHashSet)objConnectedComponent.get(i);
            elts = obj.toArray();
            try{
                filename = dirname + "/c" + i + ".txt";
                PrintWriter writer = new PrintWriter(filename, "UTF-8");
                for(int j = 0; j < elts.length; j ++) {
                    writer.write(elts[j] +"\n");
                }
                writer.close();
            }catch(Exception e) {
                e.printStackTrace();
            }

        }

    }


    public static void readFile(String filename) throws FileNotFoundException {
        Scanner in = new Scanner(new FileReader(filename));
        String line;
        String splittedLine[];
        int id1, id2;
        int index = 0;
        TIntArrayList obj;
        while(in.hasNextLine()) {
            index ++;
            line = in.nextLine();
            splittedLine = line.split("\\|");
            id1 = Integer.parseInt(splittedLine[0].trim());
            id2 = Integer.parseInt(splittedLine[1].trim());

            if(objAdjacencyList.containsKey(id1) == false) {
                obj  = new TIntArrayList();
                obj.add(id2);
                objAdjacencyList.put(id1, obj);
            } else {
                obj = (TIntArrayList) objAdjacencyList.get(id1);
                obj.add(id2);
                objAdjacencyList.put(id1, obj);
            }
            if(objAdjacencyList.containsKey(id2) == false) {
                obj = new TIntArrayList();
                obj.add(id1);
                objAdjacencyList.put(id2, obj);
            } else {
                obj = (TIntArrayList)objAdjacencyList.get(id2);
                obj.add(id1);
                objAdjacencyList.put(id2, obj);
            }


        }




    }


}

import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by pradap on 5/25/15.
 */
public class DriverClusterCreation {
    public static TIntObjectHashMap adjMap = new TIntObjectHashMap();
    public static FibonacciHeap<ClusterPair> PQ = new FibonacciHeap<ClusterPair>();
    //public static TCharArrayList clusterList = new TCharArrayList();
    public static List<String> clusterList = new ArrayList<String>();

    public static void main(String[] args) throws FileNotFoundException{

        if(args.length != 2) {
            System.err.println("(Usage)java <class_name> similarity_output.txt conncomp_outputfile.txt");
        }
        String filename = args[0];
        String ccFilename = args[1];

        // time the operation.
        long start, end;
        start = System.currentTimeMillis();
        readFile(filename);
        end = System.currentTimeMillis();
        System.err.println("Reading  file took : " + (end - start)/1000.0 + "  secs");


        // Actual clustering now

        ClusterPair cp;
        String arr;
        int id1, id2;
        double dist;
        TIntObjectHashMap obj;
        TIntSet nSet = new TIntHashSet();
        double distvals[] = new double[2];
        int adjNodes[];
        int adjNode;
        FibonacciHeap.Entry<ClusterPair> link1, link2;
        double newDist;
        ClusterPair newCp;
        FibonacciHeap.Entry<ClusterPair> newCpEntry;
        LinkageStrategy linkageStrategy = new SingleLinkageStrategy();
        nSet.add(0);
        int index = 0;
        start = System.currentTimeMillis();
        while(PQ.isEmpty() == false) {
            ++index;

            if(index%100.0 == 0) {
                System.out.println("Iterations : " + index);
            }


            //System.out.println(adjMap.get(1091));
            nSet.clear();
            //System.out.println("Iteration : " + ++index);
            cp = PQ.dequeueMin().getValue();
            arr = cp.id1 + "|" + cp.id2 +"|" + cp.dist  ;

            id1 = cp.id1;
            id2 = cp.id2;
            dist = cp.dist;
            // System.out.println(id1);
            //remove
            clusterList.add(arr);

            obj = (TIntObjectHashMap)adjMap.get(id1);
            obj.remove(id2);
            adjMap.put(id1, obj);
            //get the keys !!!
            if(obj.isEmpty() == false) {
                nSet.addAll(obj.keySet());
            }

            obj = (TIntObjectHashMap)adjMap.get(id2);
            obj.remove(id1);
            adjMap.put(id2, obj);

            //update the adjacency list;
            if(obj.isEmpty() == false) {
                nSet.addAll(obj.keySet());
            }


            adjNodes = nSet.toArray();
            for(int i = 0; i < adjNodes.length; i ++) {
                adjNode = adjNodes[i];
                // distvals initialization
                distvals[0] = FileManager.INVALID_DISTANCE; distvals[1] = FileManager.INVALID_DISTANCE;

                obj = (TIntObjectHashMap)adjMap.get(id1);
                if(obj != null) {
                    if(obj.containsKey(adjNode)) {
                        link1 = (FibonacciHeap.Entry<ClusterPair>)obj.get(adjNode);
                        PQ.delete(link1);
                        distvals[0] = link1.getValue().dist;
                    }
                }
                obj = (TIntObjectHashMap) adjMap.get(id2);
                if(obj != null) {
                    if(obj.containsKey(adjNode)) {
                        link2 = (FibonacciHeap.Entry<ClusterPair>) obj.get(adjNode);
                        PQ.delete(link2);
                        distvals[1] = link2.getValue().dist;
                    }
                }

                newDist = linkageStrategy.calculateDistance(distvals);
                if(newDist != FileManager.INVALID_DISTANCE) {
                    newCp = new ClusterPair(id1, adjNode, newDist);
                    newCpEntry = PQ.enqueue(newCp, newCp.dist);
                    if (distvals[0] != FileManager.INVALID_DISTANCE) {
                        obj = (TIntObjectHashMap) adjMap.get(id1);
                        if (obj != null) {
                            if (obj.containsKey(adjNode)) {
                                //remove from PQ
                                //add pq
                                obj.put(adjNode, newCpEntry);
                                adjMap.put(id1, obj);
                            }
                        }
                        obj = (TIntObjectHashMap) adjMap.get(adjNode);
                        if (obj != null) {
                            if (obj.containsKey(id1)) {
                                //remove from PQ
                                //add pq
                                obj.put(id1, newCpEntry);
                                adjMap.put(adjNode, obj);
                            }
                        }
                        if (distvals[1] != FileManager.INVALID_DISTANCE) {
                            //adjMap.remove(adjNode);
                            if (adjMap.containsKey(id2)) {
                                obj = (TIntObjectHashMap) adjMap.get(id2);
                                if(obj.containsKey(adjNode)) {
                                    //remove from PQ
                                    obj.remove(adjNode);
                                    adjMap.put(id2, obj);
                                }
                            }
                            obj = (TIntObjectHashMap) adjMap.get(adjNode);
                            if (obj.containsKey(id2)) {
                                //remove from PQ
                                obj.remove(id2);
                                adjMap.put(adjNode, obj);
                            }
                        }
                    } else if (distvals[1] != FileManager.INVALID_DISTANCE) {
                        if (adjMap.containsKey(id2)) {
                            obj = (TIntObjectHashMap) adjMap.get(id2);
                            if(obj.containsKey(adjNode)) {
                                //remove from PQ
                                obj.remove(adjNode);
                                adjMap.put(id2, obj);
                            }
                        }
                        obj = (TIntObjectHashMap) adjMap.get(adjNode);
                        if (obj.containsKey(id2)) {
                            //remove from PQ
                            obj.remove(id2);
                            adjMap.put(adjNode, obj);
                        }
                        obj = (TIntObjectHashMap) adjMap.get(id1);
                        if (obj != null) {
                            //remove PQ
                            //add PQ
                            obj.put(adjNode, newCpEntry);
                            adjMap.put(id1, obj);

                        }
                        obj = (TIntObjectHashMap) adjMap.get(adjNode);
                        if (obj != null) {
                            //remove PQ
                            // add PQ
                            obj.put(id1, newCpEntry);
                            adjMap.put(adjNode, obj);
                        }

                    }
                }




            }



        }
        end = System.currentTimeMillis();
        System.err.println("Execution took : " + (end-start)/1000.0 + " secs");

        System.out.println(clusterList.size());
        PrintWriter writer = null;
        start = System.currentTimeMillis();
        try {
            writer = new PrintWriter(ccFilename, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        for(String s: clusterList) {
            writer.println(s);
        }
        writer.close();
        end = System.currentTimeMillis();
        System.err.println("Execution took : " + (end-start)/1000.0 + " secs");





    }

    public static void readFile(String filename) throws FileNotFoundException {
        Scanner in  = new Scanner( new FileReader(filename));
        String line;
        String splittedLine[];
        int id1, id2;
        double dist;
        ClusterPair cp;
        FibonacciHeap.Entry<ClusterPair> objEnt;
        int index = 1;
        while(in.hasNextLine()) {
            index ++;
            if(index%1000000.0 == 0) {
                System.out.println("Index:" + index);
            }
            line = in.nextLine();
            splittedLine = line.split("\\|");
            id1 = Integer.parseInt(splittedLine[0].trim());
            id2 = Integer.parseInt(splittedLine[1].trim());
            dist = 1-Double.parseDouble(splittedLine[2].trim());
            cp = new ClusterPair(id1, id2, dist);

            objEnt = PQ.enqueue(cp, cp.dist);
            if(adjMap.containsKey(id1) == false) {
                TIntObjectHashMap obj = new TIntObjectHashMap();
                obj.put(id2, objEnt);
                adjMap.put(id1, obj);
            } else {
                TIntObjectHashMap obj = (TIntObjectHashMap)adjMap.get(id1);
                assert(obj.containsKey(id2) == false);
                obj.put(id2, objEnt);
                adjMap.put(id1, obj);
            }

            if(adjMap.containsKey(id2) == false) {
                TIntObjectHashMap obj = new TIntObjectHashMap();
                obj.put(id1, objEnt);
                adjMap.put(id2, obj);
            } else {
                TIntObjectHashMap obj = (TIntObjectHashMap)adjMap.get(id2);
                assert(obj.containsKey(id1) == false);
                obj.put(id1, objEnt);
                adjMap.put(id2, obj);
            }

        }

    }

}

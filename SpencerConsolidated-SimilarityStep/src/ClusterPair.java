public class ClusterPair implements Comparable<ClusterPair> {

    private Cluster lCluster;
    private Cluster rCluster;
    private Double linkageDistance;

    public ClusterPair(){
    }
    public ClusterPair(Cluster left, Cluster right, Double distance){
        lCluster=left;
        rCluster=right;
        linkageDistance=distance;
    }

    public Cluster getlCluster() {
        return lCluster;
    }

    public void setlCluster(Cluster lCluster) {
        this.lCluster = lCluster;
    }

    public Cluster getrCluster() {
        return rCluster;
    }

    public void setrCluster(Cluster rCluster) {
        this.rCluster = rCluster;
    }

    public Double getLinkageDistance() {
        return linkageDistance;
    }

    public void setLinkageDistance(Double distance) {
        this.linkageDistance = distance;
    }

    /**
     * @return a new ClusterPair with the two left/right inverted
     */
    public ClusterPair reverse() {
        ClusterPair cp = new ClusterPair(getrCluster(), getlCluster(), getLinkageDistance());
        return cp;
    }



    @Override
    public int compareTo(ClusterPair o) {
        int result;
        if (o == null || o.getLinkageDistance() == null) {
            result = -1;
        } else if (getLinkageDistance() == null) {
            result = 1;
        } else {
            result = getLinkageDistance().compareTo(o.getLinkageDistance());
        }

        return result;
    }

    public Cluster agglomerate(String name) {
        if (name == null) {
            StringBuilder sb = new StringBuilder();
            if (lCluster != null) {
                sb.append(lCluster.getName());
            }
            if (rCluster != null) {
                if (sb.length() > 0) {
                    sb.append("&");
                }
                sb.append(rCluster.getName());
            }
            name = sb.toString();
        }
        Cluster cluster = new Cluster(name);
        cluster.setDistance(getLinkageDistance());
        cluster.addChild(lCluster);
        cluster.addChild(rCluster);
        lCluster.setParent(cluster);
        rCluster.setParent(cluster);
        return cluster;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (lCluster != null) {
            sb.append(lCluster.getName());
        }
        if (rCluster != null) {
            if (sb.length() > 0) {
                sb.append(" + ");
            }
            sb.append(rCluster.getName());
        }
        sb.append(" : ").append(linkageDistance);
        return sb.toString();
    }

}
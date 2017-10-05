package roadgraph;

import geography.GeographicPoint;

import java.util.ArrayList;
import java.util.List;

public class MapNode {

    private GeographicPoint nodeLocation;

    //we could use it for .....
    private String nodeName;

    //a list of outcoming edges
    private List<MapEdge> listEdges;

    //for using search by distance
    private double distance;

    //for using search by the speed limitat
    private double time;

    /**
     * @param nodeLocation
     * @param nodeName
     */
    public MapNode(final GeographicPoint nodeLocation, final String nodeName) {
        setNodeLocation(nodeLocation);
        setNodeName(nodeName);
        setListEdges(new ArrayList<>());
        setDistance(0.0);
        setTime(0.0);
    }

    public GeographicPoint getNodeLocation() {
        return nodeLocation;
    }

    public void setNodeLocation(GeographicPoint nodeLocation) {
        this.nodeLocation = nodeLocation;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public List<MapEdge> getListEdges() {
        return listEdges;
    }

    public void setListEdges(List<MapEdge> listEdges) {
        this.listEdges = listEdges;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MapNode mapNode = (MapNode) o;

        if (Double.compare(mapNode.getDistance(), getDistance()) != 0) return false;
        if (Double.compare(mapNode.getTime(), getTime()) != 0) return false;
        if (!getNodeLocation().equals(mapNode.getNodeLocation())) return false;
        if (getNodeName() != null ? !getNodeName().equals(mapNode.getNodeName()) : mapNode.getNodeName() != null)
            return false;
        return getListEdges() != null ? getListEdges().equals(mapNode.getListEdges()) : mapNode.getListEdges() == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getNodeLocation().hashCode();
        result = 31 * result + (getNodeName() != null ? getNodeName().hashCode() : 0);
        result = 31 * result + (getListEdges() != null ? getListEdges().hashCode() : 0);
        temp = Double.doubleToLongBits(getDistance());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getTime());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MapNode{");
        sb.append("nodeLocation=").append(nodeLocation);
        sb.append(", nodeName='").append(nodeName).append('\'');
        sb.append(", listEdges=").append(listEdges);
        sb.append(", distance=").append(distance);
        sb.append(", time=").append(time);
        sb.append('}');
        return sb.toString();
    }
}

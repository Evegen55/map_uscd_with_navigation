/**
 * @author UCSD MOOC development team
 * @author Evegen55
 * <p>
 * A class which reprsents a graph of geographic locations
 * Nodes in the graph are intersections between
 */
package roadgraph;


import geography.GeographicPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author UCSD MOOC development team and
 * @author Johnn
 * A class which represents a graph of geographic locations
 * Nodes in the graph are intersections between
 */
public class MapGraph {

    private final static Logger LOGGER = LoggerFactory.getLogger(MapGraph.class);

    //member variables here in WEEK 2
    private int numVertices;

    private HashMap<GeographicPoint, MapNode> listNodes;

    /**
     * Create a new empty MapGraph
     */
    public MapGraph() {
        //Implemented this constructor in WEEK 2
        listNodes = new HashMap<>();
        numVertices = 0;
    }

    /**
     * Get the number of vertices (road intersections) in the graph
     *
     * @return The number of vertices in the graph.
     */
    public int getNumVertices() {
        //Implemented this method in WEEK 2
        numVertices = listNodes.size();
        return numVertices;
    }

    /**
     * Return the intersections, which are the vertices in this graph.
     *
     * @return The vertices in this graph as GeographicPoints
     */
    public Set<GeographicPoint> getVertices() {
        //implemented this method in WEEK 2
        Set<GeographicPoint> str = listNodes.keySet();
        return str;
    }

    /**
     * Get the number of road segments in the graph
     *
     * @return The number of edges in the graph.
     */
    public int getNumEdges() {
        return listNodes.entrySet().stream().mapToInt(entry -> entry.getValue().getListEdges().size()).sum();
    }

    @Deprecated
    int getNumEdgesAsJava7() {
        //Implemented this method in WEEK 2
        int numEdges = 0;
        for (Entry<GeographicPoint, MapNode> entry : listNodes.entrySet()) {
            numEdges += entry.getValue().getListEdges().size();
        }
        return numEdges;
    }

    /**
     * Add a node corresponding to an intersection at a Geographic Point
     * If the location is already in the graph or null, this method does
     * not change the graph.
     *
     * @param location The location of the intersection
     * @return true if a node was added, false if it was not (the node
     * was already in the graph, or the parameter is null).
     */
    public boolean addVertex(final GeographicPoint location) {
        //Implemented this method in WEEK 2
        //add a distance for week 3 - set a distance to infinity
        LOGGER.debug("adding" + "\t" + location.toString());
        if (!listNodes.containsKey(location)) {
            MapNode mapNode = new MapNode(location, "");
            listNodes.put(location, mapNode);
            return true;
        }
        return false;
    }

    /**
     * Adds a DIRECTED edge to the graph from pt1 to pt2 with speed limitation depends on the road type
     * Precondition: Both GeographicPoints have already been added to the graph
     * <p>
     * set a speed limit on the road that depending od a type of a street
     * motorway_link  with no limitations (we could use 500 km/h)
     * (A) road, specially designed and built for motor traffic,
     * which does not serve properties bordering on it, and which:
     * is provided, except at special points or temporarily,
     * with separate carriage ways for the two directions of traffic,
     * separated from each other, either by a dividing strip not intended for traffic,
     * or exceptionally by other means;
     * does not cross at level with any road, railway or tramway track, or footpath;
     * is specially sign posted as a motorway and is reserved for specific categories of road motor vehicles.
     * 'Entry and exit lanes of motorways are included irrespectively
     * of the location of the signposts. Urban motorways are also included.'
     *
     * @param from     The starting point of the edge
     * @param to       The ending point of the edge
     * @param roadName The name of the road
     * @param roadType The type of the road
     * @param length   The length of the road, in km
     * @throws IllegalArgumentException If the points have not already been
     *                                  added as nodes to the graph, if any of the arguments is null,
     *                                  or if the length is less than 0.
     * @see <a href = "https://www.wikiwand.com/en/Controlled-access_highway"> </a>
     * residential (max speed limit in most cases 60 km/h)
     * Residential throughways such as 19th Avenue, Guerrero, California, Oak and Fell Streets
     * have high levels of fast-moving traffic with residential land uses.
     * As such, they are often not designed to serve residential uses, and can be unpleasant to walk or live along.
     * Streetscape improvements should focus on buffering the sidewalk and adjacent homes from vehicles
     * passing in the street and providing a generous, useable public realm through landscaping, curb extensions,
     * or widened sidewalks where roadway space allows.
     * @see <a href = "http://www.sfbetterstreets.org/design-guidelines/street-types/residential-throughways/"> </a>
     * living_street (max speed limit on the world 20 km/h)
     * A living street is a street designed primarily with the interests of pedestrians
     * and cyclists in mind and as a social space where people can meet and where
     * children may also be able to play legally and safely.
     * These roads are still available for use by motor vehicles, however their design aims
     * to reduce both the speed and dominance of motorised transport.
     * This is often achieved using the shared space approach, with greatly reduced demarcations
     * between vehicle traffic and pedestrians. Vehicle parking may also be restricted to designated bays.
     * It became popular during the 1970s in the Netherlands, which is why
     * the Dutch word for a living street (woonerf) is often used as a synonym.
     * Country-specific living street implementations include: home zone (United Kingdom),
     * residential zone (ru:����� ����, Russia), shared zone (Australia/New Zealand),
     * woonerf (Netherlands and Flanders) and zone residentielle (France).
     * secondary
     * a simple secondary highway. The maximum speed, in kilometers per hour which is allowed on the road, for example 120
     * tertiary
     * The highway=tertiary tag is used for roads connecting smaller settlements, and within large settlements for roads connecting local centres.
     * In terms of the transportation network, OpenStreetMap "tertiary" roads commonly also connect minor streets to more major roads.
     * (max speed limit in most cases 80 km/h)
     * unclassified (max speed limit in most cases 80 km/h)
     * The tag highway=unclassified is used for minor public roads typically at the lowest level of the interconnecting grid network.
     * Unclassified roads have lower importance in the road network than tertiary roads, and are not residential streets or agricultural tracks.
     * highway=unclassified should be used for roads used for local traffic and used to connect other towns, villages or hamlets.
     * Unclassified roads are considered usable by motor cars.
     * @see <a href = "http://wiki.openstreetmap.org/wiki/Tag:highway%3Dunclassified"> </a>
     * @see <a href = "https://www.wikiwand.com/en/Hierarchy_of_roads"> </a>
     * @see <a href = "https://www.wikiwand.com/en/Street_hierarchy"> </a>
     */
    public void addEdge(final GeographicPoint from, final GeographicPoint to, final String roadName,
                        final String roadType, final double length) throws IllegalArgumentException {
        //implemented this method in WEEK 2
        if (listNodes.containsKey(from) && listNodes.containsKey(to)) {
            final MapNode startNode = listNodes.get(from);
            final MapNode finishNode = listNodes.get(to);
            final MapEdge addedMapEdge = new MapEdge(startNode, finishNode, roadName, roadType, length);

            if ((roadType.compareTo("motorway") == 0) || (roadType.compareTo("motorway_link") == 0)) {
                addedMapEdge.setSpeedLimit(500);
            } else if (roadType.compareTo("living_street") == 0) {
                addedMapEdge.setSpeedLimit(20);
            } else if (roadType.compareTo("residential") == 0) {
                addedMapEdge.setSpeedLimit(60);
            } else if (roadType.compareTo("unclassified") == 0) {
                addedMapEdge.setSpeedLimit(80);
            } else if ((roadType.compareTo("secondary") == 0) || (roadType.compareTo("secondary_link") == 0)) {
                addedMapEdge.setSpeedLimit(120);
            } else if ((roadType.compareTo("tertiary") == 0) || (roadType.compareTo("tertiary_link") == 0)) {
                addedMapEdge.setSpeedLimit(80);
            } else if ((roadType.compareTo("primary") == 0) || (roadType.compareTo("primary_link") == 0)) {
                addedMapEdge.setSpeedLimit(80);
            } else if ((roadType.compareTo("trunk") == 0) || (roadType.compareTo(" trunk_link") == 0)) {
                addedMapEdge.setSpeedLimit(90);
            }

            //add an OUTCOMING edge from -> to
            listNodes.get(from).getListEdges().add(addedMapEdge);
            LOGGER.debug("an OUTCOMING edge from -> to added: " + "\t" + addedMapEdge.toString());
        }
    }

    /**
     * @param forSearch
     * @return
     */
    public List<MapNode> getNeighbours(MapNode forSearch) {
        List<MapNode> att = new ArrayList<>();
        List<MapEdge> listForSearch = forSearch.getListEdges();
        for (MapEdge sch : listForSearch) {
            MapNode mdn = sch.getFinishNode();
            att.add(mdn);
        }
        return att;
    }

    /**
     * @param start
     * @param end
     * @return
     */
    private double getSpeedLimit(MapNode start, MapNode end) {
        double lim = 0;
        List<MapEdge> listForSearch = start.getListEdges();
        for (MapEdge sch : listForSearch) {
            if ((sch.getStartNode().getNodeLocation().toString().compareTo(start.getNodeLocation().toString()) == 0) &&
                    (sch.getFinishNode().getNodeLocation().toString().compareTo(end.getNodeLocation().toString()) == 0)) {
                return lim = sch.getSpeedLimit();
            }
        }
        return lim;
    }

    /**
     * @param parentMap
     * @param start
     * @param goal
     * @return
     * @author UCSD MOOC development team
     */
    private List<GeographicPoint> reconstructPath(final HashMap<MapNode, MapNode> parentMap, final MapNode start,
                                                  final MapNode goal) {
        final LinkedList<GeographicPoint> path = new LinkedList<>();
        MapNode current = goal;
        while ((current != null) && (!current.equals(start))) {
            path.addFirst(current.getNodeLocation());
            current = parentMap.get(current);
        }
        path.addFirst(start.getNodeLocation());
        return path;
    }

    /**
     * @param parentMap
     */
    private void printNodesMap(HashMap<MapNode, MapNode> parentMap) {
        for (Map.Entry<MapNode, MapNode> entry : parentMap.entrySet()) {
            System.out.print("key" + "\t" + entry.getKey().getNodeLocation().toString() + "\t" + "distance " + entry.getKey().getDistance() + "\t" + "time " + entry.getKey().getTime() + "\t");
            System.out.println("value" + "\t" + entry.getValue().getNodeLocation().toString() + "\t" + "distance " + entry.getValue().getDistance() + "\t" + "time " + entry.getValue().getTime());
        }
    }

    /**
     * A helper method to get length between two nodes
     *
     * @param start
     * @param end
     * @return
     */
    private double getLengthEdgeBeetwen(MapNode start, MapNode end) {
        // TODO Auto-generated method stub
        double length = 0.0;
        List<MapEdge> listForSearch = start.getListEdges();
        for (MapEdge sch : listForSearch) {
            if ((sch.getStartNode().getNodeLocation().toString().compareTo(start.getNodeLocation().toString()) == 0) &&
                    (sch.getFinishNode().getNodeLocation().toString().compareTo(end.getNodeLocation().toString()) == 0)) {
                return length = sch.getStreetLength();
            }
        }
        return length;
    }

    /**
     * a helper method for using it as distance priority
     *
     * @return comparator
     */
    public Comparator<MapNode> createComparator() {
        Comparator<MapNode> comparator = new Comparator<MapNode>() {
            @Override
            public int compare(MapNode x, MapNode y) {
                // You could return x.getDistance() - y.getDistance(), which would be more efficient.
                return (int) (x.getDistance() - y.getDistance());
                //but for more better understanding you should use smth like this:
                //     if (x.getDistance() < y.getDistance()) {
                //         return -1;
                //          }
                //      if (x.getDistance() > y.getDistance()) {
                //          return 1;
                //      }
                //       return 0;
            }
        };
        return comparator;
    }

    /**
     * a helper method for using it as distance priority
     *
     * @return comparator
     */
    private Comparator<MapNode> createComparatorByTime() {
        Comparator<MapNode> comparator = (x, y) -> {
            // You could return x.getDistance() - y.getDistance(), which would be more efficient.
            return (int) (x.getTime() - y.getTime());
        };
        return comparator;
    }

    /**
     * a helper method for getting a reduced cost
     *
     * @param start
     * @param goal
     * @return
     * @see <a href = "https://en.wikipedia.org/wiki/A*_search_algorithm"> </a>
     */
    private double getReducedCost(GeographicPoint start, GeographicPoint goal) {
        return (Math.sqrt(Math.pow((start.x - goal.x), 2) + Math.pow((start.y - goal.y), 2)));
    }

    /**
     * @param start
     * @param end
     * @return
     */
    private double getTimeBetweenNodes(MapNode start, MapNode end) {
        // TODO Auto-generated method stub
        double limit = getSpeedLimit(start, end);
        double dist = getLengthEdgeBeetwen(start, end);
        return dist / limit;
    }


    //*************************************************************************************************
    //list of searching methods


    /**
     * Find the path from start to goal using breadth first search
     *
     * @param start The starting location
     * @param goal  The goal location
     * @return The list of intersections that form the shortest (unweighted)
     * path from start to goal (including both start and goal).
     */
    public List<GeographicPoint> bfs(GeographicPoint start, GeographicPoint goal) {
        // Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {
        };
        return bfs(start, goal, temp);
    }

    /**
     * Find the path from start to goal using breadth first search
     *
     * @param start        The starting location
     * @param goal         The goal location
     * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
     * @return The list of intersections that form the shortest (unweighted)
     * path from start to goal (including both start and goal).
     */
    public List<GeographicPoint> bfs(final GeographicPoint start, final GeographicPoint goal,
                                     final Consumer<GeographicPoint> nodeSearched) {
        //Implemented this method in WEEK 2
        if (start == null || goal == null) {
            throw new NullPointerException("Cannot find route from or to null node");
        }

        final MapNode startNode = listNodes.get(start);
        final MapNode endNode = listNodes.get(goal);

        if (startNode == null) {
            LOGGER.error("Start node " + start + " does not exist");
            return null;
        }
        if (endNode == null) {
            LOGGER.error("End node " + goal + " does not exist");
            return null;
        }

        // setup to begin BFS
        final HashMap<MapNode, MapNode> parentMap = new HashMap<>();
        final Queue<MapNode> toExplore = new LinkedList<>();
        final HashSet<MapNode> visited = new HashSet<>();

        toExplore.add(startNode);
        MapNode next = null;
        while (!toExplore.isEmpty()) {
            next = toExplore.remove();
            //----------------------------
            // hook for visualization
            nodeSearched.accept(next.getNodeLocation());
            //----------------------
            if (next.equals(endNode)) break;
            List<MapNode> neighbors = getNeighbours(next);
            for (MapNode neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parentMap.put(neighbor, next);
                    toExplore.add(neighbor);
                }
            }
        }
        if (!next.equals(endNode)) {
            LOGGER.error("No path found from " + start + " to " + goal);
            return null;
        }
        // Reconstruct the parent path
        return reconstructPath(parentMap, startNode, endNode);
    }


    //===================================================================================================
    //for week 3
    //===================================================================================================
    //part 1
    //===================================================================================================

    /**
     * Find the path from start to goal using Dijkstra's algorithm
     *
     * @param start The starting location
     * @param goal  The goal location
     * @return The list of intersections that form the shortest path from
     * start to goal (including both start and goal).
     */
    public List<GeographicPoint> dijkstra(final GeographicPoint start, final GeographicPoint goal) {
        // Dummy variable for calling the search algorithms
        // You do not need to change this method.
        Consumer<GeographicPoint> temp = (x) -> {
        };
        return dijkstra(start, goal, temp);
    }

    /**
     * Find the path from start to goal using Dijkstra's algorithm
     *
     * @param start        The starting location
     * @param goal         The goal location
     * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
     * @return The list of intersections that form the shortest path from
     * start to goal (including both start and goal).
     */
    public List<GeographicPoint> dijkstra(final GeographicPoint start, final GeographicPoint goal,
                                          final Consumer<GeographicPoint> nodeSearched) {
        //implemented this method in WEEK 3
        List<GeographicPoint> lfs;
        if (listNodes.containsKey(start) && listNodes.containsKey(goal)) {
            //initialize ADT
            //we should use a comparator!!!
            final Comparator<MapNode> mapNodeComparator = createComparator();
            final PriorityQueue<MapNode> mapNodePriorityQueue = new PriorityQueue<>(5, mapNodeComparator);
            final HashMap<MapNode, MapNode> parentMap = new HashMap<>();
            final Set<MapNode> visited = new HashSet<>();
            //set a distance for all nodes to infinity
            listNodes.entrySet().forEach(entry -> entry.getValue().setDistance(Double.POSITIVE_INFINITY));

            //getting a start and goal node
            final MapNode startNode = listNodes.get(start);
            final MapNode goalNode = listNodes.get(goal);
            //set a distance start node as 0
            startNode.setDistance(0.0);
            //start working with a PriorityQueue
            mapNodePriorityQueue.add(startNode);
            //start a loop through PriorityQueue
            MapNodeCheckContainer mapNodeCheckContainer = new MapNodeCheckContainer();
            while (!mapNodePriorityQueue.isEmpty()) {
                MapNode current = mapNodePriorityQueue.poll();
                mapNodeCheckContainer.setMapNode(current);
                //--------------------------------------------
                // hook for visualization
                nodeSearched.accept(current.getNodeLocation());
                //--------------------------------------------
                if (!visited.contains(current)) {
                    visited.add(current);
                    if ((goal.toString().compareTo(current.getNodeLocation().toString())) == 0) break;
                    getNeighbours(current).forEach(neighbour -> {
                        //not in visited set ->
                        if (!visited.contains(neighbour)) {
                            //if path through current to n is shorter ->
                            double edgeLength = getLengthEdgeBeetwen(current, neighbour);
                            if (current.getDistance() + edgeLength < neighbour.getDistance()) {
                                //update neighbour's distance
                                neighbour.setDistance(current.getDistance() + edgeLength);
                                parentMap.put(neighbour, current);
                            }
                            //enqueue into the mapNodePriorityQueue
                            mapNodePriorityQueue.add(neighbour);
                        }
                    });
                }
            }

            //it just because we havent got the full road map in some cases
            if (!mapNodeCheckContainer.getMapNode().equals(goalNode)) {
                LOGGER.warn("No path found from " + start + " to " + goal);
                return null;
            }
            lfs = reconstructPath(parentMap, startNode, goalNode);
        } else {
            throw new NullPointerException("Cannot find route from or to null node");
        }
        return lfs;
    }


    //part 2
    //===================================================================================================

    /**
     * Find the path from start to goal using A-Star search
     *
     * @param start The starting location
     * @param goal  The goal location
     * @return The list of intersections that form the shortest path from
     * start to goal (including both start and goal).
     */
    public List<GeographicPoint> aStarSearch(final GeographicPoint start, final GeographicPoint goal) {
        // Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {
        };
        return aStarSearch(start, goal, temp);
    }

    /**
     * Find the path from start to goal using A-Star search
     *
     * @param start        The starting location
     * @param goal         The goal location
     * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
     * @return The list of intersections that form the shortest path from
     * start to goal (including both start and goal).
     */
    public List<GeographicPoint> aStarSearch(final GeographicPoint start,
                                             final GeographicPoint goal, final Consumer<GeographicPoint> nodeSearched) {
        //implemented this method in WEEK 3
        List<GeographicPoint> lfs;
        if (listNodes.containsKey(start) && listNodes.containsKey(goal)) {
            //initialize ADT
            //we should use a comparator!!!
            final Comparator<MapNode> mapNodeComparator = createComparator();
            final PriorityQueue<MapNode> mapNodePriorityQueue = new PriorityQueue<>(5, mapNodeComparator);
            final HashMap<MapNode, MapNode> parentMap = new HashMap<>();
            final Set<MapNode> visited = new HashSet<>();
            //set a distance to infinity
            listNodes.entrySet().forEach(entry -> entry.getValue().setDistance(Double.POSITIVE_INFINITY));
            //getting a  reduced cost
            double redCost = getReducedCost(start, goal);
            LOGGER.info("getting a  reduced cost \t" + redCost);
            //get a start and goal node
            final MapNode startNode = listNodes.get(start);
            final MapNode goalNode = listNodes.get(goal);
            //set a distance start node as 0
            startNode.setDistance(0.0);
            //start working with a PriorityQueue
            mapNodePriorityQueue.add(startNode);
            //start a loop through PriorityQueue
            MapNodeCheckContainer mapNodeCheckContainer = new MapNodeCheckContainer();
            while (!mapNodePriorityQueue.isEmpty()) {
                MapNode current = mapNodePriorityQueue.poll();
                mapNodeCheckContainer.setMapNode(current);
                //--------------------------------------------
                // hook for visualization
                nodeSearched.accept(current.getNodeLocation());
                //--------------------------------------------
                if (!visited.contains(current)) {
                    visited.add(current);
                    if (goal.toString().equalsIgnoreCase(current.getNodeLocation().toString())) break;
                    getNeighbours(current).forEach(neighbour -> {
                        //not in visited set ->
                        if (!visited.contains(neighbour)) {
                            //if path through current to n is shorter ->
                            double edgeLength = getLengthEdgeBeetwen(current, neighbour);
                            if ((current.getDistance() + edgeLength < neighbour.getDistance())
                                    && getReducedCost(current.getNodeLocation(), goal) <= redCost) {
                                LOGGER.debug("" + getReducedCost(current.getNodeLocation(), goal));
                                //update neighbour's distance
                                neighbour.setDistance(current.getDistance() + edgeLength);
                                parentMap.put(neighbour, current);
                            }
                            //enqueue into the mapNodePriorityQueue
                            mapNodePriorityQueue.add(neighbour);
                        }
                    });
                }
            }
            //it just because we havent got the full road map in some cases
            if (!mapNodeCheckContainer.getMapNode().equals(goalNode)) {
                LOGGER.warn("No path found from " + start + " to " + goal);
                return null;
            }
            lfs = reconstructPath(parentMap, startNode, goalNode);
        } else {
            throw new NullPointerException("Cannot find route from or to null node");
        }
        return lfs;
    }

    //==============================================================================================================
    //MY VARIANT
    //==============================================================================================================

    /**
     * Find the path from start to goal using Dijkstra's algorithm
     *
     * @param start The starting location
     * @param goal  The goal location
     * @return The list of intersections that form the faster path from
     * start to goal (including both start and goal).
     */
    public List<GeographicPoint> dijkstraByTime(GeographicPoint start, GeographicPoint goal) {
        // Dummy variable for calling the search algorithms
        // You do not need to change this method.
        Consumer<GeographicPoint> temp = (x) -> {
        };
        return dijkstraByTime(start, goal, temp);
    }

    /**
     * Find the path from start to goal using Dijkstra's algorithm
     *
     * @param start        The starting location
     * @param goal         The goal location
     * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
     * @return The list of intersections that form the faster path from
     * start to goal (including both start and goal).
     */
    public List<GeographicPoint> dijkstraByTime(final GeographicPoint start, final GeographicPoint goal,
                                                final Consumer<GeographicPoint> nodeSearched) {
        // TODO: Implement this method in WEEK 3
        List<GeographicPoint> lfs;
        if (listNodes.containsKey(start) && listNodes.containsKey(goal)) {
            //initialize ADT
            //we should use a comparator created depends on time!!!
            final Comparator<MapNode> comparatorByTime = createComparatorByTime();
            final PriorityQueue<MapNode> pq = new PriorityQueue<>(5, comparatorByTime);
            final HashMap<MapNode, MapNode> parentMap = new HashMap<>();
            final Set<MapNode> visited = new HashSet<>();
            //set a time to goal to infinity
            listNodes.entrySet().forEach(entry -> entry.getValue().setDistance(Double.POSITIVE_INFINITY));
            //get a start and goal node
            final MapNode startNode = listNodes.get(start);
            final MapNode goalNode = listNodes.get(goal);
            //set a time start node as 0
            startNode.setTime(0.0);
            //start working with a PriorityQueue
            pq.add(startNode);
            //start a loop through PriorityQueue
            MapNodeCheckContainer mapNodeCheckContainer = new MapNodeCheckContainer();
            while (!pq.isEmpty()) {
                MapNode current = pq.poll();
                mapNodeCheckContainer.setMapNode(current);
                //--------------------------------------------
                // hook for visualization
                nodeSearched.accept(current.getNodeLocation());
                //--------------------------------------------
                if (!visited.contains(current)) {
                    visited.add(current);
                    if ((goal.toString().compareTo(current.getNodeLocation().toString())) == 0) break;
                    //for each of current's neighbors, "next", ->
                    getNeighbours(current).forEach(next -> {
                        //not in visited set ->
                        if (!visited.contains(next)) {
                            //if path through current to n is faster ->
                            double timeToNextNode = getTimeBetweenNodes(current, next);
                            LOGGER.debug("timeToNextNode" + "\t" + timeToNextNode);
                            if (current.getTime() + timeToNextNode < next.getTime()) {
                                //update next's speed Limit
                                next.setTime(current.getTime() + timeToNextNode);
                                parentMap.put(next, current);
                            }
                            //enqueue into the pq
                            pq.add(next);
                        }
                    });
                }

            }
            //it just because we havent got the full road map in some cases
            if (!mapNodeCheckContainer.getMapNode().equals(goalNode)) {
                LOGGER.debug("No path found from " + start + " to " + goal);
                return null;
            }

            lfs = reconstructPath(parentMap, startNode, goalNode);
        } else {
            throw new NullPointerException("Cannot find route from or to null node");
        }
        return lfs;
    }


    /**
     * Find the path from start to goal using A-Star search
     *
     * @param start The starting location
     * @param goal  The goal location
     * @return The list of intersections that form the shortest path from
     * start to goal (including both start and goal).
     */
    public List<GeographicPoint> aStarSearchByTime(GeographicPoint start, GeographicPoint goal) {
        // Dummy variable for calling the search algorithms
        Consumer<GeographicPoint> temp = (x) -> {
        };
        return aStarSearchByTime(start, goal, temp);
    }

    /**
     * Find the path from start to goal using A-Star search
     *
     * @param start        The starting location
     * @param goal         The goal location
     * @param nodeSearched A hook for visualization.  See assignment instructions for how to use it.
     * @return The list of intersections that form the shortest path from
     * start to goal (including both start and goal).
     */
    public List<GeographicPoint> aStarSearchByTime(GeographicPoint start,
                                                   GeographicPoint goal, Consumer<GeographicPoint> nodeSearched) {
        // TODO: Implement this method in WEEK 3
        List<GeographicPoint> lfs = new LinkedList<>();
        if (listNodes.containsKey(start) && listNodes.containsKey(goal)) {
            //initialize ADT
            //we should use a comparator created depends on time!!!
            Comparator<MapNode> cmtr = createComparatorByTime();
            PriorityQueue<MapNode> pq = new PriorityQueue<>(5, cmtr);
            HashMap<MapNode, MapNode> parentMap = new HashMap<>();
            Set<MapNode> visited = new HashSet<>();
            //set a time to goal to infinity
            for (Map.Entry<GeographicPoint, MapNode> entry : listNodes.entrySet()) {
                entry.getValue().setTime(Double.POSITIVE_INFINITY);
            }
            //getting a  reduced cost
            double redCost = getReducedCost(start, goal);                                             //System.out.println(redCost);
            //get a start and goal node
            MapNode startNode = listNodes.get(start);
            MapNode goalNode = listNodes.get(goal);
            //set a distance start node as 0
            startNode.setTime(0.0);
            //start working with a PriorityQueue
            pq.add(startNode);
            //start a loop through PriorityQueue
            while (!pq.isEmpty()) {
                MapNode curr = pq.poll();
                //--------------------------------------------
                // hook for visualization
                nodeSearched.accept(curr.getNodeLocation());
                //--------------------------------------------
                if (!visited.contains(curr)) {
                    visited.add(curr);
                    if (goal.toString().equalsIgnoreCase(curr.getNodeLocation().toString())) break;
                    //for each of curr's neighbors, "next", ->
                    List<MapNode> neighbors = getNeighbours(curr);
                    for (MapNode next : neighbors) {
                        //not in visited set ->
                        if (!visited.contains(next)) {
                            //if path through curr to n is faster ->
                            double timeToNextNode = getTimeBetweenNodes(curr, next);
                            //test it
                            //System.out.println("timeToNextNode" + "\t" +timeToNextNode);
                            if ((curr.getTime() + timeToNextNode < next.getTime()) && getReducedCost(curr.getNodeLocation(), goal) <= redCost) {
                                //update next's distance
                                next.setTime(curr.getTime() + timeToNextNode);
                                parentMap.put(next, curr);
                            }
                            //enqueue into the pq
                            pq.add(next);
                        }
                    }
                }
            }
            lfs = reconstructPath(parentMap, startNode, goalNode);
        } else {
            throw new NullPointerException("Cannot find route from or to null node");
        }
        return lfs;
    }

}

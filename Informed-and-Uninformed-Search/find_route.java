import java.util.*;
import java.io.File;

public class find_route
{
    public static class Fringe
    {
        // this class is for storing info about nodes
        // while doing the search
        public Fringe parent;
        public Vertex node;
        public int depth;
        public int distance;


        public Fringe(Fringe parent,Vertex node, int depth, int distance)
        {
            this.parent = parent;
            this.node = node;
            this.depth = depth;
            this.distance = distance;
        }
    }
    public static class Solution
    {
        // This class holds the entire solution answers
        // and solved parts for output
        // contains methods for getting the path correctly

        public ArrayList<Vertex> path;
        public ArrayList<Vertex> solutionPath;
        public ArrayList<Fringe> solutionFringe;
        public LinkedList<Fringe> fringe;
        public HashSet<Vertex> closedSet;
        public boolean withHeuristic;

        public static int nodesExpanded = 0;
        public static int nodesGenerated = 1;
        public static int distance = 0;
        public String pathText;

        public Solution()
        {
            path = new ArrayList<Vertex>();
            fringe = new LinkedList<Fringe>();
            closedSet = new HashSet<Vertex>();
            solutionFringe = new ArrayList<Fringe>();
            solutionPath = new ArrayList<Vertex>();
        }
        public void getPath()
        {
            if(solutionFringe.size() == 0)
            {
                // case for no solution
                return;
            }
            Fringe f = solutionFringe.get(solutionFringe.size()-1);
            //solutionPath.add(f.node);
            while(f != null)
            {
                // iterate through the parent of each node
                // in fringe to get the solution

                solutionPath.add(f.node);
                //System.out.println(f.node.name +" "+f.depth);
                f = f.parent;
            }
        }
        public String prepPath()
        {
            // here the entire route will be represented by
            // a single string pathText
            pathText = "route: \n";
            //System.out.println("route: ");
            if(solutionPath.size() == 0)
            {
                // case for no solution

                //System.out.println("none");
                pathText += "none\n";
                return pathText;
            }
            for(int i = solutionPath.size()-1; i > 0; i--)
            {
                ArrayList<Edge> edges = solutionPath.get(i).getEdges();
                int weight = -1;
                for(Edge e : edges)
                {
                    // this loop gets the corresponding edge between the nodes
                    if(e.getTo().equals(solutionPath.get(i-1)))
                    {
                        weight = e.weight;
                        break;
                    }
                }
                //System.out.println();
                //if(solutionPath.get(i).name != solutionPath.get(i+1).getName())
                //System.out.printf("%s to %s, %d km\n",solutionPath.get(i).name,
                //    solutionPath.get(i+1).getName(),weight);

                pathText += solutionPath.get(i).name + " to " +
                    solutionPath.get(i-1).name + ", " + weight + " km\n";

                distance += weight;
            }
            return pathText;
        }
        /*
        public void getDistance()
        {
            System.out.println("route: ");
            if(path.size() == 0)
            {
                System.out.println("none");
                return;
            }
            for(int i = 0; i < path.size()-1; i++)
            {
                ArrayList<Edge> edges = path.get(i).getEdges();
                int weight = -1;
                for(Edge e : edges)
                {
                    if(e.getTo().equals(path.get(i+1)))
                    {
                        weight = e.weight;
                        break;
                    }
                    //System.out.print(e.weight + " ");
                }
                //System.out.println();
                //if(path.get(i).name != path.get(i+1).getName())
                System.out.printf("%s to %s, %d km\n",path.get(i).name,path.get(i+1).getName(),weight);
            }
            return;
        }
        */
    }
    public static class Edge
    {
        // create class for edges of vertices

        public Vertex to;
        public int weight;

        public Edge(Vertex to, int weight)
        {
            this.to = to;
            this.weight = weight;
        }
        public Vertex getTo()
        {
            return to;
        }
        public int getWeight()
        {
            return weight;
        }
    }
    public static class Vertex
    {
        //create class for vertices
        // the edges arraylist represents all edges from that node

        public String name;
        public int heuristic;
        public ArrayList<Edge> edges;

        public Vertex(String name)
        {
            this.name = name;
            edges = new ArrayList<>();
        }
        public String getName()
        {
            return name;
        }
        public void addEdge(Edge edge)
        {
            edges.add(edge);
        }
        public ArrayList<Edge> getEdges()
        {
            return new ArrayList<Edge>(edges);
        }
    }
    public static class Graph
    {
        // graph class that represents the input file
        // as connected nodes and edges

        public HashMap<String, Vertex> vertices;

        public Graph()
        {
            vertices = new HashMap<String, Vertex>();
        }

        public HashMap<String, Vertex> getVertices()
        {
            return new HashMap<String, Vertex>(vertices);
        }

        public void addVertex(String name, Vertex vertex)
        {
            vertices.put(name, vertex);
        }
        public Vertex getVertex(String key)
        {
            return vertices.get(key);
        }
    }
    public static void main(String[] args)
    {
        if(args.length != 3 && args.length != 4)
        {
            System.out.println("Wrong input arguments need 3 or 4.");
            System.exit(0);
        }
        Graph graph = new Graph();
        Solution solution = new Solution();
        boolean withHeuristic = false;

        String inputTextString = args[0];
        String origin = args[1];
        String destination = args[2]; // is the goal

        if(args.length == 4)
        {
            // condition when heuristic file is given

            withHeuristic = true;
        }
        solution.withHeuristic = withHeuristic;
        try
        {
            // get input graph information

            File inFile1 = new File(inputTextString);
            Scanner fileScanner = new Scanner(inFile1);

            while(fileScanner.hasNextLine())
            {
                String line = fileScanner.nextLine();
                if(line.equals("END OF INPUT"))
                    break;

                String tokens[] = line.split(" ");
                int weight = Integer.valueOf(tokens[2]);

                // condition to add city to hashmap if not in already

                if(!graph.vertices.containsKey(tokens[0]))
                {
                    Vertex newVertex = new Vertex(tokens[0]);
                    graph.addVertex(tokens[0], newVertex);
                }
                if(!graph.vertices.containsKey(tokens[1]))
                {
                    Vertex newVertex = new Vertex(tokens[1]);
                    graph.addVertex(tokens[1], newVertex);
                }

                // create two directed edges with weight in both

                graph.getVertex(tokens[0]).addEdge(new Edge(graph.getVertex(tokens[1]), weight));
                graph.getVertex(tokens[1]).addEdge(new Edge(graph.getVertex(tokens[0]), weight));
            }
            fileScanner.close();
        }
        catch (Exception e)
        {
            System.out.println("File error occured");
            System.exit(0);
        }
        if(withHeuristic)
        {
            try
            {
                // get input graph information

                File inFile1 = new File(args[3]);
                Scanner fileScanner = new Scanner(inFile1);

                while(fileScanner.hasNextLine())
                {
                    String line = fileScanner.nextLine();
                    if(line.equals("END OF INPUT"))
                        break;

                    String tokens[] = line.split(" ");
                    graph.vertices.get(tokens[0]).heuristic = Integer.valueOf(tokens[1]);

                }
                fileScanner.close();
            }
            catch (Exception e)
            {
                System.out.println("File error occured");
                System.exit(0);
            }
        }

        /*
        // Error checking with graph

        for(Edge edge: graph.getVertex("Birmingham").edges)
        {
            System.out.println(edge.weight);
        }

        for (Map.Entry<String, Vertex> e : graph.vertices.entrySet())
        {
            System.out.print("Key: " + e.getKey() + " Values: ");
            for(Edge edge: e.getValue().edges)
            {
                System.out.print(edge.weight+ " ");
            }
            System.out.println();
        }
        */

        // perform uninformed search
        // heuristic boolean is pass with solution object
        // and will be used in insertFringe();

        graphSearch(graph, origin, destination, solution);

        System.out.printf("Nodes expanded: %d\nNodes generated: %d\n",
            solution.nodesExpanded, solution.nodesGenerated);
        solution.getPath();
        String wholePathString = solution.prepPath();
        if(solution.distance == -1)
        {
            System.out.println("Distance: infinity");
        }
        else
        {
            System.out.println("Distance: " + solution.distance + " km");
        }
        System.out.println(wholePathString);

        return;
    }

    public static void graphSearch(Graph graph, String start, String end, Solution solution)
    {
        // add start node to fringe
        // note adds to closed set later
        solution.fringe.add(new Fringe(null,graph.getVertex(start),0,0));
        //insertFringe(solution.fringe, null, graph.getVertex(start));
        while(true)
        {
            for(Fringe f: solution.fringe)
            {
                //System.out.println(f.node.name +" "+solution.fringe.size());
            }
            //System.out.println(solution.fringe.size());
            if(solution.fringe.size() == 0)
            {
                // condition for failed search, empty fringe
                solution.path.clear();
                solution.solutionFringe.clear();;
                solution.distance = -1;
                return;
            }
            Fringe currentFringe = solution.fringe.pop();
            solution.nodesExpanded++;
            //System.out.println(currentFringe.node.getName());
            if(currentFringe.node.name.equals(end))
            {
                // goal test

                solution.path.add(currentFringe.node);
                solution.solutionFringe.add(currentFringe);
                return;
            }

            if(!solution.closedSet.contains(currentFringe.node))
            {
                // add to closed and generate connected nodes
                solution.closedSet.add(currentFringe.node);
                for(Edge edge : currentFringe.node.edges)
                {
                    //System.out.println(solution.fringe.size());
                    // iterate through all nodes connected to current node
                    solution.nodesGenerated++;
                    solution.path.add(currentFringe.node);
                    solution.solutionFringe.add(currentFringe);
                    insertFringe(solution, currentFringe, edge.getTo());

                }
            }
        }
    }
    public static void insertFringe(Solution solution, Fringe parent, Vertex node)
    {
        // get edges of parent node

        ArrayList<Edge> edges = parent.node.getEdges();
        int weight = 0;


        for(Edge e : edges)
        {
            if(e.getTo().equals(node))
            {
                //System.out.println("correctly found edge");
                weight = e.weight;
                //System.out.println(parent.node.name + " "+e.getTo().name +" "+e.weight);
                break;
            }
        }

        Fringe newFringe = new Fringe(parent, node, (parent.depth+1), (parent.distance+weight));
        int i = 0;
        if(solution.fringe.size() == 0)
        {
            // insert as only element
            solution.fringe.add(0,newFringe);
            return;
        }
        for(Fringe f: solution.fringe)
        {
            // insert in ascending order

            if(!solution.withHeuristic)
            {
                if(newFringe.distance <= f.distance)
                {
                    //System.out.println(solution.fringe.get(i).distance);
                    //System.out.println(newFringe.distance);
                    //System.out.println(i);
                    solution.fringe.add(i, newFringe);
                    //System.out.println("added to fringe");
                    return;
                }
                i++;
            }
            else
            {
                // For heuristic use heuristic value in comparison

                if(newFringe.distance + newFringe.node.heuristic <= f.distance + f.node.heuristic)
                {
                    //System.out.println(solution.fringe.get(i).distance);
                    //System.out.println(newFringe.distance);
                    //System.out.println(i);
                    solution.fringe.add(i, newFringe);
                    //System.out.println("added to fringe");
                    return;
                }
                i++;
            }

        }
        solution.fringe.add(solution.fringe.size(),newFringe);
    }

}

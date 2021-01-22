import org.apache.commons.math3.util.Pair;

import java.util.*;

public class MinimumSpanningTree{
    public static void main(String[] args) {
        Prim prim = new Prim(createGraph());
        System.out.println();
        prim.run();
        prim.resetPrintHistory();
        System.out.println("Edge      weight");
        System.out.println(prim.printMinimumSpanningTree());
        System.out.println("Cost of Minimum Spanning Tree  = "+Vertex.MSTCost);
        System.out.println("-------------------------------------------------");
    }

    public static List<Vertex> createGraph() {
        Scanner scanner = new Scanner(System.in);
        List<Vertex> graph = new ArrayList<>();
        while (true){
            String line = scanner.nextLine();
            if (line.equals("-1")){
                break;
            }
            if (line.isEmpty()){
                continue;
            }
            String []inputs = line.split(" ");
            Vertex src = new Vertex(inputs[0]);
            Vertex destination = new Vertex(inputs[1]);
            Edge edge = new Edge(Integer.parseInt(inputs[2]));
            if (!graph.contains(src)){
              graph.add(src);
            }
            if (!graph.contains(destination)){
                graph.add(destination);
            }
            int v1 = graph.indexOf(src);
            int v2 = graph.indexOf(destination);
            Vertex s = graph.get(v1);
            Vertex d = graph.get(v2);
            s.addEdge(d,edge);
            d.addEdge(s,edge);
        }
        return graph;
    }

    public static class Edge {
        int weight;
        boolean isIncluded = false;
        boolean isPrinted = false;

        public Edge(int weight) {
            this.weight = weight;
        }
    }
    public static class Vertex {
        static int  MSTCost = 0;
        String label = null;
        Map<Vertex, Edge> edges = new HashMap<>();
        boolean isVisited = false;

        public Vertex(String label){
            this.label = label;
        }

        public void addEdge(Vertex vertex, Edge edge){
            if (this.edges.containsKey(vertex)){
                if (edge.weight < this.edges.get(vertex).weight){
                    this.edges.replace(vertex, edge);
                }
            } else {
                this.edges.put(vertex, edge);
            }
        }

        public Pair<Vertex, Edge> nextMinimum(){
            Edge nextMinimum = new Edge(Integer.MAX_VALUE);
            Vertex nextVertex = this;
            Iterator<Map.Entry<Vertex,Edge>> it = edges.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Vertex,Edge> pair = it.next();
                if (!pair.getKey().isVisited){
                    if (!pair.getValue().isIncluded) {
                        if (pair.getValue().weight < nextMinimum.weight) {
                            nextMinimum = pair.getValue();
                            nextVertex = pair.getKey();
                        }
                    }
                }
            }
            return new Pair<>(nextVertex, nextMinimum);
        }

        public String includedToString(){
            StringBuilder sb = new StringBuilder();
            if (isVisited) {
                Iterator<Map.Entry<Vertex,Edge>> it = edges.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<Vertex,Edge> pair = it.next();
                    if (pair.getValue().isIncluded) {
                        if (!pair.getValue().isPrinted) {
                            sb.append(label);
                            sb.append(" --- ");
                            sb.append(pair.getKey().label);
                            sb.append("     ");
                            sb.append(pair.getValue().weight);
                            sb.append("\n");
                            pair.getValue().isPrinted = true;
                            MSTCost+=pair.getValue().weight;
                        }
                    }
                }
            }
            return sb.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Vertex vertex = (Vertex) o;
            return Objects.equals(label, vertex.label);
        }

        @Override
        public int hashCode() {
            return Objects.hash(label);
        }
    }
    public static class Prim {

        private List<Vertex> graph;

        public Prim(List<Vertex> graph){
            this.graph = graph;
        }

        public void run(){
            if (graph.size() > 0){
                graph.get(0).isVisited = true;
            }
            while (isDisconnected()){
                Edge nextMinimum = new Edge(Integer.MAX_VALUE);
                Vertex nextVertex = graph.get(0);
                for (Vertex vertex : graph){
                    if (vertex.isVisited){
                        Pair<Vertex, Edge> candidate = vertex.nextMinimum();
                        if (candidate.getValue().weight < nextMinimum.weight){
                            nextMinimum = candidate.getValue();
                            nextVertex = candidate.getKey();
                        }
                    }
                }
                nextMinimum.isIncluded = true;
                nextVertex.isVisited = true;
            }
        }

        private boolean isDisconnected(){
            for (Vertex vertex : graph){
                if (!vertex.isVisited){
                    return true;
                }
            }
            return false;
        }

        public void resetPrintHistory(){
            for (Vertex vertex : graph){
                Iterator<Map.Entry<Vertex,Edge>> it = vertex.edges.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<Vertex,Edge> pair = it.next();
                    pair.getValue().isPrinted = false;
                }
            }
        }

        public String printMinimumSpanningTree(){
            StringBuilder sb = new StringBuilder();
            for (Vertex vertex : graph){
                sb.append(vertex.includedToString());
            }
            return sb.toString();
        }
    }

}

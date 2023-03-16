import java.util.*;

public class Map {
    int n;
    List<List<Integer>> minDistances;
    HashMap<String, Integer> nameToId = new HashMap<>();
    List<String> idToName = new ArrayList<>();
    List<List<String>> adjListCity;
    List<List<Integer>> adjListDist;

    static class Pair {
        int first;
        int second;

        Pair(int first, int second) {
            this.first = first;
            this.second = second;
        }
    }

    public Map(List<String> cityConnections){
        adjListCity = new ArrayList<>();
        adjListDist = new ArrayList<>();
        cityConnections.add("Storefront to 123 Main St = 2 miles");
        for(String edge : cityConnections){
            int toIndex = edge.indexOf(" to ");
            int equalsIndex = edge.indexOf(" = ");
            int lastSpace = edge.indexOf(" ", equalsIndex + 3);
            String address1 = edge.substring(0, toIndex);
            String address2 = edge.substring(toIndex + 4, equalsIndex);
            Integer distance = Integer.valueOf(edge.substring(equalsIndex + 3, lastSpace));
            if(!nameToId.containsKey(address1)){
                nameToId.put(address1, nameToId.size());
                idToName.add(address1);
                adjListCity.add(new ArrayList<>());
                adjListDist.add(new ArrayList<>());
            }
            if(!nameToId.containsKey(address2)){
                nameToId.put(address2, nameToId.size());
                idToName.add(address2);
                adjListCity.add(new ArrayList<>());
                adjListDist.add(new ArrayList<>());
            }
            adjListCity.get(nameToId.get(address1)).add(address2);
            adjListDist.get(nameToId.get(address1)).add(distance);
            adjListCity.get(nameToId.get(address2)).add(address1);
            adjListDist.get(nameToId.get(address2)).add(distance);
        }
        n = nameToId.size();
        minDistances = new ArrayList<>();
        for(int i = 0; i < n; i++){
            minDistances.add(new ArrayList<>());
            for(int j = 0; j < n; j++){
                minDistances.get(i).add(Integer.MAX_VALUE);
            }
            minDistances.get(i).set(i, 0);
        }

        // Dijkstra's n times
        for(int i = 0; i < n; i++){
            PriorityQueue<Pair> pq = new PriorityQueue<>(n, Comparator.comparingInt(x -> x.first));
            pq.add(new Pair(0, i));
            while(!pq.isEmpty()){
                Pair p = pq.poll();
                for(int j = 0; j < adjListCity.get(p.second).size(); j++){
                    int jNodeDist = minDistances.get(i).get(nameToId.get(adjListCity.get(p.second).get(j)));
                    int pNodeDist = minDistances.get(i).get(p.second);
                    int eDistance = adjListDist.get(p.second).get(j);
                    if(jNodeDist > pNodeDist + eDistance){
                        minDistances.get(i).set(nameToId.get(adjListCity.get(p.second).get(j)), pNodeDist + eDistance);
                        pq.add(new Pair(pNodeDist + eDistance, nameToId.get(adjListCity.get(p.second).get(j))));
                    }
                }
            }
        }
    }

    public int shortestDistance(String address1, String address2){
        int id1 = nameToId.get(address1);
        int id2 = nameToId.get(address2);
        return minDistances.get(id1).get(id2);
    }
}

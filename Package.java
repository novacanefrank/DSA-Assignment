import java.util.*;

public class Package {
    
    
    public static int minRoads(int[] packages, int[][] roads) {
        int n = packages.length;
        
        
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] r : roads) {
            graph.get(r[0]).add(r[1]);
            graph.get(r[1]).add(r[0]);
        }
        
       
        List<Integer> packageLoc = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (packages[i] == 1) {
                packageLoc.add(i);
            }
        }
        int M = packageLoc.size(); 
        if (M == 0) {
            return 0;
        }
        
     
        int[][] dist = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], -1);
          
            Queue<Integer> queue = new LinkedList<>();
            dist[i][i] = 0;
            queue.offer(i);
            while (!queue.isEmpty()) {
                int curr = queue.poll();
                for (int neigh : graph.get(curr)) {
                    if (dist[i][neigh] == -1) {
                        dist[i][neigh] = dist[i][curr] + 1;
                        queue.offer(neigh);
                    }
                }
            }
        }
        
 
        int[] coverage = new int[n];
        for (int u = 0; u < n; u++) {
            int mask = 0;
            for (int p = 0; p < M; p++) {
                int pkgNode = packageLoc.get(p);
               
                if (dist[u][pkgNode] != -1 && dist[u][pkgNode] <= 2) {
                    mask |= (1 << p);
                }
            }
            coverage[u] = mask;
        }
        
        
        int bestAnswer = Integer.MAX_VALUE;
        
        for (int start = 0; start < n; start++) {
            
            int[][] distState = new int[n][1 << M];
            for (int i = 0; i < n; i++) {
                Arrays.fill(distState[i], Integer.MAX_VALUE);
            }
            
         
            int initMask = coverage[start];
            distState[start][initMask] = 0;
            
            Queue<int[]> queue = new LinkedList<>();
            queue.offer(new int[]{start, initMask});
            
            while (!queue.isEmpty()) {
                int[] cur = queue.poll();
                int node = cur[0];
                int mask = cur[1];
                int roadsUsed = distState[node][mask];
                
                // Check if we have all packages
                if (mask == (1 << M) - 1) {
                    // Add cost to return to 'start'
                    if (dist[node][start] != -1) {
                        bestAnswer = Math.min(bestAnswer, roadsUsed + dist[node][start]);
                    }
                  
                }
                
             
                for (int neigh : graph.get(node)) {
                    int newMask = mask | coverage[neigh];
                    int newCost = roadsUsed + 1;  // traveling one road
                    if (newCost < distState[neigh][newMask]) {
                        distState[neigh][newMask] = newCost;
                        queue.offer(new int[]{neigh, newMask});
                    }
                }
            }
        }
        
        return (bestAnswer == Integer.MAX_VALUE) ? 0 : bestAnswer;
    }
    
    public static void main(String[] args) {
        // Test 1
        int[] packages1 = {1, 0, 0, 0, 0, 1};
        int[][] roads1 = {
            {0, 1}, {1, 2}, {2, 3}, {3, 4}, {4, 5}
        };
        System.out.println(minRoads(packages1, roads1)); // Expected 2
        
        // Test 2
        int[] packages2 = {0, 0, 0, 1, 1, 0, 0, 1};
        int[][] roads2 = {
            {0, 1}, {0, 2}, {1, 3}, {1, 4}, {2, 5}, {5, 6}, {5, 7}
        };
        System.out.println(minRoads(packages2, roads2)); // Expected 2
    }
}
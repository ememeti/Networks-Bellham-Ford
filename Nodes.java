/* Implements the necessary infomation for each node, including
    each source node's neighbors and cost to travel to those
    neighbors
   @author: Endrit Memeti
   @UTAID: 1001509938 
   @NetID: exm9938
   @date: 04-26-2020 */
import java.io.*;
import java.util.*;

public class Nodes {
    /* Will keep track of the node table for a specific node */
    private static int src_router = 0;

    /* Will hold the neighbors of the source router and the cost
       to get to that router */
    private static int[][] rtr_neighbors;

    public Nodes( int src, int[][] destination ) {
        this.src_router    = src;
        this.rtr_neighbors = destination;
    }

    /* Return the source router id for each member of the Nodes object array
       @params None.
       @return src_router: Returns the number of the specific source router
            in the routing_GUI.java file
    */
    public static int getSource() {
        return src_router;
    }

    /* Return the source router's neighbors for each member of the Nodes object array
       @params None.
       @return src_router: Returns the 2D array of the router information table
            in the routing_GUI.java file
    */
    public static int[][] getRoute() {
        return rtr_neighbors;
    }

    /* Updates the router table with any user update or when filling in the
            table with the shortest paths from each source to destination routers
       @params Nodes[] nodes: An array of Nodes objects that represent each router
                    from the input file
               int src: The source router whose cost to a destination will be 
                    updated
               int dest: The destination router who's cost from the source will
                    be updated
               int cost: Updates the cost from the source to the destination
                    router from user's input or the distance vector inputs
       @return src_router: Returns nothing but updates the table's values 
                with the update cost values
    */
    public static void updateRouterInfo( Nodes[] nodes, int src, int dest, int cost ) {
        /* Inputs the cost from going from the source to the neighbor
           to the [source, destination] index of the graph */
        rtr_neighbors[src][dest]  = cost;

        /* Inputs the cost from going from the neighbor to the source
           to the [destination, source=] index of the graph */
        rtr_neighbors[dest][src] = cost;
    }

    /* Implements the Bellham-Ford algorithm in order to generate the Distance
            Vector Table
       @params int[] rtr_dist: An array of integers that will constantly be updated
                    when the shortest distance was found
               int src: The source router whose cost to a destination will be 
                    updated
       @return int: The number of cycles it took to complete the Distance
                Vector Table
    */
    public static int bellhamFordAlgo( int src, int[] rtr_dist ) {
        /* Keeps track of the amount of cycles
           that it takes to complete the DV table */
        int cycles = 0;

        /* Goes through each route available and stores the shortest paths from
           each source and destination routers */
        for( int i = 0; i < 5; i++ ) {
            for( int j = 0; j < 6; j++ ) {
                for( int k = 0; k < 6; k++ ) {
                    /* Any value that are still 16 will be updated */
                    if( rtr_neighbors[j][k] != 16) {
                        /* Increase the number of cycles for each iteration */
                        cycles++;
                        int new_cost = rtr_dist[j] + rtr_neighbors[j][k];
                        /* Updates the cost of between one router to another with
                           the shortest path available */
                        if( rtr_dist[k] > new_cost ) {
                            rtr_dist[k] = new_cost;
                        }
                    }
                }
            }
        }

        /* Updates the distance vector table for a specific node with the shortest paths to
           other destination routers */
        for( int i = 0; i < 6; i++ ){
            rtr_neighbors[src][i] = rtr_dist[i];
        }

        return cycles;
    }

}
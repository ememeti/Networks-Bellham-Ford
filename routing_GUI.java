/* Implements and creates the GUI that is shown and generate the File Input
    and Distance Vectors from a user-designated file
   @author: Endrit Memeti
   @UTAID: 1001509938 
   @NetID: exm9938
   @date: 04-26-2020 */

/* These are all the necessary libraries that are used to make
   GUI and functions work */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import java.lang.*;

class routing_GUI {
    /* Makes the table outputs global as they
       will frequently change */
    public static JLabel input_row2;
    public static JLabel  algo_row2;

    public static JLabel input_row3;
    public static JLabel  algo_row3;

    public static JLabel input_row4;
    public static JLabel  algo_row4;

    public static JLabel input_row5;
    public static JLabel  algo_row5;

    public static JLabel input_row6;
    public static JLabel  algo_row6;

    public static JLabel input_row7;
    public static JLabel  algo_row7;

    public static JLabel input_info;
    public static JLabel  algo_info;

    /* Keeps track of the current router that we
       are updating in the Distance Vector
       Table */
    public static int src_idx = 0;

    /* Denotes whether or not the user wants
       to go through the file step-by-step(this
       case returns true) or all-at-once(this
       case returns false) */
    public static boolean pick;

    /* Keeps track of the amount of cycles
       that it takes to complete the DV table */
    public static int cycles = 0;

    /* Begins the timer for when we called the 
       bellhamFordAlgo() function in Nodes.java */
    public static long start;

    /* Ends the timer for when we return from the 
       bellhamFordAlgo() function in Nodes.java */
    public static long   end;

    /* We are only allowed to have 6 nodes */
    final public static int MAX_NODES = 6;

    /* A node is only supposed to have a 
       of maximum 4 links */ 
    final public static int MAX_LINKS = 4;

    /* Any cost to go from one router to another
       must not exceed 16, otherwise it will
       take an infinite amount of time to compute */
    final public static int INFINITY  = 16;

    /* Used to create a routing table from an input file chosen by the user
            through the GUI
       @params FileInputStream file: Opens a stream that takes input from the 
                file and allows to be read-in by the program
            Nodes[] nodes: An array of Nodes objects that represent each router
                from the input file.
       @return Returns nothing but fills in the information for each
            router's neighbors and cost to go to the neighbors
    */
    public static void createRoutingTable( FileInputStream file, Nodes[] nodes ) {
        /* Text is read from a character-input stream, in this case
           a txt file */
        try ( BufferedReader br = new BufferedReader( new InputStreamReader( file ) ) ) {
            String line;

            /* Stores each line of the file into the line object until
               there aren't any lines to read from the imported file */
            while( (line = br.readLine() ) != null ) {
                /* Tokenizes the line object by each space */
                StringTokenizer token = new StringTokenizer( line );

                /* Converts the source router to an Integer type in
                   order to use this value as an index for the Nodes
                   array that is used to draw the routing table */
                int src_rtr  = Integer.parseInt( token.nextToken() ) - 1;

                /* Converts the destination router to an Integer type 
                   in order to use this value as an index for the 
                   neighbors 2D array that is used to draw the 
                   denote all the neighbors of the source router */
                int dest_rtr = Integer.parseInt( token.nextToken() ) - 1;

                /* Converts the destination router to an Integer type 
                   in order to use this calue as an index for the 
                   neighbors 2D array that is used to denote the 
                   cost to go from the source to its neighbor */
                int src_dest_cost = Integer.parseInt( token.nextToken() );

                /* Takes the information from the input file and carries
                   it in a function that will be stored in the Nodes
                   object */
                nodes[src_rtr].updateRouterInfo( nodes,
                                                 src_rtr,
                                                 dest_rtr,
                                                 src_dest_cost );

            }
        } catch ( FileNotFoundException fx ){ /* Catches if the files was not found */
            /* Prints to the terminal that the file was not found */
            fx.printStackTrace();
        }
        catch ( IOException ix ) { /* Catches if an error was when reading the input */
            /* Prints to the terminal that there was an error when reading the input */
            ix.printStackTrace();
        }
    }

    /* Used to create a routing table from an input file chosen by the user
            through the GUI
       @params JFrame frame: Creates the window with all the components of the
                GUI using the built-in Java Swift library
            Nodes[] nodeTable: An array of Nodes objects that represent each router
                from the input file
       @return Returns nothing but draws and outputs the GUI to be
            visible and be interactive with the user
    */
    public static void drawGUI( JFrame frame, Nodes[] nodeTable ) {
        /* When the user presses the exit button on the top
           left, the GUI and program will terminate */
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        /* Sets the width to 800 pixels by a height of 400 pixels */
        frame.setSize(800, 400);

        /* Creating the MenuBar and adding components */
        JMenuBar menu     = new JMenuBar();
        JMenu m1          = new JMenu( "FILE" );
        menu.add( m1 );

        /* Used to import new files */
        JMenuItem m11     = new JMenuItem( "Import" );

        /* Adds the import option to the menu bar at the top */
        m1.add( m11 );

        m11.addActionListener(new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                /* This object is used to catch when a user clicks
                   an option from a menu bar object from a GUI
                   created with the Swift library */
                JFileChooser chooser = new JFileChooser();

                /* Returns a 0 if the user has successfully clicked
                   the Import button in the FILE menu bar */
                int selection = chooser.showOpenDialog( frame );

                /* If JFileChooser returns a 0, too, this means that
                   the menu bar button was successfully pushed and 
                   can be registered as input in the system */
                if (selection == JFileChooser.APPROVE_OPTION) {
                    /* Stores the name of the file that the user wants to import
                       from their file directory */
                    File file = chooser.getSelectedFile();

                    FileInputStream fis = null;

                    try {
                        /* Stores all the information from the file in a file
                           stream */
                        fis = new FileInputStream( file );

                        int[][] newNodes = new int[MAX_NODES][MAX_NODES];
                        Nodes[] freeNodes = new Nodes[MAX_NODES];

                        freeNodes = initializeNodes( nodeTable, newNodes );

                        /* Fills the table with costs between the source and
                           destination routers that is read in from the file */
                        createRoutingTable( fis, nodeTable );

                        /* Prints a new table with the file's router info
                           to the new GUI */
                        printTable( nodeTable );

                    } catch ( Exception f ) {
                        f.printStackTrace();
                    } finally {
                        try {
                            fis.close();
                        } catch ( Exception ex ) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });

        /* Displays the input file to the left and the current distance
           vector table to the right */
        JPanel table_disp = new JPanel();

        /* Creates the middle part of the GUI which will include
           the tables of the original graph, the graph that
           includes all the distance vector tables for each node,
           and the text entry and button to update the lines in the
           input graph data */
        table_disp.setLayout( new GridLayout( 13, 2, 10, 10 ) );

        /* The titles for the nodes and edges of the table from
           the input of text file and after the distance vector
           algorithm has been complete */
        JLabel input_ttl  = new JLabel("                    Original Table");
        JLabel vector_ttl = new JLabel("             Distance Vector Table");

        /* Lines 230-256 draw the tables in a visual format */
        JLabel input_file = new JLabel( "           1      2      3      4      5      6" );
        JLabel input_row1 = new JLabel( "--|-----------------------------" );

        JLabel algo_table = new JLabel( "           1      2      3      4      5      6" );
        JLabel algo_row1  = new JLabel( "--|-----------------------------" );

        input_row2 = new JLabel( " 1 |      0      0      0      0      0      0" );
        algo_row2  = new JLabel( " 1 |      0      0      0      0      0      0" );

        input_row3 = new JLabel( " 2 |      0      0      0      0      0      0" );
        algo_row3  = new JLabel( " 2 |      0      0      0      0      0      0" );

        input_row4 = new JLabel( " 3 |      0      0      0      0      0      0" );
        algo_row4  = new JLabel( " 3 |      0      0      0      0      0      0" );

        input_row5 = new JLabel( " 4 |      0      0      0      0      0      0" );
        algo_row5  = new JLabel( " 4 |      0      0      0      0      0      0" );

        input_row6 = new JLabel( " 5 |      0      0      0      0      0      0" );
        algo_row6  = new JLabel( " 5 |      0      0      0      0      0      0" );

        input_row7 = new JLabel( " 6 |      0      0      0      0      0      0" );
        algo_row7  = new JLabel( " 6 |      0      0      0      0      0      0" );

        input_info = new JLabel( "" );
        algo_info  = new JLabel( "" );

        /* Lines 258-287 add the above text into the GUI */
        table_disp.add( input_ttl );
        table_disp.add( vector_ttl );

        table_disp.add( input_file );
        table_disp.add( algo_table );

        table_disp.add( input_row1 );
        table_disp.add( algo_row1  );

        table_disp.add( input_row2 );
        table_disp.add( algo_row2  );

        table_disp.add( input_row3 );
        table_disp.add( algo_row3  );

        table_disp.add( input_row4 );
        table_disp.add( algo_row4  );

        table_disp.add( input_row5 );
        table_disp.add( algo_row5  );

        table_disp.add( input_row6 );
        table_disp.add( algo_row6  );

        table_disp.add( input_row7 );
        table_disp.add( algo_row7  );

        table_disp.add( input_info );
        table_disp.add( algo_info  );

        /* The user can change a specific line in the file by indicating
         the line of the file they want to change, the two neighboring
           routers, and the cost to travel from one router to the other */

        /* Allows for user input from the GUI */
        JLabel label   = new JLabel( "Change input (Router1 Router2 Cost):" );
        JTextField tf  = new JTextField( 7 );
        JButton update = new JButton("Update");

        /* Add components to the layout of the update section */
        table_disp.add( label );
        table_disp.add( new Label("") );
        table_disp.add( tf );
        table_disp.add( update );

        /* This will catch whenever the user clicks on the Update button 
           and will execute the given instructions to change the cost values
           in the distance vector table */
        update.addActionListener(new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                /* Pulls the text inputted by the user in the
                   the tf text entry box of the GUI */
                String userInput = tf.getText();

                /* Tokenizes the line object by each space */
                StringTokenizer token = new StringTokenizer( userInput );

                /* Converts the source router to an Integer type in
                    order to use this value as an index for the Nodes
                    array that is used to update the DV table */
                int src_rtr  = Integer.parseInt( token.nextToken() ) - 1;

                /* Converts the destination router to an Integer type 
                    in order to use this value as an index for the 
                    neighbors 2D array that is used to update all the 
                    neighbors from the router */
                int dest_rtr = Integer.parseInt( token.nextToken() ) - 1;

                /* Converts the destination router to an Integer type 
                    in order to use this value as an index for the 
                    neighbors 2D array that is used to update the 
                    cost to go from a designated source router to 
                    its neighbor routers */
                int src_dest_cost = Integer.parseInt( token.nextToken() );

                /* Takes the changed link that the user inputted and carries
                   it in a function that will be stored in the previously
                   made Nodes objects */
                nodeTable[src_rtr].updateRouterInfo( nodeTable,
                                                    src_rtr,
                                                    dest_rtr,
                                                    src_dest_cost );
            }
        });

        /* Part of the GUI where user can choose how they want to perform
           the Distance Vector algorithm */
        JPanel algo_panel = new JPanel();

        /* Layout will be 1 row and 2 columns */
        algo_panel.setLayout( new GridLayout( 1, 2, 10, 10 ) );

        /* Clicking this will run the Distance Vector Algorithm step-by-step
           So the user will get to see the math behind how each value was
           chosen for each node-to-node relationship */
        JButton stepbystep = new JButton( "Step-by-Step Run" );

        /* Clicking this will completely run the Distance Vector Algorithm
           without going through algorithm step-by-step */
        JButton complete   = new JButton( "Complete Run" );

        /* Add components to the layout of the algorithm panel */
        algo_panel.add( stepbystep );
        algo_panel.add( complete );

        /* This will catch whenever the user clicks on the Step-by-Step Run 
           button and will create the distance vector table every time
           the button is clicked */
        stepbystep.addActionListener(new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                /* Denotes that the user has clicked the Step-by-Step Run 
                   button by returning true */
                pick = true;

                /* When the source number goes beyond the maximum number of nodes
                   this means we have completed the distance vector table */
                if( src_idx == MAX_NODES ) {
                    /* Reset the state of the node */
                    src_idx = 0;

                    /* Denotes that the distance vector table is complete */
                    pick = false;

                    /* Print out the distance vector table */
                    printDVT( nodeTable, src_idx, pick );
                }
                else{
                    /* Stores all the source's neighbors and costs to those neighbors
                       in a separate array */
                    int[] router_distances = new int[6];
                    int[][] costs = nodeTable[src_idx].getRoute();

                    /* Updates the costs from the source to a destination router
                       with the shortest path available */
                    for( int i = 0; i < MAX_NODES; i++ ) {
                        router_distances[i] = costs[src_idx][i];
                    }

                    /* Counts the number of times the bellhamFordAlgo loops
                       through the function until the distance vector table is
                       complete */
                    cycles += nodeTable[src_idx].bellhamFordAlgo( src_idx, router_distances );

                    /* Print out the distance vector table */
                    printDVT( nodeTable, src_idx, pick );

                    /* Moves the state of the nodes to the next node */
                    src_idx++;
                }
            }
        });

        /* This will catch whenever the user clicks on the Complete Run 
           button and will create the distance vector table after
           the button is clicked once */
        complete.addActionListener(new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                /* Begins the timer for how long it takes to
                   create the distance vector table after reading
                   in the file input */
                start = System.nanoTime();

                pick = false;

                /* Stores all the source's neighbors and costs to those neighbors
                    in a separate array */
                int[] router_distances = new int[6];
                int[][] costs;

                /* Completes the distance vector table all at once */
                for( int i = 0; i < MAX_NODES; i++ ) {
                    if( src_idx == MAX_NODES ) { 
                        src_idx = 0;

                        break; 
                    }
                    else {
                        /* Stores all the source's neighbors and costs to those neighbors
                           in a separate array */
                        costs = nodeTable[src_idx].getRoute();

                        /* Updates the costs from the source to a destination router
                           with the shortest path available */
                        for( int j = 0; j < MAX_NODES; j++ ) {
                            router_distances[j] = costs[src_idx][j];
                        }

                        /* Counts the number of times the bellhamFordAlgo loops
                           through the function until the distance vector table is
                           complete */
                        cycles += nodeTable[src_idx].bellhamFordAlgo( src_idx, router_distances );

                        /* Moves the state of the nodes to the next node */
                        src_idx++;
                    }
                }
                /* Ends the timer for how long it takes to
                   create the distance vector table after reading
                   in the file input */
                end = System.nanoTime();

                /* Print out the distance vector table */
                printDVT( nodeTable, src_idx, pick );
            }
        });

        /* Add the frames to the layout of GUI */
        frame.getContentPane().add( BorderLayout.CENTER, table_disp );
        frame.getContentPane().add( BorderLayout.SOUTH, algo_panel );
        frame.getContentPane().add( BorderLayout.NORTH, menu );

        /* GUI will now be visible to users */
        frame.setVisible( true );
    }

    /* All the Nodes objects for each source router will be initialized with zeroes and 16s
            to be filled in later.
       @params Nodes[] nodeTable: An array of Nodes objects that represent each router
                from the input file
               int[][] completeTable: A 2D array that has the uses the source router as
                the first index and the destination router as the second index and located
                at those coordinates is the cost to go from the source to the destination
                router
       @return Nodes[]: All the nodes with the information about all the neighbors
            to the source router and the cost to get to the neighbor routers
    */
    public static Nodes[] initializeNodes( Nodes[] allNodes, int[][] completeTable ) {
        /* We start at 0 but this represents the first router */
        for( int i = 0; i < MAX_NODES; i++ ) {
            for( int j = 0; j < MAX_NODES; j++ ) {
                /* When the source router is equal to the destination
                   router, we set this value to zero to denote
                   that the cost from going to the source to the 
                   destination costs nothing */
                if( i == j ) {
                    completeTable[i][j] = 0;
                }
                /* Otherwise, set all the nodes to infinity
                   This will change once the distance vector
                   table is computed */
                else {
                    completeTable[i][j] = 16;
                }
            }
            /* The specific source router is now instantianted into
               a Nodes object for later use */
            allNodes[i] = new Nodes( i, completeTable );
        }

        return allNodes;
    }

    /* Prints the completed Distance Vector table
       @params Nodes[] nodes: An array of Nodes objects that represent each router
                from the input file
               int node: The source node in order to print the specific node
                information
               boolean choice: Used to choose how the distance vector table is printed
                depending on the user's choice
       @return Nothing but prints out the completed Distance Vector Table
    */
    public static void printDVT( Nodes[] nodes, int node, boolean choice ) {
        /* Stores all the source's neighbors and costs to those neighbors
            in a separate array */
        int[][] neighborNodes = new int[6][6];

        /* Will store the lines that will be inputted into the table */
        String routerLine;

        /* Used to print out the actual router */
        int actualRouter;

        /* Prints this when the user clicks on the Step-by-Step Run button */
        if( choice ) {
            /* Since our routers start at zero, we add one to denote the 
               actual source node value */
            actualRouter = node + 1;

            /* Stores all the source's neighbors and costs to those neighbors
                in a separate array */
            neighborNodes = nodes[node].getRoute();

            routerLine = String.format( " %d |   %3d  %5d  %5d  %5d  %5d  %5d", actualRouter,
                                                                                neighborNodes[node][0],
                                                                                neighborNodes[node][1],
                                                                                neighborNodes[node][2],
                                                                                neighborNodes[node][3],
                                                                                neighborNodes[node][4],
                                                                                neighborNodes[node][5] );
            
            /* Updates the Distance Vector Table line-by-line */
            switch( node ) {
                case 0:
                    algo_row2.setText( routerLine );
                    break;
                case 1:
                    algo_row3.setText( routerLine );
                    break;
                case 2:
                    algo_row4.setText( routerLine );
                    break;
                case 3:
                    algo_row5.setText( routerLine );
                    break;
                case 4:
                    algo_row6.setText( routerLine );
                    break;
                case 5:
                    algo_row7.setText( routerLine );
                    break;
                default:
                    System.out.println( "There was an issue when going through the table." );
                    System.exit( 0 );
            }

            /* Tells the user what node was updated */
            String user_info = String.format( "           Routes to %d is complete.", actualRouter );
            algo_info.setText( user_info );
        }
        else{
            /* Completes the distance vector table all at once */
            for( int i = 0; i < MAX_NODES; i++ ) {
                /* Since our routers start at zero, we add one to denote the 
                    actual source node value */
                actualRouter = i + 1;

                /* Stores all the source's neighbors and costs to those neighbors
                    in a separate array */
                neighborNodes = nodes[i].getRoute();
                
                routerLine = String.format( " %d |   %3d  %5d  %5d  %5d  %5d  %5d", actualRouter,
                                                                            neighborNodes[i][0],
                                                                            neighborNodes[i][1],
                                                                            neighborNodes[i][2],
                                                                            neighborNodes[i][3],
                                                                            neighborNodes[i][4],
                                                                            neighborNodes[i][5] );

                /* Updates the Distance Vector Table line-by-line */
                switch( i ) {
                    case 0:
                        algo_row2.setText( routerLine );
                        break;
                    case 1:
                        algo_row3.setText( routerLine );
                        break;
                    case 2:
                        algo_row4.setText( routerLine );
                        break;
                    case 3:
                        algo_row5.setText( routerLine );
                        break;
                    case 4:
                        algo_row6.setText( routerLine );
                        break;
                    case 5:
                        algo_row7.setText( routerLine );
                        break;
                    default:
                        System.out.println( "There was an issue when going through the table." );
                        System.exit( 0 );
                }
            }
            /* Stores the total time it took to compute the distance vector table */
            long totalTime = end - start;

            /* Tells the user what node was updated */
            String user_info = String.format("      Took %d cycles in "+ totalTime +" ns to reach a stable state.", cycles );
            algo_info.setText( user_info );

            /* Reset the cycle counter once the table was complete */
            cycles = 0;
        }    
    }

    /* Prints the completed File Input table
       @params Nodes[] nodes: An array of Nodes objects that represent each router
                from the input file
       @return Nothing but prints out the completed Distance Vector Table
    */
    public static void printTable( Nodes[] nodes ) {
        /* Stores all the source's neighbors and costs to those neighbors
            in a separate array */
        int[][] neighborNodes = new int[6][6];

        /* Will store the lines that will be inputted into the table */
        String routerLine;

        /* Used to print out the actual router */
        int actualRouter;

        /* Completes the file input table all at once */
        for( int i = 0; i < MAX_NODES; i++ ){
            /* Since our routers start at zero, we add one to denote the 
                    actual source node value */
            actualRouter = i + 1;

            /* Stores all the source's neighbors and costs to those neighbors
                in a separate array */
            neighborNodes = nodes[i].getRoute();

            routerLine = String.format( " %d |   %3d  %5d  %5d  %5d  %5d  %5d", actualRouter,
                                                                        neighborNodes[i][0],
                                                                        neighborNodes[i][1],
                                                                        neighborNodes[i][2],
                                                                        neighborNodes[i][3],
                                                                        neighborNodes[i][4],
                                                                        neighborNodes[i][5] );

            /* Updates the Distance Vector Table line-by-line */
            switch( i ) {
                case 0:
                    input_row2.setText( routerLine );
                    break;
                case 1:
                    input_row3.setText( routerLine );
                    break;
                case 2:
                    input_row4.setText( routerLine );
                    break;
                case 3:
                    input_row5.setText( routerLine );
                    break;
                case 4:
                    input_row6.setText( routerLine );
                    break;
                case 5:
                    input_row7.setText( routerLine );
                    break;
                default:
                    System.out.println( "There was an issue when going through the table." );
                    System.exit( 0 );
            }

            /* If the function is successful, a message will be outputted to
               the GUI that informs the user of its success */
            input_info.setText( "           File successfully imported." );
        }
    }

    public static void main(String args[]) {
        /* Initializes the GUI that is shown on screen */
        JFrame proj_frame  = new JFrame( "Distance Vector Algorithm" );

        /* Individual node table for 6 different nodes */
        Nodes[] totalNodes = new Nodes[MAX_NODES];

        /* Create a 2D array that will store all the 
           neighbors to each node and the cost to go
           to each destination */
        int[][] nodeNeighbors = new int[MAX_NODES][MAX_NODES];

        /* Initialize the table with all zeroes */
        totalNodes = initializeNodes( totalNodes, nodeNeighbors );

        /* Initiates the creation of the GUI */
        drawGUI( proj_frame, totalNodes );
    }
}
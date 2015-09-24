package ravensproject;

// Uncomment these lines to access image processing.
//import java.awt.Image;
//import java.io.File;
//import javax.imageio.ImageIO;

import java.util.*;

/**
 * Your Agent for solving Raven's Progressive Matrices. You MUST modify this
 * file.
 * 
 * You may also create and submit new files in addition to modifying this file.
 * 
 * Make sure your file retains methods with the signatures:
 * public Agent()
 * public char Solve(RavensProblem problem)
 * 
 * These methods will be necessary for the project's main method to run.
 * 
 */
public class Agent {
    /**
     * The default constructor for your Agent. Make sure to execute any
     * processing necessary before your Agent starts solving problems here.
     * 
     * Do not add any variables to this signature; they will not be used by
     * main().
     * 
     */
    public Agent() {



    }
    /**
     * The primary method for solving incoming Raven's Progressive Matrices.
     * For each problem, your Agent's Solve() method will be called. At the
     * conclusion of Solve(), your Agent should return a String representing its
     * answer to the question: "1", "2", "3", "4", "5", or "6". These Strings
     * are also the Names of the individual RavensFigures, obtained through
     * RavensFigure.getName().
     * 
     * In addition to returning your answer at the end of the method, your Agent
     * may also call problem.checkAnswer(String givenAnswer). The parameter
     * passed to checkAnswer should be your Agent's current guess for the
     * problem; checkAnswer will return the correct answer to the problem. This
     * allows your Agent to check its answer. Note, however, that after your
     * agent has called checkAnswer, it will *not* be able to change its answer.
     * checkAnswer is used to allow your Agent to learn from its incorrect
     * answers; however, your Agent cannot change the answer to a question it
     * has already answered.
     * 
     * If your Agent calls checkAnswer during execution of Solve, the answer it
     * returns will be ignored; otherwise, the answer returned at the end of
     * Solve will be taken as your Agent's answer to this problem.
     * 
     * @param problem the RavensProblem your agent should solve
     * @return your Agent's answer to this problem
     */
    public int Solve(RavensProblem problem) {

        int cols = Integer.parseInt(problem.getProblemType().substring(0, 1));
        int rows = cols;

        System.out.println("PROBLEM "+problem.getName()+":");

        Map<String, RavensFigure> figureMap = problem.getFigures();
        Map<String, SemanticNetwork> semanticMap = new HashMap<>();
        for(String fig : figureMap.keySet()) {
            semanticMap.put(fig, buildSemanticNetwork(figureMap.get(fig)));
        }
        System.out.println(semanticMap.toString());

        Map<String, List<String>> adjacencyMap = determineFigureAdjacency(figureMap);
        compareAdjacentFigures(figureMap, adjacencyMap, semanticMap);
        //compareSemanticNetworks();

        /*
        Step 1: loop through and create semantic network for each figure
        Step 2: compare semantic networks to find relations between figures
            --> create map of relations
        Step 3: create map of changes between figures based on metrics
        Step 4:
         */

        return -1;
    }

    public SemanticNetwork buildSemanticNetwork(RavensFigure figure) {

        SemanticNetwork semanticNetwork = new SemanticNetwork();
        Map<String, RavensObject> objectMap = figure.getObjects();
        for(String obj : objectMap.keySet()) {
            semanticNetwork.addObject(objectMap.get(obj));
        }

        return semanticNetwork;
    }

    public Map<String, List<String>> determineFigureAdjacency(Map<String, RavensFigure> figureMap) {
        Map<String, List<String>> adjacencyMap = new HashMap<>();

        // A list to store figures related to problem and not solution (i.e. A, B, C,...)
        // Only used as a "look-up" reference by name... doesn't actually store RavensFigures
        List<String> figureList = new ArrayList<>();
        for(String fig : figureMap.keySet()) {
            if(fig.matches("[A-Z]")) {
                figureList.add(fig);
            }
        }

        Collections.sort(figureList);

        // Form adjacency between figures to add to map
        // This should work for all 2x2 and 3x3
        int size = (int) Math.sqrt(figureList.size() + 1);
        for(int i = 0; i < figureList.size() - size; i++) {
            List<String> tempList = new ArrayList<>();
            if(i % size != 0 || i < size - 1) {
                tempList.add(figureList.get(i + 1));
            }
            tempList.add(figureList.get(i + size));
            adjacencyMap.put(figureList.get(i), tempList);
        }

        return adjacencyMap;
    }

    /**
     * This method is used for determining the relationship between objects in the problem
     * figures, not analyze the solution figures.
     *
     * @param figureMap
     * @param adjacencyMap
     * @param semanticMap
     * @return
     */
    public boolean compareAdjacentFigures(
            Map<String, RavensFigure> figureMap,
            Map<String, List<String>> adjacencyMap,
            Map<String, SemanticNetwork> semanticMap) {

        for(String fig : adjacencyMap.keySet()) {
            for(String comp : adjacencyMap.get(fig)) {
                //Todo - compare objects in adjacent figures and compare semantic networks

                //semanticList.get(semanticList.);
                //System.out.println(fig+" : "+comp);
            }
        }

        return false;
    }



}

/*
Todo:
- maybe make a object class so attributes are easier to compare
- maybe make a figure class so semantic networks of figures are easier to compare
 */

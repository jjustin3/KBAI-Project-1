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

    private Generator generator;

    /**
     * The default constructor for your Agent. Make sure to execute any
     * processing necessary before your Agent starts solving problems here.
     * 
     * Do not add any variables to this signature; they will not be used by
     * main().
     * 
     */
    public Agent() {
        generator = new Generator();
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

        System.out.println("Analyzing: "+problem.getName());

        // Array for potential answers
        List<String> answers = new ArrayList<>();

        // Todo - don't hardcode this for 2x2... make dynamic
        // Retrieve figures from problem
        RavensFigure figA = problem.getFigures().get("A");
        RavensFigure figB = problem.getFigures().get("B");
        RavensFigure figC = problem.getFigures().get("C");
        RavensFigure fig1 = problem.getFigures().get("1");
        RavensFigure fig2 = problem.getFigures().get("2");
        RavensFigure fig3 = problem.getFigures().get("3");
        RavensFigure fig4 = problem.getFigures().get("4");
        RavensFigure fig5 = problem.getFigures().get("5");
        RavensFigure fig6 = problem.getFigures().get("6");

        // Determine relationships between objects in figures
        Map<String, List<String>>  figAtoFigB = formRelationships(figA, figB, null);
        Map<String, List<String>>  figCtoFig1 = formRelationships(figC, fig1, figAtoFigB);
        Map<String, List<String>>  figCtoFig2 = formRelationships(figC, fig2, figAtoFigB);
        Map<String, List<String>>  figCtoFig3 = formRelationships(figC, fig3, figAtoFigB);
        Map<String, List<String>>  figCtoFig4 = formRelationships(figC, fig4, figAtoFigB);
        Map<String, List<String>>  figCtoFig5 = formRelationships(figC, fig5, figAtoFigB);
        Map<String, List<String>>  figCtoFig6 = formRelationships(figC, fig6, figAtoFigB);

        // Store relationships between C and solutions to map
        Map<String, Map<String, List<String>>> step1sols = new HashMap<>();
        step1sols.put("1", figCtoFig1);
        step1sols.put("2", figCtoFig2);
        step1sols.put("3", figCtoFig3);
        step1sols.put("4", figCtoFig4);
        step1sols.put("5", figCtoFig5);
        step1sols.put("6", figCtoFig6);

        // Determine transformations between figures
        int figAtoFigBDiff = figB.getObjects().keySet().size() - figA.getObjects().keySet().size(); //Todo
        Map<String, Integer> step1Scores = new HashMap<>();
        List<String> sol1List = new ArrayList<>(step1sols.keySet());
        for (String sol : sol1List) {
            step1Scores.put(sol, 0);

            // Todo - this should go somewhere near the pair formation
            // i.e. B-12 --> I don't analyze elements of an array at indices
            // greater than the array being compared to (misses some pairings)
            int figCtoSolDiff = problem.getFigures().get(sol).getObjects().keySet().size()
                    - figC.getObjects().keySet().size(); //Todo

            // Todo - ensure this works for given scores!
            if (problem.getName().equals("Basic Problem B-12"))
//                System.out.println("AtoB: "+figAtoFigBDiff+" , Cto"+sol+": "+figCtoSolDiff);
            if (figAtoFigBDiff == figCtoSolDiff)
                step1Scores.put(sol, Math.abs(figCtoSolDiff) + 2);

            for (List<List<String>> pair : (List<List<List<String>>>)generator.formPairs(
                    new ArrayList<>(figAtoFigB.values()),
                    new ArrayList<>(step1sols.get(sol).values()))) {

                int tempScore = step1Scores.get(sol) + generator.intersection(pair.get(0), pair.get(1)).size();
                step1Scores.put(sol, tempScore);
            }
        }

        for (String sol : sol1List) {
//            if (problem.getName().equals("Basic Problem B-12"))
//                System.out.println("Sol"+sol+" points: "+step1Scores.get(sol));
            if (step1Scores.get(sol).equals(Collections.max(step1Scores.values())))
                answers.add(sol);
        }

        System.out.println("Answers at step 1: "+answers);

        //Todo
        if (answers.size() > 1) {
//            List<String> figBLocations = getLocations(figB);
//            List<String> fig1Locations = getLocations(fig1);
//            List<String> fig2Locations = getLocations(fig2);
//            List<String> fig3Locations = getLocations(fig3);
//            List<String> fig4Locations = getLocations(fig4);
//            List<String> fig5Locations = getLocations(fig5);
//            List<String> fig6Locations = getLocations(fig6);
//
//            Map<String, List<String>> step2sols = new HashMap<>();
//            step2sols.put("1", fig1Locations);
//            step2sols.put("2", fig2Locations);
//            step2sols.put("3", fig3Locations);
//            step2sols.put("4", fig4Locations);
//            step2sols.put("5", fig5Locations);
//            step2sols.put("6", fig6Locations);
//
//            List<String> sol2List = new ArrayList<>(step2sols.keySet());
//            for (String sol : sol2List) {
//              List<String> ansFigLocations = getLocations(problem.getFigures().get(ans));
//                int tempScore = generator.intersection(figBLocations, ansFigLocations).size();
//                step2Scores.put(ans, tempScore);
//            }
//            for (String sol : answers)
//                if (step2Scores.get(sol) < Collections.max(step2Scores.values()))
//                    answers.remove(sol);
            

            List<String> figBLocations = getLocations(figB);
            Map<String, Integer> step2Scores = new HashMap<>();
            for (String ans : answers) {
                List<String> ansFigLocations = getLocations(problem.getFigures().get(ans));
                int tempScore = generator.intersection(figBLocations, ansFigLocations).size();
                step2Scores.put(ans, tempScore);
            }

            for (String sol : answers)
                if (step2Scores.get(sol) < Collections.max(step2Scores.values()))
                    answers.remove(sol);

        }

        System.out.println("Answers at step 2: "+answers);



        return -1;
    }

    public Map<String, List<String>> formRelationships(RavensFigure figure1,
                                                       RavensFigure figure2,
                                                       Map<String, List<String>> comparison) {

        // Retrieve figure1's objects and figure2's objects for comparison
        HashMap<String, RavensObject> figure1Objects = figure1.getObjects();
        HashMap<String, RavensObject> figure2Objects = figure2.getObjects();

        // Compare number of objects in each figure
        List<String> figure1Names = new ArrayList<>(figure1Objects.keySet());
        List<String> figure2Names = new ArrayList<>(figure2Objects.keySet());
        while (figure1Names.size() != figure2Names.size()) {
            if (figure1Names.size() > figure2Names.size())
                figure2Names.add(null);
            else if (figure1Names.size() < figure2Names.size()) {
                figure1Names.add(null);
            }
        }

        // Get all permutations of figure2 for comparison to figure1
        List<List<String>> figure2Permutations = generator.generatePermutations(figure2Names);

        int bestScore = 0;
        Map<String, List<String>> bestRelationships = new HashMap<>();
        for (List<String> permutation : figure2Permutations) {
            int score = 0;

            Map<String, List<String>> relationships = new HashMap<>();
            for (List<String> pair : (List<List<String>>)generator.formPairs(figure1Names, permutation)) {
                RavensObject fig1Object = figure1Objects.get(pair.get(0));
                RavensObject fig2Object = figure2Objects.get(pair.get(1));
                List<String> fig1AttrList = new ArrayList<>();
                List<String> fig2AttrList = new ArrayList<>();

                if (fig1Object == null && fig2Object != null)
                    fig2AttrList.add("added");
                else if (fig1Object != null && fig2Object == null)
                    fig1AttrList.add("deleted");
                else if (fig1Object != null && fig2Object != null) {
                    HashMap<String, String> fig1Attributes = fig1Object.getAttributes();
                    HashMap<String, String> fig2Attributes = fig2Object.getAttributes();

                    if (compareAttributes(fig1Attributes, fig2Attributes, "shape")) {
                        score += 5;
                        fig2AttrList.add("sameShape");
                    } else if(fig1Attributes.get("shape") != null && fig2Attributes.get("shape") != null)
                        fig2AttrList.add("diffShape");

                    if (compareAttributes(fig1Attributes, fig2Attributes, "size")) {
                        score += 5;
                        fig2AttrList.add("sameSize");
                    } else if(fig1Attributes.get("size") != null && fig2Attributes.get("size") != null) {
                        score += 2;
                        fig2AttrList.add("diffSize");
                    }

                    if (compareAttributes(fig1Attributes, fig2Attributes, "fill")) {
                        score += 5;
                        fig2AttrList.add("sameFill");
                    } else if(fig1Attributes.get("fill") != null && fig2Attributes.get("fill") != null) {
                        score += 2;
                        fig2AttrList.add("diffFill");
                    }

                    if (compareAttributes(fig1Attributes, fig2Attributes, "alignment")) {
                        score += 5;
                        fig2AttrList.add("sameAlignment");
                    } else if(fig1Attributes.get("alignment") != null && fig2Attributes.get("alignment") != null) {
                        score += 2;
                        fig2AttrList.add("diffAlignment");
                    }

                    if (compareAttributes(fig1Attributes, fig2Attributes, "angle")) {
                        score += 4;
                        fig2AttrList.add("sameAngle");
                    } else if(fig1Attributes.get("angle") != null && fig2Attributes.get("angle") != null) {
                        score += 3;
                        int angleDiff = Math.abs(Integer.parseInt(fig2Attributes.get("angle"))
                                - Integer.parseInt(fig1Attributes.get("angle")));
                        fig2AttrList.add(Integer.toString(angleDiff));
                    }


                }

                if (fig1Object != null && !fig1AttrList.isEmpty())
                    relationships.put(fig1Object.getName(), fig1AttrList);
                if (fig2Object != null && !fig2AttrList.isEmpty())
                    relationships.put(fig2Object.getName(), fig2AttrList);
            }

            // Todo - check if this works (probably doesn't) --> make a comparison function (check if comparison is null)
            if (relationships == comparison)
                score += 100;
            if (score > bestScore) {
                bestRelationships = relationships;
                bestScore = score;

            }
            //compareRelationships(relationships, comparison);

        }

        return bestRelationships;
    }

    public List<String> getLocations(RavensFigure figure) {

        Map<String, RavensObject> figureObjects = figure.getObjects();

        // Get relative locations of each object in the figure
        List<String> locations = new ArrayList<>();
        for (String name : figureObjects.keySet()) {
            Map<String, String> figAttributes = figureObjects.get(name).getAttributes();

            if(figAttributes.get("overlaps") != null)
                locations.add("overlaps");
            if(figAttributes.get("inside") != null)
                locations.add("inside");
        }

        return locations;
    }

    // Todo - implement this comparison
    public boolean compareRelationships(Map<String, List<String>> relationship1,
                                        Map<String, List<String>> relationship2) {

        if(relationship1 != null && relationship2 != null) {
            List<List<String>> rel1Values = new ArrayList<>(relationship1.values());
            List<List<String>> rel2Values = new ArrayList<>(relationship2.values());
            List<List<List<String>>> permutations = generator.generatePermutations(rel2Values);
//            System.out.println(permutations);
            for (List<List<String>> permutation : permutations) {
                boolean same = true;
                for (int i = 0; i < rel1Values.size(); i++) {
                    if (!rel1Values.get(i).equals(permutation.get(i)))
                        same = false;
                    else
                        System.out.println(rel1Values + " ::: " + permutation);
                }
            }

        }
        return false;
    }



    /**
     * This method compares the attributes of each figure. The point is to pull this
     * logic out of the main algorithm because it is repeated so much.
     *
     * @param fig1Attributes
     * @param fig2Attributes
     * @param attribute
     * @return Whether or not the attributes are the same
     */
    public boolean compareAttributes (HashMap<String, String> fig1Attributes,
                                  HashMap<String, String> fig2Attributes,
                                  String attribute) {

        String fig1Attribute = fig1Attributes.get(attribute);
        String fig2Attribute = fig2Attributes.get(attribute);
        if(fig1Attribute != null && fig2Attribute != null)
            if (fig1Attribute.equals(fig2Attribute))
                return true;
        return false;
    }
}

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
    private Random random;

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
        random = new Random();
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
        int answer; // actual answer to return

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
        Map<String, List<String>>  figAtoFigB = formRelationships(figA, figB);
        Map<String, List<String>>  figCtoFig1 = formRelationships(figC, fig1);
        Map<String, List<String>>  figCtoFig2 = formRelationships(figC, fig2);
        Map<String, List<String>>  figCtoFig3 = formRelationships(figC, fig3);
        Map<String, List<String>>  figCtoFig4 = formRelationships(figC, fig4);
        Map<String, List<String>>  figCtoFig5 = formRelationships(figC, fig5);
        Map<String, List<String>>  figCtoFig6 = formRelationships(figC, fig6);

        // Store relationships between C and solutions to map
        Map<String, Map<String, List<String>>> step1sols = new HashMap<>();
        step1sols.put("1", figCtoFig1);
        step1sols.put("2", figCtoFig2);
        step1sols.put("3", figCtoFig3);
        step1sols.put("4", figCtoFig4);
        step1sols.put("5", figCtoFig5);
        step1sols.put("6", figCtoFig6);

        // Determine transformations between figures
        int figAtoFigBDiff = figB.getObjects().keySet().size() - figA.getObjects().keySet().size();
        Map<String, Integer> step1Scores = new HashMap<>();
        List<String> sol1List = new ArrayList<>(step1sols.keySet());
        for (String sol : sol1List) {
            step1Scores.put(sol, 0);

            int figCtoSolDiff = problem.getFigures().get(sol).getObjects().keySet().size()
                    - figC.getObjects().keySet().size();

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
            if (step1Scores.get(sol).equals(Collections.max(step1Scores.values())))
                answers.add(sol);
        }

        if (answers.size() > 1) {
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

        if (answers.size() > 1)
            return Integer.parseInt(answers.get(random.nextInt(answers.size() - 1)));
        else if (answers.size() < 1)
            return -1;
        return Integer.parseInt(answers.get(0));
    }

    // Todo - pass in baseline relation of A to B for comparison
    public Map<String, List<String>> formRelationships(RavensFigure figure1,
                                                       RavensFigure figure2) {

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
                    } else if (fig1Attributes.get("shape") != null && fig2Attributes.get("shape") != null)
                        fig2AttrList.add("diffShape");

                    if (compareAttributes(fig1Attributes, fig2Attributes, "size")) {
                        score += 5;
                        fig2AttrList.add("sameSize");
                    } else if (fig1Attributes.get("size") != null && fig2Attributes.get("size") != null) {
                        score += 2;
                        fig2AttrList.add("diffSize");
                    }

                    if (compareAttributes(fig1Attributes, fig2Attributes, "fill")) {
                        score += 5;
                        fig2AttrList.add("sameFill");
                    } else if (fig1Attributes.get("fill") != null && fig2Attributes.get("fill") != null) {
                        score += 2;
                        String fill = determineFill(
                                fig1Attributes.get("fill"), fig2Attributes.get("fill")
                        );
                        fig2AttrList.add(fill);
                    }

                    if (compareAttributes(fig1Attributes, fig2Attributes, "alignment")) {
                        score += 5;
                        fig2AttrList.add("sameAlignment");
                    } else if (fig1Attributes.get("alignment") != null && fig2Attributes.get("alignment") != null) {
                        score += 2;
                        String align = determineAlignment(
                                fig1Attributes.get("alignment"), fig2Attributes.get("alignment")
                        );
                        fig2AttrList.add(align);
                    }

                    if (compareAttributes(fig1Attributes, fig2Attributes, "angle")) {
                        score += 5;
                        fig2AttrList.add("sameAngle");
                    } else if (fig1Attributes.get("angle") != null && fig2Attributes.get("angle") != null) {
                        score += 2;
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

            // Todo - check to see if relationship == comparison passed in
            if (score > bestScore) {
                bestRelationships = relationships;
                bestScore = score;
            }

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
            if(figAttributes.get("above") != null)
                locations.add("above");
            if(figAttributes.get("left-of") != null)
                locations.add("left-of");
        }

        return locations;
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

    public String determineAlignment(String fig1Align, String fig2Align) {
        String[] fig1Attrs = fig1Align.split("-");
        String[] fig2Attrs = fig2Align.split("-");
        String vertChange = "";
        String horizChange = "";
        String change;

        if (fig1Attrs[0].equals("bottom") && fig2Attrs[0].equals("top"))
            vertChange = "up";
        else if (fig1Attrs[0].equals("top") && fig2Attrs[0].equals("bottom"))
            vertChange = "down";

        if (fig1Attrs[1].equals("left") && fig2Attrs[1].equals("right"))
            horizChange = "right";
        if (fig1Attrs[1].equals("right") && fig2Attrs[1].equals("left"))
            horizChange = "left";

        if (!vertChange.equals("") && !horizChange.equals(""))
            change = vertChange + "-" + horizChange;
        else
            change = vertChange + horizChange;

        return change;
    }

    public String determineFill(String fig1Fill, String fig2Fill) {
        String[] fig1Attrs = fig1Fill.split("-");
        String[] fig2Attrs = fig2Fill.split("-");
        int change = 0; //rotation change in degrees

        if (fig1Attrs[0].equals("bottom")) {
            switch (fig2Attrs[0]) {
                case "top":
                    change = 180;
                case "right":
                    change = 90;
                case "left":
                    change = 270;
            }
        } else if (fig1Attrs[0].equals("top")) {
            switch (fig2Attrs[0]) {
                case "bottom" : change = 180;
                case "right" : change = 270;
                case "left" : change = 90;
            }
        } else if (fig1Attrs[0].equals("left")) {
            switch (fig2Attrs[0]) {
                case "top" : change = 270;
                case "bottom" : change = 90;
                case "right" : change = 180;
            }
        } else if (fig1Attrs[0].equals("right")) {
            switch (fig2Attrs[0]) {
                case "top" : change = 90;
                case "bottom" : change = 270;
                case "left": change = 180;
            }
        }

        return Integer.toString(change);
    }
}

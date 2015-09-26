package ravensproject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by justinjackson on 9/26/15.
 */
public class Generator<E> {

    public List<List<E>> generatePermutations(List<E> original) {
        if (original.size() == 0) {
            List<List<E>> result = new ArrayList<>();
            result.add(new ArrayList<E>());
            return result;
        }
        E firstElement = original.remove(0);
        List<List<E>> permutationList = new ArrayList<>();
        List<List<E>> permutations = generatePermutations(original);
        for (List<E> smaller : permutations) {
            for (int index=0; index <= smaller.size(); index++) {
                List<E> temp = new ArrayList<>(smaller);
                temp.add(index, firstElement);
                permutationList.add(temp);
            }
        }
        return permutationList;
    }

    /**
     * This method forms pairs between elements of two arrays and returns a list of such
     * of said pairs.
     *
     * @param list1
     * @param list2
     * @return The list of pairs
     */
    public List<List<E>> formPairs(List<E> list1, List<E> list2) {
        // Todo - check to see if lists are same size. If not --> exception.
        List<List<E>> pairList = new ArrayList<>();

        for (int i = 0; i < list1.size(); i++) {
            List<E> pair = new ArrayList<>();
            pair.add(list1.get(i));
            pair.add(list2.get(i));
            pairList.add(pair);
            System.out.println(pairList);
        }

        return pairList;
    }
}

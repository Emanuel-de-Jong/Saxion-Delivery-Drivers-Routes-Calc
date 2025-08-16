package tree;

import models.tree.SimpleTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleTreeTests {

    private SimpleTree<String> root;


    /**
     * Initialize root and give it the following structure:
     *                R
     *          /     |     \
     *         A      B      C
     *        / \          / | \
     *       AA  AB       CA CB CC
     *      /
     *     AAA
     */
    @BeforeEach
    public void setup() {
        root = new SimpleTree<>("R");

        root.insert("A");
        root.getChild(0).insert("AA");
        root.getChild(0).getChild(0).insert("AAA");
        root.getChild(0).insert("AB");

        root.insert("B");

        root.insert("C");
        root.getChild(2).insert("CA");
        root.getChild(2).insert("CB");
        root.getChild(2).insert("CC");
    }


    @Test()
    @DisplayName("Can the tree be iterated by height?")
    public void iterByAscHeight() {
        ArrayList<String> out = new ArrayList<>();
        iterByAscHeightLoop(root, out);

        String actual = String.join("\n", out);
        String expected =
                "R\n" +
                "B\n" +
                "C\n" +
                "CA\n" +
                "CB\n" +
                "CC\n" +
                "A\n" +
                "AB\n" +
                "AA\n" +
                "AAA";

        assertEquals(actual, expected);
    }

    /**
     * Recursive code of the iterByAscHeight test.
     */
    private void iterByAscHeightLoop(SimpleTree<String> tree, ArrayList<String> out) {
        out.add(tree.getData());

        // Sort children by ascending height.
        ArrayList<SimpleTree> children = (ArrayList<SimpleTree>) tree.getChildren().clone();
        children.sort(Comparator.comparingInt(SimpleTree::getHeight));

        children.forEach(c -> iterByAscHeightLoop(c, out));
    }

}

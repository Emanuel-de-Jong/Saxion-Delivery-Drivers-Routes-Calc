package controllers;

import models.Location;
import models.Route;
import models.graph.GraphEdge;
import models.graph.GraphNode;
import models.tree.SimpleTree;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Visualize data with images.
 */
public class Visualizer {

    /**
     * The directory that images will be saved to.
     * Is the program's base folder by default.
     */
    private static final String DIR = System.getProperty("user.dir") + "\\";

    /**
     * Image that is drawn onto and later saved.
     */
    private final BufferedImage bufferedImage;
    /**
     * Interface to draw onto bufferedImage.
     */
    private final Graphics2D g2d;


    /**
     * @param width  The width of the image.
     * @param height The height of the image.
     */
    public Visualizer(int width, int height) {
        assert (width >= 1) : "Width shouldn't be less than 1";
        assert (height >= 1) : "Height shouldn't be less than 1";

        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        g2d = bufferedImage.createGraphics();
    }


    public Graphics2D getG2d() {
        return g2d;
    }


    /**
     * Saves the bufferedImage to a PNG with the given name in the DIR directory.
     */
    public void save(String name) {
        assert (name != null) : "Name shouldn't be null";

        assert (Files.exists(Paths.get(DIR))) : "Parent directory doesn't exist";
        File file = new File(DIR + name + ".png");

        try {
            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public static void drawEdges(ArrayList<GraphEdge> edges) {
        assert (edges != null) : "Edges shouldn't be null";

        Visualizer visualizer = new Visualizer(1000, 1000);
        Graphics2D g2d = visualizer.getG2d();

        int nodeSize = 10;
        g2d.setColor(Color.RED);

        for (GraphEdge edge : edges) {
            for (GraphNode node : edge.getNodes()) {
                g2d.fillOval(node.x - nodeSize / 2, node.y - nodeSize / 2, nodeSize, nodeSize);
            }
            g2d.drawLine(edge.node1.x, edge.node1.y, edge.node2.x, edge.node2.y);
        }

        g2d.dispose();
        visualizer.save("edges");
    }

    public static void drawSimpleTree(SimpleTree<GraphNode> tree) {
        assert (tree != null) : "Tree shouldn't be null";

        Visualizer visualizer = new Visualizer(1000, 1000);
        Graphics2D g2d = visualizer.getG2d();

        g2d.setColor(Color.BLUE);
        int nodeSize = 10;
        drawSimpleTreeLoop(tree, g2d, nodeSize);

        g2d.dispose();
        visualizer.save("tree");
    }

    /**
     * Recursive code of the drawSimpleTree function.
     *
     * @see #drawSimpleTree
     */
    private static void drawSimpleTreeLoop(SimpleTree<GraphNode> tree, Graphics2D g2d, int nodeSize) {
        assert (tree != null) : "Tree shouldn't be null";
        assert (g2d != null) : "G2d shouldn't be null";
        assert (nodeSize >= 1) : "NodeSize shouldn't be less than 1";

        GraphNode node = tree.getData();
        g2d.fillOval(node.x - nodeSize / 2, node.y - nodeSize / 2, nodeSize, nodeSize);

        for (SimpleTree<GraphNode> child : tree.getChildren()) {
            GraphNode childNode = child.getData();
            g2d.drawLine(node.x, node.y, childNode.x, childNode.y);

            drawSimpleTreeLoop(child, g2d, nodeSize);
        }
    }

    public static void drawRoute(Route route) {
        assert (route != null) : "Route shouldn't be null";

        Visualizer visualizer = new Visualizer(1000, 1000);
        Graphics2D g2d = visualizer.getG2d();

        int nodeSize = 10;
        g2d.setColor(Color.GREEN);

        Location last = null;
        for (Location location : route) {
            g2d.fillOval(location.x - nodeSize / 2, location.y - nodeSize / 2, nodeSize, nodeSize);
            if (last != null) {
                g2d.drawLine(location.x, location.y, last.x, last.y);
            }
            last = location;
        }

        g2d.dispose();
        visualizer.save("route");
    }

}

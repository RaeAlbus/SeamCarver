import javalib.worldimages.Posn;

import java.util.ArrayList;
import java.util.Arrays;

// Represents a utilities class

/**
 * filler documentation
 */
public class Utils {

    // All possible directions for a Pixel to have a neighbor in
    ArrayList<String> directions;

    // Initializes the directions ArrayList<String>
    public Utils() {
        String[] directionsStrings = new String[]{"t", "b", "l", "r"};
        directions = new ArrayList<>(Arrays.asList(directionsStrings));
    }

    // Connects the two passed in APixels as neighbors where the boolean
    // determines whether we're connecting left to right or top to bottom
    void createTwoWayLink(boolean isHorizontal, APixel p1, APixel p2) {
        if (isHorizontal) {
            p1.right = p2;
            p2.left = p1;
        } else {
            p1.bottom = p2;
            p2.top = p1;
        }
    }

    // Returns the SeamInfo with the lowest totalWeight out of the
    // ArrayList<SeamInfo> passed in
    /**
     * filler documentation
     */
    public SeamInfo minSeam(ArrayList<SeamInfo> seams) {
        SeamInfo minSeam = seams.get(0);
        // Iterates through each SeamInfo in the passed in ArrayList<SeamInfo>
        for (SeamInfo s : seams) {
            if (s.totalWeight < minSeam.totalWeight) {
                minSeam = s;
            }
        }
        return minSeam;
    }

    // Returns a String that represents the direction that alreadyRemoved is in
    // relation to toBeRemoved for vertical seams
    /**
     * filler documentation
     */
    public String getDirectionFromVert(APixel alreadyRemoved, APixel toBeRemoved) {
        if (alreadyRemoved.top.left == toBeRemoved) {
            return "br";
        } else if (alreadyRemoved.top.right == toBeRemoved) {
            return "bl";
        } else {
            return "b";
        }
    }

    // Returns a String that represents the direction that alreadyRemoved is in
    // relation to toBeRemoved for horizontal seams
    /**
     * filler documentation
     */
    public String getDirectionFromHor(APixel alreadyRemoved, APixel toBeRemoved) {
        if (toBeRemoved.top.right == alreadyRemoved) {
            return "tr";
        } else if (toBeRemoved.bottom.right == alreadyRemoved) {
            return "br";
        } else {
            return "r";
        }
    }

    // Returns a new position based off the position in the
    // direction of the given position
    /**
     * filler documentation
     */
    public Posn changePosn(Posn p, String dir) {
        Posn newPosn;
        if (dir.equals("t")) {
            newPosn = new Posn(p.x, p.y - 1);
        } else if (dir.equals("b")) {
            newPosn = new Posn(p.x, p.y + 1);
        } else if (dir.equals("l")) {
            newPosn = new Posn(p.x - 1, p.y);
        } else {
            newPosn = new Posn(p.x + 1, p.y);
        }
        return newPosn;
    }

    // Returns whether the neighbor of the position given in
    // the direction given is out of bounds of the image
    public boolean outOfBounds(Posn p, String dir, int width, int height) {
        Posn newPosn = new Utils().changePosn(p, dir);
        return ((newPosn.x < 0) || (newPosn.y < 0) || (newPosn.x >= width) || (newPosn.y >= height));
    }

}

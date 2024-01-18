import javalib.impworld.World;
import javalib.impworld.WorldScene;
import javalib.worldimages.ComputedPixelImage;
import javalib.worldimages.FromFileImage;
import javalib.worldimages.Posn;
import javalib.worldimages.TextImage;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;

// This class represents a SeamCarver object

/**
 * filler documentation
 */
public class SeamCarver extends World {

    // for the red highlighting
    int count;
    SeamInfo toRemove;

    // for pausing and changing directions
    boolean isPaused;
    boolean isHorizontal;
    // represents the direction of the highlighted in red seam before
    // we have removed it (-1 = horizontal, 1 = vertical)
    int removeDir;

    // for turning into grayscale
    boolean isGrayScale;

    int width;
    int height;
    FromFileImage image;

    // this represents the bottom left pixel in the original image
    APixel origin;

    ArrayList<ArrayList<APixel>> pixelArrayList = new ArrayList<>();

    // Default constructor to construct a SeamCarver
    SeamCarver(FromFileImage image) {
        this.count = 0;
        this.isPaused = false;
        this.isHorizontal = true;
        this.removeDir = 0;
        this.isGrayScale = false;
        this.image = image;
        this.width = (int) image.getWidth();
        this.height = (int) image.getHeight();
        this.convertTo2DArrayList();
        this.fixLinks();
    }

    // Constructor for testing - we do not convertTo2DArrayList or fixLinks yet
    SeamCarver(FromFileImage image, boolean forTesting) {
        this.count = 0;
        this.isPaused = false;
        this.isHorizontal = true;
        this.removeDir = 0;
        this.isGrayScale = false;
        this.image = image;
        this.width = (int) image.getWidth();
        this.height = (int) image.getHeight();
    }

    // Converts the pixels in this image into pixels in a 2D arraylist
    // in the same order they were in the image
    void convertTo2DArrayList() {
        // Iterates through the rows of the 2D ArrayList
        for (int i = 0; i < image.getHeight(); i += 1) {
            ArrayList<APixel> row = new ArrayList<>();
            // Iterates through the elements in each row
            for (int j = 0; j < image.getWidth(); j += 1) {
                row.add(new Pixel(image.getColorAt(j, i)));
            }
            this.pixelArrayList.add(row);
        }
    }

    // Assigns all the APixels their corresponding neighbors
    // and eliminates the need for a 2D array list
    void fixLinks() {

        this.origin = this.pixelArrayList.get(0).get(0);
        Utils utils = new Utils();

        // Iterates through the rows of the 2D ArrayList
        for (int y = 0; y < this.height; y += 1) {
            // Iterates through the elements in each row
            for (int x = 0; x < this.width; x += 1) {
                APixel current = pixelArrayList.get(y).get(x);
                for (String dir : utils.directions) {
                    if (utils.outOfBounds(new Posn(x, y), dir, this.width, this.height)) {
                        // this is an edge
                        if (dir.equals("t")) {
                            current.top = new Edge("b", current);
                        } else if (dir.equals("b")) {
                            current.bottom = new Edge("t", current);
                        } else if (dir.equals("l")) {
                            current.left = new Edge("r", current);
                        } else if (dir.equals("r")) {
                            current.right = new Edge("l", current);
                        }
                    } else {
                        // assign non-edge neighbors :3
                        Posn neighborPosn = utils.changePosn(new Posn(x, y), dir);
                        APixel neighbor = pixelArrayList.get(neighborPosn.y).get(neighborPosn.x);
                        if (dir.equals("t")) {
                            current.top = neighbor;
                        } else if (dir.equals("b")) {
                            current.bottom = neighbor;
                        } else if (dir.equals("l")) {
                            current.left = neighbor;
                        } else if (dir.equals("r")) {
                            current.right = neighbor;
                        }
                    }
                }
            }
        }
    }

    // Returns the smallest vertical seam in the image based off energies and
    // starts at the top of the image
    public SeamInfo vertSeamInitialize() {
        return this.findVertSeam(origin.getAllRight(new ArrayList<>()));
    }

    // Returns the smallest vertical seam in the image based off energies

    /**
     * filler documentation
     */
    public SeamInfo findVertSeam(ArrayList<SeamInfo> prevRow) {

        // prevRow should be the last row
        if (prevRow.get(0).pixel.bottom == prevRow.get(0).pixel) {
            return new Utils().minSeam(prevRow).cameFrom;
        }

        ArrayList<SeamInfo> input = new ArrayList<>();
        ArrayList<SeamInfo> newRow = new ArrayList<>();

        // Iterates through prevRow to access each SeamInfo for the rows of the image
        for (int i = 0; i < prevRow.size(); i += 1) {
            input.add(prevRow.get(i));
            if (i - 1 >= 0) {
                input.add(prevRow.get(i - 1));
            }
            if (i + 1 < prevRow.size()) {
                input.add(prevRow.get(i + 1));
            }
            SeamInfo smallestSeam = new Utils().minSeam(input);
            APixel newPixel = prevRow.get(i).pixel.bottom;
            SeamInfo newSeam = new SeamInfo(newPixel,
                    smallestSeam.totalWeight + newPixel.calculateEnergy(), smallestSeam);
            newRow.add(newSeam);
            // reset everything
            input.clear();
        }

        return this.findVertSeam(newRow);

    }

    // Removes the bottom of the smallest vertical seam from the image

    /**
     * filler documentation
     */
    public boolean removeVertSeamInitialize(SeamInfo remove) {

        // given remove, remove that seam from the graph

        // last pixel in the SeamInfo
        APixel toRemove = remove.pixel;
        APixel left = toRemove.left;
        APixel right = toRemove.right;

        new Utils().createTwoWayLink(true, left, right);
        return this.removeVertSeam(toRemove, remove.cameFrom);
    }

    // Removes the smallest vertical seam from the image

    /**
     * filler documentation
     */
    public boolean removeVertSeam(APixel justRemoved, SeamInfo remove) {

        if (remove.pixel.top == remove.pixel) {
            // we are at the edge
            this.origin = remove.pixel.bottom.getLeftmost();
            return true;
        }

        APixel current = remove.pixel;
        APixel left = current.left;
        APixel right = current.right;

        String dir = new Utils().getDirectionFromVert(justRemoved, current);
        new Utils().createTwoWayLink(true, left, right);

        if (dir.equals("bl")) {
            new Utils().createTwoWayLink(false, left, justRemoved.right);

        } else if (dir.equals("br")) {
            new Utils().createTwoWayLink(false, right, justRemoved.left);
        }

        if (remove.cameFrom == null) {
            // we are at the edge
            this.origin = remove.pixel.getLeftmost();
            return true;
        } else {
            return this.removeVertSeam(current, remove.cameFrom);
        }
    }

    // Returns the smallest horizontal seam in the image based off energies and
    // starts at the top of the image
    public SeamInfo horSeamInitialize() {
        return this.findHorSeam(origin.getAllBottom(new ArrayList<>()));
    }

    // Returns the smallest horizontal seam in the image based off energies

    /**
     * filler documentation
     */
    public SeamInfo findHorSeam(ArrayList<SeamInfo> prevRow) {

        // prevRow should be the last row
        if (prevRow.get(0).pixel.right == prevRow.get(0).pixel) {
            return new Utils().minSeam(prevRow).cameFrom;
        }

        ArrayList<SeamInfo> input = new ArrayList<>();
        ArrayList<SeamInfo> newRow = new ArrayList<>();

        // Iterates through prevRow to access each SeamInfo for the rows of the image
        for (int i = 0; i < prevRow.size(); i += 1) {
            input.add(prevRow.get(i));
            if (i - 1 >= 0) {
                input.add(prevRow.get(i - 1));
            }
            if (i + 1 < prevRow.size()) {
                input.add(prevRow.get(i + 1));
            }
            SeamInfo smallestSeam = new Utils().minSeam(input);
            APixel newPixel = prevRow.get(i).pixel.right;
            SeamInfo newSeam = new SeamInfo(newPixel,
                    smallestSeam.totalWeight + newPixel.calculateEnergy(), smallestSeam);
            newRow.add(newSeam);
            // reset everything
            input.clear();
        }

        return this.findHorSeam(newRow);

    }

    // Removes the bottom of the smallest horizontal seam from the image
    public boolean removeHorSeamInitialize(SeamInfo remove) {

        // given remove, remove that seam from the graph

        // last pixel in the SeamInfo
        APixel toRemove = remove.pixel;
        APixel top = toRemove.top;
        APixel bottom = toRemove.bottom;

        new Utils().createTwoWayLink(false, top, bottom);
        return this.removeHorSeam(toRemove, remove.cameFrom);
    }

    // Removes the smallest horizontal seam from the image

    /**
     * filler documentation
     */
    public boolean removeHorSeam(APixel justRemoved, SeamInfo remove) {

        if (remove.pixel.left == remove.pixel) {
            // we are at the edge
            this.origin = remove.pixel.right.getTopmost();
            return true;
        }

        String dir = new Utils().getDirectionFromHor(justRemoved, remove.pixel);

        new Utils().createTwoWayLink(false, remove.pixel.top, remove.pixel.bottom);

        if (dir.equals("tr")) {
            new Utils().createTwoWayLink(true, remove.pixel.top, justRemoved.bottom);
        } else if (dir.equals("br")) {
            new Utils().createTwoWayLink(true, remove.pixel.bottom, justRemoved.top);
        }

        if (remove.cameFrom == null) {
            // we are at the edge
            this.origin = remove.pixel.getTopmost();
            return true;
        } else {
            return this.removeHorSeam(remove.pixel, remove.cameFrom);
        }

    }

    // Converts our SeamCarver graph into a ComputedPixelImage

    /**
     * filler documentation
     */
    public ComputedPixelImage draw() {
        ComputedPixelImage image = new ComputedPixelImage(this.width, this.height);
        image = this.origin.modifyImage(image, 0, 0, this.isGrayScale, this.highestEnergy());
        return image;
    }

    // Returns the highest energy of a pixel in the entire image
    public double highestEnergy() {

        double highest = 0;

        ArrayDeque<APixel> unvisited = new ArrayDeque<>();
        HashSet<APixel> visited = new HashSet<>();

        unvisited.add(origin);

        while (unvisited.size() > 0) {

            APixel current = unvisited.poll();
            if (current.calculateEnergy() > highest) {
                highest = current.calculateEnergy();
            }
            visited.add(current);

            // add all then neighbors
            ArrayList<APixel> neighbors = new ArrayList<>();

            neighbors.add(current.top);
            neighbors.add(current.bottom);
            neighbors.add(current.left);
            neighbors.add(current.right);

            for (APixel node : neighbors) {
                if (!unvisited.contains(node) && !visited.contains(node)) {
                    unvisited.add(node);
                }
            }

        }
        return highest;
    }

    // Creates base scene

    /**
     * filler documentation
     */
    public WorldScene makeScene() {
        WorldScene scene = new WorldScene(1000, 1000);
        scene.placeImageXY(this.draw(), 500, 250);
        return scene;
    }

    // Highlights the smallest seam in red one even ticks
    // and removes said seam on odd ticks

    /**
     * filler documentation
     */
    public void onTick() {

        if (!isPaused) {
            // -1 means need to remove a last horizontal seam
            // 1 means need to remove a last vertical seam
            // 0 means that we are good to just keep going
            if (removeDir == -1) {
                this.removeHorSeamInitialize(this.toRemove);
                this.removeDir = 0;
            } else if (removeDir == 1) {
                this.removeVertSeamInitialize(this.toRemove);
                this.removeDir = 0;
            }

            if (isHorizontal) {
                // remove hor seam
                if (this.count % 2 == 0) { // if we're on an even tick
                    // highlight the seam
                    this.toRemove = this.horSeamInitialize();
                    this.toRemove.highlight();
                    this.removeDir = -1;
                } else {
                    // remove the seam
                    if (this.height > 1) { // if we're NOT on last seam
                        this.removeHorSeamInitialize(this.toRemove);
                        this.removeDir = 0;
                        this.height -= 1;
                    } else if (this.height == 1) { // we are on the last seam
                        this.endOfWorld("Finished! :3"); // end the world
                    }
                }
            } else {
                // remove vert seam
                if (this.count % 2 == 0) { // on even tick
                    // highlight seam
                    this.toRemove = this.vertSeamInitialize();
                    this.toRemove.highlight();
                    this.removeDir = 1;
                } else { // on odd tick
                    // remove the seam
                    if (this.width > 1) { // not on last seam
                        this.removeVertSeamInitialize(toRemove);
                        this.removeDir = 0;
                        this.width -= 1;
                    } else if (this.width == 1) { // on last seam
                        this.endOfWorld("Finished! :3");
                    }
                }
            }
        }
    }

    // if "h" or "v" is pressed, the Seams horizontally or vertically will
    // start to be removed, respectfully
    // if " " is pressed (the space bar), the user can unpause and pause the seamcarver
    public void onKeyEvent(String s) {

        if (s.equals("h")) {
            this.isHorizontal = true;
        } else if (s.equals("v")) {
            this.isHorizontal = false;
        } else if (s.equals(" ") && !this.isPaused) {
            this.isPaused = true;
        } else if (s.equals(" ")) {
            this.isPaused = false;
        } else if (s.equals("g") && this.isGrayScale) {
            this.isGrayScale = false;
        } else if (s.equals("g")) {
            this.isGrayScale = true;
        }

    }

    // Returns a WorldScene that represents the last screen you see after carving
    // away an entire image

    /**
     * filler documentation
     */
    public WorldScene lastScene(String msg) {
        WorldScene scene = new WorldScene(1000, 1000);
        scene.placeImageXY(new TextImage(msg, 50, Color.PINK), 500, 250);
        return scene;
    }

    // Ends the World
    public void endOfWorld(String s) {
        super.endOfWorld(s);
    }

}

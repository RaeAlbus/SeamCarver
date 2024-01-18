import javalib.worldimages.ComputedPixelImage;

import java.awt.*;
import java.util.ArrayList;

// This abstract class represents a Pixel in a SeamCarver image
abstract class APixel {

    // Represents the neighbors of this APixel
    APixel top;
    APixel bottom;
    APixel left;
    APixel right;

    // Represents the pixel's color and brightness
    Color color;
    double brightness;

    // Returns the energy of this APixel which is dependent
    // on the brightness of this APixel and its neighbors
    abstract double calculateEnergy();

    // Returns an ArrayList<SeamInfo> that contains new SeamInfos for this APixel and
    // all the APixel to its right or left in the graph
    abstract ArrayList<SeamInfo> getAllRight(ArrayList<SeamInfo> acc);

    // Returns the leftmost neighbor of this APixel
    abstract APixel getLeftmost();

    // Modifies the given ComputedPixelImage to reflect the same color/order as
    // this APixel and it's neighbors
    abstract ComputedPixelImage modifyImage(ComputedPixelImage image, int x, int y, boolean gray, double highestEnergy);

    // Modifies the given ComputedPixelImage to reflect the same color/order as
    // this APixel and it's neighbors in a row
    abstract ComputedPixelImage modifyRow(ComputedPixelImage image, int x, int y, boolean gray, double highestEnergy);

    // Returns the APixel that is at the top of the column that this APixel is in
    abstract APixel getTopmost();

    // Returns an ArrayList<SeamInfo> containing SeamInfos for each APixel in the
    // row that this APixel is currently in
    abstract ArrayList<SeamInfo> getAllBottom(ArrayList<SeamInfo> acc);

    // Turns this APixel into a grayscale version (white to black) of its energy based on the
    // highest energy of the image that it is
    abstract Color turnGray(double highestEnergy);
}

// Represents a pixel
class Pixel extends APixel {

    public Pixel(Color color) {
        this.color = color;
        this.brightness = (color.getBlue() + color.getRed() + color.getGreen()) / 3.0 / 255.0;
    }

    // Returns the energy of this pixel which is dependent
    // on the brightness of this pixel and its neighbors
    public double calculateEnergy() {
        double horizEnergy = (this.top.left.brightness + (2.0 * this.left.brightness)
                + this.bottom.left.brightness)
                - (this.top.right.brightness + (2.0 * this.right.brightness)
                + this.bottom.right.brightness);
        double vertEnergy = (this.top.left.brightness + (2.0 * this.top.brightness)
                + this.top.right.brightness)
                - (this.bottom.left.brightness + (2.0 * this.bottom.brightness)
                + this.bottom.right.brightness);

        return Math.sqrt(Math.pow(horizEnergy, 2.0) + Math.pow(vertEnergy, 2.0));
    }

    // Returns an ArrayList<SeamInfo> that contains new SeamInfos for this Pixel and
    // all the Pixel to its right or left in the graph
    public ArrayList<SeamInfo> getAllRight(ArrayList<SeamInfo> acc) {
        SeamInfo curSeam = new SeamInfo(this, this.calculateEnergy());
        acc.add(curSeam);
        return this.right.getAllRight(acc);
    }

    // Returns an ArrayList<SeamInfo> that contains new SeamInfos for this Pixel and
    // all the Pixel below it
    public ArrayList<SeamInfo> getAllBottom(ArrayList<SeamInfo> acc) {
        SeamInfo curSeam = new SeamInfo(this, this.calculateEnergy());
        acc.add(curSeam);
        return this.bottom.getAllBottom(acc);
    }

    // Turns this Pixel into a grayscale version (white to black) of its energy based on the
    // highest energy of the image that it is
    Color turnGray(double highestEnergy) {
        double newEnergy = this.calculateEnergy() / highestEnergy;
        return new Color(0.0F, 0.0F, 0.0F, (float) newEnergy);
    }

    // Returns the topmost neighbor of this Pixel
    public APixel getTopmost() {
        return this.top.getTopmost();
    }

    // Returns the leftmost neighbor of this Pixel
    public APixel getLeftmost() {
        return this.left.getLeftmost();
    }

    // Modifies the given ComputedPixelImage to reflect the same color/order as
    // this APixel and it's neighbors
    public ComputedPixelImage modifyImage(ComputedPixelImage image, int x, int y, boolean gray, double highestEnergy) {
        this.bottom.modifyImage(image, x, y + 1, gray, highestEnergy);
        this.modifyRow(image, x, y, gray, highestEnergy);
        return image;
    }

    // Modifies the given ComputedPixelImage to reflect the same color/order as
    // this APixel and it's neighbors in a row
    public ComputedPixelImage modifyRow(ComputedPixelImage image, int x, int y, boolean gray, double highestEnergy) {
        if (gray) {
            image.setPixel(x, y, this.turnGray(highestEnergy));
        } else {
            image.setPixel(x, y, this.color);
        }
        this.right.modifyRow(image, x + 1, y, gray, highestEnergy);
        return image;
    }

}

// Represents an edge of the picture
class Edge extends APixel {

    // here, the direction is where the connection should be
    public Edge(String dir, APixel connection) {

        this.top = this;
        this.bottom = this;
        this.left = this;
        this.right = this;

        if (dir.equals("t")) {
            this.top = connection;
        } else if (dir.equals("b")) {
            this.bottom = connection;
        } else if (dir.equals("r")) {
            this.right = connection;
        } else if (dir.equals("l")) {
            this.left = connection;
        }

        this.color = Color.BLACK;
        this.brightness = 0.0;
    }

    // Returns the energy of this Edge which is always 0.0
    public double calculateEnergy() {
        return 0.0;
    }

    // Returns an ArrayList<SeamInfo> that contains new SeamInfos for this Edge and
    // all the Edge to its right or left in the graph
    public ArrayList<SeamInfo> getAllRight(ArrayList<SeamInfo> acc) {
        return acc;
    }

    // Returns the leftmost neighbor of this Edge but since an Edge
    // on the left will get itself for its left, returns its right neighbor
    public APixel getLeftmost() {
        return this.right;
    }

    public ArrayList<SeamInfo> getAllBottom(ArrayList<SeamInfo> acc) {
        return acc;
    }

    // Modifies the given ComputedPixelImage to reflect the same color/order as
    // this APixel and it's neighbors but since we do not want an Edge to be seen
    // in the image, do nothing
    public ComputedPixelImage modifyImage(ComputedPixelImage image, int x, int y, boolean gray, double highestEnergy) {
        return image;
    }

    // Modifies the given ComputedPixelImage to reflect the same color/order as
    // this Edge and it's in a row but since we do not want an Edge to be seen
    // in the image, do nothing
    public ComputedPixelImage modifyRow(ComputedPixelImage image, int x, int y, boolean gray, double highestEnergy) {
        return image;
    }

    // Returns the topmost neighbor of this Edge
    public APixel getTopmost() {
        return this.bottom;
    }

    // Turns this Edge into a grayscale version (white to black) of its energy based on the
    // highest energy of the image that it is
    // since edges are always 0 energy, return white
    public Color turnGray(double highestEnergy) {
        return new Color(0, 0, 0, 0);
    }

}

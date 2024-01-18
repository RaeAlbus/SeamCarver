import java.awt.*;

// Represents a Seam and its Info for a SeamCarver
/**
 * filler documentation
 */
public class SeamInfo {

    // Current APixel we're at in the Seam
    APixel pixel;

    // All the energies of every APixel in this
    // SeamInfo summed up
    double totalWeight;

    // Path prior to getting to this pixel
    SeamInfo cameFrom;

    // Constructor for a new Seam that only has one pixel
    SeamInfo(APixel pixel, double totalWeight) {
        this.pixel = pixel;
        this.totalWeight = totalWeight;
        this.cameFrom = null;
    }

    // Constructor for a new Seam that has a path cameFrom
    SeamInfo(APixel pixel, double totalWeight, SeamInfo cameFrom) {
        this.pixel = pixel;
        this.totalWeight = totalWeight;
        this.cameFrom = cameFrom;
    }

    // Turns every pixel in this SeamInfo to red
    public void highlight() {
        this.pixel.color = Color.RED;
        if (this.cameFrom != null) {
            this.cameFrom.highlight();
        }
    }

}

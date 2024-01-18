import javalib.impworld.WorldScene;
import javalib.worldimages.ComputedPixelImage;
import javalib.worldimages.FromFileImage;
import javalib.worldimages.Posn;
import javalib.worldimages.TextImage;
import tester.Tester;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

// Represents our example SeamCarver objects and tests

/**
 * filler documentation
 */
public class ExamplesSeamCarver {

    // Base objects for tests (we have functions below that initializes them
    // based on whether we are removing a seam horizontally or vertically

    FromFileImage testImage;
    SeamCarver testSeamCarver;

    FromFileImage balloonImage = new FromFileImage("src/images/balloons.jpg");
    SeamCarver testSeamCarver2 = new SeamCarver(balloonImage);

    // Example ComputedPixelImage
    ComputedPixelImage reImageVert;

    // EXAMPLES FOR REMOVING VERTICAL

    // Example pixels
    APixel a;
    APixel b;
    APixel c;
    APixel d;
    APixel e;
    APixel f;

    // Example 2D ArrayList for the APixels in testSeamCarver
    ArrayList<ArrayList<APixel>> pixelsArrayVert;
    ArrayList<APixel> row1Vert;
    ArrayList<APixel> row2Vert;
    ArrayList<APixel> row3Vert;
    // Example pixels
    APixel r;
    APixel s;
    APixel t;
    APixel u;

    // EXAMPLES FOR REMOVING HORIZONTAL
    APixel v;
    APixel w;
    // Example 2D ArrayList for the APixels in testSeamCarver
    ArrayList<ArrayList<APixel>> pixelsArrayHor;
    ArrayList<APixel> row1Hor;
    ArrayList<APixel> row2Hor;

    // a b
    // c d
    // e f
    // Represents a small 3x2 pixel image which is associated with the pixels below
    // and represented like the commented rectangle above
    void resetVert() {
        testImage = new FromFileImage("src/images/3x2pixel copy.png");
        testSeamCarver = new SeamCarver(testImage, true);

    }

    // Initalizes testing values for the APixel and the 2D ArrayList
    void putInArrayVert() {
        pixelsArrayVert = new ArrayList<>();
        row1Vert = new ArrayList<>();
        row2Vert = new ArrayList<>();
        row3Vert = new ArrayList<>();

        a = new Pixel(new Color(237, 232, 29));
        b = new Pixel(new Color(219, 31, 38));
        c = new Pixel(new Color(5, 169, 103));
        d = new Pixel(new Color(214, 145, 48));
        e = new Pixel(new Color(240, 94, 154));
        f = new Pixel(new Color(211, 221, 241));

        row1Vert.add(a);
        row1Vert.add(b);
        row2Vert.add(c);
        row2Vert.add(d);
        row3Vert.add(e);
        row3Vert.add(f);
        pixelsArrayVert.add(row1Vert);
        pixelsArrayVert.add(row2Vert);
        pixelsArrayVert.add(row3Vert);

        reImageVert = new ComputedPixelImage(2, 3);
        reImageVert.setPixel(0, 0, a.color);
        reImageVert.setPixel(1, 0, b.color);
        reImageVert.setPixel(0, 1, c.color);
        reImageVert.setPixel(1, 1, d.color);
        reImageVert.setPixel(0, 2, e.color);
        reImageVert.setPixel(1, 2, f.color);

    }

    // Fixes the neighbors between the example APixels
    void linkUpVert() {
        a.top = new Edge("b", a);
        a.bottom = c;
        a.left = new Edge("r", a);
        a.right = b;

        b.top = new Edge("b", b);
        b.bottom = d;
        b.left = a;
        b.right = new Edge("l", b);

        c.top = a;
        c.bottom = e;
        c.left = new Edge("r", c);
        c.right = d;

        d.top = b;
        d.bottom = f;
        d.left = c;
        d.right = new Edge("l", d);

        e.top = c;
        e.bottom = new Edge("t", e);
        e.left = new Edge("r", e);
        e.right = f;

        f.top = d;
        f.bottom = new Edge("t", f);
        f.left = e;
        f.right = new Edge("l", f);
    }

    // Fixes the neighbors between the APixels above once the
    // smallest seam has been removed
    void linkUpRemoveVert() {
        b.top = new Edge("b", b);
        b.bottom = c;
        b.left = new Edge("r", b);
        b.right = new Edge("l", b);

        c.top = b;
        c.bottom = e;
        c.left = new Edge("r", c);
        c.right = new Edge("l", c);

        e.top = c;
        e.bottom = new Edge("t", e);
        e.left = new Edge("r", e);
        e.right = new Edge("l", e);
    }

    // s u w
    // r t v
    // Represents a small 2x3 pixel image which is associated with the pixels below
    // and represented like the commented rectangle above
    void resetHor() {
        testImage = new FromFileImage("src/images/3x2Hor.png");
        testSeamCarver = new SeamCarver(testImage, true);
    }

    // Initalizes testing values for the APixel and the 2D ArrayList
    void putInArrayHor() {

        pixelsArrayHor = new ArrayList<>();
        row1Hor = new ArrayList<>();
        row2Hor = new ArrayList<>();

        r = new Pixel(new Color(237, 232, 29));
        s = new Pixel(new Color(219, 31, 38));
        t = new Pixel(new Color(5, 169, 103));
        u = new Pixel(new Color(214, 145, 48));
        v = new Pixel(new Color(240, 94, 154));
        w = new Pixel(new Color(211, 221, 241));

        row1Hor.add(s);
        row1Hor.add(u);
        row1Hor.add(w);
        row2Hor.add(r);
        row2Hor.add(t);
        row2Hor.add(v);
        pixelsArrayHor.add(row1Hor);
        pixelsArrayHor.add(row2Hor);

    }

    // Fixes the neighbors between the example APixels
    void linkUpHor() {

        r.top = s;
        r.bottom = new Edge("t", r);
        r.left = new Edge("r", r);
        r.right = t;

        s.top = new Edge("b", s);
        s.bottom = r;
        s.left = new Edge("r", s);
        s.right = u;

        t.top = u;
        t.bottom = new Edge("t", t);
        t.left = r;
        t.right = v;

        u.top = new Edge("b", u);
        u.bottom = t;
        u.left = s;
        u.right = w;

        v.top = w;
        v.bottom = new Edge("t", v);
        v.left = t;
        v.right = new Edge("l", v);

        w.top = new Edge("b", w);
        w.bottom = v;
        w.left = u;
        w.right = new Edge("l", w);
    }

    // Fixes the neighbors between the APixels above once the
    // smallest seam has been removed
    void linkUpRemoveHor() {
        s.top = new Edge("b", s);
        s.bottom = new Edge("t", s);
        s.left = new Edge("r", s);
        s.right = t;

        t.top = new Edge("b", t);
        t.bottom = new Edge("t", t);
        t.left = s;
        t.right = v;

        v.top = new Edge("b", v);
        v.bottom = new Edge("t", v);
        v.left = t;
        v.right = new Edge("l", v);
    }

    // TESTS FOR THE SEAMCARVER CLASS

    // Tests the convertTo2DArrayList() method
    void testConvertTo2DArrayList(Tester t) {

        putInArrayVert();

        resetVert();
        testSeamCarver.convertTo2DArrayList();

        t.checkExpect(testSeamCarver.pixelArrayList, pixelsArrayVert);
    }

    // Tests the fixLinks() method
    void testFixLinks(Tester t) {
        putInArrayVert();
        linkUpVert();

        resetVert();
        testSeamCarver.convertTo2DArrayList();
        testSeamCarver.fixLinks();

        t.checkExpect(testSeamCarver.origin, a);
    }

    // Tests the findVertSeam() and vertSeamInitalize() method
    // vertSeamInitalize() calls findVertSeam()
    void testFindVertSeam(Tester t) {
        putInArrayVert();
        linkUpVert();

        resetVert();
        testSeamCarver.convertTo2DArrayList();
        testSeamCarver.fixLinks();

        // Represents the smallest seam in testSeamCarver
        SeamInfo cameFrom = new SeamInfo(a, a.calculateEnergy());
        SeamInfo cameFromA = new SeamInfo(d, a.calculateEnergy() + d.calculateEnergy(), cameFrom);
        SeamInfo cameFromD = new SeamInfo(f,
                a.calculateEnergy() + d.calculateEnergy() + f.calculateEnergy(), cameFromA);

        t.checkInexact(testSeamCarver.vertSeamInitialize(), cameFromD, 1000);
    }

    void testFindHorSeam(Tester t) {

        putInArrayHor();
        linkUpHor();

        resetHor();
        testSeamCarver.convertTo2DArrayList();
        testSeamCarver.fixLinks();

        SeamInfo cameFrom = new SeamInfo(r, r.calculateEnergy());
        SeamInfo cameFromA = new SeamInfo(u, r.calculateEnergy() + u.calculateEnergy(), cameFrom);
        SeamInfo cameFromD = new SeamInfo(w, r.calculateEnergy() + u.calculateEnergy() + w.calculateEnergy(), cameFromA);

        t.checkInexact(testSeamCarver.horSeamInitialize(), cameFromD, 0.01);

    }

    // Tests the removeSeam() and removeSeamInitalize() method for vertical seams
    // removeSeamInitalize() calls removeSeam()
    void testRemoveSeamVert(Tester t) {

        putInArrayVert();
        linkUpRemoveVert();

        resetVert();
        testSeamCarver.convertTo2DArrayList();
        testSeamCarver.fixLinks();

        testSeamCarver.removeVertSeamInitialize(testSeamCarver.vertSeamInitialize());

        t.checkExpect(testSeamCarver.origin, b);
    }

    // Tests the removeSeam() and removeSeamInitalize() method
    // removeSeamInitalize() calls removeSeam()
    void testRemoveSeamHor(Tester t) {

        putInArrayHor();
        linkUpRemoveHor();

        resetHor();
        testSeamCarver.convertTo2DArrayList();
        testSeamCarver.fixLinks();

        testSeamCarver.removeHorSeamInitialize(testSeamCarver.horSeamInitialize());

        t.checkExpect(testSeamCarver.origin, s);

    }

    // Tests the draw() method
    void testDraw(Tester t) {
        putInArrayVert();
        linkUpVert();

        resetVert();
        testSeamCarver.convertTo2DArrayList();
        testSeamCarver.fixLinks();

        ComputedPixelImage pixelImage = testSeamCarver.draw();

        t.checkExpect(pixelImage, reImageVert);
    }

    // Tests the makeScene() method
    void testMakeScene(Tester t) {

        putInArrayVert();
        linkUpVert();

        resetVert();
        testSeamCarver.convertTo2DArrayList();
        testSeamCarver.fixLinks();

        WorldScene testScene = new WorldScene(1000, 1000);
        testScene.placeImageXY(reImageVert, 500, 250);

        t.checkExpect(testSeamCarver.makeScene(), testScene);
    }

    // Tests the lastScene(String) method
    void testLastScene(Tester t) {

        putInArrayVert();
        linkUpVert();

        resetVert();
        testSeamCarver.convertTo2DArrayList();
        testSeamCarver.fixLinks();

        WorldScene testScene = new WorldScene(1000, 1000);
        testScene.placeImageXY(new TextImage("Finished :3", 50, Color.PINK), 500, 250);

        t.checkExpect(testSeamCarver.lastScene("Finished :3"), testScene);

    }

    // Tests the highestEnergies() method
    void testHighestEnergies(Tester t) {
        resetVert();
        testSeamCarver.convertTo2DArrayList();
        testSeamCarver.fixLinks();

        t.checkInexact(testSeamCarver.highestEnergy(), 2.613, 0.01);

        testSeamCarver.removeVertSeamInitialize(testSeamCarver.vertSeamInitialize());

        t.checkInexact(testSeamCarver.highestEnergy(), 0.724, 0.01);

    }

    // TESTS FOR THE APIXEL CLASS`

    // Test the calculateEnergy() method
    void testCalculateEnergy(Tester t) {
        putInArrayVert();
        linkUpVert();

        t.checkInexact(a.calculateEnergy(), 1.798, 0.01);
        t.checkInexact(b.calculateEnergy(), 2.191, 0.01);
        t.checkInexact(c.calculateEnergy(), 2.369, 0.01);
        t.checkInexact(d.calculateEnergy(), 2.246, 0.01);
        t.checkInexact(e.calculateEnergy(), 2.614, 0.01);
        t.checkInexact(f.calculateEnergy(), 2.172, 0.01);
    }

    // Tests the getAllRight(ArrayList<>()) method
    void testGetAllRight(Tester t) {

        putInArrayVert();
        linkUpVert();

        resetVert();
        testSeamCarver.convertTo2DArrayList();
        testSeamCarver.fixLinks();

        ArrayList<SeamInfo> allRightTop = new ArrayList<>();
        allRightTop.add(new SeamInfo(a, a.calculateEnergy()));
        allRightTop.add(new SeamInfo(b, b.calculateEnergy()));

        t.checkExpect(a.getAllRight(new ArrayList<>()), allRightTop);
    }

    // Tests the getLeftmost() method
    void testGetLeftMost(Tester t) {
        resetVert();
        putInArrayVert();
        linkUpVert();

        t.checkExpect(a.getLeftmost(), a);
        t.checkExpect(c.getLeftmost(), c);
        t.checkExpect(b.getLeftmost(), a);
    }

    // Tests the modifyImage(ComputedPixelImage image, int x, int y, boolean isgray, double highestEnergy) method
    void testModifyImage(Tester t) {
        putInArrayVert();
        linkUpVert();

        resetVert();
        testSeamCarver.convertTo2DArrayList();
        testSeamCarver.fixLinks();

        ComputedPixelImage baseImage = new ComputedPixelImage(testSeamCarver.width,
                testSeamCarver.height);

        t.checkExpect(testSeamCarver.origin.modifyImage(baseImage, 0, 0, false,
                testSeamCarver.highestEnergy()), reImageVert);
    }

    // Tests the modifyRow(ComputedPixelImage image, int x, int y, boolean isGray, double highestEnergy) method
    void testsModifyRow(Tester t) {

        putInArrayVert();
        linkUpVert();

        resetVert();
        testSeamCarver.convertTo2DArrayList();
        testSeamCarver.fixLinks();

        ComputedPixelImage baseImage = new ComputedPixelImage(testSeamCarver.width,
                testSeamCarver.height);
        baseImage = a.modifyRow(baseImage, 0, 0, false, testSeamCarver.highestEnergy());
        baseImage = c.modifyRow(baseImage, 0, 1, false, testSeamCarver.highestEnergy());
        baseImage = e.modifyRow(baseImage, 0, 2, false, testSeamCarver.highestEnergy());

        t.checkExpect(baseImage, reImageVert);
    }

    // Tests the getTopMost() method
    void testGetTopmost(Tester t) {

        putInArrayVert();
        linkUpVert();

        resetVert();
        testSeamCarver.convertTo2DArrayList();
        testSeamCarver.fixLinks();

        t.checkExpect(e.getTopmost(), a);
        t.checkExpect(f.getTopmost(), b);
    }

    // tests the getAllBottom(ArrayList<SeamInfo>) method
    void getAllBottom(Tester t) {

        putInArrayHor();
        linkUpHor();

        resetHor();
        testSeamCarver.convertTo2DArrayList();
        testSeamCarver.fixLinks();

        // b, d, f
        // a, c, e

        SeamInfo bSeam = new SeamInfo(b, b.calculateEnergy());
        SeamInfo aSeam = new SeamInfo(a, a.calculateEnergy());

        ArrayList<SeamInfo> seams = new ArrayList<>();
        seams.add(bSeam);
        seams.add(aSeam);

        t.checkExpect(b.getAllBottom(new ArrayList<>()), seams);

    }

    // tests the turnGray(double) method
    void testTurnGray(Tester t) {

        putInArrayHor();
        linkUpHor();

        resetHor();
        testSeamCarver.convertTo2DArrayList();
        testSeamCarver.fixLinks();

        t.checkExpect(testSeamCarver.origin.turnGray(testSeamCarver.highestEnergy()), new Color(0, 0, 0, 214));
        t.checkExpect(testSeamCarver.origin.bottom.turnGray(testSeamCarver.highestEnergy()), new Color(0, 0, 0, 175));
        t.checkExpect(testSeamCarver.origin.left.turnGray(testSeamCarver.highestEnergy()), new Color(0, 0, 0, 0));
        t.checkExpect(testSeamCarver.origin.right.turnGray(testSeamCarver.highestEnergy()), new Color(0, 0, 0, 219));

    }

    // TESTS FOR THE UTILS CLASS

    // tests the twoWayLink() method in the Utils class
    void testTwoWayLink(Tester t) {

        APixel x = new Pixel(Color.BLACK);
        APixel y = new Pixel(Color.GRAY);
        APixel z = new Pixel(Color.WHITE);

        // x, y, z
        x.right = y;
        y.left = x;
        y.right = z;
        z.left = y;

        // this essentially deletes y from the linked list
        new Utils().createTwoWayLink(true, x, z);
        t.checkExpect(x.right, z);
        t.checkExpect(z.left, x);

        APixel r = new Pixel(Color.BLACK);
        APixel s = new Pixel(Color.GRAY);
        APixel v = new Pixel(Color.WHITE);

        r.bottom = s;
        s.bottom = v;
        v.top = s;
        s.top = r;

        new Utils().createTwoWayLink(false, r, v);
        t.checkExpect(r.bottom, v);
        t.checkExpect(v.top, r);
    }

    // tests the minSeam(ArrayList<>()) method in the Utils class
    void testMinSeam(Tester t) {

        SeamInfo s1 = new SeamInfo(e, 3);
        SeamInfo s2 = new SeamInfo(c, 10);
        SeamInfo s3 = new SeamInfo(a, 12);

        ArrayList<SeamInfo> a1 = new ArrayList<>(Arrays.asList(s1, s2, s3));
        ArrayList<SeamInfo> a2 = new ArrayList<>(Arrays.asList(s1, s3));
        ArrayList<SeamInfo> a3 = new ArrayList<>(Arrays.asList(s2, s3));

        t.checkExpect(new Utils().minSeam(a1), s1);
        t.checkExpect(new Utils().minSeam(a2), s1);
        t.checkExpect(new Utils().minSeam(a3), s2);

    }

    // tests the getDirectionFromVert(APixel alreadyRemoved, APixel toBeRemove)
    void testGetDirectionFromVert(Tester t) {

        resetVert();
        // this initializes the test values
        putInArrayVert();
        linkUpVert();

        t.checkExpect(new Utils().getDirectionFromVert(f, d), "b");
        t.checkExpect(new Utils().getDirectionFromVert(d, a), "br");
        t.checkExpect(new Utils().getDirectionFromVert(c, b), "bl");

    }

    // tests the getDirectionFromHor(APixel alreadyRemoved, APixel toBeRemove) methos
    void testGetDirectionFromHor(Tester t) {

        resetHor();
        // this initializes tests values
        putInArrayHor();
        linkUpHor();

        t.checkExpect(new Utils().getDirectionFromHor(w, u), "r");
        t.checkExpect(new Utils().getDirectionFromHor(w, this.t), "tr");
        t.checkExpect(new Utils().getDirectionFromHor(v, u), "br");
    }

    // tests the changePosn(Posn, String) method
    void testChangePosn(Tester t) {
        Posn examplePos = new Posn(0, 0);
        t.checkExpect(new Utils().changePosn(examplePos, "t"), new Posn(0, -1));
        t.checkExpect(new Utils().changePosn(examplePos, "b"), new Posn(0, 1));
        t.checkExpect(new Utils().changePosn(examplePos, "l"), new Posn(-1, 0));
        t.checkExpect(new Utils().changePosn(examplePos, "r"), new Posn(1, 0));
    }

    // tests the OutOfBounds(Posn, String, int, int) method
    void testOutOfBounds(Tester t) {

        Posn examplePos = new Posn(0, 0);
        t.checkExpect(new Utils().outOfBounds(examplePos, "t", 5, 5), true);
        t.checkExpect(new Utils().outOfBounds(examplePos, "b", 5, 5), false);
        t.checkExpect(new Utils().outOfBounds(examplePos, "r", 5, 5), false);
        t.checkExpect(new Utils().outOfBounds(examplePos, "l", 5, 5), true);

        Posn examplePos2 = new Posn(0, 2);
        t.checkExpect(new Utils().outOfBounds(examplePos2, "t", 5, 5), false);
        t.checkExpect(new Utils().outOfBounds(examplePos2, "b", 5, 5), false);
        t.checkExpect(new Utils().outOfBounds(examplePos2, "r", 5, 5), false);
        t.checkExpect(new Utils().outOfBounds(examplePos2, "l", 5, 5), true);

    }

    // TESTS FOR SEMAINFO

    void testHighlight(Tester t) {

        putInArrayVert();
        linkUpVert();

        resetVert();
        testSeamCarver.convertTo2DArrayList();
        testSeamCarver.fixLinks();

        SeamInfo cameFrom = new SeamInfo(a, a.calculateEnergy());
        SeamInfo cameFromA = new SeamInfo(d, a.calculateEnergy() + d.calculateEnergy(), cameFrom);
        SeamInfo cameFromD = new SeamInfo(f, a.calculateEnergy() + d.calculateEnergy() + f.calculateEnergy(), cameFromA);

        t.checkExpect(a.color != Color.RED, true);
        t.checkExpect(d.color != Color.RED, true);
        t.checkExpect(f.color != Color.RED, true);

        cameFromD.highlight();

        t.checkExpect(a.color, Color.RED);
        t.checkExpect(d.color, Color.RED);
        t.checkExpect(f.color, Color.RED);

    }

    // Starts the SeamCarver animation
    void testBigBang(Tester t) {
        testSeamCarver2.bigBang(1000, 500, 0.1);
    }

}

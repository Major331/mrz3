package by.bsuir.mrz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Network {

    private static final int WIDTH = 5;
    private static final int PIXELS = 25;
    private final Matrix image;
    private Matrix weight;
    private final List<Matrix> images;
    private static final int MAX_STEPS = 1000;
    Matrix curr;

    public Network() throws IOException {

        images = new ArrayList<>();

        images.add(ImageParser.readImage("images/tank.txt"));
        images.add(ImageParser.readImage("images/z.txt"));
        images.add(ImageParser.readImage("images/v.txt"));
        images.add(ImageParser.readImage("images/rectangle.txt"));

        image = ImageParser.readImage("images/broken/tankBroken.txt");
        ImagePrinter.showImages(images, PIXELS, WIDTH);
        System.out.println("-------------------");
        System.out.println("Image to find out");
        ImagePrinter.showImage(image, PIXELS, WIDTH);
        createWeightMatrix();

    }

    public void createWeightMatrix() {
        weight = new Matrix(PIXELS, PIXELS);
        images.forEach(i -> {
            var xi = image.transpose();
            var deltaWeights
                = xi.multiplyMatrix(xi.transpose()).multiplyMatrix(1.0 / PIXELS);
            deltaWeights.ogDiagonal();
            weight = weight.addMatrix(deltaWeights);
        });
    }

    public void recognize() {
        var previousImg = image.copy();
        var steps = 0;
        var recognized = false;

        while (steps < MAX_STEPS && !recognized) {
            curr = previousImg.multiplyMatrix(weight);
            curr = curr.activateMatrix(Math::tanh);
            recognized = curr.equals(previousImg) ;
            previousImg = curr;
            steps++;
        }
        System.out.println("Steps: " + steps);
        ImagePrinter.showImage(curr, PIXELS, WIDTH);

        System.out.println("Founded!");
    }
}

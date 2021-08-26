import graphs.Graph;

import java.util.*;

public class DynamicProgrammingSeamFinder implements SeamFinder {

    private double[][] energyArray;
    private DualGradientEnergyFunction energy;
    private Picture picture;

    public List<Integer> findSeam(Picture picture, EnergyFunction f) {
        this.energyArray = new double[picture.width()][picture.height()];
        this.energy = new DualGradientEnergyFunction();
        this.picture = picture;
        for(int y = 0; y < picture.height(); y++) {
            this.energyArray[0][y] = this.energy.apply(picture, 0, y);
        }
        ArrayList<Integer> minEnergyPath = new ArrayList<>();
        this.minimalEnergyFill();
        int starting_y = this.startMinIndex(picture.width() - 1);
        minEnergyPath.add(starting_y);
        for(int x = picture.width() - 2; x >= 0; x--) {
            double min_value = Double.MAX_VALUE;
            int y_coord = -1;
            if(starting_y > 0 && starting_y < this.picture.height() - 1) {
                double top_left = this.energyArray[x][starting_y - 1];
                double mid_left = this.energyArray[x][starting_y];
                double bottom_left = this.energyArray[x][starting_y + 1];
                min_value = Math.min(top_left, Math.min(mid_left, bottom_left));
                y_coord = this.getMinIndex(min_value, starting_y - 1, starting_y + 1, x);
            } else if(starting_y == 0) {
                double mid_left = this.energyArray[x][starting_y];
                double bottom_left = this.energyArray[x][starting_y + 1];
                min_value = Math.min(mid_left, bottom_left);
                y_coord = this.getMinIndex(min_value, 0, 1, x);
            } else if(starting_y == this.picture.height() - 1) {
                double top_left = this.energyArray[x][starting_y - 1];
                double mid_left = this.energyArray[x][starting_y];
                min_value = Math.min(top_left, mid_left);
                y_coord = this.getMinIndex(min_value, this.picture.height() - 2, this.picture.height() - 1, x);
            }
            starting_y = y_coord;
            minEnergyPath.add(y_coord);
        }
        Collections.reverse(minEnergyPath);
        return minEnergyPath;
    }

    private void minimalEnergyFill() {
        for(int x = 1; x < this.picture.width(); x++) {
            double top = 0.0;
            double middle = 0.0;
            double bottom = 0.0;
            double top_mid = this.energyArray[x - 1][0];
            double top_bottom = this.energyArray[x - 1][1];
            double bottom_mid = this.energyArray[x - 1][this.picture.height() - 1];
            double bottom_top = this.energyArray[x - 1][this.picture.height() - 2];
            this.energyArray[x][0] = Math.min(top_mid, top_bottom) + energy.apply(picture, x, 0);
            this.energyArray[x][this.picture.height() - 1] = Math.min(bottom_mid, bottom_top) + energy.apply(picture, x, this.picture.height() - 1);
            for(int y = 1; y < this.picture.height() - 1; y++) {
                top = this.energyArray[x - 1][y + 1];
                middle = this.energyArray[x - 1][y];
                bottom = this.energyArray[x - 1][y - 1];
                this.energyArray[x][y] = Math.min(Math.min(top, middle), bottom) + energy.apply(picture, x, y);
            }
        }
    }

    private int startMinIndex(int x) {
        double minValue = this.energyArray[x][0];
        int y_coordinte = 0;
        for(int y = 1; y < this.picture.height(); y++) {
          if(this.energyArray[x][y] < minValue) {
            y_coordinte = y;
            minValue = this.energyArray[x][y];
          }
        }
        return y_coordinte;
    }

    private int getMinIndex(double min_value, int start_index, int end_index, int x_coord) {
        int minIndex = -1;
        for(int y = start_index; y <= end_index; y++) {
            if(min_value == this.energyArray[x_coord][y]) {
                minIndex = y;
            }
        }
        return minIndex;
    }

}


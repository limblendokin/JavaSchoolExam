package com.tsystems.javaschool.tasks.pyramid;

import java.util.Comparator;
import java.util.List;

public class PyramidBuilder {

    /**
     * Builds a pyramid with sorted values (with minumum value at the top line and maximum at the bottom,
     * from left to right). All vacant positions in the array are zeros.
     *
     * @param inputNumbers to be used in the pyramid
     * @return 2d array with pyramid inside
     * @throws {@link CannotBuildPyramidException} if the pyramid cannot be build with given input
     */
    public int[][] buildPyramid(List<Integer> inputNumbers) {

        double doubleRows = (-1 + Math.sqrt(1 + inputNumbers.size() * 8)) / 2;
        // TODO : Check NaN and +-Inf
        if(doubleRows%1==0 && !inputNumbers.contains(null)){
            inputNumbers.sort(Comparator.naturalOrder());
            int rows = (int)doubleRows;
            int width = 1 + 2*(rows-1);
            int inputNumbersIndex = 0;
            int[][] arr = new int[rows][width];
            for(int i = 0; i<rows; i++){
                for(int j = rows-i-1; j<=width-rows+i; j+=2){
                    arr[i][j] = inputNumbers.get(inputNumbersIndex++);
                }
            }
            return arr;
        }
        else throw new CannotBuildPyramidException();

    }


}

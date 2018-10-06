package com.tsystems.javaschool.tasks.pyramid;

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
        if(doubleRows % 1 == 0 && !inputNumbers.contains(null)){
            //Collections.sort(inputNumbers);
            quickSort(inputNumbers);
            int rows = (int)doubleRows;
            int width = 2 * rows - 1;
            int inputNumbersIndex = 0;
            int[][] arr = new int[rows][width];
            for(int i = 0; i<rows; i++){
                for(int j = rows - i - 1; j <= width-rows + i; j += 2){
                    arr[i][j] = inputNumbers.get(inputNumbersIndex++);
                }
            }
            return arr;
        }
        else throw new CannotBuildPyramidException();

    }

    private void quickSort(List<Integer> list){
        int low = 0;
        int high = list.size() - 1;
        sort(list, low, high);
    }

    private int partition(List<Integer> list, int low, int high)
    {
        int pivot = list.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++)
        {
            if (list.get(j) <= pivot)
            {
                i++;
                int temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }
        int temp = list.get(i + 1);
        list.set(i + 1, list.get(high));
        list.set(high, temp);

        return i + 1;
    }

    private void sort(List<Integer> list, int low, int high)
    {
        if (low < high)
        {
            int partitioningIndex = partition(list, low, high);
            sort(list, low, partitioningIndex - 1);
            sort(list, partitioningIndex + 1, high);
        }
    }

}

package com.tsystems.javaschool.tasks.subsequence;

import com.sun.javaws.exceptions.InvalidArgumentException;

import java.util.List;

public class Subsequence {

    /**
     * Checks if it is possible to get a sequence which is equal to the first
     * one by removing some elements from the second one.
     *
     * @param x first sequence
     * @param y second sequence
     * @return <code>true</code> if possible, otherwise <code>false</code>
     */
    @SuppressWarnings("rawtypes")
    public boolean find(List x, List y) {
        int j = 0;
        int i = 0;
        if(x != null && y != null) {
            while(i < x.size() && j < y.size()){
                if(y.get(j)==x.get(i)){
                    i++;
                }
                j++;
            }
        }
        else throw new IllegalArgumentException();
        if(i == x.size())
            return true;
        else
            return false;
    }
}

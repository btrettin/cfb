package com.example.cfbstats.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SortService {

    @Autowired
    public SortService() {}

    public List<Integer> sortNumbers(List<Integer> toSort){
        if(toSort.size() <= 1){
            return toSort;
        }
        Integer pivot = toSort.get(0);

        List<Integer> lessThanList = new ArrayList<>();
        List<Integer> greaterThanList = new ArrayList<>();

        toSort.forEach(curr -> {
        if (curr > pivot) {
            greaterThanList.add(curr);
        } else if (curr < pivot) {
            lessThanList.add(curr);
        }
        });
        return Stream.of(sortNumbers(lessThanList), Collections.singletonList(pivot), sortNumbers(greaterThanList)).flatMap(Collection::stream).collect(Collectors.toList());
    }
}

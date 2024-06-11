package com.example.searc.service;

import com.example.searc.model.Dishes;
import com.example.searc.model.Location;
import com.example.searc.repository.DishesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishesService {
    @Autowired
    private DishesRepository dishRepository;
    private LocationService locationService;

    //    public List<Dishes> getAllDishesWithDistances(String currentLocationName) {
//        List<Dishes> dishes = dishRepository.findAll();
//        for (Dishes dish : dishes) {
//            double distance = locationService.calculateDistance(currentLocationName, dish.getLocation());
//            dish.setDistance(distance);
//        }
//        return dishes;
//    }
    public List<Dishes> getTop10HotDishes(List<Dishes> allDishes) {
        List<Dishes> top10Dishes = new ArrayList<>();
        if (allDishes.size() <= 10) {
            return allDishes;
        }

        int k = 10;
        quickSelect(allDishes, 0, allDishes.size() - 1, k);

        for (int i = 0; i < k; i++) {
            top10Dishes.add(allDishes.get(i));
        }

        return top10Dishes;
    }

    private void quickSelect(List<Dishes> dishes, int left, int right, int k) {//for hot
        int pivotIndex = partition(dishes, left, right);
        if (pivotIndex == k - 1) {
            return;
        } else if (pivotIndex > k - 1) {
            quickSelect(dishes, left, pivotIndex - 1, k);
        } else {
            quickSelect(dishes, pivotIndex + 1, right, k);
        }
    }

    private void quickSelect_score(List<Dishes> dishes, int left, int right, int k) {//for score
        int pivotIndex = partition_score(dishes, left, right);
        if (pivotIndex == k - 1) {
            return;
        } else if (pivotIndex > k - 1) {
            quickSelect_score(dishes, left, pivotIndex - 1, k);
        } else {
            quickSelect_score(dishes, pivotIndex + 1, right, k);
        }
    }

    private int partition(List<Dishes> dishes, int left, int right) {
        Dishes pivot = dishes.get(right);
        int i = left;
        for (int j = left; j < right; j++) {
            if (dishes.get(j).getHot() >= pivot.getHot()) {
                Collections.swap(dishes, i, j);
                i++;
            }
        }
        Collections.swap(dishes, i, right);
        return i;
    }

    private int partition_score(List<Dishes> dishes, int left, int right) {//for score
        Dishes pivot = dishes.get(right);
        int i = left;
        for (int j = left; j < right; j++) {
            if (dishes.get(j).getScore() >= pivot.getScore()) {
                Collections.swap(dishes, i, j);
                i++;
            }
        }
        Collections.swap(dishes, i, right);
        return i;
    }

    public List<Dishes> getTop10ScoreDishes(List<Dishes> allDishes) {
        List<Dishes> top10Dishes = new ArrayList<>();
        if (allDishes.size() <= 10) {
            return allDishes;
        }

        int k = 10;
        quickSelect_score(allDishes, 0, allDishes.size() - 1, k);

        for (int i = 0; i < k; i++) {
            top10Dishes.add(allDishes.get(i));
        }

        return top10Dishes;
    }

    public List<Dishes> findDishesByName(String name, String category) {
        return dishRepository.findByNameContainingIgnoreCase(name).stream()
                .sorted(getComparator(category))
                .collect(Collectors.toList());
    }

    public List<Dishes> findDishesByKind(String kind, String category) {
        return dishRepository.findByKindContainingIgnoreCase(kind).stream()
                .sorted(getComparator(category))
                .collect(Collectors.toList());
    }

    public List<Dishes> findDishesByLocation(String location, String category) {
        return dishRepository.findByLocationContainingIgnoreCase(location).stream()
                .sorted(getComparator(category))
                .collect(Collectors.toList());
    }

    private Comparator<Dishes> getComparator(String category) {
        if ("hot".equalsIgnoreCase(category)) {
            return Comparator.comparingLong(Dishes::getHot).reversed();
        } else if ("score".equalsIgnoreCase(category)) {
            return Comparator.comparingDouble(Dishes::getScore).reversed();
        } else {
            return Comparator.comparingLong(Dishes::getHot).reversed();
        }
    }

    public List<Dishes> getTop10NearDishes(List<Dishes> allDishes) {
        List<Dishes> top10Dishes = new ArrayList<>();
        if (allDishes.size() <= 10) {
            return allDishes;
        }

        int k = 10;
        quickSelect_distance(allDishes, 0, allDishes.size() - 1, k);

        for (int i = 0; i < k; i++) {
            top10Dishes.add(allDishes.get(i));
        }

        return top10Dishes;
    }

    private void quickSelect_distance(List<Dishes> dishes, int left, int right, int k) {
        int pivotIndex = partition_distance(dishes, left, right);
        if (pivotIndex == k - 1) {
            return;
        } else if (pivotIndex > k - 1) {
            quickSelect_distance(dishes, left, pivotIndex - 1, k);
        } else {
            quickSelect_distance(dishes, pivotIndex + 1, right, k);
        }
    }

    private int partition_distance(List<Dishes> dishes, int left, int right) {
        Dishes pivot = dishes.get(right);
        int i = left;
        for (int j = left; j < right; j++) {
            if (dishes.get(j).getDistance() >= pivot.getDistance()) {
                Collections.swap(dishes, i, j);
                i++;
            }
        }
        Collections.swap(dishes, i, right);
        return i;
    }
}

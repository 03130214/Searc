package com.example.searc.controller;

import com.example.searc.model.Dishes;
import com.example.searc.model.Location;
import com.example.searc.repository.DishesRepository;
import com.example.searc.repository.LocationRepository;
import com.example.searc.service.DishesService;
import com.example.searc.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class DishesController {
    @Autowired
    private final DishesService dishesService;
    @Autowired
    private final DishesRepository dishesRepository;
    @Autowired
    private LocationService locationService;

    public DishesController(DishesService dishesService, DishesRepository dishesRepository) {
        this.dishesService = dishesService;
        this.dishesRepository = dishesRepository;
    }

    @GetMapping("/dishes")
    public List<Dishes> getDishes(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String kind,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String category) {
        if (name != null && !name.isEmpty()) {
            return dishesService.findDishesByName(name,category);
        } else if (kind != null && !kind.isEmpty()) {
            return dishesService.findDishesByKind(kind,category);
        } else if (location != null && !location.isEmpty()) {
            return dishesService.findDishesByLocation(location,category);
        } else {
            return List.of();  // 返回空列表
        }
    }
    @GetMapping("/10dishes")
    public List<Dishes> getTop10Dishes(@RequestParam(required = false) String location, @RequestParam(required = false) String choice, @RequestParam(required = false) String kind) {
        List<Dishes> allDishes,filteredDishes;
            allDishes = dishesRepository.findAll();
        if ((location != null && !location.isEmpty())) {
            if ((kind != null && !kind.isEmpty())) {
                allDishes = allDishes.stream()
                        .filter(dish -> dish.getKind().equals(kind))
                        .collect(Collectors.toList());
            }
            filteredDishes = allDishes.stream()
                    .filter(dish -> dish.getLocation().equals(location))
                    .collect(Collectors.toList());
        } else if ((kind != null && !kind.isEmpty())) {
            filteredDishes = allDishes.stream()
                    .filter(dish -> dish.getKind().equals(kind))
                    .collect(Collectors.toList());
        } else {
            filteredDishes = allDishes;
        }

        if (choice.equals("hot"))
            return dishesService.getTop10HotDishes(filteredDishes);
        else if(choice.equals("distance"))
            return dishesService.getTop10NearDishes(filteredDishes);
        else
            return dishesService.getTop10ScoreDishes(filteredDishes);
    }
}

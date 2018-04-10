package com.example.demo.Car;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CarController {
	CarService carService;
	
	CarController(CarService carService) {
		this.carService = carService;
	}
	
	@RequestMapping(value="/cars", method=RequestMethod.GET)
	public List<Car> getAllCars() {
		return carService.getAllCars();
		
	}
	
	@CrossOrigin
	@RequestMapping(value="/cars/add", method=RequestMethod.POST, consumes="application/json")
	public Car addCar(@RequestBody Car car) {
		return carService.addCar(car);
		
	}

}

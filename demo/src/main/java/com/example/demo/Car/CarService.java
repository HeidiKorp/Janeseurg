package com.example.demo.Car;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class CarService {
	CarRepository carRepository;
	
	CarService(CarRepository carRepository) {
		this.carRepository = carRepository;
	}
	List<Car> getAllCars() {
		return carRepository.findAll();
	}
	Car addCar(Car car) {
		return carRepository.save(car);
	}

}

package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

// model for the second task
public class City {

    private String cityName;
    private Map<City, Integer> transferCostToOtherCity = new HashMap<>();

    public City(String cityName) {
        this.cityName = cityName;
    }

    public String getCityName() {
        return cityName;
    }

    public Map<City, Integer> getTransferCostToOtherCity() {
        return transferCostToOtherCity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return Objects.equals(cityName, city.cityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityName);  // Only include cityName in hashCode to avoid circular references
    }
}

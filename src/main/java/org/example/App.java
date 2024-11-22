package org.example;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class App {

    public static void main(String[] args) {
        System.out.println(countSumOfTheDigitsInTheFactorial(100));
    }

    /**
     * The first task, that count a correct parenthetical expressions.
     * In the first step method checking on null, and on negative the number, after this, method using
     * the formula of catalan number, and returning the result of formula.
     *
     * @param n - The correct number ( n >= 0)
     * @return Integer - the number of correct parenthetical expressions
     */
    public static Integer countCorrectParentheticalExpressions(Integer n) {
        // Check if number is null
        Objects.requireNonNull(n, "Integer cannot be null");
        //Check if number have negative value
        if (n < 0) throw new IllegalArgumentException("Integer cannot be negative");

        //Counting with formula of catalan number the result
        return countFactorial(2 * n) / (countFactorial(n + 1) * countFactorial(n));
    }

    /**
     * Method accepting a not null value, after this method calculating a factorial(number) and returning a result
     *
     * @param number - Not null value
     * @return Integer - result of factorial number
     */
    private static Integer countFactorial(Integer number) {
        // Check if number is null
        Objects.requireNonNull(number, "Integer cannot be null");

        int result = 1;

        //calculate factorial algorithm
        for (int i = 1; i <= number; i++) {
            result *= i;
        }

        //return result
        return result;
    }

    /**
     * Method is calculating the minimal price, to travel fromCity, to toCity
     * For the first step method initializating names of cities, and group them to List<String> citiesNames,
     * after that, method is creating a List<City> and adding to list all cities with cost to travel to neighbors,
     * method after this, will calculate with Dijkstra's algorithm(https://www.baeldung.com/java-dijkstra), and return a result
     * minimal cost of travel
     *
     * @param citiesAndConnectCost - List of names of cities, and numbers of neighbors,
     *                             and references with cost to travel to other cities
     * @param fromCity - String, name of city, from where we need to start
     * @param toCity - String, name of city, where should we come
     * @return Integer - the minimal cost of travel from start city to end city
     */
    public static Integer countMinimalCost(List<String> citiesAndConnectCost, String fromCity, String toCity) {
        //check on null all arguments of method
        checkOnNull(citiesAndConnectCost, fromCity, toCity);

        //Creating a list with citiesNames, and collect them into it
        List<String> cityNames = citiesAndConnectCost.stream()
                .filter(s -> s.matches("[a-zA-Z'-]+"))
                .collect(Collectors.toList());

        //creating helpful collection to collect data
        List<City> cities = new ArrayList<>();
        Map<String, City> cityNameToCity = new HashMap<>();

        //Creating City models, and group them to List<City>
        int i = 0;
        for (String cityName : cityNames) {
            City city = new City(cityName);
            cities.add(city);
            cityNameToCity.put(cityName, city);
            i++;
        }

        //Modify cities of List<City>, where we are adding a neighbors, with travel cost
        modifyList(citiesAndConnectCost, cities, cityNameToCity);

        //converting fromCity and ToCity, into a City model
        City startCity = cityNameToCity.get(fromCity);
        City endCity = cityNameToCity.get(toCity);

        //Checking on null
        if (startCity == null || endCity == null) {
            return null;
        }

        //Algorithm to find a minimal price to travel
        Map<City, Integer> shortestPaths = new HashMap<>();
        PriorityQueue<City> pq = new PriorityQueue<>(Comparator.comparingInt(shortestPaths::get));

        shortestPaths.put(startCity, 0);
        pq.add(startCity);

        for (City city : cities) {
            if (!city.equals(startCity)) {
                shortestPaths.put(city, Integer.MAX_VALUE);
            }
        }

        while (!pq.isEmpty()) {
            City currentCity = pq.poll();
            int currentCost = shortestPaths.get(currentCity);

            if (currentCity.equals(endCity)) {
                return currentCost;
            }

            for (Map.Entry<City, Integer> entry : currentCity.getTransferCostToOtherCity().entrySet()) {
                City neighbor = entry.getKey();
                int newCost = currentCost + entry.getValue();

                if (newCost < shortestPaths.get(neighbor)) {
                    shortestPaths.put(neighbor, newCost);
                    pq.add(neighbor);
                }
            }
        }

        return null;
    }

    /**
     * Method, that modifying cities list, and adding data( neighbors and cost for travel to them)
     * @param citiesAndConnectCost - list with all data
     * @param cities - list with cities, which one method should modify
     * @param cityNameToCity - list, which one containing name of city in String, and Model City
     */
    private static void modifyList(List<String> citiesAndConnectCost,  List<City> cities, Map<String, City> cityNameToCity){
        int i = 0;
        while (i < citiesAndConnectCost.size()) {
            String currentCityName = citiesAndConnectCost.get(i);
            //checking that string is name of city
            if (!currentCityName.matches("[a-zA-Z'-]+")) {
                i++;
                continue;
            }

            City city = cityNameToCity.get(currentCityName);
            i++;

            while (i < citiesAndConnectCost.size() && !citiesAndConnectCost.get(i).matches("[a-zA-Z'-]+")) {
                String[] parts = citiesAndConnectCost.get(i).split(" ");

                if (parts.length == 2) {
                    try {
                        //getting index of connected City in cities List
                        int connectedCityIndex = Integer.parseInt(parts[0]) - 1;
                        //getting cost value of connected City
                        int cost = Integer.parseInt(parts[1]);

                        if (connectedCityIndex >= 0 && connectedCityIndex < cities.size()) {
                            //Creating connectedCity
                            City connectedCity = cities.get(connectedCityIndex);
                            //adding into a transferCostToOtherCity a value with connectedCity and cost
                            city.getTransferCostToOtherCity().put(connectedCity, cost);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid connection data: " + Arrays.toString(parts));
                    }
                }
                i++;
            }
        }
    }

    /**
     * private method, that checking on null objects
     * @param citiesAndConnectCost - List<String> with data of names of city, count of their neighbors, and cost travels
     * @param fromCity - String, name of city, from where we need to start
     * @param toCity - String, name of city, where should we come
     */
    private static void checkOnNull(List<String> citiesAndConnectCost, String fromCity, String toCity){
        Objects.requireNonNull(citiesAndConnectCost, "List of cities and connect cost cannot be null");
        Objects.requireNonNull(fromCity, "String fromCity cannot be null");
        Objects.requireNonNull(toCity, "String toCity cannot be null");
    }

    /**
     *Method which one is counting a sum of digits of factorial result
     *
     * @param number - the number, which the method must find the factorial
     * @return - sum of digits of factorial result( for example: 3! = 1*2*3*4 = 24, method should return 2+4)
     */
    public static Integer countSumOfTheDigitsInTheFactorial(Integer number) {
        Objects.requireNonNull(number, "Integer cannot be null");
        BigInteger bigInteger = BigInteger.valueOf(1);
        for (int i = 1; i <= number; i++) {
            bigInteger = bigInteger.multiply(BigInteger.valueOf(i));
        }

        Integer result = 0;
        for(char c : bigInteger.toString().toCharArray()) {
            result += Character.getNumericValue(c);
        }
        return result;
    }
}

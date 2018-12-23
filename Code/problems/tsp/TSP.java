package problems.tsp;

import java.util.ArrayList;
import java.util.Random;

import optimization.*;
import utils.*;
import visualization.*;

/**
 * Implements a version of the classical TSP. The agent begins the tour in a
 * random-initial position and must reach the exit after visiting all cities
 * once. The size of the problem corresponds to the number of cities.
 */
public class TSP extends OptimizationProblem implements ProblemVisualizable {

	/* The class uses its own random generator (for reproducibility). */
	private static Random random = new Random();

	/* Definition of the problem. */
	protected int maxXYPos; // Dimensions
	protected Position posAgent; // Initial position of the agent
	protected ArrayList<Position> posCities; // Positions of the cities
	protected Position posExit; // Exit

	// Construction and parameter setting.

	/**
	 * Constructors
	 */
	public TSP() {
		generateInstance(20, 10, 0);
	}

	public TSP(int maxXYPos, int numCities) {
		generateInstance(maxXYPos, numCities, 0);
	}

	public TSP(int maxXYPos, int numCities, int seed) {
		generateInstance(maxXYPos, numCities, seed);
	}

	/**
	 * Generates an instance of the problem given its size, number of cheeses and
	 * seed.
	 */
	protected void generateInstance(int maxXYPos, int numCities, int seed) {
		// Sets the parameters
		this.maxXYPos = maxXYPos;
		this.size = numCities;
		random.setSeed(seed);
		// Places the agent
		int agentX = random.nextInt(maxXYPos);
		int agentY = random.nextInt(maxXYPos);
		posAgent = new Position(agentX, agentY);
		// Places the cities
		posCities = new ArrayList<Position>(numCities);
		int cityX, cityY, idCity = 0;
		while (idCity < numCities) {
			cityX = random.nextInt(maxXYPos);
			cityY = random.nextInt(maxXYPos);
			if ((cityX != agentX) || (cityY != agentY)) {
				posCities.add(new Position(cityX, cityY));
				idCity++;
			}
		}
		// Places the exit.
		posExit = new Position(maxXYPos - 1, maxXYPos - 1);
	}

	/**
	 * Sets the parameters of the problem.
	 */
	@Override
	public void setParams(String[] params) {
		try {
			if (params.length == 2)
				generateInstance(Integer.parseInt(params[0]), Integer.parseInt(params[1]), 0);
			else if (params.length > 2)
				generateInstance(Integer.parseInt(params[0]), Integer.parseInt(params[1]), Integer.parseInt(params[2]));
			else
				System.out.println("At least the size and number of cities must be provided.");
		} catch (Exception E) {
			System.out.println("There has been an error while generating the new instance of TSP problem.");
		}
	}

	// Getters and setters

	public int getMaxXYPos() {
		return maxXYPos;
	}

	public Position getPosAgent() {
		return posAgent;
	}

	public ArrayList<Position> getPosCities() {
		return posCities;
	}

	public Position getPosExit() {
		return posExit;
	}

	// Problem description.

	/**
	 * Calculates the score of a configuration as the sum of the path.
	 */
	@Override
	public double score(Configuration configuration) {
		// Dada una secuencia de ciudades, devuelve la longitud del camino.
		// distancia eucl�dea.
		/**
		 * COMPLETAR
		 */
		int[] secuencia = configuration.getValues();
		System.out.println("EMPEZAMOS");
		for (int x = 0; x < secuencia.length; x++) {

			System.out.println("ciudad" + "[" + x + "]=" + secuencia[x]);

		}

		System.out.println(posCities);
		double dist_eucli = 0d;
		double dist_eucliHamster = 0d;
		int pos_x = 0;
		int pos_y = 0;
		int pos_x2 = 0;
		int pos_y2 = 0;
		for (int i = 0; i < secuencia.length; i++) {
			int se = secuencia[i];
			if ((i + 1) < secuencia.length) {
				int se2 = secuencia[i + 1];
				pos_x = posCities.get(se).x;
				pos_y = posCities.get(se).y;
				pos_x2 = posCities.get(se2).x;
				pos_y2 = posCities.get(se2).y;
			} else {
				pos_x = pos_x2;
				pos_y = pos_y2;
				pos_x2 = this.posAgent.x;
				pos_y2 = this.posAgent.y;
			}
			dist_eucli = dist_eucli + Math.sqrt(Math.pow((pos_x - pos_x2), 2) + Math.pow((pos_y - pos_y2), 2));

		}

		// ditancia de pos hamster al primer queso + todas las calculadas antes.
		pos_x2 = posCities.get(secuencia[0]).x;
		pos_y2 = posCities.get(secuencia[0]).y;
		dist_eucli = dist_eucli
				+ Math.sqrt(Math.pow((this.posAgent.x - pos_x2), 2) + Math.pow((this.posAgent.y - pos_y2), 2));
		System.out.println("------------------->" + dist_eucli);
		return dist_eucli;
	}

	/**
	 * Generates a random configuration. In this case, a permutation.
	 */
	@Override
	public Configuration genRandomConfiguration() {
		// Creates the values (ordered)
		int[] values = new int[size];
		for (int idCity = 0; idCity < size; idCity++)
			values[idCity] = idCity;
		// Uses Fisher Yates method to shuffle the array:
		// https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle
		int index;
		int aux;
		for (int idCity = size - 1; idCity > 0; idCity--) {
			index = random.nextInt(idCity + 1);
			aux = values[index];
			values[index] = values[idCity];
			values[idCity] = aux;
		}
		return new Configuration(values);
	}

	// Utilities

	/**
	 * Calculates the distance between two points.
	 */
	private double dist(Position from, Position to) {
		return Math.sqrt(Math.pow(from.x - to.x, 2) + Math.pow(from.y - to.y, 2));
	}

	/**
	 * Returns a view that allows displaying the result.
	 */
	@Override
	public ProblemView getView() {
		TSPView mazeView = new TSPView(this, 700);
		return mazeView;
	}

}

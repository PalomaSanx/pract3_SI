package algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import optimization.Configuration;
import optimization.OptimizationAlgorithm;

public class HillClimbing extends OptimizationAlgorithm {

	protected double k; //nº de vecinos

	@Override
	public void search() {
		// TODO Auto-generated method stub

		// Algorithms must call this function always!
		initSearch();

		System.out.println("problem.Random-->" + problem.genRandomConfiguration());
		applyHillClimbing(problem.genRandomConfiguration());

		// Algorithms must call this function always!
		stopSearch();

	}

	@Override
	public void showAlgorithmStats() {
		// TODO Auto-generated method stub
		// muestra los resultados particulares de un algoritmo (generaciones,nº de
		// mutaciones, permutaciones, etc).
		System.out.println("nº de vecinos generados: "+k);

	}

	@Override
	public void setParams(String[] args) {
		// TODO Auto-generated method stub
		try {

			k = Double.parseDouble(args[0]);
			System.out.println("Using specified configuration: k = " + k);
		} catch (Exception ex) {

			k = 1;
			System.out.println("Using default configuration: k = " + k);
		}
	}

	public Configuration applyHillClimbing(Configuration initialSolution) {

		boolean improves;
		Configuration currentSol;

		currentSol = initialSolution.clone();
		evaluate(currentSol);
		improves = true;

		while (improves) {

			improves = false;

			for (Configuration neighbor : generateNeighbors(currentSol)) {

				double score = evaluate(neighbor);

				if (score < currentSol.score()) {
					currentSol = neighbor.clone();
					improves = true;
				}
			}
		}
		return currentSol;
	}

	public ArrayList<Configuration> generateNeighbors(Configuration configuration) {

		double min, max;
		double step;
		int[] params; // copia de array
		int x1, x2 = 0;
		int valor1, valor2;
		ArrayList<Configuration> neighbors = null;

		neighbors = new ArrayList<Configuration>();

		for (int i = 0; i < k; i++) { // 2,1,3,0;

			min = 0;// limite inferior=0
			max = problem.size(); // limite superior=4-1=3

			// step = k * (max - min); // 0.1*(3-0)= 0.3

			params = Arrays.copyOf(configuration.getValues(), problem.size());
			for (int ent : params) {
				System.out.println(ent);
			}

			Random generator = new Random();
			// calculamos 2 quesos random
			x1 = (int) Math.floor(Math.random() * max);
			System.out.println("x1=" + x1);

			x2 = (int) Math.floor(Math.random() * max);
			while (x1 == x2) {
				x2 = (int) Math.floor(Math.random() * max);
			}

			System.out.println("x2=" + x2);

			valor1 = params[x1];
			System.out.println("valor1=" + params[x1]);
			valor2 = params[x2];
			System.out.println("valor2=" + params[x2]);

			params[x1] = valor2;
			System.out.println("params[x1]=" + params[x1]);
			params[x2] = valor1;
			System.out.println("params[x2]=" + params[x2]);
			neighbors.add(new Configuration(params));

			// params[i] = (int) Math.min(configuration.getValues()[i] +
			// generator.nextDouble() * step, max);
			// neighbors.add(new Configuration(params));

			// params[i] = (int) Math.max(configuration.getValues()[i] -
			// generator.nextDouble() * step, min);
			// neighbors.add(new Configuration(params));

		}

		return neighbors;
	}

}

package algorithms;

import optimization.Configuration;
import optimization.OptimizationAlgorithm;

public class GeneticAlgorithm extends OptimizationAlgorithm {

	private int numSolutions = 1000;
	@Override
	public void search() {
		// TODO Auto-generated method stub
		
		// Algorithms must call this function always!
		initSearch();

		// Generates all the configurations.
		Configuration randomConfiguration;

		for (int nSolution = 0; nSolution < numSolutions; nSolution++) {
			// Generates a configuration.
			randomConfiguration = problem.genRandomConfiguration();
			// Evaluates it.
			evaluate(randomConfiguration);
			// SearchAlgorithm keeps track of the best solution evaluated so far,
			// therefore, it is not necessary to do it here.
		}

		// Algorithms must call this function always!
		stopSearch();

	}

	@Override
	public void showAlgorithmStats() {
		// TODO Auto-generated method stub
		//muestra los resultados particulares de un algoritmo (generaciones,nº de mutaciones, permutaciones, etc).
		

	}

	@Override
	public void setParams(String[] args) {
		// TODO Auto-generated method stub
		if (args.length>0){
			try{
				numSolutions = Integer.parseInt(args[0]);
			} 
			catch(Exception e){
				System.out.println("Generating 1000 random solutions (\"default\").");
			}
		}
	}

}

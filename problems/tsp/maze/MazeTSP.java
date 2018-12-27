package problems.tsp.maze;

import optimization.Configuration;
import problems.tsp.TSP;
import utils.Position;
import visualization.*;

/**
 * Extends the TSP to represent it in a maze where movements are either
 * horizontal or vertical, and uses manhattan as distance.
 */
public class MazeTSP extends TSP implements ProblemVisualizable {

	/** Constructors */
	public MazeTSP() {
		generateInstance(20, 10, 0);
	}

	public MazeTSP(int rangeXY, int numCities) {
		generateInstance(rangeXY, numCities, 0);
	}

	public MazeTSP(int rangeXY, int numCities, int seed) {
		generateInstance(rangeXY, numCities, seed);
	}

	/** Returns a view of the problem. */
	@Override
	public ProblemView getView() {
		MazeTSPView mazeView = new MazeTSPView(this, 600);
		return mazeView;
	}

	/**
	 * Calculates the score of a configuration as the sum of the path.
	 */
	@Override
	public double score(Configuration configuration) {
		// Dada una secuencia de ciudades, devuelve la longitud del camino.(IGUAL QUE
		// tsp.TSP).
		// distancia manhattan
		/**
		 * COMPLETAR
		 */
		
		int[] secuencia = configuration.getValues();
		System.out.println("EMPEZAMOS");
		for (int x = 0; x < secuencia.length; x++) {

			System.out.println("ciudad" + "[" + x + "]=" + secuencia[x]);

		}

		System.out.println(posCities);
		double dist_manh = 0d;
		double dist_manhHamster = 0d;
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
				pos_x2 = this.posExit.x;
				pos_y2 = this.posExit.y;
			}
			dist_manh = dist_manh + (Math.abs((pos_x - pos_x2)) + Math.abs(pos_y - pos_y2));

		}

		// ditancia de pos hamster al primer queso + todas las calculadas antes.
		pos_x2 = posCities.get(secuencia[0]).x;
		pos_y2 = posCities.get(secuencia[0]).y;
		dist_manh = dist_manh
				+ (Math.abs((this.posAgent.x - pos_x2)) + Math.abs((this.posAgent.y - pos_y2)));
		System.out.println("dist_manh------------------->" + dist_manh);
		return dist_manh;
	}

	/** Calculates the distance between two points. */
	private double dist(Position from, Position to) {
		return Math.abs(from.x - to.x) + Math.abs(from.y - to.y);
	}
}

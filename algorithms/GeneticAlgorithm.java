package algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import optimization.Configuration;
import optimization.OptimizationAlgorithm;

public class GeneticAlgorithm extends OptimizationAlgorithm {
	// ---Atributos--//
	private int TamañoDeLaPoblacion;
	private int generaciones = 0;
	private double probCruce = 0.9;
	private double probMutacion = 0.1;

	// ----ALGORITMO---//
	@Override
	public void search() {
		// TODO Auto-generated method stub

		Configuration[] poblacion;
		Configuration[] poblacionPrincipal;

		boolean stop = false;

		// Algorithms must call this function always!
		initSearch();

		// Generamos la poblacion
		poblacion = generarPoblacion();

		// evaluamos (actualizamos score).
		for (int i = 0; i < poblacion.length; i++) {
			evaluate(poblacion[i]);
		}
		// pseudocódigo
		while (!stop) {

			poblacionPrincipal = SeleccionPorTorneo(poblacion).clone(); // Seleccion
			cruce(poblacionPrincipal); // Cruce
			mutacion(poblacionPrincipal); // Mutacion
			for (int i = 0; i < poblacionPrincipal.length; i++) {
				evaluate(poblacionPrincipal[i]); // evaluacion
			}
			// Obtains the score of the new poblacion
			poblacion = combinacion(poblacion, poblacionPrincipal).clone(); // nueva generación.

			// Checks stop condition
			stop = criterioParadaGeneraciones(generaciones--);
		}

		// Algorithms must call this function always!
		stopSearch();

	}

	@Override
	public void showAlgorithmStats() {
		// TODO Auto-generated method stub
		// muestra los resultados particulares de un algoritmo (generaciones,nº de
		// mutaciones, permutaciones, etc).
		System.out.println("nº de generaciones: "+this.generaciones);
		
	}

	@Override
	public void setParams(String[] args) {
		// TODO Auto-generated method stub
		try {
			TamañoDeLaPoblacion = Integer.parseInt(args[0]);
			generaciones = Integer.parseInt(args[1]);
			probCruce = Double.parseDouble(args[2]);
			probMutacion = Double.parseDouble(args[3]);
		} catch (Exception ex) {
			TamañoDeLaPoblacion = 200;
			generaciones = 100;
			probCruce = 0.9;
			probMutacion = 0.1;
		}
	}

	// ----GENERAR POBLACIÓN ALEATORIA-------//
	private Configuration[] generarPoblacion() {

		Configuration[] poblacion;

		poblacion = new Configuration[TamañoDeLaPoblacion];

		for (int i = 0; i < TamañoDeLaPoblacion; i++) {
			poblacion[i] = problem.genRandomConfiguration();
		}
		System.out.println("pob-->" + poblacion);
		return poblacion;
	}

	// ------------SELECCION POR TORNEO------------------//
	private Configuration[] SeleccionPorTorneo(Configuration[] poblacion) {

		Random random;
		int S;

		Configuration[] poblacionSeleccionada;
		Configuration[] torneo;

		random = new Random();
		S = 2;

		poblacionSeleccionada = new Configuration[TamañoDeLaPoblacion];
		torneo = new Configuration[S];

		for (int i = 0; i < TamañoDeLaPoblacion; i++) {

			for (int j = 0; j < S; j++) {
				torneo[j] = poblacion[random.nextInt(TamañoDeLaPoblacion)];
			}
			poblacionSeleccionada[i] = Collections.min(Arrays.asList(torneo));
		}

		return poblacionSeleccionada;
	}

	private void cruce2(Configuration[] poblacion) { // cruce aritmético simple

		// Local variables
		Random random;

		int k;
		double alpha;

		// Initialization
		random = new Random();

		// Random value from 0(inclusive) to problem size(exclusive) for k
		k = random.nextInt(problem.size());

		// Alpha takes a random value from 0(inclusive) to 1(exclusive)
		alpha = random.nextDouble();

		// We apply crossover in consecutive pairs
		for (int i = 0; i < poblacion.length; i = i + 2) {

			// Children are initialize with the same values of the parents
			Configuration c1 = poblacion[i].clone();
			Configuration c2 = poblacion[i + 1].clone();

			// We go through as many parameters as k indicates
			for (int j = 0; j <= k; j++) { // k is generated as index level so, we need to select also the last index

				c1.getValues()[j] = (int) (alpha * poblacion[i].getValues()[j]
						+ (1 - alpha) * poblacion[i + 1].getValues()[j]);
				c2.getValues()[j] = (int) (alpha * poblacion[i + 1].getValues()[j]
						+ (1 - alpha) * poblacion[i].getValues()[j]);
			}

			// We change the parents
			poblacion[i] = c1.clone();
			poblacion[i + 1] = c2.clone();
		}
	}

	private void cruce(Configuration[] poblacion) {

		// Variables locales
		Random random;

		double alpha; 
		double max; 
		double min; 
		double l; 
		double probabilidadRandom;
		// Inicialización alpha
		random = new Random();
		alpha = random.nextDouble();

		// aplicamos cruce cada dos (por pares).
		for (int i = 0; i < poblacion.length; i = i + 2) {

			//generamos los dos hijos
			Configuration c1 = poblacion[i].clone();
			Configuration c2 = poblacion[i + 1].clone();

			for (int j = 0; j < problem.size(); j++) {

				// Initialization of minimum, maximum, l and alpha
				min = Math.min(poblacion[i].getValues()[j], poblacion[i + 1].getValues()[j]);
				max = Math.max(poblacion[i].getValues()[j], poblacion[i + 1].getValues()[j]);
				l = max - min;
				probabilidadRandom = random.nextDouble();
				if (probabilidadRandom < probCruce) {
					// cambiamos los valores de los hijos de acuerdo al nº random en [minimum -
					// l * alpha, maximum + l * alpha]
					c1.getValues()[j] = (int) ((min - l * alpha)
							+ ((max + l * alpha) - (min - l * alpha)) * random.nextDouble());
					c2.getValues()[j] = (int) ((min - l * alpha)
							+ ((max + l * alpha) - (min - l * alpha)) * random.nextDouble());
				}
			}
		}
	}

	private void mutacion(Configuration[] poblacion) {

		// We mutate at individual level
		for (int i = 0; i < poblacion.length; i++) {

			// Get a random double to show if mutation is going to be applied
			Random random = new Random();
			double probabilidadRandom = random.nextDouble();

			// Mutation must be applied
			if (probabilidadRandom < probMutacion) {
				Configuration pob = poblacion[i].clone();
				poblacion[i] = genMut(pob);
			}
		}
	}

	private Configuration genMut(Configuration poblacion) {

		double min, max;
		double step;
		int[] params; // copia de array
		int x1, x2 = 0;
		int valor1, valor2;

		min = 0;// limite inferior=0
		max = problem.size(); // limite superior=4-1=3

		params = Arrays.copyOf(poblacion.getValues(), problem.size());

		Random generator = new Random();
		// calculamos 2 quesos random
		x1 = (int) Math.floor(Math.random() * max);

		x2 = (int) Math.floor(Math.random() * max);
		while (x1 == x2) {
			x2 = (int) Math.floor(Math.random() * max);
		}

		valor1 = params[x1];
		valor2 = params[x2];

		params[x1] = valor2;
		params[x2] = valor1;

		return new Configuration(params);

	}

	private Configuration[] combinacion(Configuration[] poblacionOriginal, Configuration[] poblacionNueva) {

		return poblacionNueva;
	}

	private boolean criterioParadaGeneraciones(int generacion) {

		return generaciones == 0;
	}
}

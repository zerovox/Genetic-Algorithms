package com.zerovx.genetic.sudoku;

import java.util.Arrays;
import java.util.Random;

public class GridPopulation {
	private GeneticGrid[] population;
	private long generations;

	public GridPopulation(int[] start, int count) {
		population = new GeneticGrid[count];
		for (int i = 0; i < count; i++) {
			population[i] = GeneticGrid.random(start);
		}
		generations = 0;
		Arrays.sort(population);
	}

	private GridPopulation(GeneticGrid[] newPopulation, long generations) {
		population = newPopulation;
		this.generations = generations;
		Arrays.sort(population);
	}

	public GridPopulation nextAge(Double stickyness, Integer mutations, Double mateiness) {
		Random random = new Random();
		GeneticGrid[] newPopulation = new GeneticGrid[population.length];
		GeneticGrid current = population[0];
		int j = 0;
		for(int i = 0; i<newPopulation.length; i++){
			if(random.nextDouble() > mateiness && j >0)
				newPopulation[i] = current.mate(population[random.nextInt(j)]);
			else
				newPopulation[i] = current.multimutate(random.nextInt(mutations));
			if(j < (population.length-1) && random.nextDouble() > stickyness)
				j++;
			current = population[j];
		}			
		return new GridPopulation(newPopulation, generations + 1);
	}

	public String toString() {
		String out = "Generation " + generations + "\n";
		for (GeneticGrid g : population) {
			out += "Score : " + g.score() + " - ";
			for (int n : g.numbers) {
				out += n;
			}
			out += "\n";
		}
		return out;
	}
	
	public static GeneticGrid solution(GridPopulation gp){
		int distance = 81;
		while(!gp.solved()){
			gp = gp.nextAge(0.3, 4, 0.4);
			if(gp.population[0].score() < distance){
				distance = gp.population[0].score();
				System.out.println("Now at " + gp.population[0].score() + " after " + gp.generations + " generations.");
			}
		}
		return gp.population[0];
	}

	public boolean solved() {
		return population[0].score() == 0;
	}

}

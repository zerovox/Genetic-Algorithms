package com.zerovx.genetic.sudoku.tests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.zerovx.genetic.sudoku.GeneticGrid;
import com.zerovx.genetic.sudoku.GridPopulation;

public class GeneticGridTests {
	final int[] startPoint = fromString("400070200063052000000483017230700008600805002900004076840531000000240580007060003");

	@Test
	public void printTest() {
		GeneticGrid g;
		for(int i = 20; i>0; i--){
			g = GeneticGrid.random(startPoint);
			System.out.println(g.toString() + "Score: " + g.score()+"\n");
		}
	}
	
	private int[] fromString(String s){
		int[] a = new int[81];
		for(int i = 0; i<81; i++){
			a[i] = s.charAt(i) - 48;
		}
		return a;
	}
	
	@Test
	public void test(){
		ageTest();
	}
	
	public long ageTest(){
		GridPopulation gp = new GridPopulation(startPoint, 10);
		int start = (int) (System.currentTimeMillis()/1000);
		GeneticGrid solution = GridPopulation.solution(gp);
		System.out.print(solution);		
		long time = (System.currentTimeMillis()/1000) - start;
		System.out.println("Took " + time + " seconds");
		assertTrue(solution.score() == 0);
		return time;
	}

}

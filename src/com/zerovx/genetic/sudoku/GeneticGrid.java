package com.zerovx.genetic.sudoku;

import java.util.Arrays;
import java.util.Random;

public class GeneticGrid implements Comparable<GeneticGrid>{
	public final int[] numbers;
	public final boolean[] flags;
	private int fixed; 

	private GeneticGrid(int[] in, boolean[] fixedCells) {
		assert(in.length == 81);
		assert(fixedCells.length == 81);
		numbers = in;
		flags = fixedCells;
		for(boolean b : flags){
			if(b)
				fixed++;
		}
		assert(fixed<81);
	}
	
	public GeneticGrid mate(GeneticGrid partner){
		Random random = new Random();
		Arrays.equals(flags, partner.flags);
		int pivot = random.nextInt(81);
		int[] child = new int[81];
		System.arraycopy(numbers, 0, child, 0, pivot);
		System.arraycopy(partner.numbers, pivot, child, pivot, 81-pivot);
		return new GeneticGrid(child, flags);
	}
	
	public static GeneticGrid random(int[] in){
		Random random = new Random();
		boolean[] fixedCells = new boolean[81];
		int[] ins = new int[81];
		for(int i = 0; i<81; i++){
			if(in[i] == 0){
				fixedCells[i] = false;
				ins[i] = 1+random.nextInt(9);
			}else{
				ins[i] = in[i];
				fixedCells[i] = true;
			}
		}
		return new GeneticGrid(ins, fixedCells);
	}
	
	public GeneticGrid random(){
		Random random = new Random();
		int[] news = new int[81];
		for(int i = 0; i<81; i++){
			if(flags[i]){
				news[i] = numbers[i];
			}else{
				news[i] = 1+random.nextInt(9);
			}
		}
		return new GeneticGrid(news, flags);
	}
	
	public GeneticGrid mutate(double chance){
		Random random = new Random();
		if(random.nextDouble()>chance)
			return this;
		int flipbit = random.nextInt(81-fixed);
		int i = 0;
		while(flipbit > 0){
			if(!flags[i])
				flipbit--;
			i++;
		}
		int[] n = new int[81];
		System.arraycopy(numbers, 0, n, 0, 81);
		n[i] = random.nextInt(9) +1;
		return new GeneticGrid(n, flags);
	}
	
	public GeneticGrid multimutate(int mutations){
		Random random = new Random();
		int[] n = new int[numbers.length];
		System.arraycopy(numbers, 0, n, 0, numbers.length);
		while(mutations>0){
			int next = random.nextInt(81);
			if(!flags[next])
				n[next] = 1+random.nextInt(9);			
			mutations--;
		}
		return new GeneticGrid(n, flags);
	}

	public int score() {
		int score = 0;
		for (int i = 0; i < 9; i++) {
			score += duplicates(getRow(i));
			score += duplicates(getCol(i));
			score += duplicates(getBox(i));
		}
		return score;
	}

	private int duplicates(int[] ints) {
		assert(ints.length == 9);
		boolean[] counts = new boolean[9];
		for(int i = 0; i<9; i++){
			counts[i] = false;
		}
		
		int out = 0;
		for(int j : ints){
			if(counts[j-1])
				out++;
			else 
				counts[j-1] = true;
		}		
		
		return out;
	}

	private int[] getBox(int i) {
		assert (i < 9);
		int[] out = new int[9];
		int x = (3 * (i % 3));
		int y = (int) Math.floor(i/3.0)*27;
		System.arraycopy(numbers, y + x, out, 0, 3);
		System.arraycopy(numbers, y + 9 + x, out, 3, 3);
		System.arraycopy(numbers, y + 18 + x, out, 6, 3);
		return out;
	}

	private int[] getCol(int i) {
		assert (i < 9);
		int[] out = new int[9];
		for (int j = 0; j < 9; j++) {
			out[j] = numbers[i + 9*j];
		}
		return out;
	}

	private int[] getRow(int i) {
		assert (i < 9);
		int[] out = new int[9];
		System.arraycopy(numbers, i * 9, out, 0, 9);
		return out;
	}
	
	public String toString(){
		String out = "";
		for(int i = 0; i<9;i++){
			if(i == 3 || i == 6)
				out += "-----------\n";
			int[] row = getRow(i);
			for(int j = 0; j<9;j++){
				if(j == 3 || j == 6)
					out += "|";
				out += row[j];
			}
			out += "\n";
		}
		return out;
	}

	@Override
	public int compareTo(GeneticGrid o) {
		return this.score() - o.score();
	}

}

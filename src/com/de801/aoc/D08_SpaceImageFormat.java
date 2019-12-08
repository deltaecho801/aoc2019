package com.de801.aoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.de801.aoc.utils.DEFileReader;

public class D08_SpaceImageFormat {

	public static final int SIF_WIDTH = 25;
	public static final int SIF_HEIGHT = 6;
	
	public static void main(String[] args) {
		
		List<String> input = DEFileReader.getInputsFromFile("resources/d08_input");
		String image = input.get(0);
		
		ArrayList<int[][]> layers = processInput(image);
		
		ArrayList<Integer> digitMatch = new ArrayList<Integer>();
		for(int[][] layer : layers) {
			digitMatch.add(countDigits(layer, 0));
		}
		
		Integer min = Collections.min(digitMatch);
		System.out.println("min: " + min );
		
		int[][] matchingLayer = layers.get(digitMatch.indexOf(min));
		int result = countDigits(matchingLayer, 1) * countDigits(matchingLayer, 2);
		
		String layerPretty =  Arrays.deepToString(layers.get(digitMatch.indexOf(min)));
		layerPretty = layerPretty.replace("],", "]\r");

		System.out.println("Layer with least zeroes:");
		System.out.println(layerPretty);
		System.out.println("Result: " + result);
		
		int[][] blank = generateTransparentImage();
		for(int[][] layer : layers) {
			blank = overlayLayers(blank, layer);
		}
		String prettyImage =  Arrays.deepToString(blank);
		prettyImage = prettyImage.replace("],", "]\r").replace(", ","").replace("0", "  ").replace("1", "XX");

		System.out.println("Overlayed image:");
		System.out.println(prettyImage);
	}
	
	public static ArrayList<int[][]> processInput(String image) {
		ArrayList<int[][]> layers = new ArrayList<int[][]>();
		int[][] layer = new int[SIF_HEIGHT][SIF_WIDTH];
		
		int x = 0;
		int y = 0;
		for(Character c : image.toCharArray()) {
			layer[y][x] = Integer.valueOf(c.toString());
			x++;
			if(x == SIF_WIDTH) {
				y++;
				x = 0;
			}
			if(y == SIF_HEIGHT) {
				y = 0;
				x = 0;
				layers.add(layer);
				layer = new int[SIF_HEIGHT][SIF_WIDTH]; 
			}
		}
		
		return layers;
	}
	
	public static int[][] overlayLayers(int[][] layer1, int[][] layer2) {
		for(int y = 0; y < SIF_HEIGHT; y++) {
			for(int x = 0; x < SIF_WIDTH; x++) {
				if(layer1[y][x] == 2 && layer2[y][x] != 2)
					layer1[y][x] = layer2[y][x];
			}
		}
		return layer1;
	}
	
	public static int countDigits(int[][] layer, int digit) {
		int count = 0;
		for(int y = 0; y < SIF_HEIGHT; y++) {
			for(int x = 0; x < SIF_WIDTH; x++) {
				if(layer[y][x] == digit)
					count++;
			}
		}
		return count;
	}
	
	public static int[][] generateTransparentImage() {
		int[][] blank = new int[SIF_HEIGHT][SIF_WIDTH];
		for(int y = 0; y < SIF_HEIGHT; y++) {
			for(int x = 0; x < SIF_WIDTH; x++) {
				blank[y][x] = 2;
			}
		}
		return blank;

	}
	
}

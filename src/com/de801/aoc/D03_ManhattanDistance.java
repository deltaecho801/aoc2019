package com.de801.aoc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.de801.aoc.utils.DEFileReader;

public class D03_ManhattanDistance {
	
	static class Coordinate {
		public int x = 0;
		public int y = 0;
		
		public Coordinate(){}
		
		public Coordinate(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public Coordinate(int x, int y, int steps) {
			this.x = x;
			this.y = y;
		}
		
		@Override public boolean equals(Object o) {
			if(o != null &&	getClass() == o.getClass()) {
				Coordinate p  = (Coordinate) o;
				return (p.x == this.x && p.y == this.y);
			} else
				return false;
		}
		
		@Override public String toString() {
			return "[" + x + "," + y + "]";
		}
	}
	
	static class WireRoute {
		
		public static final char UP 	= 'U';
		public static final char DOWN 	= 'D';
		public static final char LEFT 	= 'L';
		public static final char RIGHT 	= 'R';
		
		ArrayList<Coordinate> route = new ArrayList<Coordinate>();
		
		public WireRoute() {
			route.add(new Coordinate());
		}
		
		public void runDirection(char dir, int len) {
			if(len == 0) return;
			
			Coordinate curr = route.get(route.size() - 1);
			switch(dir) {
				case UP:
					route.add(new Coordinate(curr.x, curr.y + 1));
					break;
				case DOWN:
					route.add(new Coordinate(curr.x, curr.y - 1));
					break;
				case LEFT:
					route.add(new Coordinate(curr.x - 1, curr.y));
					break;
				case RIGHT:
					route.add(new Coordinate(curr.x + 1, curr.y));
					break;
			}
			runDirection(dir, len - 1);
		}
		
		public void processDirections(String dirs) {
			String[] dirArray = dirs.split(",");
			for(String d : dirArray) {
				runDirection(d.charAt(0), Integer.valueOf(d.substring(1)));
			}
		}
		
		@Override
		public String toString() {
			return route.toString();
		}
		
	}
	
	public static void main(String[] args) {

		//Tests
//		String[] routes = {"R75,D30,R83,U83,L12,D49,R71,U7,L72", "U62,R66,U55,R34,D71,R55,D58,R83"};
//		String[] routes = {"R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51", "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7"};
		
		//Final Inputs
		List<String> routes = DEFileReader.getInputsFromFile("resources/d03_input");

		ArrayList<WireRoute> wires = new ArrayList<WireRoute>();
		for(String route : routes) {
			WireRoute r = new WireRoute();
			r.processDirections(route);
//			System.out.println(r.route);
			wires.add(r);
		}
		
		//find collisions
		ArrayList<Coordinate> collisions = null;
		for(WireRoute r : wires) {
			if(collisions == null)
				collisions = new ArrayList<Coordinate>(r.route);
			else
				collisions.retainAll(r.route);
		}
		//nobody cares about the initial 0,0
		collisions.remove(0);
		
		
		int distance = Integer.MAX_VALUE;
		
		//get nearest collision
//		for(Coordinate c : collisions) {
//			System.out.println(c);
//			int d = (Math.abs(c.x) + Math.abs(c.y));
//			if(d < distance)
//				distance = d;
//		}
		
		//get shortest collision
		HashMap<Coordinate, Integer> collPoints = new HashMap<Coordinate, Integer>();
		for(Coordinate c : collisions) {
			int steps = 0;
			for(WireRoute r  : wires) {
				steps += r.route.indexOf(c);
			}
			collPoints.put(c,  steps);
			if(steps < distance)
				distance = steps;
		}
		
		System.out.println(collPoints);
		
		System.out.println(distance);
	}

	
	
}

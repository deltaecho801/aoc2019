package com.de801.aoc;

import java.util.ArrayList;
import java.util.List;

import com.de801.aoc.utils.DEFileReader;

public class D06_Rozy {
	
	public static ArrayList<String> orbitee = new ArrayList<String>();
	public static ArrayList<String> orbiter = new ArrayList<String>();
	public static ArrayList<String> youlist = new ArrayList<String>();
	public static ArrayList<String> sanlist = new ArrayList<String>();

	public static int findorbits(String s, ArrayList<String> routelist) {
	    if(orbiter.contains(s)) {
	        routelist.add(s);
	        return 1 + findorbits(orbitee.get(orbiter.indexOf(s)),routelist);
	    } else {
	        return 0;
	    }
	}
	public static void main(String[] args) {


		List<String> inputs = DEFileReader.getInputsFromFile("resources/d06_input");
		for(String line : inputs) {
			String[] pair = line.split("\\)"); //java wants to treat that as regex
		    orbitee.add(pair[0]);
		    orbiter.add(pair[1]);
		}

		findorbits("YOU",youlist);
		findorbits("SAN",sanlist);

		int common = 0;

		for (String i : youlist) {
		    if( sanlist.contains(i) )
		        common += 1;
		}

		System.out.println(youlist.size() + sanlist.size() - common - common-2);
		
	}

}

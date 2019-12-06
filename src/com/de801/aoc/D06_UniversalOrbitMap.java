package com.de801.aoc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.de801.aoc.utils.DEFileReader;

public class D06_UniversalOrbitMap {

	public class OrbitalNode {
		public String name;
		public OrbitalNode directOrbit;
		public HashSet<OrbitalNode> children = new HashSet<OrbitalNode>();

		public OrbitalNode(String name, OrbitalNode parent) {
			this.name = name;
			this.directOrbit = parent;
		}
		
		public void addChild(OrbitalNode o) {
			children.add(o);
		}
		
		public OrbitalNode getChildWithNode(OrbitalNode o) {
			for(OrbitalNode c : children) {
				if(c == o)
					return c;
				else {
					OrbitalNode n = c.getChildWithNode(o);
					if(n != null)
						return c;
				}
			}
			return null;
		}
		
		public int countOrbits() {
			if(directOrbit == null)
				return 0;
			else 
				return 1 + directOrbit.countOrbits();
		}
		
		public OrbitalNode[] getRouteHome() {
			ArrayList<OrbitalNode> route = new ArrayList<OrbitalNode>();
			route.add(this);
			if(directOrbit != null)
				route.addAll(Arrays.asList(directOrbit.getRouteHome()));
			return route.toArray(new OrbitalNode[]{});
		}
		
		@Override
		public String toString() {
			String parent = "";
			if(directOrbit != null)
				parent = directOrbit.name;
			return "[OrbitalNode: " + name + ", parents: " + parent + ", countOrbits: " + countOrbits() + "]";
		}
		
		@Override
		public boolean equals(Object o) {
			if(o != null &&	getClass() == o.getClass()) {
				OrbitalNode p  = (OrbitalNode) o;
				return (p.name == this.name);
			} else
				return false;

		}
	}

	public HashMap<String, OrbitalNode> orbitalTree = new HashMap<String, OrbitalNode>();
	
	public void populateOrbitalTree(List<String> orbitalMap) {
		for (String s : orbitalMap) {
			String[] pair = s.split("\\)");
			OrbitalNode parent = orbitalTree.get(pair[0]);

			if (parent == null) {
				parent = new OrbitalNode(pair[0], null);
				orbitalTree.put(pair[0], parent);
			}
			
			OrbitalNode n = orbitalTree.get(pair[1]);
			if(n == null) { 
				n = new OrbitalNode(pair[1], parent);
				orbitalTree.put(pair[1], n);
			} else {
				n.directOrbit = parent;
			}
			parent.addChild(n);
		}
	}
	
	public int stepsBetweenNodes(OrbitalNode node1, OrbitalNode node2) {
		
		if(node1 == node2)
			return 0;
		
		OrbitalNode jump = node1.directOrbit;
		
		if(jump == null)
			throw new RuntimeException("Nodes have no link");
		
		if(jump == node2) {
			return 1;
		}
		
		OrbitalNode scan = node1.getChildWithNode(node2);
		if(scan != null) {
			return 1 + stepsBetweenNodes(scan, node2);
		}
		
		return 1 + stepsBetweenNodes(jump, node2);
		
	}
	
	public int stepsBetweenNodes(String name1, String name2) {
		return stepsBetweenNodes(orbitalTree.get(name1), orbitalTree.get(name2));
	}
	
	public int stepsBetweenNodeOrbits(String name1, String name2) {
		return stepsBetweenNodes(orbitalTree.get(name1).directOrbit, orbitalTree.get(name2).directOrbit);
	}


	public static void main(String[] args) {

//		List<String> inputs = Arrays.asList(new String[]{"COM)B","B)C","C)D","D)E","E)F","B)G","G)H","D)I","E)J","J)K","K)L","K)YOU","I)SAN"});
		List<String> inputs = DEFileReader.getInputsFromFile("resources/d06_input");

		D06_UniversalOrbitMap map = new D06_UniversalOrbitMap();
		map.populateOrbitalTree(inputs);

		System.out.println("Set: " + map.orbitalTree.keySet().toString());
		System.out.println("Total nodes: " + map.orbitalTree.keySet().size());

		int totalOrbits = 0;
		for (String s : map.orbitalTree.keySet()) {
			totalOrbits += map.orbitalTree.get(s).countOrbits();
		}

		System.out.println("Total Orbits: " + totalOrbits);
		
		System.out.println("Steps between node orbits: " + map.stepsBetweenNodeOrbits("YOU", "SAN"));

	}
}

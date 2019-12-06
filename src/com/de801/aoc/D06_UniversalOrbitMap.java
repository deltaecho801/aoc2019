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
	
	public ArrayList<OrbitalNode> scanChildForRoute(OrbitalNode child, OrbitalNode dest, OrbitalNode parent) {
		System.out.println("scanChildForRoute(" + child + ", " + dest + ", " + parent);

		ArrayList<OrbitalNode> list = new ArrayList<OrbitalNode>();
		
		for(OrbitalNode c : child.children) {
			if(c == dest) {
				list.add(c);
				return list;
			} else if(c != parent) {
				ArrayList<OrbitalNode> n = scanChildForRoute(c, dest, null);
				if(n != null) {
					list.add(c);
					list.addAll(n);
					return list;
				}
			}
		}
		
		return null;
	}
	
	public ArrayList<OrbitalNode> findRoute(OrbitalNode target, OrbitalNode dest) {
		System.out.println("findRoute(" + target + ", " + dest);
		ArrayList<OrbitalNode> list = new ArrayList<OrbitalNode>();
		list.add(target);
		
		if(target == dest) {
			return list;
		} else if(target.directOrbit == dest) {
			list.add(target.directOrbit);
			return list;
		} else {
			ArrayList<OrbitalNode> childRoute = scanChildForRoute(target.directOrbit, dest, target);
			if(childRoute != null) {
				list.add(target.directOrbit);
				list.addAll(childRoute);
				return list;
			} else {
				return addLists(list, findRoute(target.directOrbit, dest));
			}
		}
	}
	
	//Oh my god all I want is to just concatenate lists
	public ArrayList<OrbitalNode> addLists(ArrayList<OrbitalNode> l1, ArrayList<OrbitalNode> l2) {
		l1.addAll(l2);
		return l1;
	}
	
	
	public int stepsBetweenNodes(OrbitalNode node1, OrbitalNode node2) {
		ArrayList<OrbitalNode> route = findRoute(node1, node2);
		//remove the start node
		route.remove(node1);
		System.out.println(route);
		return route.size();
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

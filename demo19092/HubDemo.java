package demo19092;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.*;


import base.Highway;
import base.Hub;
import base.Location;
import base.Truck;

class HubDemo extends Hub {
	private ArrayList<Truck> trucks;
	private Truck curr_truck;
	public HubDemo(Location loc) {
		super(loc);
		trucks = new ArrayList<Truck>();
	}

	@Override
	public synchronized boolean add(Truck truck) {
		if(trucks.size() < getCapacity())
		{
			trucks.add(truck);
			return true;
		}
		return false;
	}

	@Override
	public synchronized void remove(Truck truck) {
		trucks.remove(truck);
	}

	@Override
	public Highway getNextHighway(Hub from, Hub dest) {
		int minimum_dis = Integer.MAX_VALUE;
		Highway next_highway = null;
		for(Highway hiwy : from.getHighways())
		{
			int distance = isreachable(from,dest,hiwy);
			if(distance<minimum_dis && !hiwy.getEnd().equals(curr_truck.getLastHub()) && distance>0)
			{
				minimum_dis = distance;
				next_highway = hiwy;
			}
		}
		return next_highway;
	}


	//Calculates the shortest path if possible using bfs
	private static int isreachable(Hub s, Hub d , Highway hwy)
	{
		LinkedList<Highway> queue =  new LinkedList<Highway>();
		Set<Highway> visited = new HashSet<Highway>();
		Map<Highway,Integer> distances = new HashMap<>();
		Highway next = null;

		distances.put(hwy,1);
		visited.add(hwy);
		queue.add(hwy);
		while(queue.size() != 0)
		{
			next = queue.remove();
			Hub n = next.getEnd();
			if(next.getEnd().equals(d))
			{
				return distances.get(next);
			}
			for(int i=0;i<n.getHighways().size();i++)
			{
				if(!visited.contains(n.getHighways().get(i)))
				{
					queue.add(n.getHighways().get(i));
					distances.put(n.getHighways().get(i),distances.get(next)+1);
					visited.add(hwy);
				}
			}
		}
		return -1;
	}

	@Override
	protected synchronized void processQ(int deltaT) {
		ArrayList<Truck> rem_trucks = new ArrayList<Truck>();
		for(Truck truc : trucks)
		{
			curr_truck = truc;
			Hub destination = NetworkDemo.getNearestHub(truc.getDest());
			Highway next = getNextHighway(this, destination);
			if(next != null)
			{
				if(next.add(truc))
				{
					// System.out.println(truc.getTruckName() + " added to highway.");
					truc.enter(next);
					rem_trucks.add(truc);
				}
			}
			else
			{
				// System.out.println(truc.getTruckName() + " highway not found.");
			}
		}
		for(Truck truc : rem_trucks)
		{
			this.remove(truc);
			// System.out.println(truc.getTruckName() + " removed from hub-"+ hubs.indexOf(this) + " .Hub capacity now: " + this.trucks.size());
		}
	}
}

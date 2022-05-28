package demo19092;
import java.util.*;

import base.Highway;
import base.Truck;

class HighwayDemo extends Highway {
	private ArrayList<Truck> trucksQueue = new ArrayList<Truck>();

	@Override
	public boolean hasCapacity() {
		if(trucksQueue.size() < getCapacity()){return true;}
		return false;
	}

	@Override
	public synchronized boolean add(Truck truck) {
		if(hasCapacity())
		{
			trucksQueue.add(truck);
			return true;
		}
		return false;
	}

	@Override
	public synchronized void remove(Truck truck) {
		trucksQueue.remove(truck);
	}

}

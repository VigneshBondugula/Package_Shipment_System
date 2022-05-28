package demo19092;


import base.Highway;
import base.Hub;
import base.Truck;
import base.Location;
import base.Network;

class TruckDemo extends Truck {
	private Highway curr_highway = null;
	private Hub prev_hub = null;
	private static int time=0;
	private static int count = 0;
	private boolean reached = false;
	private int id;
	TruckDemo(){
		count++;
		id = count;
	}
	@Override
	protected synchronized void update(int deltaT) {
		time += deltaT;
		if(time<this.getStartTime())
		{
			return;
		}
		if(getLoc().getX() == getSource().getX() && getLoc().getY() == getSource().getY())
		{
			// System.out.println("add");
			Hub start_hub = NetworkDemo.getNearestHub(getSource());
			if(start_hub.add(this))
			{
				setLoc(start_hub.getLoc());
			}
			return;
		}
		if(curr_highway != null && !reached)
		{
			Location start = getLastHub().getLoc();
			Location end = curr_highway.getEnd().getLoc();
			int dx = end.getX() - start.getX();
			int dy = end.getY() - start.getY();
			double cos = dx/Math.sqrt(getLoc().distSqrd(curr_highway.getEnd().getLoc()));
			double sin = dy/Math.sqrt(getLoc().distSqrd(curr_highway.getEnd().getLoc()));
			double dist = curr_highway.getMaxSpeed()*Double.valueOf(deltaT/200);
			int x = (int)Math.round(dist * cos);
			int y = (int)Math.round(dist * sin);
			int x_new = getLoc().getX() + x;
			int y_new = getLoc().getY() + y;
			Location new_loc = new Location(x_new,y_new);
			if(new_loc.distSqrd(start) >= end.distSqrd(start))
			{
				Hub dest_hub = Network.getNearestHub(getDest());
				if(dest_hub.equals(curr_highway.getEnd()))
				{
					setLoc(getDest());
					this.reached = true;
					curr_highway.remove(this);
					curr_highway = null;
					System.out.println(this.getTruckName() + " has reached to " + getDest());
					return;
				}
				setLoc(end);
				if(curr_highway.getEnd().add(this))
				{
					curr_highway.remove(this);
					curr_highway = null;
					// System.out.println(this.getTruckName() + " added to hub.");
					return;	
				}
				// System.out.println(this.getTruckName() + " waiting on highway since hub is full.");
				return;
			} 
			// System.out.println(this.getTruckName() + this.getLoc());
			setLoc(new_loc);
		}
		

	}

	@Override
	public synchronized void enter(Highway hwy) {
		prev_hub = hwy.getStart();
		curr_highway = hwy;
	}

	@Override
	public Hub getLastHub() {
		return prev_hub;
	}
	@Override
	public String getTruckName() {
		return "Truck19092_" + id;
	}
}
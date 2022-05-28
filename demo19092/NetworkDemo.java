package demo19092;
import base.*;
import java.util.ArrayList;;
public class NetworkDemo extends Network{
	private ArrayList<Truck> trucks= new ArrayList<Truck>();
	private ArrayList<Highway> hwys= new ArrayList<Highway>();
	private ArrayList<Hub> hubs= new ArrayList<Hub>();
    @Override
    public void redisplay(Display disp) {
        for(int i=0;i<trucks.size();i++)
        {
            trucks.get(i).draw(disp);
        }
        for(int i=0;i<hwys.size();i++)
        {
            hwys.get(i).draw(disp);
        }
        for(int i=0;i<hubs.size();i++)
        {
            hubs.get(i).draw(disp);
        }
    }

    @Override
    public void start() {
        for(int i=0;i<hubs.size();i++)
        {
            hubs.get(i).start();
        }
        for(int i=0;i<trucks.size();i++)
        {
            trucks.get(i).start();
        }
    }

    @Override
    protected Hub findNearestHubForLoc(Location loc) {
        Hub nearest_hub = hubs.get(0);
        for(int i=1;i<hubs.size();i++)
        {
            if(loc.distSqrd(nearest_hub.getLoc()) > loc.distSqrd(hubs.get(i).getLoc()))
            {
                nearest_hub = hubs.get(i);
            }
        }
        return nearest_hub;
    }

    @Override
    public void add(Truck truck) {
        trucks.add(truck);
    }

    @Override
    public void add(Highway hwy) {
        // hwy.getStart().add(hwy);
        hwys.add(hwy);
    }

    @Override
    public void add(Hub hub) {
        hubs.add(hub);
    }    
}
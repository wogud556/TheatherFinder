package kr.ac.hansung.thetherfinder;

public class Theather {
    private String Location;
    private double Lng;
    private double Lat;
    public Theather(){}
    public Theather(String Location, double Lng, double Lat){
        this.Location = Location;
        this.Lng = Lng;
        this.Lat = Lat;
    }
    public String getLocation(){
        return Location;
    }
    public void setLocation(String Location){
        this.Location= Location;
    }
    public double getLnt(){
        return Lng;
    }
    public void setLnt(double Lng){
        this.Lng= Lng;
    }
    public double getLat(){
        return Lat;
    }
    public void setLat(double Lat){
        this.Lat= Lat;
    }

}

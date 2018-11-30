package kr.ac.hansung.thetherfinder;

public class Theather {
    private String Location;
    private double Lnt;
    private double Lat;

    public Theather(String Location, double Lnt, double Lat){
        this.Location = Location;
        this.Lnt = Lnt;
        this.Lat = Lat;
    }
    public String getLocation(){
        return Location;
    }
    public void setLocation(String Location){
        this.Location= Location;
    }
    public double getLnt(){
        return Lnt;
    }
    public void setLnt(double Lnt){
        this.Lnt= Lnt;
    }
    public double getLat(){
        return Lat;
    }
    public void setLat(double Lat){
        this.Lat= Lat;
    }

    @Override
    public String toString() {
        return "Lnt = " + Lnt +  ", Lat=" + Lat + ", Location = " + Location;
    }
}

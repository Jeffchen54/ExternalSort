public class Pair {
    private double key;
    private long ID;
    
    public Pair(long id, double key) {
        this.ID = id;
        this.key = key;
    }
    
    
    
    public long getId() {
        return ID;
    }
    
    public double getKey() {
        return key;
    }
}
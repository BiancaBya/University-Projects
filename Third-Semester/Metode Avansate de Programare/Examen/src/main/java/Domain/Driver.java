package Domain;

public class Driver extends Entity<Integer>{

    private String name;

    public Driver(Integer integer, String name) {
        super(integer);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


public class ModelClient
{
    private String name1;
    private int amount1;
    private long id1;

    public long getId1() {
        return id1;
    }

    public void setId1(long id1) {
        this.id1 = id1;
    }

    public ModelClient() {}

    public ModelClient(String Name1, int amount1, long id1) {
        this.name1 = Name1;
        this.amount1 = amount1;
        this.id1 = id1;
    }

    public String toString() {
        return " Name: " + name1 + " | Amount: " + amount1;
    }

    public void setamount1(int s)
    {
        this.amount1 = s;
    }

    public void setName1(String name) {
        this.name1 = name1;
    }

    public String getName1() {
        return name1;
    }

    public int getamount1() {
        return amount1;
    }
}

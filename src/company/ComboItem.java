package company;

public class ComboItem {
    private Integer id;
    private String name;

    public ComboItem(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}

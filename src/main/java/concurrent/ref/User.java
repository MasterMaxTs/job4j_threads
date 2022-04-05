package concurrent.ref;

public class User {

    private String name;
    private int id;

    public static User of(String name) {
        User user = new User();
        user.name = name;
        return user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

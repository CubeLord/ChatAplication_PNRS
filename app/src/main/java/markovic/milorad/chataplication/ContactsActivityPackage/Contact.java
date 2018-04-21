package markovic.milorad.chataplication.ContactsActivityPackage;

public class Contact {
    String name;
    String firstName;
    String LastName;
    int id;

    public Contact(String name, String firstName, String lastName, int id) {
        this.name = name;
        this.firstName = firstName;
        LastName = lastName;
        this.id = id;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {

        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

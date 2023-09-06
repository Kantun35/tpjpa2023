package domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@NamedQuery(name = "findDptByID",query = "SELECT dpt FROM Department dpt WHERE dpt.ID = :id")
public class Department {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @OneToMany(mappedBy = "department", cascade = CascadeType.PERSIST)
    private List<Employee> employees = new ArrayList<Employee>();

    public Department(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    @Override
    public String toString() {
        return "Department [id=" + this.getId() + ", name=" + this.getName() + "]\n";
    }
}

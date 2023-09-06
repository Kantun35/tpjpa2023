package domain;

import jakarta.persistence.*;

@Entity(name = "Employee") // pas nécessaire de renommer si tu veux garder le nom
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type_employee")
@DiscriminatorValue("EMP")
public class Employee {
    @GeneratedValue
    @Column(name = "id")
    @Id
    private Long id;

    @Column(name = "name")
    private String name;


    @ManyToOne(cascade = CascadeType.PERSIST) //
    private Department department;


    public Employee() {
    }

    public Employee(String name, Department department) {
        this.name = name;
        this.department = department;
    }

    public Employee(String name) {
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

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return "Employee [id=" + id + ", name=" + name + ", department="
                + department.getName() + "]";
    }

}

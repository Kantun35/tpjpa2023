package jpa;


import domain.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class JpaTest {


	private EntityManager manager;

	public JpaTest(EntityManager manager) {
		this.manager = manager;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EntityManager manager = EntityManagerHelper.getEntityManager();

		JpaTest test = new JpaTest(manager);

		EmployeeDAOImpl managerEmployee = new EmployeeDAOImpl(manager);
		AdministrateurDAOImpl managerAdmin = new AdministrateurDAOImpl(manager);
		DepartmentDAOImpl managerDPT = new DepartmentDAOImpl(manager);


		try {
			test.createEmployees();

			//Department dpt1 = new Department("Douplard");

			Department dpt1 = managerDPT.save(new Department("Douplard"));
			managerAdmin.save(new Administrateur("Parry Hotter",dpt1,"Expresso Patron Homme"));

			System.out.println("----------EMPLOYEE TEST----------");
			System.out.println(managerEmployee.getAll());
			System.out.println("----------ADMIN TEST----------");
			System.out.println(managerAdmin.getAll());
			System.out.println("----------DPT TEST----------");
			System.out.println(managerDPT.getAll());
			// TODO create and persist entity
		} catch (Exception e) {
			e.printStackTrace();
		}


			
   	 manager.close();
		EntityManagerHelper.closeEntityManagerFactory();
		System.out.println(".. done");
	}

	private void createEmployees() {
		EntityTransaction tx = manager.getTransaction();
		tx.begin();
		int numOfEmployees = manager.createQuery("Select a From Employee a", Employee.class).getResultList().size();

		Department dpt1 = new Department("java");
		Department dpt2 = new Department("NIOS2");
		Department dpt3 = new Department("NAZE");
		manager.persist(dpt1);
		manager.persist(dpt2);
		manager.persist(dpt3);

		manager.persist(new Employee("Jordan", dpt1));
		manager.persist(new Employee("Kantun", dpt1));

		manager.persist(new Employee("Kevin", dpt2));
		manager.persist(new Employee("Titouan", dpt2));

		manager.persist(new Employee("Cunégonde", dpt3));

		manager.persist(new Administrateur("Bob le stagiaire",dpt3,"azerty1234"));

		System.out.println(manager.createNamedQuery("findDptByID",Department.class).setParameter("id",1).getSingleResult().getName());

		tx.commit();
	}
}

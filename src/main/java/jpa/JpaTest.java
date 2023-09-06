package jpa;


import domain.Administrateur;
import domain.Department;
import domain.Employee;
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

		EntityTransaction tx = manager.getTransaction();
		tx.begin();
		try {
			test.createEmployees();
			// TODO create and persist entity
		} catch (Exception e) {
			e.printStackTrace();
		}
		tx.commit();

			
   	 manager.close();
		EntityManagerHelper.closeEntityManagerFactory();
		System.out.println(".. done");
	}

	private void createEmployees() {
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


	}
}

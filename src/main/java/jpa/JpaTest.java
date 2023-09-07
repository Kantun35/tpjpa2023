package jpa;


import domain.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.Date;

public class JpaTest {


	private EntityManager manager;
	private UtilisateurDAOImpl managerUser;
	private PatientDAOImpl managerPatient;
	private PracticienDAOImpl managerPracticien;
	private KineDAOImpl managerKine;
	private ChirurgienDAOImpl managerChirurgien;

	private EmployeeDAOImpl managerEmployee;

	private AdministrateurDAOImpl managerAdmin;

	private DepartmentDAOImpl managerDPT;


	public JpaTest(EntityManager manager, UtilisateurDAOImpl managerUser, PatientDAOImpl managerPatient, PracticienDAOImpl managerPracticien, KineDAOImpl managerKine, ChirurgienDAOImpl managerChirurgien) {
		this.manager = manager;
		this.managerUser = managerUser;
		this.managerPatient = managerPatient;
		this.managerPracticien = managerPracticien;
		this.managerKine = managerKine;
		this.managerChirurgien = managerChirurgien;
	}

	public JpaTest(EntityManager manager, EmployeeDAOImpl managerEmployee, AdministrateurDAOImpl managerAdmin, DepartmentDAOImpl managerDPT) {
		this.manager = manager;
		this.managerEmployee = managerEmployee;
		this.managerAdmin = managerAdmin;
		this.managerDPT = managerDPT;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//versionPreProjet(); // à décommenter pour voir la version initiale (pas doctolib)
		versionDoctoLib();
	}

	private static void versionDoctoLib() {
		EntityManager manager = EntityManagerHelper.getEntityManager();

		//UtilisateurDAOImpl managerUser, PatientDAOImpl managerPatient, PracticienDAOImpl managerPracticien, KineDAOImpl managerKine, ChirurgienDAOImpl managerChirurgien

		UtilisateurDAOImpl managerUser = new UtilisateurDAOImpl(manager);
		PatientDAOImpl managerPatient = new PatientDAOImpl(manager);
		PracticienDAOImpl managerPracticien = new PracticienDAOImpl(manager);
		KineDAOImpl managerKine = new KineDAOImpl(manager);
		ChirurgienDAOImpl managerChirurgien = new ChirurgienDAOImpl(manager);

		JpaTest test = new JpaTest(manager,managerUser,managerPatient,managerPracticien,managerKine,managerChirurgien);

		try {
			test.creationJeuDeDonnees();
		} catch (Exception e) {
			e.printStackTrace();
		}



		manager.close();
		EntityManagerHelper.closeEntityManagerFactory();
		System.out.println(".. done");
	}

	private void creationJeuDeDonnees() {
		//EntityTransaction tx = manager.getTransaction();
		//tx.begin();
		try {

			managerUser.save(new Utilisateur("Bob Bill","9867475869"));
			managerUser.save(new Utilisateur("Bill Bob","8495837573"));

			managerPatient.save(new Patient("Tom Pit","8748576432","XXXXXXXXXXXXXX"));
			managerPatient.save(new Patient("Tim Pit","7594586948","YYYYYYYYYYYYYY"));

			managerPracticien.save(new Practicien("Jordan FONSECA LEITE DA SILVA","7598675423",new Date()));
			managerPracticien.save(new Practicien("Quentin BIGOT","3456739709",new Date()));

			managerChirurgien.save(new Chirurgien("Gaston","8674321230",new Date(),5));
			managerChirurgien.save(new Chirurgien("Pierre","8674321230",new Date(),11));

			managerKine.save(new Kine("Karen","8756449765",new Date(),1280));
			managerKine.save(new Kine("Paul","8798569765",new Date(),11));
			//tx.commit();

		} catch (Exception e) {
			System.out.flush();
			System.out.println("YOU FAILED !!!");
			System.out.println("------------------------\n");
			e.printStackTrace(System.out);
			System.out.println("------------------------\n");

			//tx.rollback();
		}
		//tx.commit();
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

	private static void versionPreProjet() {
		EntityManager manager = EntityManagerHelper.getEntityManager();

		EmployeeDAOImpl managerEmployee = new EmployeeDAOImpl(manager);
		AdministrateurDAOImpl managerAdmin = new AdministrateurDAOImpl(manager);
		DepartmentDAOImpl managerDPT = new DepartmentDAOImpl(manager);

		JpaTest test = new JpaTest(manager,managerEmployee,managerAdmin,managerDPT);


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
}

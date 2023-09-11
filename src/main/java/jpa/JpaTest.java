package jpa;


import DAO.DAOImpl.*;
import domain.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.Date;
import java.util.List;

public class JpaTest {

	//TODO Date SQL, daoIMPL update, refaire le depot


	private EntityManager manager;
	private UtilisateurDAOImpl managerUser;
	private PatientDAOImpl managerPatient;
	private PracticienDAOImpl managerPracticien;
	private KineDAOImpl managerKine;
	private ChirurgienDAOImpl managerChirurgien;

	private EmployeeDAOImpl managerEmployee;

	private AdministrateurDAOImpl managerAdmin;

	private DepartmentDAOImpl managerDPT;

	private RDVDAOImpl managerRDV;


	public JpaTest(EntityManager manager, UtilisateurDAOImpl managerUser, PatientDAOImpl managerPatient, PracticienDAOImpl managerPracticien, KineDAOImpl managerKine, ChirurgienDAOImpl managerChirurgien, RDVDAOImpl managerRDV) {
		this.manager = manager;
		this.managerUser = managerUser;
		this.managerPatient = managerPatient;
		this.managerPracticien = managerPracticien;
		this.managerKine = managerKine;
		this.managerChirurgien = managerChirurgien;
		this.managerRDV = managerRDV;
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
		RDVDAOImpl managerRDV = new RDVDAOImpl(manager);

		JpaTest test = new JpaTest(manager,managerUser,managerPatient,managerPracticien,managerKine,managerChirurgien,managerRDV);

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

			Patient pa1 = managerPatient.save(new Patient("Tom Pit","8748576432","XXXXXXXXXXXXXX"));
			Patient pa2 = managerPatient.save(new Patient("Tim Pit","7594586948","YYYYYYYYYYYYYY"));

			managerPracticien.save(new Practicien("Jordan FONSECA LEITE DA SILVA","7598675423",new Date()));
			managerPracticien.save(new Practicien("Quentin BIGOT","3456739709",new Date()));

			Chirurgien ch1 = managerChirurgien.save(new Chirurgien("Gaston","8674321230",new Date(),5));
			Chirurgien ch2 = managerChirurgien.save(new Chirurgien("Pierre","8674321230",new Date(),11));

			Kine k1 = managerKine.save(new Kine("Karen","8756449765",new Date(),1280));
			Kine k2 = managerKine.save(new Kine("Paul","8798569765",new Date(),11));

			List<Patient> patients = managerPatient.getAll();
			List<Practicien> practicien = managerPracticien.getAll();

			RDV saveRDV;

			RDV rdv1 = managerRDV.prendreUnRDV(pa1,pa1,ch1,new Date());

			//un user ne peut pas validé son propre rendez-vous
			saveRDV = rdv1.getCopy();
			rdv1 = managerRDV.validerUnRDV(pa1,rdv1);
			System.out.println("[VALID] nouvel état :" + rdv1.getEtat() + " précédent :" + saveRDV.getEtat() + " voulu : En attente de validation");

			//un rdv ne peut être modifié que par le practicien auquel il est lié
			saveRDV = rdv1.getCopy();
			rdv1 = managerRDV.validerUnRDV(pa2,rdv1);
			System.out.println("[VALID] nouvel état :" + rdv1.getEtat() + " précédent :" + saveRDV.getEtat() + " voulu : En attente de validation");

			saveRDV = rdv1.getCopy();
			rdv1 = managerRDV.validerUnRDV(ch1,rdv1);
			System.out.println("[VALID] nouvel état :" + rdv1.getEtat() + " précédent :" + saveRDV.getEtat() + " voulu : Validé");


			RDV rdv2 = managerRDV.prendreUnRDV(pa1,pa1,ch1,new Date());

			saveRDV = rdv2.getCopy();
			rdv2 = managerRDV.refuserUnRDV(ch1,rdv2);
			System.out.println("[REFUS] nouvel état :" + rdv2.getEtat() + " précédent :" + saveRDV.getEtat() + " voulu : Refusé");

			saveRDV = rdv2.getCopy();
			rdv2 = managerRDV.reporterUnRDV(ch1,rdv2,new Date());
			System.out.println("[REPORT] nouvel état :" + rdv2.getEtat() + " précédent :" + saveRDV.getEtat()+ " voulu : Refusé");

			RDV rdv3 = managerRDV.prendreUnRDV(pa1,pa2,ch1,new Date());
			if (rdv3 == null) {
				System.out.println("[RDV invalide voulu - invalide obtenu]");
			} else {
				System.out.println("[RDV invalide voulu - valide obtenu]");
			}

			RDV rdv4 = managerRDV.prendreUnRDV(pa2,pa2,ch2,new Date());
			if (rdv4 == null) {
				System.out.println("[RDV valide voulu - invalide obtenu]");
			} else {
				System.out.println("[RDV valide voulu - valide obtenu]");
			}

			RDV rdv5 = managerRDV.prendreUnRDV(pa1,pa1,k1,new Date());
			if (rdv5 == null) {
				System.out.println("[RDV valide voulu - invalide obtenu]");
			} else {
				System.out.println("[RDV valide voulu - valide obtenu]");
			}

			List<RDV> lRDV = managerRDV.getAllForValid();
			if(lRDV.size() == 2) {
				System.out.println("getAllForValid - valide (2:" + lRDV.size() +")");
			} else {
				System.out.println("getAllForValid - invalide (2:" + lRDV.size() +")");
			}

			lRDV = managerRDV.getAll();
			if(lRDV.size() == 4) {
				System.out.println("getAll - valide (4:" + lRDV.size() +")");
			} else {
				System.out.println("getAll - invalide (4:" + lRDV.size() +")");
			}

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

package pack;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;


/**
 * classe qui contient toutes les informations sur un stage<br>
 * s'indentifie par son code et sa date
 * @author BERON Jean-Sébastien
 *
 */
public class Stage implements Serializable /*,Cloneable*/ {

	private static final long serialVersionUID = -3775254007031121727L;

	private Long id;
	
	//attributs
	private String code;
	private Integer idx, idxMax;
	private String date;
	private String libelle;
	private int maxiPresent;
	private String leader;
	private String compagnie;
	private ArrayList<Module> moduleList;
	private ArrayList<Stagiaire> stagiaireList;
	private Stage coStage;
	
	/**
	 * constructeur par defaut
	 */
	public Stage(){
		moduleList = new ArrayList<Module>();
		stagiaireList = new ArrayList<Stagiaire>();
		this.idx = 0;
		this.idxMax = 0;
		this.setCoStage(null);
	}
	
	/**
	 * constructeur prenant en parametre un premier module
	 * @param unModule
	 */
	public Stage(Module unModule){
		id = unModule.getId();
		moduleList = new ArrayList<Module>();
		stagiaireList = new ArrayList<Stagiaire>();
		this.code = unModule.getCodeStage();
		this.date = unModule.getDate();
		this.leader = unModule.getNomLeader();
		this.compagnie = unModule.getCompagnie();
		unModule.setStage(this);
		moduleList.add(unModule);
		this.idx = 0;
		this.idxMax = 0;
		this.setCoStage(null);
		affectationInfoStage();
	}//fin Stage(Module unModule)
	
	/*
	public Stage clone() {
		Stage o = null;
		try {
			// On récupère l'instance à renvoyer par l'appel de la 
			// méthode super.clone()
			o = (Stage) super.clone();
		} catch(CloneNotSupportedException cnse) {
			// Ne devrait jamais arriver car nous implémentons 
			// l'interface Cloneable
			cnse.printStackTrace(System.err);
		}
		o.moduleList = (ArrayList<Module>)moduleList.clone();
		o.stagiaireList = (ArrayList<Stagiaire>)stagiaireList.clone();
		
		// on renvoie le clone
		return o;
	}
	*/
	
	public long getId() {
		return id;
	}
	
	public long getIdx() {
		return idx;
	}
	public long getIdxMax() {
		return idxMax;
	}
	public void setIdx(Integer idx, Integer idxM) {
		this.idx = idx;
		this.idxMax = idxM;
	}
	
	/**
	 * setter de code
	 * @param code
	 */
	public void setCode(String code) {
		this.code = code;
		for (Module m: moduleList) {
			m.setCodeStage(getCode());
		}
		affectationInfoStage();
	}
	/**
	 * getter de code
	 * @return
	 */
	public String getCodeI() {
		return code;
	}
	public String getCode() {
		String c = code;
		if (idxMax != null && idxMax > 1) {
			c += "-"+idx;
		}
		return c;
	}

	/**
	 * setter de date
	 * @param date
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * retour la date en format string
	 * @return
	 */
	public String getDateStr() {
		return date;
	}
	
	/**
	 * retourne la date sous le format date
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public Date getDateDt() {
		//recuperation de chaque élément de la date
		int year = Integer.parseInt(date.substring(6, 10))-1900;
		int mois = Integer.parseInt(date.substring(3, 5))-1;
		int day = Integer.parseInt(date.substring(0, 2));
		//formation de la date
		Date newdate = new Date(year,mois,day);
		//retour
		return newdate;
	}//fin getDateDt()
	
	/**
	 * setter de libelle
	 * @param libelle
	 */
	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	/**
	 * getter de libelle
	 * @return libelle
	 */
	public String getLibelle() {
		return libelle;
	}
	
	/**
	 * getter de maxiPresent
	 * @return maxiPresent
	 */
	public int getMaxiPresent(){
		return maxiPresent;
	}

	/**
	 * setter de leader
	 * @param leader
	 */
	public void setLeader(String leader) {
		this.leader = leader;
	}

	/**
	 * getter de leader
	 * @return leader
	 */
	public String getLeader() {
		return leader;
	}

	/**
	 * revoit le premier module du stage
	 * @return module
	 */
	public Module getFirstModule(){
		if (coStage != null) {
			return coStage.getFirstModule();
		}
		else {
			return moduleList.get(0);
		}
	}


	/**
	 * /**
	 * ajout d'un module dans la liste des modules
	 * @param leModule
	 */
	public void ajoutModule(Module leModule){
		moduleList.add(leModule);
		trierModuleList();
	}
	
	/**
	 * procedure triant les modules dans l'ordre de passage
	 */
	public void trierModuleList(){
		
		Module modTemps ;
		boolean good = false;
		//tant que le tri n'est pas bon
		while (! good) {
			good = true;
			for (int i = 0; i < moduleList.size()-1; i++) {
				if(moduleList.get(i).getnbMin() > moduleList.get(i+1).getnbMin()){
					good = false;
					//echange
					modTemps = moduleList.get(i);
					moduleList.set(i, moduleList.get(i+1));
					moduleList.set(i+1, modTemps);
				}//finsi
			}//finpour
		}//fin tant que
		
	}//fin trierModuleList()
	
	/**
	 * getter d'un element de moduleList a un index donné
	 * @param index
	 * @return module
	 */
	public Module getEltModuleList(int index){
		return moduleList.get(index);
	}
	
	/**
	 * getter de moduleList
	 * @return moduleList
	 */
	public ArrayList<Module> getModuleList(){
		return moduleList;
	}
	
	/**
	 * ajout d'un stagiaire dans la liste des stagiaire
	 * @param leStagiaire
	 */
	public void ajoutStagiaire(Stagiaire leStagiaire){
		stagiaireList.add(leStagiaire);
		trierStagiaireList();
	}
	
	/**
	 * suppression d'un stagiaire dans la liste des stagiaires
	 * @param index
	 */
	public void supprimerStagiaire(int index){
		stagiaireList.remove(index);
	}
	
	public void supprimerStagiaire(Stagiaire st){
		stagiaireList.remove(st);
	}
	
	public void supprimerStagiaire(ArrayList<Stagiaire> sl){
		stagiaireList.removeAll(sl);
	}

	/**
	 * procedure triant les stagiaire par ordre alphabetique
	 */
	public void trierStagiaireList(){
		Stagiaire stgTemps ;
		boolean good = false;
		//tant que le tri n'est pas bon
		while (! good) {
			good = true;
			for (int i = 0; i < stagiaireList.size()-1; i++) {
				if(stagiaireList.get(i).getNom().compareToIgnoreCase(stagiaireList.get(i+1).getNom()) > 0){
					good = false;
					//echange
					stgTemps = stagiaireList.get(i);
					stagiaireList.set(i, stagiaireList.get(i+1));
					stagiaireList.set(i+1, stgTemps);
				}//finsi
			}//finpour
		}//fin tant que
	}//fin trierStagiaireList()
	
	/**
	 * getter de stagiaireList
	 * @return stagiaireList
	 */
	public ArrayList<Stagiaire> getStagiaireList(){
		return stagiaireList;
	}
	
	/**
	 * getter d'un element de stagiaireList a un index donné
	 * @param index
	 * @return stage
	 */
	public Stagiaire getEltStagiaireList(int index){
		return stagiaireList.get(index);
	}
	
	/**
	 * retourne le nombre de stagiaire dans stagiaireList
	 * @return size
	 */
	public int getSizeStagiaireList(){
		return stagiaireList.size();
	}
	
	/**
	 * affecte un libelle au stage et egalement le nombre maximum de stagiaires
	 * les libelles sont dans le fichier dataSystem\libelleStage.txt
	 */
	private void affectationInfoStage(){
		String libelle = "Stage Sécurité Sauvetage";
		String[] info = new String[3];
		try {
			//lecture du fichier
			FileReader fichier = new FileReader("dataSystem\\libelleStage.txt");
			BufferedReader reader = new BufferedReader(fichier);
			String ligne;
			String chaine;
			int index;
			boolean good;
			good = false;
			//lecture de chaque ligne
			while ((ligne = reader.readLine()) != null){
				chaine = "";
				index = 0;
				for (int i = 0; i < ligne.length(); i++) {
					if(ligne.substring(i, i+1).equalsIgnoreCase("/")){
						info[index] = chaine;
						index++;
						chaine = "";
					}else{//sinon
						chaine = chaine + ligne.substring(i, i+1);
					}//fin si
				}//fin pour
				if(this.code.startsWith(info[0])){
					good = true;
					this.libelle = info[1];
					this.maxiPresent = Integer.parseInt(info[2]);
				}
			}//fin tant que
			if(! good){
				this.libelle = libelle;
				this.maxiPresent = 20;
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}//fin affectationLibelle()
	
	/**
	 * procedure qui renvoit un index <br/>
	 * index >= 0 si le module passé en parametre est deja dans la liste des modules de ce stage
	 * @param leModule
	 * @return exist
	 */
	public int exist(Module leModule){
		//revoit -1 si le module n'existe pas
		int exist = -1;
		for (int i = 0; i < moduleList.size(); i++) {
			if(moduleList.get(i).getHeureDebut().equalsIgnoreCase(leModule.getHeureDebut())
					&& moduleList.get(i).getHeureFin().equalsIgnoreCase(leModule.getHeureFin())){
						exist = i;
			}
		}
		return exist;
	}//fin exist()
	
	/**
	 * retourne le nombre de minute de l'heure de debut de stage a partir de minuit <br/>
	 * sert pour les comparaison de tri
	 * @return nbmin
	 */
	public int getnbMin(){
		//recuperation de l'heure de debut
		String heureDeb = getFirstModule().getHeureDebut();
		int nbmin;
		nbmin = Integer.parseInt(heureDeb.substring(0, 2))*60 + Integer.parseInt(heureDeb.substring(3, 5));
		//retour
		return nbmin;
	}//fin getnbmin()

	public String getCompagnie() {
		return compagnie;
	}

	public void setCompagnie(String compagnie) {
		this.compagnie = compagnie;
	}

	public Stage getCoStage() {
		return coStage;
	}

	public void setCoStage(Stage coStage) {
		this.coStage = coStage;
	}
	
}//fin class

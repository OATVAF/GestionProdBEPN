package pack;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;


/**
 * classe qui contient toutes les informations sur un stage<br>
 * s'indentifie par son code et sa date
 * @author BERON Jean-Sébastien
 *
 */
public class Stage implements Serializable, Comparable<Stage> /*,Cloneable*/ {

	private static final long serialVersionUID = -3775254007031121727L;

	private Long id;
	
	//attributs
	private String code;
	private Integer idx, idxMax;
	private String date;
	private Date dateD;
	private String libelle;
	private int maxiPresent;
	private String leader;
	private String compagnie;
	private ArrayList<Module> moduleList;
	private ArrayList<Stagiaire> stagiaireList;
	private Stage coStage;
	private ArrayList<Stage> coStageList;
	private Stage pnStage;
	
	private SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
	
	/**
	 * constructeur par defaut
	 */
	public Stage(){
		moduleList = new ArrayList<Module>();
		stagiaireList = new ArrayList<Stagiaire>();
		this.idx = 0;
		this.idxMax = 0;
		this.setCoStage(null);
		coStageList = new ArrayList<Stage>();
		
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
		try {
			this.dateD = fmt.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.leader = unModule.getNomLeader();
		this.compagnie = unModule.getCompagnie();
		unModule.setStage(this);
		moduleList.add(unModule);
		this.idx = 0;
		this.idxMax = 0;
		this.setCoStage(null);
		coStageList = new ArrayList<Stage>();
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
	 * getter de code stage (sans index)
	 * @return
	 */
	public String getCodeI() {
		return code;
	}
	/**
	 * getter de code stage (avec index)
	 * @return
	 */
	public String getCode() {
		String c = code;
		if (idxMax != null && idxMax > 1) {
			c += "-"+idx;
		}
		return c;
	}
	
	public String getSCode() {
		return code.replaceFirst(" .*", "");
	}
	/**
	 * setter de date
	 * @param date
	 */
	public void setDate(String date) {
		this.date = date;
		try {
			this.dateD = fmt.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	public Date getDateDt() {
		return dateD;
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
	 * renvoit le premier module du stage
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
	 * renvoit le dernier module du stage
	 * @return module
	 */
	public Module getLastModule(){
		if (coStage != null) {
			return coStage.getLastModule();
		}
		else {
			return moduleList.get(moduleList.size()-1);
		}
	}

	/**
	 * renvoit le module du stage à une heure donnée
	 * @return module
	 */
	public Module getModuleAtTime(String h){
		for (Module m : moduleList) {
			if (m.getHeureDebut().equals(h))
				return m;
		}
		return null;
	}
	
	/**
	 * renvoit le premier module du stage à une heure donnée
	 * @return module
	 */
	public Module getModuleAtTime(Module mod){
		for (Module m : moduleList) {
			if (m.getHeureDebut().equals(mod.getHeureDebut()))
				return m;
		}
		return null;
	}
	
	/**
	 * renvoit la liste de modules du stage à une heure donnée
	 * @return liste des modules
	 */
	
	public ArrayList<Module> getModulesAtTime(Module mod){
		ArrayList<Module> l= new ArrayList<Module>();
		for (Module m : moduleList) {
			if (m.getHeureDebut().equals(mod.getHeureDebut()))
				l.add(m); 
		}
		return l;
	}

	/**
	 * renvoit le module du stage avec un libellé donné
	 * @return module
	 */
	public Module getModuleWithLibelle(String str){
		for (Module m : moduleList) {
			if (m.getLibelle().equals(str))
				return m;
		}
		return null;
	}
	
	/**
	 * renvoit la liste de modules du stage avec un libellé donné
	 * @return liste des modules
	 */
	public ArrayList<Module> getModulesWithLibelle(String str){
		ArrayList<Module> l= new ArrayList<Module>();
		for (Module m : moduleList) {
			if (m.getLibelle().equals(str))
				l.add(m); 
		}
		return l;
	}

	/**
	 * /**
	 * ajout d'un module dans la liste des modules
	 * @param leModule
	 */
	public void ajoutModule(Module leModule){
		if (leModule.getStage() != this) {
			if (leModule.getStage() != null) {
				System.out.println(leModule+" a un stage : " + leModule.getStage());
			}
			else {
				leModule.setStage(this);
			}
		}
		moduleList.add(leModule);
		Collections.sort(moduleList); //, new ModuleStartComparator());
	}
	
	public void sortModules() {
		Collections.sort(moduleList);
	}
	
	/**
	 * suppression d'un stagiaire dans la liste des stagiaires
	 * @param index
	 */
	public void supprimerModule(int index){
		moduleList.remove(index);
	}
	
	public void supprimerModule(Module mo){
		moduleList.remove(mo);
	}
	
	public void supprimerModule(ArrayList<Module> ml){
		moduleList.removeAll(ml);
	}
	
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
		//trierStagiaireList();
		Collections.sort(stagiaireList);
	}
	
	public void sortStagiaires(){
		Collections.sort(stagiaireList);
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
		String cfg = "stage."+this.getSCode();
		if (Config.get(cfg+".info") != null) {
			this.libelle = Config.get(cfg+".info");
			this.maxiPresent = Config.getI(cfg+".max");
		}
		else {
			this.libelle = Config.get("stage.def.info");;
			this.maxiPresent = Config.getI("stage.def.max");
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

	public Stage getCoStageI(int  idx) {
		if (hasCoStage())
			if (idx == 1)
				if (isMainCoStage())
					return this;
				else
					return coStage;
			else
				if (isMainCoStage())
					return coStageList.get(idx-2);
				else
					return coStage.coStageList.get(idx-2);
		else
			return this;
	}

	public void setCoStage(Stage coStage) {
		this.coStage = coStage;
		if (coStage != null)
			coStage.coStageList.add(this);
	}
	
	public ArrayList<Stage> getCoStageList() {
		return this.coStageList;
	}

	/**
	 * @return true si le stage fait partie d'un goupe de co-stages 4S
	 */
	public boolean hasCoStage() {
		return ((coStage != null) || (coStageList.size() != 0)) ;
	}

	/**
	 * @return true si le stage est le n°1 des co-stages 4S
	 */
	public boolean isMainCoStage() {
		return (coStageList.size() != 0) ;
	}

	public Stage getPnStage() {
		return pnStage;
	}
	
	public boolean hasPnStage() {
		return (pnStage != null);
	}

	public void setPnStage(Stage s) {
		this.pnStage = s;
		s.pnStage=this;
		
		// liste tous les modules SMG/4S
		ArrayList <Module> mL = new ArrayList<Module>(moduleList);
		mL.addAll(s.moduleList);
		Collections.sort(mL);
		
		// groupe les co-modules et cherche les manques
		for (int i=0; i<mL.size()-1; i++) {
			Module m1 = mL.get(i);
			Module m2 = mL.get(i+1);
			if (m1.getLibelle().matches(Config.get("imp.pnc.smg_s2.modules_communs.pattern"))){
				System.out.println("==> Check module "+m1.getLibelle() + " at "+m1.getHeureDebut());				
				if (m1.getHeureDebut().equals(m2.getHeureDebut())
						/*&& m1.getLibelle().equals(m2.getLibelle())*/) {
					m1.setCoModule(m2);
					i++;
				}
				else {
					System.out.println("  ++ Ajout Module commun SMG/4S:"+m1+" => "+m1.getStage().pnStage);
					m1.getStage().pnStage.ajoutModule(m1);
				}
			}
		}
	}
	public int compareTo(Stage other) {
		return (this.toString().compareTo(other.toString()));
	}

	public String toString() {
		return getCode();
		//return compagnie + "." + code + "." + idx + "." + date;
	}

}//fin class

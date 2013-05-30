package pack;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.ws.BindingProvider;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.interceptor.transform.TransformInInterceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.cxf.endpoint.Client;

import mosws.AuthenticateUserReturn;
import mosws.Course;
import mosws.MosChorusLmsProfile;
import mosws.MosChorusLmsSearch;
import mosws.MosChorusWS;
import mosws.ReturnCourses;

/**
 * Config Enum : reads ini.properties to get conf.files
 */

public enum MosWS {
	
	INSTANCE;
	
	private MosChorusWS MosWS = new MosChorusWS();
	private MosChorusLmsProfile profileWS;
	private MosChorusLmsSearch searchWS;

	private Client profileClient;
	private Client searchClient;

	private MosWS() {
		System.out.println("[MosWS()] : Init !");

		profileWS = MosWS.getMosChorusLmsProfile();
		searchWS = MosWS.getMosChorusLmsSearch();
		profileClient = ClientProxy.getClient(profileWS);
		searchClient = ClientProxy.getClient(searchWS);

		
		Map<String, String> inTransformMap =  Collections.singletonMap("*", "{urn:MosWS}*");
		TransformInInterceptor transformInInterceptor =
		    new TransformInInterceptor();
		transformInInterceptor.setInTransformElements(inTransformMap);
		profileClient.getInInterceptors().add(transformInInterceptor);
		searchClient.getInInterceptors().add(transformInInterceptor);
		
		// Log
		/*
		LoggingInInterceptor logI = new LoggingInInterceptor();
		LoggingOutInterceptor logO = new LoggingOutInterceptor();
		logI.setPrettyLogging(true);
		logO.setPrettyLogging(true);
		searchClient.getOutInterceptors().add(logO);
		//c4.getOutInterceptors().add(logO);
		searchClient.getInInterceptors().add(logI);
		//c4.getInInterceptors().add(logI);
		*/
	}
	
	public static MosChorusWS getMosWS() {
		return INSTANCE.MosWS;
	}

	public static MosChorusLmsProfile getProfileWS() {
		return INSTANCE.profileWS;
	}

	public static MosChorusLmsSearch getSearchWS() {
		return INSTANCE.searchWS;
	}
	
	public static boolean MosAuth(String user) {
		AuthenticateUserReturn aur = INSTANCE.profileWS.authenticateUser(PF_crypt(user), PF_crypt(user),
				true, true, true, "", "");
		return aur.isIsAuthenticated();
	}
	
	public static String MosCourses(String user) {
		String str="";
		Map<String, Object> outProps = new HashMap<String, Object>();
		outProps.put("action", "UsernameToken"); 
		outProps.put("passwordType", "PasswordText"); 
		outProps.put("user", user);
		WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(outProps);
		if (INSTANCE.searchClient.getOutInterceptors().size() > 0) {
			INSTANCE.searchClient.getOutInterceptors().remove(0);
		}
		INSTANCE.searchClient.getOutInterceptors().add(wssOut);
		((BindingProvider) INSTANCE.searchWS).getRequestContext().put("password", user);

		ReturnCourses avail = INSTANCE.searchWS.availableCourses(user, "-120", "fr", null, null, "50", 
				null, null, null, null, null, "TVE01", null, null, null, false, false, null, true, null);
		List<Course>  cList = avail.getCourse();
		for (Course c : cList) {
					str += c.getGroupID() + ":" + (Float.parseFloat(c.getUserScore())*100);
		}
		return str;
	}
	
	private static String PF_crypt(String chaine) {
		StringEscapeUtils.escapeJavaScript(chaine);
		//chaine=escape(chaine);
	    String tabPc[]=chaine.split("%");
	    for (int i=0;i<tabPc.length;i++) {
	    	String tmp=tabPc[i];
	        if (tmp.length()>1) {
	        	String oReg="%"+tmp.substring(0,2);
	            chaine=chaine.replaceAll(oReg,"%"+tmp.substring(0,2).toLowerCase());
	        }
	    }
	    chaine=chaine.replaceAll("\\+",":PlS:");
	    chaine=chaine.replaceAll("%20","+");
	    String crypt=stringToHex(chaine);
	    crypt=crypt.replaceAll("30","R");
	    crypt=crypt.replaceAll("d6","T");
	    crypt=crypt.replaceAll("74/","Y");
	    crypt=crypt.replaceAll("f0/","I");
	    crypt=crypt.replaceAll("f5/","L");
	    crypt=crypt.replaceAll("776","H");
	    int lim=(int)Math.floor(crypt.length()/3);
	    if (lim>0) {
	        crypt=crypt.substring(lim)+crypt.substring(0,lim);
	    }
	    return crypt;
	}
	
	private static String stringToHex(String s) {
		String r = "";
		String hexes[] = {"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};
	    for (int i=0; i<s.length(); i++) {
	        r += hexes [s.charAt(i) >> 4] + hexes [s.charAt(i) & 0xf];
	    }
	    return r;
	}

}

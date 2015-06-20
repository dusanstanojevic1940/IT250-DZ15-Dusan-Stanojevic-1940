/**
 * 
 */
package com.dusanstanojevic.MetHotels.pages;

import java.io.IOException;

import net.smartam.leeloo.common.exception.OAuthProblemException;
import net.smartam.leeloo.common.exception.OAuthSystemException;

import org.apache.tapestry5.annotations.ActivationRequestParameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.dusanstanojevic.MetHotels.dao.GenericDAO;
import com.dusanstanojevic.MetHotels.dao.KorisnikDAO;
import com.dusanstanojevic.MetHotels.entities.Korisnik;
import com.dusanstanojevic.MetHotels.entities.Role;
import com.dusanstanojevic.MetHotels.services.FacebookService;
import com.dusanstanojevic.MetHotels.services.FacebookServiceInformation;
import com.dusanstanojevic.MetHotels.util.Util;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;

/**
 * @author dusanstanojevic
 *
 */
public class SignUp {
	@Property
	@Persist
	private String username;
	@Property
	@Persist
	private String password;
	@Inject
	private GenericDAO<Korisnik> korisnikgDAO;
	@Inject
	private KorisnikDAO korisnikDAO;
	@SessionState
	private Korisnik k;
	
	@CommitAfter
	private Object onSuccess() {
		Korisnik k = korisnikDAO.find(username, Util.getMD5Hash(password));
		if (k != null)
			korisnikgDAO.saveOrUpdate(k);
		k = korisnikDAO.find(username, Util.getMD5Hash(password));
		if (k != null)
			this.k = k;
		return Sobe.class;
	}
	
	 @Inject
	 private FacebookService facebookService;
	 @SessionState
	 @Property
	 private FacebookServiceInformation facebookServiceInformation;
	 @SessionState
	 @Property
	 private FacebookServiceInformation information;
	 @Property
	 private com.restfb.types.User userfb;
	 @Property
	 @ActivationRequestParameter
	 private String code;
	 Object onActivate() {
		 if (k.ime != null) {
			 return Sobe.class;
		 }
		 return null;
	 }
	 
	 public String getFacebookAuthentificationLink() throws OAuthSystemException {
		 return facebookService.getFacebookAuthentificationLink();
	 }

	 @CommitAfter
	 public boolean isLoggedInFb() {
		 if (facebookServiceInformation.getAccessToken() != null) {
			 Korisnik fbuser = new Korisnik();
			 fbuser.ime = userfb.getEmail();
			 fbuser.pass = "";
			 fbuser.role = Role.ADMIN;
			 Korisnik exist = null;

			 exist = korisnikDAO.checkIfFbExists(userfb.getId());

			 if (exist == null) {
				korisnikgDAO.saveOrUpdate(fbuser);
			 	k = fbuser;
			 	System.out.println("registruje");
			 } else {
			 	k = exist;
		 		System.out.println("postoji");
		 	}
		 }
		 return facebookServiceInformation.getAccessToken() != null;
	 }
	 
	 @SetupRender
	 public void setup() throws IOException, OAuthSystemException,
	 OAuthProblemException {
		 if (code != null) {
			 facebookService.getUserAccessToken(code, information.getAccessToken());
		 }
		 code = null;
		 FacebookClient facebookClient = new DefaultFacebookClient(information.getAccessToken());
		 if (information.isLoggedIn()) {
			 userfb = facebookClient.fetchObject("me", com.restfb.types.User.class);
		 }
	 }
}
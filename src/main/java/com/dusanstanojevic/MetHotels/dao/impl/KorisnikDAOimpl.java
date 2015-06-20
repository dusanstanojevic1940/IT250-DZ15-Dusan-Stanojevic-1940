package com.dusanstanojevic.MetHotels.dao.impl;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.dusanstanojevic.MetHotels.dao.KorisnikDAO;
import com.dusanstanojevic.MetHotels.entities.Korisnik;

public class KorisnikDAOimpl implements KorisnikDAO {
	@Inject
	private Session session;

	@Override
	public Korisnik find(String username, String password) {
		return (Korisnik) session.createCriteria(Korisnik.class).add(Restrictions.eq("ime", username)).add(Restrictions.eq("pass", password)).uniqueResult();
	}

	@Override
	public Korisnik checkIfFbExists(String fbId) {
		return (Korisnik) session.createCriteria(Korisnik.class).add(Restrictions.eq("facebookId", fbId)).uniqueResult();
	}

}
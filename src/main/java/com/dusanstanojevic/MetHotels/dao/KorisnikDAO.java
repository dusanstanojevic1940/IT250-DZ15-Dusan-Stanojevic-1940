package com.dusanstanojevic.MetHotels.dao;

import com.dusanstanojevic.MetHotels.entities.Korisnik;

public interface KorisnikDAO { 
	public Korisnik find(String username, String password);
	public Korisnik checkIfFbExists(String fbId);
}
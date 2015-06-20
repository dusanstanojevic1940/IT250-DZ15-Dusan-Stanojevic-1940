package com.dusanstanojevic.MetHotels.entities;

import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.tapestry5.beaneditor.Validate;

@Entity
@Table(name="korisnik")
public class Korisnik extends AbstractEntity {
	@Transient
	private static final long serialVersionUID = 1L;
	
	@Nonnull
	public Role role;
	
	@Validate("minlength=5")
	public String ime;
	
	public String pass;
	
	@Column(name = "FACEBOOK_ID", length=100)
	 private String facebookId;
}

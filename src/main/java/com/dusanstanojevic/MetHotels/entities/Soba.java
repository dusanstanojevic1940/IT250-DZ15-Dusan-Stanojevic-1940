package com.dusanstanojevic.MetHotels.entities;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.tapestry5.beaneditor.Validate;

@Entity
@Table(name="soba")
@XmlRootElement(name = "soba")
public class Soba extends AbstractEntity {
	@Transient
	private static final long serialVersionUID = 1L;

	@Validate("minlength=5")
	public String ime;
	
	public int sprat;
	
	public boolean tv;
	
	public boolean internet;
	
	public boolean djakuzi;
	
	@Override
	public String toString() {
		
		return super.toString();
	}
}

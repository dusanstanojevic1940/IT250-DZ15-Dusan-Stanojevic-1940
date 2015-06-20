package com.dusanstanojevic.MetHotels.pages;

import javax.annotation.security.RolesAllowed;

import com.dusanstanojevic.MetHotels.annotations.ProtectedPage;
import com.dusanstanojevic.MetHotels.components.GenericEditor;
import com.dusanstanojevic.MetHotels.entities.Soba;

/**
 * Start page of application MetHotels.
 */
@ProtectedPage
@RolesAllowed(value={"ADMIN"})
public class Sobe extends GenericEditor<Soba> {}

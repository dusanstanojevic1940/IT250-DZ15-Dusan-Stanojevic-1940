package com.dusanstanojevic.MetHotels.services;

import java.io.IOException;

import javax.annotation.security.RolesAllowed;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.ComponentRequestFilter;
import org.apache.tapestry5.services.ComponentRequestHandler;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;
import org.slf4j.Logger;

import com.dusanstanojevic.MetHotels.annotations.ProtectedPage;
import com.dusanstanojevic.MetHotels.entities.Korisnik;
import com.dusanstanojevic.MetHotels.entities.Role;
import com.dusanstanojevic.MetHotels.pages.Index;

public class PageProtectionFilter implements ComponentRequestFilter {

    private final PageRenderLinkSource pageRenderLinkSource;
    private final ComponentSource componentSource;
    private final Request request;
    private final Response response;
    private ApplicationStateManager sessionStateManager;
    
    public PageProtectionFilter(PageRenderLinkSource pageRenderLinkSource, ComponentSource componentSource,
            Request request, Response response, ApplicationStateManager asm, Logger logger) {
        this.pageRenderLinkSource = pageRenderLinkSource;
        this.request = request;
        this.response = response;
        this.componentSource = componentSource;
        this.sessionStateManager = asm;
    }

    @Override
    public void handlePageRender(PageRenderRequestParameters parameters, ComponentRequestHandler handler)
            throws IOException {

    	if (isAuthorisedToPage(parameters.getLogicalPageName(), parameters.getActivationContext())) {
    		handler.handlePageRender(parameters);
    	} else {
    		return;
    	}
    }

    @Override
    public void handleComponentEvent(ComponentEventRequestParameters parameters, ComponentRequestHandler handler)
            throws IOException {

    	if (isAuthorisedToPage(parameters.getActivePageName(), parameters.getPageActivationContext())) {
    		handler.handleComponentEvent(parameters);
    	} else {
    		return;
    	}
    }
    
    public boolean isAuthorisedToPage(String requestPageName, EventContext eventContext) throws IOException {
    	Component page = componentSource.getPage(requestPageName);
    	boolean protectedPage = page.getClass().getAnnotation(ProtectedPage.class)!=null;
    	RolesAllowed rolesAllowed = page.getClass().getAnnotation(RolesAllowed.class);
    	if (!protectedPage) {
    		return true;
    	} else if (request.isXHR() && request.getSession(false)==null) {
    		java.io.OutputStream os = response.getOutputStream("application/json;charset=UTF-8");
    		os.write("{}".getBytes());
    		os.flush();
    		return false;
    	} else if (isAuthenticated()) {
    		if(isAuthorised(rolesAllowed)) {
    			return true;
    		} else {
    			@SuppressWarnings("unused")
				Link pageProtectedLink = pageRenderLinkSource.createPageRenderLinkWithContext(Index.class, requestPageName);
    			return false;
    		}
    	} else {
    		Link loginPageLink = pageRenderLinkSource.createPageRenderLink(Index.class);
    		response.sendRedirect(loginPageLink);
    		return false;
    	}
    }
    
    private boolean isAuthenticated() throws IOException {
    	Korisnik user = sessionStateManager.getIfExists(Korisnik.class);
    	if (user!=null) {
    		for (Role r : Role.values())
    		if (user.role == r)
    			return true;
    	}
    	return false;
    }
    
    private boolean isAuthorised(RolesAllowed rolesAllowed) throws IOException {
    	boolean authorised = false;
    	if (rolesAllowed == null) {
    		authorised = true;
    	} else {
    		Korisnik user = sessionStateManager.getIfExists(Korisnik.class);
    		Role rola = user.role;
    		for (String i : rolesAllowed.value()) {
    			if (i.equalsIgnoreCase(rola.toString()))
    				authorised = true;
    		}
    	}
    	return authorised;
    }
}
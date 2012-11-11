package org.italiangrid.voms.clients.impl;

import org.glite.voms.contact.VOMSErrorMessage;
import org.glite.voms.contact.VOMSWarningMessage;
import org.italiangrid.voms.request.VOMSACRequest;
import org.italiangrid.voms.request.VOMSServerInfo;

public interface VOMSRequestListener {
	
	public void notifyStart(VOMSACRequest request, VOMSServerInfo si);
	public void notifySuccess(VOMSACRequest request, VOMSServerInfo endpoint);
	public void notifyFailure(VOMSACRequest request, VOMSServerInfo endpoint, Throwable error);
	public void notifyErrorsInReponse(VOMSErrorMessage[] errors);
	public void notifyWarningsInResponse(VOMSWarningMessage[] warnings);
	
}

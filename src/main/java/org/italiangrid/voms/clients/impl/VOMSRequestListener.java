package org.italiangrid.voms.clients.impl;

import org.italiangrid.voms.request.VOMSACRequest;
import org.italiangrid.voms.request.VOMSServerInfo;

public interface VOMSRequestListener {
	
	public void notifyUnknownVO(VOMSACRequest request);
	public void notifyStart(VOMSACRequest request, VOMSServerInfo si);
	public void notifySuccess(VOMSACRequest request, VOMSServerInfo endpoint);
	public void notifyFailure(VOMSACRequest request, VOMSServerInfo endpoint, Throwable error);
	
}

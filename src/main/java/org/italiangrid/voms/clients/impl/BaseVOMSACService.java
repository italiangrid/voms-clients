package org.italiangrid.voms.clients.impl;

import java.io.IOException;
import java.util.Set;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.x509.AttributeCertificate;
import org.glite.voms.contact.VOMSErrorMessage;
import org.italiangrid.voms.VOMSError;
import org.italiangrid.voms.request.VOMSACRequest;
import org.italiangrid.voms.request.VOMSACService;
import org.italiangrid.voms.request.VOMSResponse;
import org.italiangrid.voms.request.VOMSServerInfo;
import org.italiangrid.voms.request.VOMSServerInfoStore;
import org.italiangrid.voms.request.VOMSServerInfoStoreListener;
import org.italiangrid.voms.request.impl.DefaultVOMSServerInfoStore;
import org.italiangrid.voms.request.impl.LegacyProtocol;
import org.italiangrid.voms.request.impl.RESTProtocol;

import eu.emi.security.authn.x509.X509Credential;

public class BaseVOMSACService implements VOMSACService {

	private VOMSRequestListener requestListener;
	private VOMSServerInfoStoreListener serverInfoStoreListener;
	
	public BaseVOMSACService(VOMSRequestListener listener, VOMSServerInfoStoreListener serverInfoStoreListener) {
		this.requestListener = listener;
		this.serverInfoStoreListener = serverInfoStoreListener;
	}

	
	protected AttributeCertificate getACFromResponse(VOMSACRequest request, VOMSResponse response){
		byte[] acBytes = response.getAC();

		ASN1InputStream asn1InputStream = new ASN1InputStream(acBytes);

		AttributeCertificate attributeCertificate = null;

		try {

			attributeCertificate = AttributeCertificate
					.getInstance(asn1InputStream.readObject());

			asn1InputStream.close();
			return attributeCertificate;

		} catch (IOException e) {

			requestListener.notifyFailure(request, null, e);
			return null;
		}
	}
	
	
	protected VOMSResponse doRESTRequest(VOMSACRequest request, VOMSServerInfo serverInfo, X509Credential credential){
		
		RESTProtocol restProtocol = new RESTProtocol(serverInfo);
		return restProtocol.doRequest(credential, request);
		
	}
	
	protected VOMSResponse doLegacyRequest(VOMSACRequest request, VOMSServerInfo serverInfo, X509Credential credential){
		
		LegacyProtocol legacyProtocol = new LegacyProtocol(serverInfo);
		return legacyProtocol.doRequest(credential, request);
		
	}
	
	protected void handleErrorsInResponse(VOMSResponse response){
		
		if (response.hasErrors())
			requestListener.notifyErrorsInReponse(response.errorMessages());		
		
	}
	
	protected void handleWarningsInResponse(VOMSResponse response){
		if (response.hasWarnings())
			requestListener.notifyWarningsInResponse(response.warningMessages());
	}
	
	@Override
	public AttributeCertificate getVOMSAttributeCertificate(
			X509Credential credential, VOMSACRequest request) {

		Set<VOMSServerInfo> vomsServerInfos = getVOMSServerInfos(request);
		
		if (vomsServerInfos.isEmpty())
			throw new VOMSError("VOMS server for VO "+request.getVoName()+" is not known! Check your vomses configuration.");
		
		VOMSResponse response = null;

		for (VOMSServerInfo vomsServerInfo : vomsServerInfos) {

			requestListener.notifyStart(request,  vomsServerInfo);
			
			response = doRESTRequest(request, vomsServerInfo, credential);

			if (response == null)
				response = doLegacyRequest(request, vomsServerInfo, credential);

			if (response != null){
				requestListener.notifySuccess(request, vomsServerInfo);
				break;
			}
			
			requestListener.notifyFailure(request, vomsServerInfo, null);
		}

		if (response == null) {
			requestListener.notifyFailure(request, null, null);
			return null;
		}
		
		handleErrorsInResponse(response);
		handleWarningsInResponse(response);
		
		return getACFromResponse(request, response);
	}

	private Set<VOMSServerInfo> getVOMSServerInfos(VOMSACRequest request) {

		VOMSServerInfoStore vomsServerInfoStore = new DefaultVOMSServerInfoStore(serverInfoStoreListener);
		Set<VOMSServerInfo> vomsServerInfos = vomsServerInfoStore
				.getVOMSServerInfo(request.getVoName());

		return vomsServerInfos;
	}
}

package org.italiangrid.voms.clients.impl;

import java.io.IOException;
import java.util.Set;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.x509.AttributeCertificate;
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

	
	@Override
	public AttributeCertificate getVOMSAttributeCertificate(
			X509Credential credential, VOMSACRequest request) {

		Set<VOMSServerInfo> vomsServerInfos = getVOMSServerInfos(request);
		
		if (vomsServerInfos.isEmpty()){
			requestListener.notifyUnknownVO(request);
			return null;
		}
		
		
		VOMSResponse response = null;

		for (VOMSServerInfo vomsServerInfo : vomsServerInfos) {

			requestListener.notifyStart(request,  vomsServerInfo);
			
			RESTProtocol restProtocol = new RESTProtocol(vomsServerInfo);
			response = restProtocol.doRequest(credential, request);

			if (response == null) {

				LegacyProtocol legacyProtocol = new LegacyProtocol(
						vomsServerInfo);
				response = legacyProtocol.doRequest(credential, request);
			}

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

		byte[] acBytes = response.getAC();

		ASN1InputStream asn1InputStream = new ASN1InputStream(acBytes);

		AttributeCertificate attributeCertificate = null;

		try {

			attributeCertificate = AttributeCertificate
					.getInstance(asn1InputStream.readObject());

			asn1InputStream.close();

		} catch (IOException e) {

			requestListener.notifyFailure(request, null, e);
			return null;
		}
				
		return attributeCertificate;
	}

	private Set<VOMSServerInfo> getVOMSServerInfos(VOMSACRequest request) {

		VOMSServerInfoStore vomsServerInfoStore = new DefaultVOMSServerInfoStore(serverInfoStoreListener);
		Set<VOMSServerInfo> vomsServerInfos = vomsServerInfoStore
				.getVOMSServerInfo(request.getVoName());

		return vomsServerInfos;
	}
}

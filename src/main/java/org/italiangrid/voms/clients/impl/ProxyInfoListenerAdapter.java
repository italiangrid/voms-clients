package org.italiangrid.voms.clients.impl;

//public interface ProxyInfoListenerAdapter extends ValidationErrorListener,
//		ValidationResultListener, VOMSTrustStoreStatusListener {

public interface ProxyInfoListenerAdapter {

	public void notifyProxyNotFound();

	// public void logInfoMessage(String msg);

}

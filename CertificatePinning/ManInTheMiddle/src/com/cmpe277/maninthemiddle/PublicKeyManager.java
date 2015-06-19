package com.cmpe277.maninthemiddle;

import java.io.BufferedReader;
import java.io.InputStream;
import java.math.BigInteger;

import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import android.util.Log;

public final class PublicKeyManager implements X509TrustManager {

	InputStream inputStream = null;
	BufferedReader reader = null;
	X509Certificate ca;
	
	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {

		assert (chain != null);
		if (chain == null) {
			throw new IllegalArgumentException(
					"checkServerTrusted: X509Certificate array is null");
		}

		assert (chain.length > 0);
		if (!(chain.length > 0)) {
			throw new IllegalArgumentException(
					"checkServerTrusted: X509Certificate is empty");
		}

		Log.d ("newSslSocketFactory-chain.length %s", Integer.toString(chain.length));
		
		/*assert (null != authType && authType.equalsIgnoreCase("RSA"));
		if (!(null != authType && authType.equalsIgnoreCase("RSA"))) {
			throw new CertificateException(
					"checkServerTrusted: AuthType is not RSA");
		}*/

        try {
        	
        	/*
			// Get an instance of the Bouncy Castle KeyStore format
	        KeyStore myKeyStore = KeyStore.getInstance("BKS");
	        // Get the raw resource, which contains the keystore with
	        // your trusted certificates (root and any intermediate certs)
	        InputStream in = MainActivity.context.getResources().openRawResource(R.raw.keystore);
	        // Initialize the keystore with the provided trusted certificates
            // Also provide the password of the keystore
			Log.d ("newSslSocketFactory- called-Initialize keystore", "called");

			myKeyStore.load(in, "testing".toCharArray());
			
			TrustManagerFactory tmf;
			tmf = TrustManagerFactory.getInstance("X509");
			//tmf.init((KeyStore) null);
			tmf.init(myKeyStore);
			
			for (TrustManager trustManager : tmf.getTrustManagers()) {
				((X509TrustManager) trustManager).checkServerTrusted(
						chain, authType);
		   	 }
           }
			catch(Exception e){
	            throw new CertificateException("Certificate not trusted. It has expired",e);
	        }
            */
        
        	// Load CAs from an InputStream
        	// (could be from a resource or ByteArrayInputStream or ...)
        	CertificateFactory cf = CertificateFactory.getInstance("X.509");
        	
        	//InputStream caInput = MainActivity.context.getResources().openRawResource(R.raw.geotrustglobalca);
        	InputStream caInput = MainActivity.context.getResources().openRawResource(R.raw.mygooglecertfile);
        	
        	try {
        	    ca = (X509Certificate)cf.generateCertificate(caInput);
        	    System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
        	    
        	} finally {
        	    
        		caInput.close();
        	}

        	// Create a KeyStore containing our trusted CAs
        	String keyStoreType = KeyStore.getDefaultType();
        	KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        	keyStore.load(null, null);
        	keyStore.setCertificateEntry("ca", ca);

        	// Create a TrustManager that trusts the CAs in our KeyStore
        	String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        	TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        	tmf.init(keyStore);
        	
        	
        } catch (Exception e) {
            throw new AssertionError(e);
        }
        
        Log.d ("\n*********************","********************\n");

        String caencoded = new BigInteger(1, ca.getPublicKey().getEncoded()).toString(16);
        Log.d ("MY CA publicKey-", caencoded);
        System.out.println("MY CA subject =" + ((X509Certificate) ca).getSubjectDN());
        System.out.println("MY CA issuer =" + ((X509Certificate) ca).getIssuerDN());
        
        String chain0encoded = new BigInteger(1, chain[0].getPublicKey().getEncoded()).toString(16);
        Log.d ("Received Chain[0] publicKey-", chain0encoded);
        System.out.println("chain[0] subject =" + chain[0].getSubjectDN());
        System.out.println("CA issuer =" + chain[0].getIssuerDN());
        
        String chain1encoded = new BigInteger(1, chain[1].getPublicKey().getEncoded()).toString(16);
        Log.d ("Received Chain[1] publicKey-", chain1encoded);
        System.out.println("chain[1] subject =" + chain[1].getSubjectDN());
        System.out.println("CA issuer =" + chain[1].getIssuerDN());
        
        String chain2encoded = new BigInteger(1, chain[2].getPublicKey().getEncoded()).toString(16);
        Log.d ("Received Chain[2] publicKey-", chain2encoded);
        System.out.println("chain[2] subject =" + chain[2].getSubjectDN());
        System.out.println("CA issuer =" + chain[2].getIssuerDN());

        Log.d ("*********************","********************");

        RSAPublicKey chain0Pubkey = (RSAPublicKey) chain[0].getPublicKey();
		String chain0Encoded = new BigInteger(1, chain0Pubkey.getEncoded())
				.toString(16);
		
		RSAPublicKey caPubkey = (RSAPublicKey) ca.getPublicKey();
		String caEncoded = new BigInteger(1, caPubkey.getEncoded())
				.toString(16);
		
		// Compare public key of chain[0] and CA
        if(!chain[0].equals(ca)){
            try
            {   
                chain[0].verify(ca.getPublicKey());
            }
            catch(Exception e){   
                 throw new CertificateException("Certificate not trusted - Expected public key: " + caEncoded
							+ ", got public key:" + chain0Encoded); 
            }
        }
        
	}

	public void checkClientTrusted(X509Certificate[] xcs, String string) {

	}

	public X509Certificate[] getAcceptedIssuers() {

		return null;
	}
}

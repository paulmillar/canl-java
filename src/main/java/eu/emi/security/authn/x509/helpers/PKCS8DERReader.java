/*
 * Copyright (c) 2011 ICM Uniwersytet Warszawski All rights reserved.
 * See LICENCE file for licensing information.
 */
package eu.emi.security.authn.x509.helpers;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.InputStream;

import org.bouncycastle.openssl.PEMReader;
import org.bouncycastle.openssl.PasswordFinder;
import org.bouncycastle.util.io.Streams;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

/**
 * This class extends the {@link PEMReader} class from the BC library.
 * It is modified to read DER input, not the PEM (it can be considered a smart-hack)
 * as otherwise BC's parsers code would need to be copied. It supports reading of the
 * PKCS8 private key in DER form. It is assumed that the key is encrypted if 
 * a password is provided.
 * <p>
 * This class interface is the readObject method. 
 * <p>
 * This implementation overrides the 
 * {@link PemReader} readPemObject method to actually read the DER. The Reader used by
 * the {@link PemReader} is not used.
 * 
 * @author K. Benedyczak
 */
public class PKCS8DERReader extends PEMReader
{
	protected InputStream is;
	protected PasswordFinder myPFinder;
	
	public PKCS8DERReader(InputStream is, PasswordFinder pFinder)
	{
		super(new CharArrayReader(new char[0]), pFinder);
		this.is = is;
		this.myPFinder = pFinder;
	}

	public PKCS8DERReader(InputStream is)
	{
		super(new CharArrayReader(new char[0]), null);
		this.is = is;
		this.myPFinder = null;
	}

	/**
	 * Generate BC's PemObject from the input stream. The object's type is 
	 * fixed to encrypted or plain private key.
	 * @return the parsed PEM object
	 * @throws IOException
	 */
	@Override
	public PemObject readPemObject() throws IOException
	{
		byte []buf = Streams.readAll(is);
		
		String name = (myPFinder == null) ? "PRIVATE KEY" : "ENCRYPTED PRIVATE KEY";  
		return new PemObject(name, buf);
	}
}





/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.springframework.security.saml2;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.security.saml2.model.Saml2Object;
import org.springframework.security.saml2.model.Saml2SignableObject;
import org.springframework.security.saml2.model.key.Saml2KeyData;
import org.springframework.security.saml2.model.signature.Signature;
import org.springframework.security.saml2.model.signature.SignatureException;

public interface Saml2Transformer {

	/**
	 * Converts a SAML object into an XML string.
	 * If the object contains signing keys, the XML object will be signed prior to converting it
	 * to a string
	 *
	 * @param saml2Object - object to be converted to XML
	 * @return string representation of the XML object
	 */
	String toXml(Saml2Object saml2Object);

	/**
	 * Converts an SAML/XML string into a Java object
	 *
	 * @param xml              the XML representation of the object
	 * @param verificationKeys Nullable. If not null, object signature will be validated upon conversion.
	 *                         The implementation will attempt each key until one succeeds
	 * @param localKeys        the configured local private keys. Used for decryption when needed.
	 * @return the Java object that was
	 * @throws org.springframework.security.saml2.model.signature.SignatureException if signature validation
	 *                                                                              fails
	 * @throws IllegalArgumentException                                             if the XML object
	 *                                                                              structure
	 *                                                                              is not
	 *                                                                              recognized or implemeted
	 */
	default Saml2Object fromXml(String xml, List<Saml2KeyData> verificationKeys, List<Saml2KeyData> localKeys) {
		return fromXml(xml.getBytes(StandardCharsets.UTF_8), verificationKeys, localKeys);
	}

	/**
	 * Converts an SAML/XML string into a Java object
	 *
	 * @param xml              the XML representation of the object
	 * @param verificationKeys Nullable. If not null, object signature will be validated upon conversion.
	 *                         The implementation will attempt each key until one succeeds
	 * @param localKeys        the configured local private keys. Used for decryption when needed.
	 * @return the Java object that was
	 * @throws org.springframework.security.saml2.model.signature.SignatureException if signature validation
	 *                                                                              fails
	 * @throws IllegalArgumentException                                             if the XML object
	 *                                                                              structure
	 *                                                                              is not
	 *                                                                              recognized or implemeted
	 */
	Saml2Object fromXml(byte[] xml, List<Saml2KeyData> verificationKeys, List<Saml2KeyData> localKeys);

	default <T extends Saml2Object> T fromXml(
		byte[] xml,
		List<Saml2KeyData> verificationKeys,
		List<Saml2KeyData> localKeys,
		Class<T> type) {
		return type.cast(fromXml(xml, verificationKeys, localKeys));
	}

	default <T extends Saml2Object> T fromXml(
		String xml,
		List<Saml2KeyData> verificationKeys,
		List<Saml2KeyData> localKeys,
		Class<T> type) {
		return type.cast(fromXml(xml, verificationKeys, localKeys));
	}

	/**
	 * Deflates and base64 encodes the SAML message readying it for transport.
	 * If the result is used as a query parameter, it still has to be URL encoded.
	 *
	 * @param s       - original string
	 * @param deflate - if set to true the DEFLATE encoding will be applied
	 * @return encoded string
	 */
	String samlEncode(String s, boolean deflate);

	/**
	 * base64 decodes and inflates the SAML message.
	 *
	 * @param s       base64 encoded deflated string
	 * @param inflate - if set to true the value will be deflated
	 * @return the original string
	 */
	String samlDecode(String s, boolean inflate);

	Signature validateSignature(Saml2SignableObject saml2Object, List<Saml2KeyData> trustedKeys)
		throws SignatureException;

}
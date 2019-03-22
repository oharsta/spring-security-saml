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
package org.springframework.security.saml2.provider.validation;

import java.time.Clock;
import java.util.List;

import org.springframework.security.saml2.Saml2Exception;
import org.springframework.security.saml2.Saml2Transformer;
import org.springframework.security.saml2.Saml2ValidationResult;
import org.springframework.security.saml2.provider.HostedSaml2IdentityProvider;
import org.springframework.security.saml2.model.Saml2Object;
import org.springframework.security.saml2.model.Saml2SignableObject;
import org.springframework.security.saml2.model.authentication.Saml2AuthenticationSaml2Request;
import org.springframework.security.saml2.model.authentication.Saml2LogoutSaml2Request;
import org.springframework.security.saml2.model.authentication.Saml2LogoutResponseSaml2;
import org.springframework.security.saml2.model.key.Saml2KeyData;
import org.springframework.security.saml2.model.metadata.ServiceProviderMetadata;
import org.springframework.security.saml2.model.signature.Signature;
import org.springframework.util.Assert;

//TODO Move to Identity Provider module
public class DefaultIdentityProviderValidator extends AbstractSamlValidator<HostedSaml2IdentityProvider>
	implements IdentityProviderValidator {

	private Saml2Transformer implementation;
	private int responseSkewTimeMillis = 1000 * 60 * 2; //two minutes
	private boolean allowUnsolicitedResponses = true;
	private int maxAuthenticationAgeMillis = 1000 * 60 * 60 * 24; //24 hours
	private Clock time = Clock.systemUTC();

	public DefaultIdentityProviderValidator(Saml2Transformer implementation) {
		setSamlTransformer(implementation);
	}

	private void setSamlTransformer(Saml2Transformer implementation) {
		this.implementation = implementation;
	}

	@Override
	public Saml2Transformer getSamlTransformer() {
		return implementation;
	}

	public DefaultIdentityProviderValidator setTime(Clock time) {
		this.time = time;
		return this;
	}

	@Override
	public Signature validateSignature(Saml2SignableObject saml2Object, List<Saml2KeyData> verificationKeys) {
		return super.validateSignature(saml2Object, verificationKeys);
	}

	@Override
	public Saml2ValidationResult validate(Saml2Object saml2Object, HostedSaml2IdentityProvider provider) {
		Assert.notNull(saml2Object, "Object to be validated cannot be null");
		Saml2ValidationResult result;
		if (saml2Object instanceof ServiceProviderMetadata) {
			result = validate((ServiceProviderMetadata)saml2Object, provider);
		}
		else if (saml2Object instanceof Saml2AuthenticationSaml2Request) {
			result = validate((Saml2AuthenticationSaml2Request)saml2Object, provider);
		}
		else if (saml2Object instanceof Saml2LogoutSaml2Request) {
			result = validate((Saml2LogoutSaml2Request)saml2Object, provider);
		}
		else if (saml2Object instanceof Saml2LogoutResponseSaml2) {
			result = validate((Saml2LogoutResponseSaml2)saml2Object, provider);
		}
		else {
			throw new Saml2Exception("No validation implemented for class:" + saml2Object.getClass().getName());
		}
		return result;

	}

	private Saml2ValidationResult validate(ServiceProviderMetadata metadata, HostedSaml2IdentityProvider provider) {
		return new Saml2ValidationResult(metadata);
	}

	private Saml2ValidationResult validate(Saml2AuthenticationSaml2Request authnRequest, HostedSaml2IdentityProvider provider) {
		Saml2ValidationResult result = new Saml2ValidationResult(authnRequest);
		checkValidSignature(authnRequest, result);
		return result;
	}

	public int getResponseSkewTimeMillis() {
		return responseSkewTimeMillis;
	}

	public DefaultIdentityProviderValidator setResponseSkewTimeMillis(int responseSkewTimeMillis) {
		this.responseSkewTimeMillis = responseSkewTimeMillis;
		return this;
	}

	public boolean isAllowUnsolicitedResponses() {
		return allowUnsolicitedResponses;
	}

	public DefaultIdentityProviderValidator setAllowUnsolicitedResponses(boolean allowUnsolicitedResponses) {
		this.allowUnsolicitedResponses = allowUnsolicitedResponses;
		return this;
	}

	public int getMaxAuthenticationAgeMillis() {
		return maxAuthenticationAgeMillis;
	}

	public Clock time() {
		return time;
	}

	public void setMaxAuthenticationAgeMillis(int maxAuthenticationAgeMillis) {
		this.maxAuthenticationAgeMillis = maxAuthenticationAgeMillis;
	}


}
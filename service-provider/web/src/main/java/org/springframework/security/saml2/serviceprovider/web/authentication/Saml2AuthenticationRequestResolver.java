/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.springframework.security.saml2.serviceprovider.web.authentication;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.saml2.model.authentication.Saml2AuthenticationRequest;

public interface Saml2AuthenticationRequestResolver {

	String encode(Saml2AuthenticationRequest authn, boolean deflate);

	Saml2AuthenticationRequest resolve(HttpServletRequest request);

}
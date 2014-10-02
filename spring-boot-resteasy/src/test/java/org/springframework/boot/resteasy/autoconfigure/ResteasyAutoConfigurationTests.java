/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.resteasy.autoconfigure;

import static org.junit.Assert.assertTrue;

import org.jboss.resteasy.spi.ResteasyDeployment;
import org.jboss.resteasy.springmvc.ResteasyHandlerAdapter;
import org.jboss.resteasy.springmvc.ResteasyHandlerMapping;
import org.junit.After;
import org.junit.Test;
import org.springframework.boot.test.EnvironmentTestUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ResteasyAutoConfigurationTests {

	private AnnotationConfigApplicationContext context;

	@After
	public void tearDown() {
		if (this.context != null) {
			this.context.close();
		}
	}

	@Test
	public void defaultBeanCreation() {
		this.context = new AnnotationConfigApplicationContext(ResteasyAutoConfiguration.class);

		this.context.getBean(ResteasyDeployment.class);
		this.context.getBean(ResteasyHandlerAdapter.class);
		this.context.getBean(ResteasyHandlerMapping.class);
	}

	@Test
	public void customizedDeploymentConfiguration() {
		this.context = new AnnotationConfigApplicationContext();
		this.context.register(ResteasyAutoConfiguration.class);
		EnvironmentTestUtils.addEnvironment(this.context, "resteasy.deployment.async_job_service_enabled:true");
		this.context.refresh();

		ResteasyDeployment deployment = this.context.getBean(ResteasyDeployment.class);
		assertTrue(deployment.isAsyncJobServiceEnabled());
	}

}

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

package sample.resteasy;

import static org.junit.Assert.assertEquals;

import java.net.URI;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SampleResteasyApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class SampleResteasyApplicationTests {

	@Value("${local.server.port}")
	private int port;

	@Test
	public void synchronousRequest() {
		ResponseEntity<String> response = new TestRestTemplate()
				.getForEntity("http://localhost:" + port + "/hello", String.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("Hello World", response.getBody());

	}

	@Test
	public void asynchronousRequest() {
		TestRestTemplate rest = new TestRestTemplate();
		ResponseEntity<String> response = rest
				.getForEntity("http://localhost:" + port + "/hello?asynch=true", String.class);
		assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());

		URI jobLocation = response.getHeaders().getLocation();

		HttpStatus jobStatus = HttpStatus.ACCEPTED;
		ResponseEntity<String> jobResponse = null;

		while (jobStatus == HttpStatus.ACCEPTED) {
			jobResponse = rest.getForEntity(jobLocation, String.class);
			jobStatus = jobResponse.getStatusCode();
		}

		assertEquals(HttpStatus.OK, jobResponse.getStatusCode());
		assertEquals("Hello World", jobResponse.getBody());

		rest.delete(jobLocation);

	}

}

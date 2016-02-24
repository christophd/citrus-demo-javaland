/*
 * Copyright 2016 the original author or authors.
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
 */

package com.consol.citrus.demo.javaland;

import com.consol.citrus.demo.javaland.model.Employee;
import com.consol.citrus.demo.javaland.model.Employees;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import java.net.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(Arquillian.class)
@RunAsClient
public class EmployeeResourceTest {

    private WebTarget webTarget;
    private String serviceUri;

    @ArquillianResource
    private URL baseUri;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(
                        RegistryApplication.class, EmployeeResource.class, Employees.class,
                        Employee.class, EmployeeRepository.class);
    }

    @Before
    public void setUp() throws MalformedURLException {
        serviceUri = new URL(baseUri, "registry/employee").toExternalForm();

        webTarget = ClientBuilder.newClient().target(URI.create(serviceUri));
        webTarget.register(Employee.class);
    }

    @Test
    @InSequence(1)
    public void testPostAndGet() {
        MultivaluedHashMap<String, String> map = new MultivaluedHashMap<>();
        map.add("name", "Penny");
        map.add("age", "20");
        webTarget.request().post(Entity.form(map));

        map.clear();
        map.add("name", "Leonard");
        map.add("age", "21");
        webTarget.request().post(Entity.form(map));

        map.clear();
        map.add("name", "Sheldon");
        map.add("age", "22");
        webTarget.request().post(Entity.form(map));

        Employee[] list = webTarget.request().get(Employee[].class);
        assertEquals(3, list.length);

        assertEquals("Penny", list[0].getName());
        assertEquals(20, list[0].getAge());

        assertEquals("Leonard", list[1].getName());
        assertEquals(21, list[1].getAge());

        assertEquals("Sheldon", list[2].getName());
        assertEquals(22, list[2].getAge());
    }

    @Test
    @InSequence(2)
    public void testGetSingle() {
        Employee p = webTarget
                .path("{id}")
                .resolveTemplate("id", "1")
                .request(MediaType.APPLICATION_XML)
                .get(Employee.class);
        assertEquals("Leonard", p.getName());
        assertEquals(21, p.getAge());
    }

    @Test
    @InSequence(3)
    public void testPut() {
        MultivaluedHashMap<String, String> map = new MultivaluedHashMap<>();
        map.add("name", "Howard");
        map.add("age", "21");
        webTarget.request().post(Entity.form(map));

        Employee[] list = webTarget.request().get(Employee[].class);
        assertEquals(4, list.length);

        assertEquals("Howard", list[3].getName());
        assertEquals(21, list[3].getAge());
    }

    @Test
    @InSequence(4)
    public void testDelete() {
        webTarget
                .path("{name}")
                .resolveTemplate("name", "Leonard")
                .request()
                .delete();
        Employee[] list = webTarget.request().get(Employee[].class);
        assertEquals(3, list.length);
    }

    @Test
    @InSequence(5)
    public void testClientSideNegotiation() {
        JsonObject json = webTarget.request().accept(MediaType.APPLICATION_JSON).get(JsonObject.class);
        JsonArray employees = json.getJsonArray("employee");
        assertEquals(3, employees.size());

        for(int i = 0; i < employees.size(); i++) {
           JsonObject employee = employees.getJsonObject(i);
           String name = employee.getString("name");
           int age = employee.getInt("age");
           
           if("Penny".equals(name)) {
              assertEquals(20,  age);
           } else if("Howard".equals(name)) {
              assertEquals(21,  age);
           } else if("Sheldon".equals(name)) {
              assertEquals(22,  age);
           } else {
              fail("Unknown Employee returned [" + name + ", " + age + "]");
           }
        }
    }

    @Test
    @InSequence(6)
    public void testDeleteAll() {
        Employee[] list = webTarget.request().get(Employee[].class);
        for (Employee p : list) {
            webTarget
                    .path("{name}")
                    .resolveTemplate("name", p.getName())
                    .request()
                    .delete();
        }
        list = webTarget.request().get(Employee[].class);
        assertEquals(0, list.length);
    }

}
package hei.tp01.controller;

import hei.tp01.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.*;


@RunWith(JUnit4.class)
public class ClientsServletStructureTestCase {

    @Test
    public void shouldExtendHttpServlet(){
        //GIVEN
        ClientsServlet servlet = new ClientsServlet();
        //WHEN
        //THEN
        assertThat(servlet).isInstanceOf(HttpServlet.class);
    }

    @Test
    public void shouldHaveWebServletAnnotation() {
        //GIVEN
        Class<ClientsServlet> clazz = ClientsServlet.class;
        WebServlet[] annotations = clazz.getAnnotationsByType(WebServlet.class);
        //WHEN
        //THEN
        assertThat(annotations).hasSize(1);
        assertThat(annotations[0].urlPatterns()).containsOnly("/clients");
    }



    @Test
    public void shouldHaveInitMethod() throws NoSuchMethodException {
        TestUtils.shouldHaveMethod(ClientsServlet.class,"init");
    }

    @Test
    public void shouldHaveDoGetMethod() throws NoSuchMethodException {
        TestUtils.shouldHaveMethod(ClientsServlet.class,"doGet", HttpServletRequest.class, HttpServletResponse.class);
    }

    @Test
    public void shouldHaveDoPostMethod() throws NoSuchMethodException {
        TestUtils.shouldHaveMethod(ClientsServlet.class,"doPost", HttpServletRequest.class, HttpServletResponse.class);
    }





}

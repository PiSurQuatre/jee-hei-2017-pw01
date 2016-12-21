package hei.tp01.filter;

import hei.tp01.controller.ClientsServlet;
import hei.tp01.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.StrictAssertions.assertThat;

@RunWith(JUnit4.class)
public class SessionFilterStructureTestCase {

    @Test
    public void shouldImplementFilter(){
        //GIVEN
        SessionFilter filter = new SessionFilter();
        //WHEN
        //THEN
        assertThat(filter).isInstanceOf(Filter.class);
    }

    @Test
    public void shouldHaveWebFilterAnnotation() {
        //GIVEN
        Class<SessionFilter> clazz = SessionFilter.class;
        WebFilter[] annotations = clazz.getAnnotationsByType(WebFilter.class);
        //WHEN
        //THEN
        assertThat(annotations).hasSize(1);
        assertThat(annotations[0].urlPatterns()).containsOnly("/*");
    }



    @Test
    public void shouldHaveInitMethod() throws NoSuchMethodException {
        TestUtils.shouldHaveMethod(SessionFilter.class,"init", FilterConfig.class);
    }

    @Test
    public void shouldHaveDoFilterMethod() throws NoSuchMethodException {
        TestUtils.shouldHaveMethod(SessionFilter.class,"doFilter", ServletRequest.class, ServletResponse.class,FilterChain.class);
    }

    @Test
    public void shouldHaveDestroyMethod() throws NoSuchMethodException {
        TestUtils.shouldHaveMethod(SessionFilter.class,"destroy");
    }

}

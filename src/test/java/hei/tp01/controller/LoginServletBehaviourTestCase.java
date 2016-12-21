package hei.tp01.controller;

import hei.tp01.model.Utilisateur;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import static org.assertj.core.api.StrictAssertions.assertThat;
import static org.assertj.core.api.StrictAssertions.tuple;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
public class LoginServletBehaviourTestCase {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher dispatcher;

    @Mock
    private ServletContext context;

    @Mock
    private HttpSession session;

    @Before
    public void beforeTests() {
        when(request.getSession()).thenReturn(session);
        when(request.getServletContext()).thenReturn(context);
        when(context.getContextPath()).thenReturn("contextPath");
        when(request.getParameter(eq("login"))).thenReturn("loginTest");
        when(request.getParameter(eq("password"))).thenReturn("passwordTest");
        when(context.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    }

    @Test
    public void shouldNotHaveUtilisateursBeforeInit() throws NoSuchFieldException, IllegalAccessException, ServletException {
        //GIVEN
        Class<LoginServlet> clazz = LoginServlet.class;
        Field utilisateursField = clazz.getDeclaredField("utilisateurs");
        utilisateursField.setAccessible(true);
        ParameterizedType listType = (ParameterizedType) utilisateursField.getGenericType();
        LoginServlet servlet = new LoginServlet();
        //WHEN
        Object utilisateurs = utilisateursField.get(servlet);
        //THEN
        assertThat(utilisateurs).isNull();
        assertThat(utilisateursField.getType()).isEqualTo(List.class);
        assertThat(listType.getActualTypeArguments()[0]).isEqualTo(Utilisateur.class);
        
    }

    @Test
    public void shouldHaveUtilisateursAfterInit() throws NoSuchFieldException, IllegalAccessException, ServletException {
        //GIVEN
        Class<LoginServlet> clazz = LoginServlet.class;
        Field utilisateursField = clazz.getDeclaredField("utilisateurs");
        utilisateursField.setAccessible(true);
        LoginServlet servlet = new LoginServlet();
        ((HttpServlet)servlet).init();
        //WHEN
        Object utilisateurs = utilisateursField.get(servlet);
        //THEN
        assertThat(utilisateurs).isInstanceOf(List.class);
        List<?> list = (List<?>) utilisateurs;
        Assertions.assertThat(list).hasSize(2);
        assertThat(list.get(0)).isInstanceOf(Utilisateur.class);
        assertThat(list.get(1)).isInstanceOf(Utilisateur.class);
        List<Utilisateur> utilisateursList = (List<Utilisateur>) utilisateurs;
        Assertions.assertThat(utilisateursList).extracting("login", "password").containsExactly(tuple("utilisateur1", "password1"), tuple("utilisateur2", "password2"));
    }

    @Test
    public void shouldLogoutIfUrlIsGood() throws ServletException, IOException {
        //GIVEN
        when(request.getQueryString()).thenReturn("logout");
        LoginServlet servlet = new LoginServlet();
        ((HttpServlet)servlet).init();
        //WHEN
        servlet.doGet(request, response);
        //THEN
        verify(session, times(1)).removeAttribute(eq("utilisateurConnecte"));
        verify(response, times(1)).sendRedirect(eq("contextPath/"));
    }

    @Test
    public void shouldNotLogoutIfUrlIsBad() throws ServletException, IOException {
        //GIVEN
        when(request.getQueryString()).thenReturn("someUrl");
        LoginServlet servlet = new LoginServlet();
        ((HttpServlet)servlet).init();
        //WHEN
        servlet.doGet(request,response);
        //THEN
        verify(session,never()).removeAttribute(eq("utilisateurConnecte"));
        verify(response,never()).sendRedirect(eq("contextPath/"));
    }

    @Test
    public void shouldNotLogInIfCredentialsAreWrong() throws ServletException, IOException {
        //GIVEN
        LoginServlet servlet = new LoginServlet();
        ((HttpServlet)servlet).init();
        //WHEN
        servlet.doPost(request, response);
        //THEN
        verify(request,times(1)).setAttribute(eq("loginError"), eq("Login ou mot de passe invalide !"));
        verify(context,times(1)).getRequestDispatcher(eq("/index.jsp"));
        verify(dispatcher,times(1)).forward(eq(request), eq(response));
    }

    @Test
    public void shouldLogInIfCredentialsAreGood() throws ServletException, IOException {
        //GIVEN
        when(request.getParameter(eq("login"))).thenReturn("utilisateur1");
        when(request.getParameter(eq("password"))).thenReturn("password1");
        LoginServlet servlet = new LoginServlet();
        ((HttpServlet)servlet).init();
        //WHEN
        servlet.doPost(request, response);
        //THEN
        verify(request,times(1)).removeAttribute(eq("loginError"));
        verify(session,times(1)).setAttribute(eq("utilisateurConnecte"),eq (new Utilisateur("utilisateur1", "password1")));
        verify(response,times(1)).sendRedirect(eq("contextPath/clients"));

    }

}

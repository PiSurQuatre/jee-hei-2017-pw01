package hei.tp01.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.StrictAssertions.assertThat;
import static org.assertj.core.api.StrictAssertions.tuple;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import hei.tp01.model.Client;

@RunWith(PowerMockRunner.class)
public class ClientsServletBehaviourTestCase {

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
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    }

    @Test
    public void shouldNotHaveClientsBeforeInit() throws NoSuchFieldException, IllegalAccessException, ServletException {
        //GIVEN
        Class<ClientsServlet> clazz = ClientsServlet.class;
        Field clientsField = clazz.getDeclaredField("clients");
        clientsField.setAccessible(true);
        ParameterizedType listType = (ParameterizedType) clientsField.getGenericType();
        ClientsServlet servlet = new ClientsServlet();
        //WHEN
        Object clients = clientsField.get(servlet);
        //THEN
        assertThat(clients).isNull();
        assertThat(clientsField.getType()).isEqualTo(List.class);
        assertThat(listType.getActualTypeArguments()[0]).isEqualTo(Client.class);
        
    }

    @Test
    public void shouldHaveClientsAfterInit() throws NoSuchFieldException, IllegalAccessException, ServletException {
        //GIVEN
        Class<ClientsServlet> clazz = ClientsServlet.class;
        Field clientsField = clazz.getDeclaredField("clients");
        clientsField.setAccessible(true);
        ClientsServlet servlet = new ClientsServlet();
        ((HttpServlet)servlet).init();
        //WHEN
        Object clients = clientsField.get(servlet);
        //THEN
        assertThat(clients).isInstanceOf(List.class);
        List<?> list = (List<?>) clients;
        assertThat(list).hasSize(2);
        assertThat(list.get(0)).isInstanceOf(Client.class);
        assertThat(list.get(1)).isInstanceOf(Client.class);
        List<Client> clientsList = (List<Client>) clients;
        Assertions.assertThat(clientsList).extracting("nom", "prenom").containsExactly(tuple("Nom1", "Prenom1"), tuple("Nom2", "Prenom2"));
    }

    @Test
    public void shouldSetClientsInRequestThenDispatch() throws ServletException, IOException, NoSuchFieldException, IllegalAccessException {
        //GIVEN

        ClientsServlet servlet = new ClientsServlet();
        ((HttpServlet)servlet).init();
        Field clientsField = ClientsServlet.class.getDeclaredField("clients");
        clientsField.setAccessible(true);
        List<Client> clients = (List<Client>) clientsField.get(servlet);
        //WHEN
        servlet.doGet(request, response);
        //THEN
        verify(request,times(1)).setAttribute(eq("clients"),eq(clients));
        verify(request, times(1)).getRequestDispatcher(eq("ClientsList.jsp"));
        verify(dispatcher,times(1)).forward(eq(request),eq(response));
    }

    @Test
    public void shouldSaveClientThenRedirect() throws NoSuchFieldException, ServletException, IllegalAccessException, IOException {
        //GIVEN
        when(request.getParameter(eq("nom"))).thenReturn("Michu");
        when(request.getParameter(eq("prenom"))).thenReturn("Germaine");
        ClientsServlet servlet = new ClientsServlet();
        ((HttpServlet)servlet).init();
        Field clientsField = ClientsServlet.class.getDeclaredField("clients");
        clientsField.setAccessible(true);
        //WHEN
        servlet.doPost(request, response);
        List<Client> clients = (List<Client>) clientsField.get(servlet);
        //THEN
        assertThat(clients).hasSize(3);
        assertThat(clients.get(2).getNom()).isEqualTo("Michu");
        assertThat(clients.get(2).getPrenom()).isEqualTo("Germaine");
        verify(response,times(1)).sendRedirect(eq("contextPath/clients"));
    }

}

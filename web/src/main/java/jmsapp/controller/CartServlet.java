package jmsapp.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import jmsapp.ejb.ProdutoEjb;
import jmsapp.modelo.Produto;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
	private static final int CREATED = 201;
	private static final long serialVersionUID = 1L;
	private static final String API_URL = "http://localhost:8081/api";
	
	@Resource(lookup = "java:/jms/MyConnectionFactory")
	ConnectionFactory connectionFactory;
	
	@Resource(lookup = "java:/jms/MyQueue")
	Destination destination;
	
	@Inject
	private ProdutoEjb produtoEjb;
	
	private Client cli =  ClientBuilder.newClient();
       
    public CartServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String clientName = request.getParameter("clientName");
		String productName = request.getParameter("productName");
		
		Client client = (Client) new jmsapp.modelo.Client(clientName);
		Produto product = new Produto(productName);
		
		sendMessageToJms(clientName, productName);
		sendClientToExternalAPI(client);
		
		request.setAttribute("product", produtoEjb.add(product));
		request.getRequestDispatcher("home.jsp").forward(request, response);
	}
	
	private void sendMessageToJms(String clientName, String productName) {
		try {
			QueueConnection connection = (QueueConnection) connectionFactory.createConnection();			
			QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageProducer producer = session.createProducer(destination);
			TextMessage message = session.createTextMessage(String.format("O produco %s pertence ao cliente %s", productName, clientName));
			
			producer.send(message);
			
			System.out.println(String.format("O produco %s pertence ao cliente %s", productName, clientName));
			System.out.println("Teste mensagem..");
			System.out.println("Mostre-me o cliente" + clientName);

			producer.close();
			session.close();
			connection.close();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private void sendClientToExternalAPI(Client client) {
		try {
			Response res =  cli
								.target(API_URL)
								.path("/clients")
								.request(MediaType.APPLICATION_JSON)
								.post(Entity.entity(client, MediaType.APPLICATION_JSON));

			if (Integer.valueOf(res.getStatus()) != CREATED) {
				System.out.println("Error to create a client.");
				throw new RuntimeException();
			}
			
			
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
			System.out.println("Integration error!");
		}
	}

}

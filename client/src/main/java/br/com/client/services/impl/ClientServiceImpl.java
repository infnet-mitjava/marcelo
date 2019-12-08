package br.com.client.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.client.model.Client;
import br.com.client.repositories.ClientRepository;
import br.com.client.services.ClientService;

@Service
public class ClientServiceImpl implements ClientService {
	
	@Autowired
	private ClientRepository clientRepository;

	@Override
	public Client create(Client client) {
		return clientRepository.save(client);
	}

}

package br.com.client.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.client.model.Client;
import br.com.client.services.ClientService;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
	
	@Autowired
	private ClientService clientService;

	@PostMapping
	ResponseEntity<?> create(@RequestBody Client client) {
		Client obj = clientService.create(client);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
					.path("/{id}")
					.buildAndExpand(obj.getId())
					.toUri();
		return ResponseEntity.created(uri).build();
	}
}

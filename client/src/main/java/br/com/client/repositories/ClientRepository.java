package br.com.client.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import br.com.client.model.Client;

@Repository
public interface ClientRepository extends MongoRepository<Client, String> {

}

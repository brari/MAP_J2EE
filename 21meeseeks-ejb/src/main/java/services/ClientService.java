package services;

import java.util.List;
import java.util.Map;

import interfaces.ClientServiceLocal;
import interfaces.ClientServiceRemote;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import entities.Client;
import entities.ClientCategory;
import entities.ClientType;

/**
 * Session Bean implementation class ClientService
 */
@Stateless
@LocalBean
public class ClientService implements ClientServiceRemote, ClientServiceLocal {
	@PersistenceContext(unitName="21meeseeks-ejb")
	EntityManager em;
	
    /**
     * Default constructor. 
     */
    public ClientService() {}
    
    /*
    add new client and return his id
    */
    
	@Override
	public int addClient(Client c) {
        c.setClientCategory(this.findClientCategory(c.getClientCategory()));
		em.persist(c);
		return (c.getIdUser());
	}
	
	//delete existing client by given id
	@Override
	public void deleteClientByID(int i) {
		Client c=(Client)em.find(Client.class, i);
		em.remove(c);		
	}
	@Override
	public Client findClientByID(int i) {
		Client c=(Client)em.find(Client.class, i);
return c;
	}

	
	
	//
	//customize category
	//
	
	
	@Override
	public int addClientCategory(ClientCategory cc) {
		TypedQuery<ClientCategory> query=em.createQuery("SELECT e from ClientCategory e  where e.name = :name",ClientCategory.class);
		if(query.setParameter("name", cc.getName()).getResultList().size()==0)
		{
			
			     em.persist(cc);
			     return cc.getIdCategory();
		}
	  return 0; 
	}
	@Override
	public ClientCategory findClientCategory(ClientCategory cc) {
		TypedQuery<ClientCategory> query=em.createQuery("SELECT e from ClientCategory e  where e.name = :name",ClientCategory.class);
		ClientCategory cf=query.setParameter("name",cc.getName()).getSingleResult();
return cf;
	}
	@Override
	public boolean deleteClientCategory(String title) {
		TypedQuery<ClientCategory> query=em.createQuery("SELECT e from ClientCategory e  where e.name = :name",ClientCategory.class);
		if(query.setParameter("name",title).getResultList().size()!=0)
		{
			em.remove(query.setParameter("name",title).getSingleResult());
		return true;
		}
		return false;
	}
	
	
	
	@Override
	public List<ClientCategory> listClientCategories() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	//
	//customize type
	//
	
	
	@Override
	public int addClientType(ClientType ct) {
		TypedQuery<ClientType> query=em.createQuery("SELECT e from ClientType e  where e.name = :name",ClientType.class);
		if(query.setParameter("name", ct.getName()).getResultList().size()==0)
		{
			
			     em.persist(ct);
			     return ct.getIdClientType();
		}
	  return 0; 
	}
	@Override
	public boolean deleteClientType(String title) {

		return true;
	}
	@Override
	public List<ClientType> listClientType() {
return null;		
	}
//searching for client with many criterias(clientName,address,mail) using query parameters
	
	@Override
	public List<Client> getClientsByCriterias(Map<String, String> criterias) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Client> cquery = cb.createQuery(Client.class);
		Root<Client> sm = cquery.from(Client.class);
		for (Map.Entry<String, String> entry : criterias.entrySet())
		{	if(entry.getKey().equals("clientCategory"))
		{
			cquery.where(cb.like(sm.get("clientCategory").get("name"), entry.getValue().toUpperCase() ));

		}
		else if(entry.getKey().equals("clientType"))
		{ 
			cquery.where(cb.like(sm.get("clientCategory").get("name"), entry.getValue().toUpperCase() ));

		}
		else
			{
			cquery.where(cb.like(sm.get(entry.getKey()), entry.getValue().toUpperCase() ));
			}

		}
		TypedQuery<Client> query=em.createQuery(cquery);
		return query.getResultList();
	}

	//
	//json converting
	//
	
	//converts a client to json
	@Override
	public String clienttoJson(Client c) {
		// TODO Auto-generated method stub
		 String test="";
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode main = mapper.createObjectNode();
		try {
			 test=mapper.writeValueAsString(c);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return test;
		}


	@Override
	public String clientlisttoJson(List<Client> c) {
		 String test="";
			ObjectMapper mapper = new ObjectMapper();
			ObjectNode main = mapper.createObjectNode();
			try {
				 test=mapper.writeValueAsString(c);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return test;
	}

}

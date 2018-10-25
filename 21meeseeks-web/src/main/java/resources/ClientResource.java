package resources;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import interfaces.ClientServiceLocal;

import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.Application;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import entities.Client;
import entities.ClientCategory;
import entities.ClientType;

@Path("client")
@RequestScoped
public class ClientResource {

	@EJB(beanName="ClientService")
	ClientServiceLocal CSL;
	private Map<String, String> criterias;
//using a map to filter query with criteria to fetch clients , or just display all of them 
	  @GET
	  @Produces( MediaType.APPLICATION_JSON )
	  public Response findClientsByName(
			    @QueryParam("clientName") String clientName,
				@QueryParam("email") String email,
				@QueryParam("address") String address,
				@QueryParam("clientType") String clientType,
				@QueryParam("clientCatogry") String clientCategory
			  ) {
		  criterias=new HashMap<String, String>();
		  if(clientName!=null){criterias.put("clientName","%"+clientName+"%");}
		  if(email!=null){criterias.put("email", email);}
		  if(address!=null){criterias.put("address", address);}
		  if(clientType!=null){criterias.put("clienType", clientType);}
		  if(clientCategory!=null)criterias.put("clientCategory", clientCategory);
		List<Client> c=CSL.getClientsByCriterias(criterias);
		if(c.size()!=0)
		{
			return  Response.status(Status.FOUND).entity(CSL.clientlisttoJson(c)).build();
		}
		else
		{
			return  Response.status(Status.NOT_FOUND).build();
		}
	  }

	  
	  
	  
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces( MediaType.TEXT_PLAIN)
	public Response addClient(Client c)
	{
		c.setIdUser(CSL.addClient(c));
		return Response.status(Status.CREATED).entity(c).build();
	}

	
	
	  //
	  //customize category (add or delete)
	  //
	  
	
	
	
	@POST
	@Path("category")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces( MediaType.TEXT_PLAIN)
	public Response addClientCategory(ClientCategory cc)
	{
		cc.setIdCategory(CSL.addClientCategory(cc));
		return Response.status(Status.CREATED).entity(cc).build();
	}
	  @DELETE
	  @Path("category/{name}")
	  public Response deleteClientCategory(@PathParam("name") String name) {
	  if(CSL.deleteClientCategory(name))
	  {
		  return Response.status(Status.FOUND).entity(name+": category deleted").build();	
				  }
	  return Response.status(Status.NOT_FOUND).entity(name+": category not found").build();	

	  }

	  //
	  //customize type (add or delete)
	  //
	  
	  
	  @POST
		@Path("type")
		@Consumes(MediaType.APPLICATION_JSON)
		@Produces( MediaType.TEXT_PLAIN)
		public Response addClientType(ClientType cc)
		{
			cc.setIdClientType(CSL.addClientType(cc));
			return Response.status(Status.CREATED).entity(cc).build();
		}
		  @DELETE
		  @Path("type/{name}")
		  public Response deleteClientType(@PathParam("name") String name) {
		  if(CSL.deleteClientType(name))
		  {
			  return Response.status(Status.FOUND).entity(name+" : type deleted").build();	
					  }
		  return Response.status(Status.NOT_FOUND).entity(name+" : type not found").build();	

		  }
	  
	  
}
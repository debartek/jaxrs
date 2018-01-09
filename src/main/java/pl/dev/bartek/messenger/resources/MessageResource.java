package pl.dev.bartek.messenger.resources;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import pl.dev.bartek.messenger.model.Message;
import pl.dev.bartek.messenger.model.Profile;
import pl.dev.bartek.messenger.service.MessageService;

@Path("/messages")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MessageResource {
	
	MessageService service = new MessageService();
	@GET
	@Produces(value = {MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
	public List<Message> getMessages(@QueryParam("year") int year,
									 @QueryParam("year") int start,
									 @QueryParam("year") int size) {
		if(year > 0) {
			return service.getMessagesByYear(year);
		}
		
		else if(start > 0 && size > 0) {
			return service.getMessagesPaginated(start, size);
		}
		else {
			
			return service.getAllMessages();
		}
	}
	
	
	@GET
	@Path("/{messageId}")
	public Message getMessage(@PathParam("messageId")long messageId, @Context UriInfo uriInfo) {
		Message message = service.getMessage(messageId);
		message.addLink(getUriForSelf(uriInfo, message), "self");
		message.addLink(getUriForProfile(uriInfo, message), "profile");
		message.addLink(getUriForComments(uriInfo, message), "comments");
		return message;
	}


	private String getUriForComments(UriInfo uriInfo, Message message) {
		URI uri = uriInfo.getBaseUriBuilder()
				.path(MessageResource.class)
				.path(MessageResource.class, "getCommentResource")
				.path(CommentResource.class)
				.resolveTemplate("messageId", message.getId())
				.build();
		return uri.toString();
	}


	private String getUriForProfile(UriInfo uriInfo, Message message) {
		URI uri = uriInfo.getBaseUriBuilder()
				.path(MessageResource.class)
				.path(ProfileResource.class)
				.path(message.getAuthor())
				.build();
		return uri.toString();
	}


	private String getUriForSelf(UriInfo uriInfo, Message message) {
		String uri = uriInfo.getBaseUriBuilder()
		.path(MessageResource.class)
		.path(Long.toString(message.getId()))
		.build()
		.toString();
		return uri;
	}
	
	@POST
	public Response addMessage(Message message,@Context UriInfo uriInfo) throws URISyntaxException {
		Message newMessage = service.addMessage(message);
		String newId = String.valueOf(newMessage.getId());
		URI uri = uriInfo.getAbsolutePathBuilder().path(newId).build();
		return Response.created(uri)
		.entity(newMessage)
		.build();
		//return service.addMessage(message);
	}
	
	@PUT
	@Path("/{messageId}")

	public Message updateMessage(Message message,@PathParam("messageId")long messageId) {
		message.setId(messageId);
		return service.updateMessage(message);
	}
	
	@DELETE
	@Path("/{messageId}")
	public void deleteMessage(@PathParam("messageId")long messageId) {
		
		service.removeMessage(messageId);
	}
	
	@Path("/{messageId}/comments")
	public CommentResource getCommentResource() {
		return new CommentResource();
	}
}

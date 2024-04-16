package io.antmedia.rest;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import io.antmedia.plugin.YOLONetPlugin;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Component
@Path("/v1/yolo-net-plugin")
public class RestService {

	@Context
	protected ServletContext servletContext;

	@POST
	@Path("/start/{streamId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response startYOLONet(@PathParam("streamId") String streamId) {
		YOLONetPlugin app = getYOLONetPlugin();
		app.startYOLONetProcess(streamId);

		return Response.status(Status.OK).entity("").build();
	}

	@POST
	@Path("/stop/{streamId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response stopYOLONet(@PathParam("streamId") String streamId) {
		YOLONetPlugin app = getYOLONetPlugin();
		app.stopYOLONetProcess(streamId);

		return Response.status(Status.OK).entity("").build();
	}
	
	private YOLONetPlugin getYOLONetPlugin() {
		ApplicationContext appCtx = (ApplicationContext) servletContext.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
		return (YOLONetPlugin) appCtx.getBean("plugin.yolonetplugin");
	}
}

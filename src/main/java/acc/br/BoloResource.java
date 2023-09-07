package acc.br;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.logging.Logger;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/bolo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BoloResource {
    
    private static final Logger log = Logger.getLogger(BoloResource.class);

    @PostConstruct
    @Transactional
    public void init() {
        log.info("************ Método init() *****************");        
        // new Bolo("Chocolate", "Melhor bolo do mundo").persist();
        // new Bolo("Sensação", "Chocolate com morango").persist();
    }
        
    @GET
  @Operation(summary = "Retorna todos os bolos cadastrados")
  @APIResponse(responseCode = "200", //
      content = @Content(//
          mediaType = MediaType.APPLICATION_JSON, //
          schema = @Schema(//
              implementation = Bolo.class, //
              type = SchemaType.ARRAY)))
  public List<Bolo> list() {
        log.info("************ Método list() *****************");
        return Bolo.listAll();
    }

    @Operation(summary = "Cadastra um bolo")
    @APIResponse(responseCode = "200", 
            description = "Retorna todos os todos os bolos cadastrados, incluindo o novo bolo", 
            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Bolo.class, type = SchemaType.ARRAY))
            )
    @POST    
    @Transactional
    public List<Bolo> add(@RequestBody(required = true, 
        content = @Content(mediaType = MediaType.APPLICATION_JSON, 
        schema = @Schema(implementation = Bolo.class))) Bolo bolo) {
        log.info("************ Método add() *****************");
        bolo.id = null;  //coisa feia, não façam isso em casa
        bolo.persist();
        return list();
    }

    @Operation(summary = "Deleta um bolo pelo nome do bolo")
    @APIResponse(responseCode = "200", 
        description = "Todos os bolos cadastrados menos aquele retirado", content = @Content(mediaType = MediaType.APPLICATION_JSON, 
        schema = @Schema(implementation = Bolo.class, type = SchemaType.ARRAY))
     )
    @DELETE
    @Path("/{nome}")
    @Transactional
    public List<Bolo> delete(@Parameter(description = "Nome do bolo a ser retirado", required = true) 
            @PathParam("nome") String nome) {
        log.info("************ Método delete() *****************");
        Bolo.delete("nome", nome);
        return Bolo.listAll();
    }

}

package cloud.platform.io.repository.remote;

import java.net.UnknownHostException;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import cloud.platform.io.config.FeignConfiguration;
import cloud.platform.io.repository.remote.dataModel.HelloMessage;

/**
 *
 * @author onelove
 */
@FeignClient(name = "hello-service", configuration = FeignConfiguration.class)
public interface HelloServiceImpl {

    final static String prefix_HelloEndpoint = "/";

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<HelloMessage> hello() throws UnknownHostException ;
      
    // @PostMapping(value = prefix + "/me", consumes = "application/json", produces = "application/json")
    // public ResponseEntity<?> userInfo(@RequestBody UserToken tokenDetails);

    // //POST /users/{id}
    // //@Valid to validate the posted body attribute content
    // @PostMapping(value = prefix + "/me/teams", consumes = "application/json", produces = "application/json")
    // public ResponseEntity<?> list_user_teams(@RequestBody UserToken tokenDetails);

    // @PostMapping(value = prefix + "team/{uuid}/users", consumes = "application/json", produces = "application/json")
    // public ResponseEntity<?> list_team_members_resource(@PathVariable UUID uuid, @RequestBody UserToken tokenDetails);

    // @PostMapping(value = prefix + "team/{uuid}/sites", consumes = "application/json", produces = "application/json")
    // public ResponseEntity<?> listTeamsSitesByTeamId(@PathVariable UUID uuid, @RequestBody UserToken tokenDetails);
}

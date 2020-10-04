package cloud.platform.io.repository.remote.dataModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author George Fiotakis
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HelloMessage {

    private String message;
    private int port;
    private String serviceID;
    private String serviceHost;
    private ConfTemplate config;

   

}

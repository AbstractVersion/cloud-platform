package cloud.platform.io.repository.remote.dataModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author onelove
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfTemplate {

    private String config01;
    private String config02;
    private int config03;

    public ConfTemplate(Configuration config) {
        this.config01 = config.getConfig01();
        this.config02 = config.getConfig02();
        this.config03 = config.getConfig03();
    }

  
}

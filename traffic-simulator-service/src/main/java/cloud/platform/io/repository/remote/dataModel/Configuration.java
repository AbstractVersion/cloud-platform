package cloud.platform.io.repository.remote.dataModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * @author George Fiotakis
 */

public class Configuration {

    private String config01;
    private String config02;
    private int config03;

    public Configuration() {
    }

    public String getConfig01() {
        return config01;
    }

    public String getConfig02() {
        return config02;
    }

    public int getConfig03() {
        return config03;
    }

    public void setConfig01(String config01) {
        this.config01 = config01;
    }

    public void setConfig02(String config02) {
        this.config02 = config02;
    }

    public void setConfig03(int config03) {
        this.config03 = config03;
    }

}

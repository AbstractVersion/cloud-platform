/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.micro.env.template;

import com.micro.env.conf.Configuration;

/**
 *
 * @author onelove
 */
public class ConfTemplate {

    private String config01;
    private String config02;
    private int config03;

    public ConfTemplate(Configuration config) {
        this.config01 = config.getConfig01();
        this.config02 = config.getConfig02();
        this.config03 = config.getConfig03();
    }

    public ConfTemplate() {
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

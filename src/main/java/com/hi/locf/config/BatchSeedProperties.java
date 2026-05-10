package com.hi.locf.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.batch")
public class BatchSeedProperties {

    private int seedContractCount = 10;

    public int getSeedContractCount() {
        return seedContractCount;
    }

    public void setSeedContractCount(int seedContractCount) {
        this.seedContractCount = seedContractCount;
    }
}

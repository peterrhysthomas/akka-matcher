package akkaexample;


import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class TestDataBuilder {

    public static Trade aTrade(){
        return new Trade (randomAlphabetic(10), randomAlphabetic(10));
    }

    public static CcpTrade aCcpTrade(){
        return aCcpTrade(randomAlphabetic(10));
    }

    public static CcpTrade aCcpTrade(String exchangeRef){
        return new CcpTrade (exchangeRef);
    }
}

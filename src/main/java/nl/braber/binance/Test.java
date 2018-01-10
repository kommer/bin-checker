package nl.braber.binance;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.event.AggTradeEvent;
import com.binance.api.client.exception.BinanceApiException;

public class Test {

    public static void main(String args[]) {
        new Test();
    }

    public Test() {
        try {
            BinanceApiWebSocketClient client = BinanceApiClientFactory.newInstance().newWebSocketClient();
            client.onAggTradeEvent("ethbtc", (AggTradeEvent response) -> {
                System.out.println(response.getPrice());
                System.out.println(response.getQuantity());
            });
        } catch (BinanceApiException e) {
            System.out.println(e.toString());
        }
    }
}

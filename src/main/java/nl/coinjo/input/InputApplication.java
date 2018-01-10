package nl.coinjo.input;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.event.AggTradeEvent;
import nl.coinjo.input.types.Types;
import nl.coinjo.input.types.TradeEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@SpringBootApplication
//@EnableScheduling
public class InputApplication {

    BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance();

    private List<String> symbols = new LinkedList<String>(Arrays.asList("trxeth","icxeth","veneth","poeeth","lendeth","xvgeth","adaeth","xlmeth","reqeth","wabieth","iotaeth","neoeth","enjeth","xrpeth"));
//    private List<String> symbols = new LinkedList<String>(Arrays.asList("trxeth","icxeth"));

	public static void main(String[] args) {
		SpringApplication.run(InputApplication.class, args);
	}

	public InputApplication() {
        StreamInput si = new StreamInputImpl();
        startBinanceInput(si);
    }

    private void startBinanceInput(StreamInput streamInput) {
	    //read config
        //start websocket per config
        for(String symbol: symbols) {
            startAggTradeWebSocket(symbol, streamInput);
        }
    }

    private void startAggTradeWebSocket(String symbol, StreamInput streamInput) {
        BinanceApiWebSocketClient webSocketClient = BinanceApiClientFactory.newInstance().newWebSocketClient();
        webSocketClient.onAggTradeEvent(symbol, (AggTradeEvent response) -> {
            streamInput.onEvent(new TradeEvent(Types.inputSource.BINANCE,symbol,Double.parseDouble(response.getPrice()),Double.parseDouble(response.getQuantity()),response.isMaker(), response.getEventTime(), response.getTradeTime()));
        });
    }








}

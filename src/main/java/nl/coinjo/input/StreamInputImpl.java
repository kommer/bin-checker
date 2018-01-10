package nl.coinjo.input;

import nl.coinjo.input.types.Event;
import nl.coinjo.input.types.TradeEvent;
import nl.coinjo.input.types.Types;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.TimeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class StreamInputImpl implements StreamInput {

    private Map<String, Logger> loggers = new HashMap<>();

    public StreamInputImpl () {
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof TradeEvent) {
            writeTradeEventToFile((TradeEvent) event);
        }
    }

    private void writeTradeEventToFile(TradeEvent tradeEvent) {
        String line = tradeEvent.getEventReceivedTimestamp()+","+tradeEvent.getEventSendTimestamp()+","+tradeEvent.getTradeTimestamp()+","+String.format("%f", tradeEvent.getPrice())+","+tradeEvent.getQuantity()+","+tradeEvent.isMaker();

        getWriter (tradeEvent.getInputSource(),tradeEvent.getSymbol()).info(line);
    }

    private Logger getWriter(Types.inputSource inputSource, String reference) {
        if (!loggers.containsKey(reference)) {
            loggers.put(reference, createNewLogger(inputSource.name(), reference));
        }
        return loggers.get(reference);
    }

    private Logger createNewLogger(String dir, String reference) {
        String name = dir+"-"+reference;
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        final Configuration config = ctx.getConfiguration();
        Layout<? extends Serializable> layout = PatternLayout.createLayout(PatternLayout.SIMPLE_CONVERSION_PATTERN, null, config, null,
                null,true, true,null,null);

        TimeBasedTriggeringPolicy policy = TimeBasedTriggeringPolicy.newBuilder().withInterval(1).build();

        DefaultRolloverStrategy strategy = DefaultRolloverStrategy.createStrategy("10", "1", null, null, null,false,config);

        Appender appender = RollingFileAppender.createAppender("/files/"+dir+"/"+reference+".csv", "/files/"+dir+"/"+reference+"-%d{yyyy-MM-dd-HH}.csv","false", "false", "false", "5", "true",
                policy, strategy, null, null, null, "false", null, config);

//         appender = FileAppender.createAppender(name+".csv", "false", "false", "File", "true",
//                "false", "false", "4000", null, null, "false", null, config);
        appender.start();
        config.addAppender(appender);
        AppenderRef ref = AppenderRef.createAppenderRef(name, null, null);
        AppenderRef[] refs = new AppenderRef[] {ref};
        LoggerConfig loggerConfig = LoggerConfig.createLogger("false", Level.INFO, "org.apache.logging.log4j",
                "true", refs, null, config, null );
        loggerConfig.addAppender(appender, null, null);
        config.addLogger(name, loggerConfig);
        ctx.updateLoggers();
        return ctx.getLogger(name);
    }
}

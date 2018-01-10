package nl.coinjo.input.types;

public class TradeEvent implements Event {

    private Types.inputSource inputSource;

    private String symbol;
    private double price;
    private double quantity;
    private boolean maker;

    private long eventSendTimestamp;
    private long tradeTimestamp;

    private long eventReceivedTimestamp;

    public TradeEvent(Types.inputSource inputSource, String symbol, double price, double quantity, boolean maker, long eventSendTimestamp, long tradeTimestamp) {
        this.inputSource = inputSource;
        this.symbol = symbol;
        this.price = price;
        this.quantity = quantity;
        this.maker = maker;
        this.eventSendTimestamp = eventSendTimestamp;
        this.tradeTimestamp = tradeTimestamp;
        eventReceivedTimestamp = System.currentTimeMillis();
    }

    @Override
    public Types.inputSource getInputSource() {
        return inputSource;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public double getQuantity() {
        return quantity;
    }

    public boolean isMaker() {
        return maker;
    }

    public long getEventSendTimestamp() {
        return eventSendTimestamp;
    }

    public long getTradeTimestamp() {
        return tradeTimestamp;
    }

    public long getEventReceivedTimestamp() {
        return eventReceivedTimestamp;
    }
}

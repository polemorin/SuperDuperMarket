package SDMSale;

public class Sale {
    private final String name;
    private final IfYouBuy ifYouBuy;
    private final ThenYouGet thenYouGet;

    public Sale(String name, jaxb.generated.IfYouBuy ifYouBuy, jaxb.generated.ThenYouGet thenYouGet) {
        this.name = name;
        this.ifYouBuy = new IfYouBuy(ifYouBuy);
        this.thenYouGet = new ThenYouGet(thenYouGet);
    }


    public String getName() {
        return name;
    }

    public IfYouBuy getIfYouBuy() {
        return ifYouBuy;
    }

    public ThenYouGet getThenYouGet() {
        return thenYouGet;
    }
}

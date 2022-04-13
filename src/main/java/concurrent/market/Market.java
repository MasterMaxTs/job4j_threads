package concurrent.market;

public interface Market {

    int PRODUCE_SALE_COUNT_PER_DAY = 10;

    void buy();

    void produce();
}

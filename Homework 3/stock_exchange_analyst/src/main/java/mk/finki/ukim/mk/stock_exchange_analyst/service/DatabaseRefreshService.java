package mk.finki.ukim.mk.stock_exchange_analyst.service;

import java.text.ParseException;

public interface DatabaseRefreshService {

    void refreshDatabase() throws ParseException;

}

package mk.finki.ukim.mk.stock_exchange_analyst.service;

import java.io.IOException;
import java.text.ParseException;

public interface DatabaseRefreshService {

    void refreshDatabase() throws ParseException, IOException;

}

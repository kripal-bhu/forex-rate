package org.currency.exchange.repository;

import org.currency.exchange.model.ExchangeRate;
import org.currency.exchange.model.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RateRepository extends JpaRepository<Rate,Long> {
    List<Rate> findTop3ByCurrencyOrderByExchangeDateDesc(String targetCurrency);
}

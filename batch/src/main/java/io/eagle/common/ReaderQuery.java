package io.eagle.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReaderQuery {
    TransactionSummaryQuery("select\n" +
            "    TR.vacation_id as vacation_id,\n" +
            "    DATE_FORMAT(TR.created_at, '%Y-%m-%d') as created_at,\n" +
            "    min(TR.price) as low_price,\n" +
            "    max(TR.price) as high_price,\n" +
            "    (select t.price\n" +
            "     from transaction as t\n" +
            "     where DATE_FORMAT(t.created_at, '%Y-%m-%d') = :lastday and t.vacation_id = TR.vacation_id\n" +
            "     order by t.created_at desc\n" +
            "     limit 1\n" +
            "    ) as standard_price,\n" +
            "    (select standard_price\n" +
            "     from price_info as p\n" +
            "     where DATE_FORMAT(p.created_at, '%Y-%m-%d') = :twoDaysAgo and p.vacation_id = vacation_id\n" +
            "     limit 1\n" +
            "    ) as start_price,\n" +
            "    sum(TR.amount) as transaction_amount,\n" +
            "    (select sum(t.amount * t.price)\n" +
            "     from transaction as t\n" +
            "     where DATE_FORMAT(created_at, '%Y-%m-%d') = :lastday and t.vacation_id = TR.vacation_id\n" +
            "    ) as transaction_money\n" +
            "from transaction as TR\n" +
            "where DATE_FORMAT(created_at, '%Y-%m-%d') = :lastday\n" +
            "group by vacation_id"),

    SelectNoTransactionsQuery("select\n" +
            "    p.* \n" +
            "from price_info as p\n" +
            "where\n" +
            "    (p.vacation_id in (select id from vacation where status = 'MARKET_ONGOING'))\n" +
            "    and\n" +
            "    (p.vacation_id not in\n" +
            "        (select vacation_id\n" +
            "         from price_info\n" +
            "         where DATE_FORMAT(created_at, '%Y-%m-%d') = :lastday\n" +
            "         group by vacation_id\n" +
            "         )\n" +
            "     )\n" +
            "    and DATE_FORMAT(p.created_at, '%Y-%m-%d') = :twoDaysAgo");

    public final String query;
}

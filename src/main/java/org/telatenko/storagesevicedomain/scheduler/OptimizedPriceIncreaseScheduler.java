package org.telatenko.storagesevicedomain.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telatenko.storagesevicedomain.annotation.MeasureExecutionTime;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Slf4j
@ConditionalOnProperty(
        value = {"app.scheduling.enable", "app.scheduling.optimization"},
        havingValue = "true"
)
@Component
public class OptimizedPriceIncreaseScheduler {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${app.scheduling.priceMultiplier}")
    private BigDecimal priceMultiplier;

    private final int BATCH_SIZE = 10000;
    private final String LOG_FILE_PATH = "updated_products.txt";

    @MeasureExecutionTime
    @Scheduled(fixedRateString = "${app.scheduling.period}")
    public void increasePrices() {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            connection.setAutoCommit(false);
            String lockSql = "LOCK TABLE products IN ACCESS EXCLUSIVE MODE;";
            try (PreparedStatement lockStmt = connection.prepareStatement(lockSql)) {
                lockStmt.execute();
            }

            String selectSql = "SELECT id, price FROM products ORDER BY id LIMIT ? OFFSET ?";
            String updateSql = "UPDATE products SET price = ? WHERE id = ?";

            try (PreparedStatement selectStmt = connection.prepareStatement(selectSql);
                 PreparedStatement updateStmt = connection.prepareStatement(updateSql);
                 BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE_PATH, true))) {

                ResultSet resultSet = connection.prepareStatement("SELECT COUNT(*) FROM products").executeQuery();
                resultSet.next();
                int totalProducts = resultSet.getInt(1);
                for (int start = 0; start < totalProducts; start += BATCH_SIZE) {
                    updateBatch(start, BATCH_SIZE, connection, priceMultiplier, selectStmt, updateStmt, writer);
                }

                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                throw new RuntimeException("Failed to update prices", e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update prices", e);
        }
    }

    private void updateBatch(int start, int batchSize, Connection connection, BigDecimal priceMultiplier,
                             PreparedStatement selectStmt, PreparedStatement updateStmt,
                             BufferedWriter writer) throws SQLException {
        try {
            selectStmt.setInt(1, batchSize);
            selectStmt.setInt(2, start);

            try (ResultSet rs = selectStmt.executeQuery()) {
                while (rs.next()) {
                    UUID id = UUID.fromString(rs.getString("id"));
                    BigDecimal oldPrice = rs.getBigDecimal("price");
                    BigDecimal newPrice = oldPrice.multiply(priceMultiplier);

                    updateStmt.setBigDecimal(1, newPrice);
                    updateStmt.setObject(2, id);
                    updateStmt.addBatch();

                    log.info(String.format("Product ID: %s, Old Price: %s, New Price: %s\n",
                            id, oldPrice, newPrice));
                    writer.write(String.format("Product ID: %s, Old Price: %s, New Price: %s\n",
                            id, oldPrice, newPrice));
                }
            }

            updateStmt.executeBatch();

        } catch (Exception e) {
            connection.rollback();
            throw new RuntimeException("Failed to update prices", e);
        }
    }
}
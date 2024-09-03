package org.telatenko.storagesevicedomain.scheduler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telatenko.storagesevicedomain.annotation.MeasureExecutionTime;
import org.telatenko.storagesevicedomain.persistence.ProductEntity;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ConditionalOnProperty(
        value = {"app.scheduling.enable", "app.scheduling.optimization"},
        havingValue = "true"
)
@Component
public class OptimizedPriceIncreaseScheduler {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${app.scheduling.priceMultiplier}")
    private String priceMultiplier;

    private final int BATCH_SIZE = 10000;
    private final String LOG_FILE_PATH = "updated_products.txt";

    @MeasureExecutionTime
    @Scheduled(fixedRateString = "${app.scheduling.period}")
    public void increasePrices() {
        BigDecimal multiplier = new BigDecimal(priceMultiplier);
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            connection.setAutoCommit(false);
            String lockSql = "LOCK TABLE products IN ACCESS EXCLUSIVE MODE;";
            connection.prepareStatement(lockSql).execute();

            ResultSet resultSet = connection.prepareStatement("SELECT COUNT(*) FROM products").executeQuery();
            resultSet.next();
            int totalProducts = resultSet.getInt(1);
            for (int start = 0; start < totalProducts; start += BATCH_SIZE) {
                updateBatch(start, BATCH_SIZE, connection, multiplier);
            }

            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update prices", e);
        }
    }

    private void updateBatch(int start, int batchSize, Connection connection, BigDecimal multiplier) {
        String selectSql = "SELECT id, price FROM products ORDER BY id LIMIT ? OFFSET ?";
        String updateSql = "UPDATE products SET price = ? WHERE id = ?";

        try (PreparedStatement selectStmt = connection.prepareStatement(selectSql);
             PreparedStatement updateStmt = connection.prepareStatement(updateSql);
             BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE_PATH, true))) {

            selectStmt.setInt(1, batchSize);
            selectStmt.setInt(2, start);

            List<ProductEntity> products = new ArrayList<>();
            try (ResultSet rs = selectStmt.executeQuery()) {
                while (rs.next()) {
                    ProductEntity product = new ProductEntity();
                    product.setId(UUID.fromString(rs.getString("id")));
                    product.setPrice(rs.getBigDecimal("price"));
                    products.add(product);
                }
            }
            System.out.println("Закончили селекты");
            for (ProductEntity product : products) {
                BigDecimal newPrice = product.getPrice().multiply(multiplier);
                updateStmt.setBigDecimal(1, newPrice);
                updateStmt.setObject(2, product.getId());
                updateStmt.addBatch();
                System.out.println(String.format("Product ID: %s, Old Price: %s, New Price: %s\n",
                        product.getId(), product.getPrice(), newPrice));
                writer.write(String.format("Product ID: %s, Old Price: %s, New Price: %s\n",
                        product.getId(), product.getPrice(), newPrice));
            }

            updateStmt.executeBatch();

        } catch (SQLException | IOException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                throw new RuntimeException("Failed to rollback transaction", rollbackEx);
            }
            throw new RuntimeException("Failed to update prices", e);
        }
    }
}
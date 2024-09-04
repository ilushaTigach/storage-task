package org.telatenko.storagesevicedomain.persistence;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для управления сущностями ProductEntity.
 * Этот интерфейс расширяет JpaRepository и предоставляет дополнительные методы для работы с продуктами.
 */
@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {

    /**
     * Находит все продукты с пагинацией.
     *
     * @param pageable Объект пагинации.
     * @return Страница с продуктами.
     */
    Page<ProductEntity> findAll(Pageable pageable);

    /**
     * Находит продукт по его артикулу.
     *
     * @param article Артикул продукта.
     * @return Optional, содержащий продукт, если он найден, или пустой, если продукт не найден.
     */
    Optional<ProductEntity> findByArticle(String article);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM ProductEntity p")
    List<ProductEntity> findAllForScheduler(LockModeType lockMode);

}


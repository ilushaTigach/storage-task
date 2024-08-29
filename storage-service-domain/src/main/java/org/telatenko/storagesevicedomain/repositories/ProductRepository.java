package org.telatenko.storagesevicedomain.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.telatenko.storagesevicedomain.models.ProductEntity;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {

    Page<ProductEntity> findAll(Pageable pageable);

    ProductEntity findByArticle(String article);

}

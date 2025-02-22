package com.temas.telegrambot.course.telegram.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by azhdanov on 20.02.2025.
 */
@Repository
public interface OrderRepository extends CrudRepository<Order, String> {
}

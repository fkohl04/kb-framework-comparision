package spring.demo.database

import org.springframework.data.jpa.repository.JpaRepository;

public interface RandomsRepository : JpaRepository<Randoms, Long>

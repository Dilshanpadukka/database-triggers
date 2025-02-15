package lk.icet.crm.repository;


import lk.icet.crm.entity.CustomerHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerHistoryRepository extends JpaRepository<CustomerHistory, Long> {
    List<CustomerHistory> findByOrderByOperationTimestampDesc();
}
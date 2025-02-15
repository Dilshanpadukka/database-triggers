package lk.icet.crm.service;

import lk.icet.crm.entity.CustomerHistory;
import lk.icet.crm.repository.CustomerHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerHistoryService {
    private final CustomerHistoryRepository customerHistoryRepository;

    public List<CustomerHistory> getAllHistory() {
        return customerHistoryRepository.findByOrderByOperationTimestampDesc();
    }
}

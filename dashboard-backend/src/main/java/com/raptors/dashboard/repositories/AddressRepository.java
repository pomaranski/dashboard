package com.raptors.dashboard.repositories;

import com.raptors.dashboard.entities.Address;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface AddressRepository extends PagingAndSortingRepository<Address, String> {
}

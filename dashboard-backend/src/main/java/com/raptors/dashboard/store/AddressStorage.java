package com.raptors.dashboard.store;

import com.raptors.dashboard.entities.Address;
import com.raptors.dashboard.repositories.AddressRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class AddressStorage {

    private final AddressRepository addressRepository;

    public AddressStorage(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public void storeAddress(URI uri) {
        StreamSupport.stream(addressRepository.findAll().spliterator(), true)
                .filter(address -> address.getUri().equals(uri))
                .findAny()
                .ifPresentOrElse((address)
                                -> log.info("Address with uri={} already exists id={}", address.getUri(), address.getId()),
                        () -> addressRepository.save(Address.builder()
                                .uri(uri)
                                .build()));
    }

    public Set<URI> fetchAllAddress() {
        return StreamSupport.stream(addressRepository.findAll().spliterator(), true)
                .map(Address::getUri)
                .collect(Collectors.toSet());
    }

}

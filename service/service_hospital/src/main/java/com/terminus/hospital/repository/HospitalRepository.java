package com.terminus.hospital.repository;

import com.terminus.model.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends MongoRepository<Hospital, String> {
    Hospital findHospitalByHoscode(String hoscode);
}

package com.terminus.hospital.repository;

import com.terminus.model.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HospitalRepository extends MongoRepository<Hospital, String> {
    Hospital findHospitalByHoscode(String hoscode);

    List<Hospital> findByHosname(String hosname);
}

package com.acme.empleaves.controller;

import com.acme.empleaves.dto.LeaveBenefitsDTO;
import com.acme.empleaves.model.LeaveBenefits;
import com.acme.empleaves.repo.LeaveBenefitsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class LeaveBenefitsController {

  public static final String benefitsURI = "/acme/v1/benefits";

  @Autowired private LeaveBenefitsRepository leaveBenefitsRepository;

  @RequestMapping(
    value = benefitsURI,
    produces = MediaType.APPLICATION_JSON_VALUE,
    method = RequestMethod.GET
  )
  public ResponseEntity<List<LeaveBenefits>> getLeaveBenefits() {
    List<LeaveBenefits> benefits = new ArrayList<>();
    HttpStatus status = HttpStatus.OK;
    leaveBenefitsRepository.findAll().forEach(e -> benefits.add(e));
    return new ResponseEntity<>(benefits, status);
  }

  @RequestMapping(
    value = benefitsURI,
    consumes = MediaType.APPLICATION_JSON_VALUE,
    method = RequestMethod.POST
  )
  public ResponseEntity<?> createLeaveBenefits(
      @RequestBody List<LeaveBenefitsDTO> dtoList) {
    for (LeaveBenefitsDTO dto : dtoList) {
      LeaveBenefits benefits = leaveBenefitsRepository.findByDesignation(dto.getDesignation());
      if (null == benefits) {
        benefits = new LeaveBenefits();
      }
      benefits.getMaxLeaveEligibilityMap().put(dto.getLeaveType(), dto.getEarned());
      leaveBenefitsRepository.save(benefits);
    }
    HttpStatus status = HttpStatus.CREATED;
    return new ResponseEntity<>(status);
  }
}

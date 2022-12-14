package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.TransferRepository;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.services.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

    @Autowired
    private TransferRepository transferRepository;

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    //Uses TransferService CLASS to handle initializing of all transfers
    //endpoint for "InitTransfer
    @PostMapping("/InitTransfer")
    public ResponseEntity<Transfer> createTransfer(@Valid @RequestBody Transfer transfer, Principal p){
        Transfer newT=null;
        try{
            return transferService.handleTransfer(transfer,p);
        }catch (Exception e){
                return new ResponseEntity<>(transfer, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    //Uses transfer repo to return a list<transfer>
    //uses principal to make sure you are only receiving your transfers
    //Note: A Transfer obj will contain several others objects including 2 account objects which each will contain a user object
    //also a type and status object
    @GetMapping("/MyTransfers")
    public ResponseEntity<List<Transfer>> getMyTransfers(Principal principal){
        try{
            List<Transfer> myTransfersList = transferRepository.findAllByUsername(principal.getName());
            if(myTransfersList.isEmpty()){
                return new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(myTransfersList,HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}

package com.example.cms.controllers.v0;

import com.example.cms.dto.Notify;
import com.example.cms.services.CardService;
import com.example.cms.utils.ValidationException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v0/card")
public class CMSController {
    final static Logger logger = Logger.getLogger(CMSController.class);

    @Autowired
    private CardService cardService;

    @GetMapping(path = "/inquiry/{amount}")
    public ResponseEntity inquiry(@PathVariable Long amount) {
        try {
            return ResponseEntity.ok().body(cardService.preparePayments(amount));
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error!");
        }
    }

    @PostMapping(path = "/notify")
    public ResponseEntity notify(@RequestBody List<Notify> notifications) {
        try {
            cardService.notifyPayments(notifications);
            return ResponseEntity.ok("");
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error!");
        }
    }
}

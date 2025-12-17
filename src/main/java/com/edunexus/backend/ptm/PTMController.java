package com.edunexus.backend.ptm;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ptm")
@CrossOrigin("*")
public class PTMController {
	@Autowired
    private PTMService ptmService;
	
	@PostMapping("/schedule")
    public ResponseEntity<?> schedule(@RequestBody PTMScheduleRequest req) {
        PTM ptm = ptmService.schedulePTM(req);
        return ResponseEntity.ok(new PTMResponseDTO(ptm));
    }
	
	@GetMapping("/completed")
    public List<PTMResponseDTO> completed(
            @RequestParam(required = false) String className
    ) {
        return ptmService.getCompletedPTMs(className);
    }
}

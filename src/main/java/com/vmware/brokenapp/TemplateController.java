package com.vmware.brokenapp;

import com.vmware.skyline.common.dto.srb.Chunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@ResponseBody
public class TemplateController {

    private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    @PostMapping(value = "test", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Chunk> uploadChunked() throws IOException {

        return new ResponseEntity<>(HttpStatus.OK);
    }
}

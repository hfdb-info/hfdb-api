package info.hfdb.hfdbapi.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import info.hfdb.hfdbapi.Controller.Status.StatusCode;

@RestController
public class HFDBAPI {

    @RequestMapping("/status")
    public Status getStatus() {
        return new Status(StatusCode.OK);
    }

    @RequestMapping("/ping")
    public String ping() {
        return "pong";
    }
}

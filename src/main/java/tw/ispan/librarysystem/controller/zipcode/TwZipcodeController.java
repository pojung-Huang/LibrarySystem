package tw.ispan.librarysystem.controller.zipcode;

import org.springframework.web.bind.annotation.*;
import tw.ispan.librarysystem.service.zipcode.TwZipcodeService;

import java.util.List;

@RestController
@RequestMapping("/api/zipcodes")
@CrossOrigin(origins = "http://localhost:3000")// 如果你要跨域給 Nuxt 前端使用
public class TwZipcodeController {

    private final TwZipcodeService service;

    public TwZipcodeController(TwZipcodeService service) {
        this.service = service;
    }

    @GetMapping("/counties")
    public List<String> getCounties() {
        return service.getAllCounties();
    }

    @GetMapping("/towns")
    public List<String> getTowns(@RequestParam String county) {
        return service.getTownsByCounty(county);
    }

    @GetMapping("/zip")
    public String getZip(@RequestParam String county, @RequestParam String town) {
        return service.getZip(county, town);
    }

}

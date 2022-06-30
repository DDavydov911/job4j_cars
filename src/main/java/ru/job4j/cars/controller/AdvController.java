package ru.job4j.cars.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cars.model.*;
import ru.job4j.cars.service.AdvService;
import ru.job4j.cars.service.BodyTypeService;
import ru.job4j.cars.service.CarService;
import ru.job4j.cars.service.UserService;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
public class AdvController {

    private final AdvService advService;
    private final CarService carService;
    private final BodyTypeService bodyTypeService;
    private final UserService userService;

    public AdvController(AdvService advService, CarService carService,
                         BodyTypeService bodyTypeService, UserService userService) {
        this.advService = advService;
        this.carService = carService;
        this.bodyTypeService = bodyTypeService;
        this.userService = userService;
    }

    @GetMapping("/index")
    public String mainPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Гость");
        }
        model.addAttribute("user", user);
        model.addAttribute("ads", advService.findAll());
        int size = advService.findAll().size();
        return "index";
    }

    @GetMapping("/addAdv")
    public String formAddAdv(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Гость");
        }
        model.addAttribute("user", user);
        model.addAttribute("carMarks", carService.getCarMarks());
        model.addAttribute("bodyTypes", bodyTypeService.getBodyTypes());
        return "/addAdv";
    }

    @PostMapping("/addAdv")
    public String addAdv(Model model, HttpSession session, @ModelAttribute Ads ad,
                         @RequestParam("carMark.name") String carMarkNane,
                         @RequestParam("bodyType.name") String bodyType,
                         @RequestParam("horsePower") String power,
                         @RequestParam("file") MultipartFile file) throws IOException {
        User user = (User) session.getAttribute("user");
        user = userService.getUserById(user.getId()).get();
        Car car = new Car(0, bodyType, new CarMark(carMarkNane),
                new Engine(Integer.parseInt(power)));
        if (!file.isEmpty()) {
            car.getPhotos().add(new Photo(file.getBytes()));
        } else {
            car.getPhotos().add(new Photo(null));
        }
        ad.setCar(car);
        Ads adv = advService.addAdv(ad);
        user.addAdv(adv);
        userService.update(user);
        model.addAttribute("user", user);
        return "redirect:/index";
    }

    @GetMapping("/adsWithPhoto")
    public String getAdsWithPhoto(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Гость");
        }
        model.addAttribute("user", user);
        model.addAttribute("ads", advService.getAdsWithPhoto());
        for (Ads ads : advService.getAdsWithPhoto()) {
            System.out.println(ads);
        }
        return "index";
    }

    @GetMapping("/lastDayAds")
    public String findLastDayAds(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Гость");
        }
        model.addAttribute("user", user);
        model.addAttribute("ads", advService.findLastDayAds());
        return "index";
    }

    @GetMapping("/ad/{adId}")
    public String getAd(Model model,
                        @PathVariable("adId") int id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Гость");
        }
        model.addAttribute("user", user);
        model.addAttribute("ad", advService.findById(id));
        System.out.println(advService.findById(id));
        return "adv";
    }

    @GetMapping("/updateAdv/{advId}")
    public String formUpdateAd(Model model,
                               @PathVariable("advId") int id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Гость");
        }
        model.addAttribute("user", user);
        model.addAttribute("ad", advService.findById(id));
        return "updateAdv";
    }

    @PostMapping("/updateAdv")
    public String updateAdv(@ModelAttribute Ads adv,
                            @RequestParam("file") MultipartFile file) throws IOException {
        Ads ad = advService.findById(adv.getId());
        ad.setDescription(adv.getDescription());
        ad.setPrice(adv.getPrice());
        ad.setSold(adv.isSold());
        Car car = ad.getCar();
        if (file.getSize() != 0 && !car.getPhotos().isEmpty()) {
            car.getPhotos().set(0, new Photo(file.getBytes()));
        }
        if (file.getSize() != 0 && car.getPhotos().isEmpty()) {
            car.getPhotos().add(new Photo(file.getBytes()));
        }
        advService.updateAdv(ad);
        return "redirect:/ad/" + ad.getId();
    }

    @GetMapping("/carPhoto/{carId}")
    public ResponseEntity<Resource> download(@PathVariable("carId") Integer carId) {
        Car car = carService.getCarById(carId);
        if (!car.getPhotos().isEmpty()) {
        return ResponseEntity.ok()
                .headers(new HttpHeaders())
                .contentLength(car.getPhotos().get(0).getPhoto().length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new ByteArrayResource(car.getPhotos().get(0).getPhoto()));
        }
        return null;
    }

    @PostMapping("/deleteAdv")
    public String deleteAdv(@RequestParam("id") int id) {
        advService.deleteAdv(id);
        return "redirect:/index";
    }
}

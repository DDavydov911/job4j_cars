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

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AdvController {

    private final AdvService advService;
    private final CarService carService;
    private final BodyTypeService bodyTypeService;

    public AdvController(AdvService advService, CarService carService,
                         BodyTypeService bodyTypeService) {
        this.advService = advService;
        this.carService = carService;
        this.bodyTypeService = bodyTypeService;
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
        Car car = new Car(0, bodyType, new CarMark(carMarkNane),
                new Engine(Integer.parseInt(power)));
        System.out.println("File is nulL: " + file == null);
        System.out.println("File is empty: " + file.isEmpty());
        System.out.println("File's size is : " + file.getSize());
        System.out.println("File's content is : " + file);
        System.out.println("Car has empty list: " + car.getPhotos().isEmpty());
        if (file.getSize() != 0) {
            car.getPhotos().add(new Photo(file.getBytes()));
        }
        System.out.println("Car has empty list: " + car.getPhotos().isEmpty());
        ad.setCar(car);
        User user = (User) session.getAttribute("user");
        Ads adv = advService.addAdv(ad);
        model.addAttribute("user", user);
        return "redirect:/index";
    }

    @GetMapping("/adsWithPhoto")
    public String getAdsWithPhoto(Model model, HttpSession session) {
        System.out.println("Start adsWithPhoto method");
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Гость");
        }
        model.addAttribute("user", user);
        model.addAttribute("ads", advService.getAdsWithPhoto());
        System.out.println("CONTROLLER_adsWhithPhoto: ");
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
        if (file.getSize() != 0) {
            car.getPhotos().set(0, new Photo(file.getBytes()));
        }
        advService.updateAdv(ad);
        return "redirect:/ad/" + ad.getId();
    }

    @GetMapping("/carPhoto/{carId}")
    public ResponseEntity<Resource> download(@PathVariable("carId") Integer carId) {
        Car car = carService.getCarById(carId);
        return ResponseEntity.ok()
                .headers(new HttpHeaders())
                .contentLength(car.getPhotos().get(0).getPhoto().length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new ByteArrayResource(car.getPhotos().get(0).getPhoto()));
    }
}

package ru.job4j.cars.service;

import org.springframework.stereotype.Service;
import ru.job4j.cars.model.Ads;
import ru.job4j.cars.store.AdvRepositoryDB;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdvService {

    private final AdvRepositoryDB store;

    public AdvService(AdvRepositoryDB store) {
        this.store = store;
    }

    public Ads addAdv(Ads ad) {
        return store.add(ad);
    }

    public List<Ads> findAll() {
        List<Ads> list = new ArrayList<>(store.findAll());
        return list;
    }

    public List<Ads> getAdsWithPhoto() {
        List<Ads> list = new ArrayList<>(store.getAdsWithPhoto());
        return list;
    }

    public Ads findById(int id) {
        return store.findAdvById(id);
    }

    public Ads updateAdv(Ads adv) {
        return store.updateAdv(adv);
    }

    public List<Ads> findLastDayAds() {
        List<Ads> list = new ArrayList<>(store.getLastDayAds());
        return list;
    }
}

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
        return new ArrayList<>(store.findAll());
    }

    public List<Ads> getAdsWithPhoto() {
        return new ArrayList<>(store.getAdsWithPhoto());
    }

    public Ads findById(int id) {
        return store.findAdvById(id);
    }

    public Ads updateAdv(Ads adv) {
        return store.updateAdv(adv);
    }

    public List<Ads> findLastDayAds() {
        return new ArrayList<>(store.getLastDayAds());
    }

    public boolean deleteAdv(int id) {
        return store.deleteAdv(id);
    }
}
